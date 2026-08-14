package sen.manaita_plus_legacy_core.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class BacktrackingUtils {
    // 字段类型常量
    private static final byte TYPE_OBJECT = 0;
    private static final byte TYPE_INT = 1;
    private static final byte TYPE_BOOLEAN = 2;
    private static final byte TYPE_DOUBLE = 3;
    private static final byte TYPE_FLOAT = 4;
    private static final byte TYPE_LONG = 5;
    private static final byte TYPE_SHORT = 6;
    private static final byte TYPE_CHAR = 7;
    private static final byte TYPE_BYTE = 8;

    // 核心存储：存活追踪对象 -> 字段快照
    public static final IdentityHashMap<Object, Tracked> liveTrackers = new IdentityHashMap<>();

    // 自动清理机制
    private static final ReferenceQueue<Object> queue = new ReferenceQueue<>();
    private static final IdentityHashMap<Reference<?>, Object> refToObject = new IdentityHashMap<>();

    // 字段元数据缓存（仅实例字段）
    private static final Map<Class<?>, FieldMeta[]> fieldMetaMap = new IdentityHashMap<>();

    // 不可变类型集合 —— 这些类型直接返回原对象，不深拷贝
    private static final Set<Class<?>> IMMUTABLE_CLASSES = Set.of(
            String.class,
            Integer.class, Long.class, Short.class, Byte.class,
            Character.class, Boolean.class, Float.class, Double.class,
            BigInteger.class, BigDecimal.class
    );

    /**
     * 注册实例对象的所有实例字段
     */
    public static synchronized void newObject(Object object) {
        if (object == null) return;
        expungeStaleEntries();
        if (liveTrackers.containsKey(object)) return;

        Class<?> clazz = object.getClass();

        // 枚举不可变，跳过
        if (clazz.isEnum()) return;

        // 数组注册
        if (clazz.isArray()) {
            TrackedArray tracker = new TrackedArray();
            liveTrackers.put(object, tracker);
            WeakReference<Object> ref = new WeakReference<>(object, queue);
            refToObject.put(ref, object);
            return;
        }

        // 普通对象：确保类已初始化（Unsafe.allocateInstance 会触发初始化）
        UnsafeAssists.allocateInstance(clazz);

        FieldMeta[] metas = getFieldMetas(clazz);
        if (metas.length == 0) return;

        TrackedObject tracker = new TrackedObject(metas);
        liveTrackers.put(object, tracker);
        WeakReference<Object> ref = new WeakReference<>(object, queue);
        refToObject.put(ref, object);
    }

    /**
     * 注册类的所有静态字段
     */
    public static synchronized void clinit(Class<?> clazz) {
        UnsafeAssists.allocateInstance(clazz);
        loadStaticFields(clazz);
    }

    /**
     * 记录所有追踪对象的当前快照
     */
    public static synchronized void record() {
        expungeStaleEntries();
        IdentityHashMap<Object, Object> cache = new IdentityHashMap<>();
        for (Map.Entry<Object, Tracked> entry : liveTrackers.entrySet()) {
            Tracked tracker = entry.getValue();
            tracker.recordSnapshot(entry.getKey(), cache);
        }
    }

    /**
     * 回溯所有追踪对象到最近一次快照
     */
    public static synchronized void backtrack() {
        expungeStaleEntries();
        for (Map.Entry<Object, Tracked> entry : liveTrackers.entrySet()) {
            Tracked tracker = entry.getValue();
            tracker.restoreSnapshot(entry.getKey());
        }
    }

    // 便捷的 Unsafe put 方法（类型精确匹配）
    public static void put(Object base, long fieldOffset, Object snapshot, Class<?> type) {
        if (type == int.class) UnsafeAssists.UNSAFE.putInt(base, fieldOffset, (Integer) snapshot);
        else if (type == boolean.class) UnsafeAssists.UNSAFE.putBoolean(base, fieldOffset, (Boolean) snapshot);
        else if (type == double.class) UnsafeAssists.UNSAFE.putDouble(base, fieldOffset, (Double) snapshot);
        else if (type == float.class) UnsafeAssists.UNSAFE.putFloat(base, fieldOffset, (Float) snapshot);
        else if (type == long.class) UnsafeAssists.UNSAFE.putLong(base, fieldOffset, (Long) snapshot);
        else if (type == short.class) UnsafeAssists.UNSAFE.putShort(base, fieldOffset, (Short) snapshot);
        else if (type == char.class) UnsafeAssists.UNSAFE.putChar(base, fieldOffset, (Character) snapshot);
        else if (type == byte.class) UnsafeAssists.UNSAFE.putByte(base, fieldOffset, (Byte) snapshot);
        else UnsafeAssists.UNSAFE.putObject(base, fieldOffset, snapshot);
    }

    // ------------------------------------------
    // 内部辅助方法
    // ------------------------------------------

    private static byte getTypeCode(Class<?> type) {
        if (type == int.class) return TYPE_INT;
        if (type == boolean.class) return TYPE_BOOLEAN;
        if (type == double.class) return TYPE_DOUBLE;
        if (type == float.class) return TYPE_FLOAT;
        if (type == long.class) return TYPE_LONG;
        if (type == short.class) return TYPE_SHORT;
        if (type == char.class) return TYPE_CHAR;
        if (type == byte.class) return TYPE_BYTE;
        return TYPE_OBJECT;
    }

    private static void expungeStaleEntries() {
        Reference<?> ref;
        while ((ref = queue.poll()) != null) {
            Object obj = refToObject.remove(ref);
            if (obj != null) {
                liveTrackers.remove(obj);
            }
        }
    }

    private static FieldMeta[] getFieldMetas(Class<?> clazz) {
        FieldMeta[] cached = fieldMetaMap.get(clazz);
        if (cached != null) return cached;

        List<FieldMeta> list = new ArrayList<>();
        Class<?> current = clazz;
        do {
            for (Field f : current.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers())) continue;
                long offset = UnsafeAssists.objectFieldOffset(f);
                byte type = getTypeCode(f.getType());
                if (offset == 0L) {
                    System.err.println("Invalid offset for field: " + f);
                    continue;
                }
                list.add(new FieldMeta(offset, type));
            }
            current = current.getSuperclass();
        } while (current != null && current != Object.class);

        FieldMeta[] arr = list.toArray(new FieldMeta[0]);
        fieldMetaMap.put(clazz, arr);
        return arr;
    }

    private static void loadStaticFields(Class<?> clazz) {
        if (fieldMetaMap.containsKey(clazz)) return; // 避免重复

        List<FieldMeta> list = new ArrayList<>();
        Class<?> current = clazz;
        Field lastField = null;
        do {
            if (current.getName().startsWith("net.minecraft")) break;
            for (Field f : current.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers())) continue;
                long offset = UnsafeAssists.staticFieldOffset(f);
                byte type = getTypeCode(f.getType());
                if (offset == 0L) {
                    System.err.println("Invalid offset for static field: " + f);
                    continue;
                }
                list.add(new FieldMeta(offset, type));
                lastField = f;
            }

            FieldMeta[] arr = list.toArray(new FieldMeta[0]);
            fieldMetaMap.put(current, null); // 标记已处理，避免重复添加
            if (arr.length == 0) return;

            TrackedObject tracker = new TrackedObject(arr);
            // 静态字段的 base 是 Class 对象
            liveTrackers.put(UnsafeAssists.staticFieldBase(lastField), tracker);
            list.clear();
            current = current.getSuperclass();
        } while (current != null && current != Object.class);
    }

    // 深拷贝核心方法
    private static Object copyOfValue(Object target, IdentityHashMap<Object, Object> cache) {
        if (target == null) return null;
        Class<?> clazz = target.getClass();
        // 不可变类型直接返回自身
        if (clazz == Class.class || clazz.isInterface() || clazz.isEnum() || IMMUTABLE_CLASSES.contains(clazz)) {
            return target;
        }
        if (clazz.getName().startsWith("net.minecraft")) return target;

        // 被追踪的非数组对象：避免破坏身份，直接返回原对象
        if (liveTrackers.containsKey(target) && !clazz.isArray()) {
            if (!cache.containsKey(target)) {
                cache.put(target, target);
            }
            return target;
        }

        // 循环引用检查
        Object cached = cache.get(target);
        if (cached != null) return cached;

        if (clazz.isArray()) {
            int len = java.lang.reflect.Array.getLength(target);
            Object copy = java.lang.reflect.Array.newInstance(clazz.getComponentType(), len);
            cache.put(target, copy);
            for (int i = 0; i < len; i++) {
                Object elem = java.lang.reflect.Array.get(target, i);
                java.lang.reflect.Array.set(copy, i, copyOfValue(elem, cache));
            }
            return copy;
        }

        try {
            Object copy = UnsafeAssists.UNSAFE.allocateInstance(clazz);
            cache.put(target, copy);
            Class<?> current = clazz;
            do {
                for (Field f : current.getDeclaredFields()) {
                    if (Modifier.isStatic(f.getModifiers())) continue;
                    long offset = UnsafeAssists.objectFieldOffset(f);
                    if (offset == 0L) {
                        System.err.println("Invalid offset for field: " + f);
                        continue;
                    }
                    byte type = getTypeCode(f.getType());
                    copyField(target, copy, offset, type, cache);
                }
                current = current.getSuperclass();
            } while (current != null && current != Object.class);
            return copy;
        } catch (InstantiationException e) {
            e.printStackTrace(System.err);
        }
        return target;
    }

    private static void copyField(Object src, Object dest, long offset, byte type,
                                  IdentityHashMap<Object, Object> cache) {
        switch (type) {
            case TYPE_OBJECT -> {
                Object val = UnsafeAssists.UNSAFE.getObject(src, offset);
                UnsafeAssists.UNSAFE.putObject(dest, offset, copyOfValue(val, cache));
            }
            case TYPE_INT -> UnsafeAssists.UNSAFE.putInt(dest, offset, UnsafeAssists.UNSAFE.getInt(src, offset));
            case TYPE_BOOLEAN -> UnsafeAssists.UNSAFE.putBoolean(dest, offset, UnsafeAssists.UNSAFE.getBoolean(src, offset));
            case TYPE_FLOAT -> UnsafeAssists.UNSAFE.putFloat(dest, offset, UnsafeAssists.UNSAFE.getFloat(src, offset));
            case TYPE_DOUBLE -> UnsafeAssists.UNSAFE.putDouble(dest, offset, UnsafeAssists.UNSAFE.getDouble(src, offset));
            case TYPE_LONG -> UnsafeAssists.UNSAFE.putLong(dest, offset, UnsafeAssists.UNSAFE.getLong(src, offset));
            case TYPE_SHORT -> UnsafeAssists.UNSAFE.putShort(dest, offset, UnsafeAssists.UNSAFE.getShort(src, offset));
            case TYPE_CHAR -> UnsafeAssists.UNSAFE.putChar(dest, offset, UnsafeAssists.UNSAFE.getChar(src, offset));
            case TYPE_BYTE -> UnsafeAssists.UNSAFE.putByte(dest, offset, UnsafeAssists.UNSAFE.getByte(src, offset));
        }
    }

    // ------------------------------------------
    // 内部数据结构
    // ------------------------------------------

    private static class FieldMeta {
        final long offset;
        final byte type;

        FieldMeta(long offset, byte type) {
            this.offset = offset;
            this.type = type;
        }
    }

    private interface Tracked {
        void recordSnapshot(Object target, IdentityHashMap<Object, Object> cache);
        void restoreSnapshot(Object target);
    }

    private static class TrackedObject implements Tracked {
        // 原始类型字段
        private long[] offsets;
        private byte[] types;
        private long[] primSnapshots;
        private boolean[] primRecord;
        private int primCount;

        // 引用类型字段
        private long[] refOffsets;
        private Object[] refSnapshots;
        private boolean[] refRecord;
        private int refCount;

        TrackedObject() {
            this(8);
        }

        TrackedObject(int capacity) {
            offsets = new long[capacity];
            types = new byte[capacity];
            primSnapshots = new long[capacity];
            primRecord = new boolean[capacity];

            refOffsets = new long[capacity];
            refSnapshots = new Object[capacity];
            refRecord = new boolean[capacity];
        }

        TrackedObject(FieldMeta[] metas) {
            this();
            for (FieldMeta m : metas) addField(m.offset, m.type);
        }

        void addField(long offset, byte type) {
            if (type == TYPE_OBJECT) {
                ensureRefCapacity(refCount + 1);
                refOffsets[refCount] = offset;
                refCount++;
            } else {
                ensurePrimCapacity(primCount + 1);
                offsets[primCount] = offset;
                types[primCount] = type;
                primCount++;
            }
        }

        private void ensurePrimCapacity(int required) {
            if (required <= offsets.length) return;
            int newCap = Math.max(offsets.length * 2, required);
            offsets = resizeArray(offsets, newCap, primCount);
            types = resizeArray(types, newCap, primCount);
            primSnapshots = resizeArray(primSnapshots, newCap, primCount);
            primRecord = resizeArray(primRecord, newCap, primCount);
        }

        private void ensureRefCapacity(int required) {
            if (required <= refOffsets.length) return;
            int newCap = Math.max(refOffsets.length * 2, required);
            refOffsets = resizeArray(refOffsets, newCap, refCount);
            refSnapshots = resizeArray(refSnapshots, newCap, refCount);
            refRecord = resizeArray(refRecord, newCap, refCount);
        }

        @Override
        public void recordSnapshot(Object target, IdentityHashMap<Object, Object> cache) {
            cache.put(target, target); // 自身引用保持不变

            for (int i = 0; i < refCount; i++) {
                long offset = refOffsets[i];
                Object val = UnsafeAssists.UNSAFE.getObject(target, offset);
                refSnapshots[i] = copyOfValue(val, cache);
                refRecord[i] = true;
            }

            for (int i = 0; i < primCount; i++) {
                long offset = offsets[i];
                switch (types[i]) {
                    case TYPE_INT    -> primSnapshots[i] = UnsafeAssists.UNSAFE.getInt(target, offset);
                    case TYPE_BOOLEAN-> primSnapshots[i] = UnsafeAssists.UNSAFE.getBoolean(target, offset) ? 1 : 0;
                    case TYPE_DOUBLE -> primSnapshots[i] = Double.doubleToRawLongBits(UnsafeAssists.UNSAFE.getDouble(target, offset));
                    case TYPE_FLOAT  -> primSnapshots[i] = Float.floatToRawIntBits(UnsafeAssists.UNSAFE.getFloat(target, offset));
                    case TYPE_LONG   -> primSnapshots[i] = UnsafeAssists.UNSAFE.getLong(target, offset);
                    case TYPE_SHORT  -> primSnapshots[i] = UnsafeAssists.UNSAFE.getShort(target, offset);
                    case TYPE_CHAR   -> primSnapshots[i] = UnsafeAssists.UNSAFE.getChar(target, offset);
                    case TYPE_BYTE   -> primSnapshots[i] = UnsafeAssists.UNSAFE.getByte(target, offset);
                }
                primRecord[i] = true;
            }
        }

        @Override
        public void restoreSnapshot(Object target) {
            for (int i = 0; i < refCount; i++) {
                if (!refRecord[i]) continue;
                UnsafeAssists.UNSAFE.putObject(target, refOffsets[i], refSnapshots[i]);
            }
            for (int i = 0; i < primCount; i++) {
                if (!primRecord[i]) continue;
                long offset = offsets[i];
                switch (types[i]) {
                    case TYPE_INT    -> UnsafeAssists.UNSAFE.putInt(target, offset, (int) primSnapshots[i]);
                    case TYPE_BOOLEAN-> UnsafeAssists.UNSAFE.putBoolean(target, offset, primSnapshots[i] != 0);
                    case TYPE_DOUBLE -> UnsafeAssists.UNSAFE.putDouble(target, offset, Double.longBitsToDouble(primSnapshots[i]));
                    case TYPE_FLOAT  -> UnsafeAssists.UNSAFE.putFloat(target, offset, Float.intBitsToFloat((int) primSnapshots[i]));
                    case TYPE_LONG   -> UnsafeAssists.UNSAFE.putLong(target, offset, primSnapshots[i]);
                    case TYPE_SHORT  -> UnsafeAssists.UNSAFE.putShort(target, offset, (short) primSnapshots[i]);
                    case TYPE_CHAR   -> UnsafeAssists.UNSAFE.putChar(target, offset, (char) primSnapshots[i]);
                    case TYPE_BYTE   -> UnsafeAssists.UNSAFE.putByte(target, offset, (byte) primSnapshots[i]);
                }
            }
        }

        private static long[] resizeArray(long[] src, int newLen, int copySize) {
            long[] dest = new long[newLen];
            System.arraycopy(src, 0, dest, 0, copySize);
            return dest;
        }
        private static byte[] resizeArray(byte[] src, int newLen, int copySize) {
            byte[] dest = new byte[newLen];
            System.arraycopy(src, 0, dest, 0, copySize);
            return dest;
        }
        private static Object[] resizeArray(Object[] src, int newLen, int copySize) {
            Object[] dest = new Object[newLen];
            System.arraycopy(src, 0, dest, 0, copySize);
            return dest;
        }
        private static boolean[] resizeArray(boolean[] src, int newLen, int copySize) {
            boolean[] dest = new boolean[newLen];
            System.arraycopy(src, 0, dest, 0, copySize);
            return dest;
        }
    }

    private static class TrackedArray implements Tracked {
        private Object snapshot;

        @Override
        public void recordSnapshot(Object target, IdentityHashMap<Object, Object> cache) {
            snapshot = copyOfValue(target, cache); // 深拷贝整个数组
        }

        @Override
        public void restoreSnapshot(Object target) {
            int len = java.lang.reflect.Array.getLength(target);
            for (int i = 0; i < len; i++) {
                java.lang.reflect.Array.set(target, i, java.lang.reflect.Array.get(snapshot, i));
            }
        }
    }
}