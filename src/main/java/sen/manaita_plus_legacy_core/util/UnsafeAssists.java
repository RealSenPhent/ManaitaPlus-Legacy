package sen.manaita_plus_legacy_core.util;

import sun.misc.Unsafe;
import sun.reflect.ReflectionFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class UnsafeAssists {
    public static final Unsafe UNSAFE = getUnsafe();
    public static final MethodHandles.Lookup LOOKUP = getLookup();
    private static MethodHandle objectFieldOffsetInternal;
    private static MethodHandle staticFieldBaseInternal;
    private static MethodHandle staticFieldOffsetInternal;

    static {
        Object internalUNSAFE = getInternalUNSAFE();
        try {
            Class<?> internalUNSAFEClass = LOOKUP.findClass("jdk.internal.misc.Unsafe");
            objectFieldOffsetInternal = LOOKUP.findVirtual(internalUNSAFEClass, "objectFieldOffset", MethodType.methodType(long.class, Field.class)).bindTo(internalUNSAFE);
            staticFieldBaseInternal = LOOKUP.findVirtual(internalUNSAFEClass, "staticFieldBase", MethodType.methodType(Object.class, Field.class)).bindTo(internalUNSAFE);
            staticFieldOffsetInternal = LOOKUP.findVirtual(internalUNSAFEClass, "staticFieldOffset", MethodType.methodType(long.class, Field.class)).bindTo(internalUNSAFE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getInternalUNSAFE() {
        try {
            Class<?> clazz = LOOKUP.findClass("jdk.internal.misc.Unsafe");
            return LOOKUP.findStatic(clazz, "getUnsafe", MethodType.methodType(clazz)).invoke();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Unsafe getUnsafe() {
        try {
            Constructor<Unsafe> c = Unsafe.class.getDeclaredConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (Throwable var3) {
            throw new RuntimeException("Failed to initialize Unsafe class", var3);
        }
    }

    public static MethodHandles.Lookup getLookup() {
        try {
            return (MethodHandles.Lookup) ReflectionFactory.getReflectionFactory().newConstructorForSerialization(MethodHandles.Lookup.class, MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Class.class, Integer.TYPE)).newInstance(Object.class, null, -1);
        } catch (Exception e) {
            return null;
        }
    }

    public static void allocateInstance(Class<?> clazz) {
        if (clazz.isPrimitive() || clazz.isEnum() || clazz.isInterface() || clazz.isRecord() || (clazz.getModifiers() & Modifier.ABSTRACT) != 0) {
            return;
        }
        if (clazz.isArray()) {
            allocateInstance(clazz.getComponentType());
        } else {
            try {
                UNSAFE.allocateInstance(clazz);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static long objectFieldOffset(Field f) {
        try {
            Class<?> declaringClass = f.getDeclaringClass();
            if (declaringClass.isHidden() || declaringClass.isRecord()) {
                return objectFieldOffset0(f);
            }
            return UNSAFE.objectFieldOffset(f);
        } catch (Throwable e) {
            return objectFieldOffset0(f);
        }
    }

    private static long objectFieldOffset0(Field f) {
        try {
            return (long) objectFieldOffsetInternal.invoke(f);
        } catch (Throwable t1) {
            t1.printStackTrace();
        }
        return 0L;
    }

    public static Object staticFieldBase(Field f) {
        try {
            Class<?> declaringClass = f.getDeclaringClass();
            if (declaringClass.isHidden() || declaringClass.isRecord()) {
                return staticFieldBase0(f);
            }
            return UNSAFE.staticFieldBase(f);
        } catch (Throwable e) {
            return staticFieldBase0(f);
        }
    }

    private static Object staticFieldBase0(Field f) {
        try {
            return staticFieldBaseInternal.invoke(f);
        } catch (Throwable t1) {
            t1.printStackTrace();
        }
        return null;
    }

    public static long staticFieldOffset(Field f) {
        try {
            Class<?> declaringClass = f.getDeclaringClass();
            if (declaringClass.isHidden() || declaringClass.isRecord()) {
                return staticFieldOffset0(f);
            }
            return UNSAFE.staticFieldOffset(f);
        } catch (Throwable e) {
            return staticFieldOffset0(f);
        }
    }

    private static long staticFieldOffset0(Field f) {
        try {
            return (long) staticFieldOffsetInternal.invoke(f);
        } catch (Throwable t1) {
            t1.printStackTrace();
        }
        return 0L;
    }
}
