package sen.manaita_plus_legacy.common.core;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;

import java.lang.reflect.Field;

public class ManaitaPlusLegacySynchedDataCore {
    public static int i = -1;

    public static void init() {
        try {
            Field declaredField = SynchedEntityData.class.getDeclaredField("f_135343_");
            declaredField.setAccessible(true);
            Object2IntMap<Class<? extends Entity>> classObject2IntMap = (Object2IntMap<Class<? extends Entity>>) declaredField.get(SynchedEntityData.class);
            if (classObject2IntMap.isEmpty()) {
                i = 1;
                classObject2IntMap.put(Entity.class, 1);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace(System.err);
        }
    }

    public static EntityDataAccessor<Integer> get() {
//        try {
//            Field declaredField = SynchedEntityData.class.getDeclaredField("f_135343_");
//            declaredField.setAccessible(true);
//            Object2IntMap<Class<? extends Entity>> classObject2IntMap = (Object2IntMap<Class<? extends Entity>>) declaredField.get(SynchedEntityData.class);
//            if (classObject2IntMap.values().stream().anyMatch(integer -> integer == i)) {
//                return null;
//            }
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace(System.err);
//        }
        return EntityDataSerializers.INT.createAccessor(i);
    }
}
