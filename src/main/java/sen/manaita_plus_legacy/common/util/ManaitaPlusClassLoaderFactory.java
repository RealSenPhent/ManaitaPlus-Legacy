package sen.manaita_plus_legacy.common.util;

import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.TransformingClassLoader;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.LockHelper;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import sen.manaita_plus_legacy_core.util.Helper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.objectweb.asm.Opcodes.*;

public class ManaitaPlusClassLoaderFactory {
    private static final ASMClassLoader LOADER = new ASMClassLoader();
    private static final HashMap<Class<?>, Class<?>> a = new HashMap<>();
    private static final LockHelper<Class<?>, Class<?>> cache = new LockHelper<>(a);
    private static final Map<Class<?>,Boolean> permissionClass = new HashMap<>();
    public static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    public static final MethodHandles.Lookup lookup1;
    private static TransformingClassLoader classLoader;
//    private static MethodHandle defineClass;
//    private static Class<?> sharedSecretsClass;
//    private static long javaLangAccessOffset;

    static {
        MethodHandles.Lookup lookup = ManaitaPlusClassLoaderFactory.lookup;
        MethodHandle defineClassM = null;
        long javaLangAccessOffset1 = 0L;
        Class<?> sharedSecrets = null;
        if (Helper.lookup != null) {
            lookup = Helper.lookup;
//            try {
//                defineClassM = lookup.findVirtual(Class.forName("jdk.internal.access.JavaLangAccess"), "defineClass", MethodType.methodType(Class.class,ClassLoader.class,Class.class, String.class, byte[].class, ProtectionDomain.class, boolean.class,int.class, Object.class));
//                sharedSecrets = Class.forName("jdk.internal.access.SharedSecrets");
//                javaLangAccessOffset1 = Helper.UNSAFE.objectFieldOffset(sharedSecrets.getDeclaredField("javaLangAccess"));
//            } catch (NoSuchMethodException | IllegalAccessException | ClassNotFoundException | NoSuchFieldException e) {
//                throw new RuntimeException(e);
//            }
        }
//        sharedSecretsClass = sharedSecrets;
//        javaLangAccessOffset = javaLangAccessOffset1;
//        defineClass = defineClassM;
        lookup1 = lookup;
        classLoader = Helper.getFieldValue(Launcher.INSTANCE, "classLoader", TransformingClassLoader.class);
    }

    public static Class<?> createWrapper(Class<?> callback) {
        if (callback == null || !Entity.class.isAssignableFrom(callback) || Player.class.isAssignableFrom(callback)) return null;
        try {
            lookup.accessClass(callback);
        } catch (IllegalAccessException e) {
            permissionClass.put(callback, Boolean.TRUE);
            return null;
        }
        if (permissionClass.containsKey(callback)) return null;
//        ManaitaPlus.LOGGER.info("ManaitaPlusClassLoaderFactory.createWrapper: " + callback.getName());
        try {
            return cache.computeIfAbsent(callback, () -> {
                var node = new ClassNode();
                transformNode(node, callback);
                return defineClass(node,callback);
            });
        } catch (Exception e) {
            cache.computeIfAbsent(callback, () -> null);
            return null;
        }
    }

