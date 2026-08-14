package sen.manaita_plus_legacy_core.util;

import com.sun.jna.Function;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.ProtectionDomain;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * 独立 Instrumentation 获取工具。
 * 使用 {@link #getInstrumentation()} 即可获取 JVM Instrumentation 实例，
 * 整个过程中不依赖任何外部业务类，完全自举。
 */
public class InstrumentationGetter {

    private static final AtomicReference<Instrumentation> INST_REF = new AtomicReference<>();

    /**
     * 内置 Agent，提供标准 agentmain 方法，负责接收 Instrumentation 引用。
     */
    public static class Agent {
        public static void agentmain(String agentArgs, Instrumentation inst) {
            INST_REF.set(inst);
        }
    }

    /**
     * 获取 Instrumentation 实例（首次调用会触发自附加，后续直接返回缓存值）。
     */
    public static synchronized Instrumentation getInstrumentation() {
        Instrumentation inst = INST_REF.get();
        if (inst != null) {
            return inst;
        }

        try {
            // 1. 将 Agent 类直接定义到 Bootstrap ClassLoader
            //    这样 JVM 在解析 Agent-Class 时就能找到它，无需 jar 中包含类文件。
            String agentClassName = Agent.class.getName();
            byte[] agentClassBytes = getClassBytes(Agent.class);
            defineClassToBootstrap(agentClassName, agentClassBytes);

            // 2. 创建仅包含 Manifest 的临时 jar（没有任何 class 文件）
            Path jarPath = Files.createTempFile("inst-agent-", ".jar");
            jarPath.toFile().deleteOnExit();

            Manifest manifest = new Manifest();
            Attributes attrs = manifest.getMainAttributes();
            attrs.put(Attributes.Name.MANIFEST_VERSION, "1.0");
            attrs.put(new Attributes.Name("Agent-Class"), agentClassName);
            attrs.put(new Attributes.Name("Can-Redefine-Classes"), "true");
            attrs.put(new Attributes.Name("Can-Retransform-Classes"), "true");

            try (JarOutputStream jos = new JarOutputStream(
                    new FileOutputStream(jarPath.toFile()), manifest)) {
                jos.flush();
            }

            // 3. 通过 JNA 调用 Agent_OnAttach 完成自附加
            Function getCreatedVMs = Function.getFunction("jvm", "JNI_GetCreatedJavaVMs");
            Pointer[] jvmRefs = new Pointer[1];
            IntByReference count = new IntByReference(1);
            int result = getCreatedVMs.invokeInt(new Object[]{jvmRefs, 1, count.getPointer()});
            if (result != 0) {
                throw new RuntimeException("JNI_GetCreatedJavaVMs failed: " + result);
            }

            Function attach = Function.getFunction("instrument", "Agent_OnAttach");
            int attachResult = attach.invokeInt(new Object[]{
                    jvmRefs[0], jarPath.toAbsolutePath().toString(), null
            });
            if (attachResult != 0) {
                throw new RuntimeException("Agent_OnAttach failed: " + attachResult);
            }

            // 4. 等待 Agent 回调注入 Instrumentation
            long deadline = System.currentTimeMillis() + 5000;
            while (INST_REF.get() == null) {
                if (System.currentTimeMillis() > deadline) {
                    throw new TimeoutException("Timed out waiting for agent to attach");
                }
                Thread.sleep(10);
            }

            return INST_REF.get();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to obtain Instrumentation", e);
        }
    }

    /* ========== 辅助方法（不依赖任何外部类） ========== */

    /**
     * 从类路径中获取指定类的字节码。
     */
    private static byte[] getClassBytes(Class<?> clazz) throws IOException {
        String resourceName = clazz.getName().replace('.', '/') + ".class";
        try (InputStream in = clazz.getClassLoader()
                .getResourceAsStream(resourceName)) {
            if (in == null) {
                throw new FileNotFoundException("Cannot find class: " + clazz.getName());
            }
            return readAllBytes(in);
        }
    }

    private static byte[] readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096];
        int n;
        while ((n = is.read(data)) != -1) {
            buffer.write(data, 0, n);
        }
        return buffer.toByteArray();
    }

    /**
     * 将类字节码直接定义到 Bootstrap ClassLoader（即 loader 参数为 null）。
     * 采用与 PLZBase 相同的底层调用，但完全自实现。
     */
    private static void defineClassToBootstrap(String className, byte[] bytes) throws Throwable {
        // 反射获取 ClassLoader 的 defineClass1 方法句柄
        MethodHandles.Lookup lookup = getTrustedLookup();
        MethodHandle defineClass1 = lookup.findStatic(ClassLoader.class,
                "defineClass1",
                MethodType.methodType(Class.class, ClassLoader.class, String.class,
                        byte[].class, int.class, int.class, ProtectionDomain.class, String.class));
        // loader 传入 null 即表示定义到 Bootstrap
        defineClass1.invoke(null, className, bytes, 0, bytes.length, null, null);
    }

    /**
     * 获取一个拥有完全特权（可以访问任何类）的 Lookup。
     * 通过反射调用 ReflectionFactory.newConstructorForSerialization 完成。
     */
    private static MethodHandles.Lookup getTrustedLookup() throws Throwable {
        // 获取 ReflectionFactory 实例（sun.reflect.ReflectionFactory）
        Class<?> rfClass = Class.forName("sun.reflect.ReflectionFactory");
        MethodHandle getFactory = MethodHandles.lookup()
                .findStatic(rfClass, "getReflectionFactory", MethodType.methodType(rfClass));
        Object factory = getFactory.invoke();

        // 用其创建一个可以绕过安全检查的构造器
        Constructor<MethodHandles.Lookup> constructor =
                (Constructor<MethodHandles.Lookup>) MethodHandles.lookup()
                        .findVirtual(rfClass, "newConstructorForSerialization",
                                MethodType.methodType(Constructor.class, Class.class, Constructor.class))
                        .invoke(factory, MethodHandles.Lookup.class,
                                MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Class.class, int.class));
        return constructor.newInstance(Object.class, null, -1);
    }
}