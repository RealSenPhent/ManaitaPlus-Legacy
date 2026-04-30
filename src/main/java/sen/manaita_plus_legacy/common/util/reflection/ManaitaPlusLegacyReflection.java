package sen.manaita_plus_legacy.common.util.reflection;

import sen.manaita_plus_legacy_core.util.Helper;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ManaitaPlusLegacyReflection {
    public static Method declaredMethod;

    public static Object get(Field f, Object instance) {
        Unsafe unsafe = Helper.UNSAFE;
        if (unsafe == null) return null;
        Object objectVolatile;
        if (Modifier.isStatic(f.getModifiers())) {
            if (f.getType() == int.class) objectVolatile = unsafe.getIntVolatile(instance, unsafe.staticFieldOffset(f));
            else if (f.getType() == boolean.class) objectVolatile = unsafe.getBooleanVolatile(instance, unsafe.staticFieldOffset(f));
            else if (f.getType() == float.class) objectVolatile = unsafe.getFloatVolatile(instance, unsafe.staticFieldOffset(f));
            else if (f.getType() == double.class) objectVolatile = unsafe.getDoubleVolatile(instance, unsafe.staticFieldOffset(f));
            else if (f.getType() == long.class) objectVolatile = unsafe.getLongVolatile(instance, unsafe.staticFieldOffset(f));
            else if (f.getType() == byte.class) objectVolatile = unsafe.getByteVolatile(instance, unsafe.staticFieldOffset(f));
            else if (f.getType() == short.class) objectVolatile = unsafe.getShortVolatile(instance, unsafe.staticFieldOffset(f));
            else if (f.getType() == char.class) objectVolatile = unsafe.getCharVolatile(instance, unsafe.staticFieldOffset(f));
            else objectVolatile = unsafe.getObjectVolatile(instance, unsafe.staticFieldOffset(f));
        } else {
            if (f.getType() == int.class) objectVolatile = unsafe.getIntVolatile(instance, unsafe.objectFieldOffset(f));
            else if (f.getType() == boolean.class) objectVolatile = unsafe.getBooleanVolatile(instance, unsafe.objectFieldOffset(f));
            else if (f.getType() == float.class) objectVolatile = unsafe.getFloatVolatile(instance, unsafe.objectFieldOffset(f));
            else if (f.getType() == double.class) objectVolatile = unsafe.getDoubleVolatile(instance, unsafe.objectFieldOffset(f));
            else if (f.getType() == long.class) objectVolatile = unsafe.getLongVolatile(instance, unsafe.objectFieldOffset(f));
            else if (f.getType() == byte.class) objectVolatile = unsafe.getByteVolatile(instance, unsafe.objectFieldOffset(f));
            else if (f.getType() == short.class) objectVolatile = unsafe.getShortVolatile(instance, unsafe.objectFieldOffset(f));
            else if (f.getType() == char.class) objectVolatile = unsafe.getCharVolatile(instance, unsafe.objectFieldOffset(f));
            else objectVolatile = unsafe.getObjectVolatile(instance, unsafe.objectFieldOffset(f));
        }
        return objectVolatile;
    }
}
