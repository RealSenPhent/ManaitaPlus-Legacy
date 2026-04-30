package sen.manaita_plus_legacy.common.util.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacySynchedDataCore;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public enum ManaitaPlusLegacyEntityData {
    manaita(1 << 0),
    death(1 << 1),
    remove(1 << 2),
    anti(1 << 3);

    public static final String cName = "manaita_plus_legacy_type";
    public static final EntityDataAccessor<Integer> Type = ManaitaPlusLegacySynchedDataCore.get();

    ManaitaPlusLegacyEntityData(int flag) {
        this.flag = flag;
        tag = "ManaitaPlusLegacy" + flag;
    }


    private final int flag;
    private final String tag;
    private final Map<UUID, Boolean> uuidBooleanMap = new WeakHashMap<>();
    private final Map<Integer, Boolean> idBooleanMap = new WeakHashMap<>();
    private final Map<Entity, Boolean> entityBooleanMap = new WeakHashMap<>();

    public void add(Entity entity) {
        if (entity == null)
            return;

        if (entity.getEntityData().hasItem(Type)) entity.getEntityData().set(Type, entity.getEntityData().get(Type) | flag);
        entity.getPersistentData().putInt(cName, entity.getPersistentData().getInt(cName) | flag);
        uuidBooleanMap.put(entity.getUUID(), Boolean.TRUE);
        idBooleanMap.put(entity.getId(), Boolean.TRUE);
        entityBooleanMap.put(entity, Boolean.TRUE);

//        if (entity instanceof LivingEntity livingEntity) {
//            AttributeInstance attribute = livingEntity.getAttribute(ManaitaPlusAttributeCore.Type.get());
//
//            if (attribute != null) {
//                int baseValue = (int) attribute.getBaseValue();
//                if ((baseValue & flag) == 0) {
//                    attribute.setBaseValue(baseValue | flag);
//                }
//            }
//        }
    }

    public void putInt(Entity entity,int i) {
        entity.getPersistentData().putInt(tag, i);
    }

    public int getInt(Entity entity) {
        return entity.getPersistentData().getInt(tag);
    }

    public void remove(Entity entity) {
        if (entity == null) return;
        if (entity.getEntityData().hasItem(Type)) entity.getEntityData().set(Type, entity.getEntityData().get(Type) & ~flag);
        entity.getPersistentData().putInt(cName, entity.getPersistentData().getInt(cName) & ~flag);
        uuidBooleanMap.remove(entity.getUUID());
        idBooleanMap.remove(entity.getId());
        entityBooleanMap.remove(entity);
    }


    public boolean accept(Entity entity) {
        if (entity == null) return false;
        if (uuidBooleanMap.containsKey(entity.getUUID()) || idBooleanMap.containsKey(entity.getId())) return true;

        return (entity.getPersistentData().getInt(cName) & flag) != 0 ||
                (entity.getEntityData().hasItem(Type) && (entity.getEntityData().get(Type) & flag) != 0);

//        if (entity instanceof LivingEntity livingEntity) {
//            return livingEntity.getAttributes() != null && livingEntity.getAttributes().hasAttribute(ManaitaPlusAttributeCore.Type.get()) && (((int) livingEntity.getAttribute(ManaitaPlusAttributeCore.Type.get()).getBaseValue()) & this.flag) != 0;
//        }
    }

    public boolean acceptSide(Entity entity) {
        if (entity == null) return false;
        if (uuidBooleanMap.containsKey(entity.getUUID()) || idBooleanMap.containsKey(entity.getId()) || entityBooleanMap.containsKey(entity)) return true;

        return(entity.getPersistentData().getInt(cName) & flag) != 0 ||
                (entity.getEntityData().hasItem(Type) && (entity.getEntityData().get(Type) & flag) != 0);
    }

    public int getFlag() {
        return flag;
    }

    public void clear() {
        idBooleanMap.clear();
        uuidBooleanMap.clear();
        entityBooleanMap.clear();
    }

    public Map<Integer, Boolean> getIdBooleanMap() {
        return idBooleanMap;
    }

    public Map<UUID, Boolean> getUuidBooleanMap() {
        return uuidBooleanMap;
    }
}