    private static Class<?> defineClass(ClassNode node,Class<?> klass) {
        var cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(cw);
        byte[] byteArray = cw.toByteArray();
        Class<?> define;
//        if (defineClass != null && sharedSecretsClass != null) {
//            try {
//                define = (Class<?>) defineClass.invoke(Helper.UNSAFE.getObject(sharedSecretsClass, javaLangAccessOffset),klass,klass.getClassLoader(),node.name.replace('/', '.'),byteArray, null,false,0x00000004,null);
//                permissionClass.put(define,Boolean.TRUE);
//            } catch (Throwable e) {
//                throw new RuntimeException(e);
//            }
//        } else
        {
            try {
                define = lookup1.defineClass(byteArray);
                permissionClass.put(define, Boolean.TRUE);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return define;
    }

    private static void transformNode(ClassNode target,Class<?> klass) {
        MethodVisitor mv;

        String replace = klass.getName().replace(".", "/");
        target.visit(V16, ACC_PUBLIC | ACC_SUPER | ACC_FINAL, replace + "Manaita", null, replace, null);

        target.visitSource(".dynamic", null);

//        List<Method> initialMethod = new ArrayList<>();
//        Class<?> superClass1 = superClass.getSuperclass();

//        for (Method method : superClass1.getMethods()) {
//            if (method.getName().equals("<init>") && (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers()))) {
//                initialMethod.add(method);
//            }
//        }
//        if (!initialMethod.isEmpty()) {
//            for (Method method : initialMethod) {
//                mv = target.visitMethod(method.getModifiers(), "<init>", getDesc(method), null, null);
//                mv.visitCode();
//                mv.visitMethodInsn(INVOKESPECIAL, "<init>", superClass1.getName().replace(",","/"), getDesc(method), false);
//                put(method, mv);
//                mv.visitInsn(RETURN);
//                mv.visitEnd();
//            }

        Set<String> finalMethod = new HashSet<>();
        do {
            Class<?> superClass = klass.getSuperclass();
            for (Method method : klass.getMethods()) {
                String descriptor = getDesc(method);
                if (finalMethod.contains(method.getName() + descriptor)) {
                    continue;
                }
                Type returnType = Type.getReturnType(method);
                int sort = returnType.getSort();
                if (true) {
                    int modifiers = method.getModifiers();
                    if (Modifier.isFinal(modifiers)) {
                        finalMethod.add(method.getName() + descriptor);
                        continue;
                    }
                    if (!Modifier.isStatic(modifiers) && !Modifier.isInterface(modifiers) && (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers))) {
                        finalMethod.add(method.getName() + descriptor);
                        String name = method.getName();

//                        System.err.println(Modifier.toString(modifiers) + " " + name + descriptor);
                        mv = target.visitMethod(modifiers, name, descriptor, null, null);
                        mv.visitCode();
                        mv.visitFieldInsn(GETSTATIC, "sen/manaita_plus_legacy/common/util/ManaitaPlusLegacyEntityData", "remove", "Lsen/manaita_plus_legacy/common/util/ManaitaPlusLegacyEntityData;");
                        mv.visitVarInsn(ALOAD, 0);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "sen/manaita_plus_legacy/util/ManaitaPlusLegacyEntityData", "accept", "(Lnet/minecraft/world/entity/Entity;)Z", false);
                        Label labelNode = new Label();
                        mv.visitJumpInsn(IFEQ, labelNode);
                        if (superClass.getName().startsWith("net.minecraft")) {
                            put(method, mv);
                            mv.visitMethodInsn(INVOKESPECIAL, superClass.getName().replace(".", "/"), name, descriptor, false);
                        } else {
                            if (sort == 0) {
                                mv.visitInsn(RETURN);
                            } else if (sort == 1) {
                                mv.visitInsn(ICONST_1);
                                mv.visitInsn(I2B);
                                mv.visitInsn(IRETURN);
                            } else if (sort == 2) {
                                mv.visitInsn(ICONST_0);
                                mv.visitInsn(I2C);
                                mv.visitInsn(IRETURN);
                            } else if (sort == 3) {
                                mv.visitInsn(ICONST_0);
                                mv.visitInsn(I2B);
                                mv.visitInsn(IRETURN);
                            } else if (sort == 4) {
                                mv.visitInsn(ICONST_0);
                                mv.visitInsn(I2S);
                                mv.visitInsn(IRETURN);
                            } else if (sort == 5) {
                                mv.visitInsn(ICONST_0);
                                mv.visitInsn(IRETURN);
                            } else if (sort == 6) {
                                mv.visitInsn(FCONST_0);
                                mv.visitInsn(FRETURN);
                            } else if (sort == 7) {
                                mv.visitInsn(LCONST_0);
                                mv.visitInsn(LRETURN);
                            } else if (sort == 8) {
                                mv.visitInsn(DCONST_0);
                                mv.visitInsn(DRETURN);
                            } else if (sort == 9) {
                                mv.visitInsn(ACONST_NULL);
                                mv.visitInsn(ARETURN);
                            } else if (sort == 10) {
                                Type elementType = returnType.getElementType();
                                int sort1 = elementType.getSort();
                                if (sort1 >= 1 && sort1 <= 8) {
                                    mv.visitInsn(ICONST_0);
                                    mv.visitIntInsn(NEWARRAY, getOpcode(sort1, T_INT));
                                    mv.visitInsn(ARETURN);
                                } else if (sort1 == 9) {
                                    mv.visitInsn(ICONST_0);
                                    mv.visitTypeInsn(ANEWARRAY, elementType.getDescriptor());
                                    mv.visitInsn(ARETURN);
                                }
                            }
                        }
                        mv.visitLabel(labelNode);
                        mv.visitFrame(F_SAME, 0, null, 0, null);

                        put(method, mv);
                        mv.visitMethodInsn(INVOKESPECIAL, klass.getName().replace(".", "/"), name, descriptor, false);
                        mv.visitInsn(returnType.getOpcode(IRETURN));

                        mv.visitEnd();
                    }
                }
            }
            if (superClass.getName().startsWith("net.minecraft")) break;

            klass = superClass;
        } while (klass.getSuperclass() != Entity.class);

        target.visitEnd();
    }

    public static int getOpcode(int sort,int opcode) {
        return switch (sort) {
            case 1 -> opcode - 6;
            case 2 -> opcode - 5;
            case 3 -> opcode - 2;
            case 4 -> opcode - 1;
            case 5 -> opcode;
            case 6 -> opcode - 4;
            case 7 -> opcode + 1;
            case 8 -> opcode - 3;
            default -> throw new UnsupportedOperationException();
        };
    }

    private static void put(final Method method,MethodVisitor mv) {
        Class<?>[] classes = method.getParameterTypes();
        int i1 = 0;
        if (Modifier.isStatic(method.getModifiers())) --i1;
        for (int i = 0; i < classes.length; i++) {
            Type type = Type.getType(classes[i]);
            i1 += type.getSize();
            mv.visitVarInsn(type.getOpcode(ILOAD), i1);
        }
    }

    private static String getDesc(final Method method) {
        Class<?>[] classes = method.getParameterTypes();
        StringBuilder sb = new StringBuilder();
        Type returnType =  Type.getReturnType(method);
        sb.append("(");
        for (int i = classes.length - 1; i >= 0; --i) {
            Type type = Type.getType(classes[i]);
            if (type.getSort() == 4) sb.append("[");
            sb.append(type.getDescriptor());
        }
        sb.append(")");
        if (returnType.getSort() == 5) sb.append("[");
        sb.append(returnType.getDescriptor());
        return sb.toString();
    }

    private static class ASMClassLoader extends ClassLoader {
        private ASMClassLoader() {
            super(null);
        }

        @Override
        protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
            return Class.forName(name, resolve, Thread.currentThread().getContextClassLoader());
        }

        Class<?> define(String name, byte[] data) {
            return defineClass(name, data, 0, data.length);
        }
    }
}
