package sen.manaita_plus_legacy.common.util.entity;

import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public enum ManaitaPlusLegacyEntities {
    fall(1 << 0);
    ManaitaPlusLegacyEntities(int flag) {
        this.flag = flag;
    }

    private final int flag;
    private final Map<Entity, Boolean> entityBooleanMap = new WeakHashMap<>();
    private final Map<String, Boolean> entityClassBooleanMap = new HashMap<>();

    public void add(Entity entity) {
        if (entity == null || accept(entity))
            return;
        entityBooleanMap.put(entity, Boolean.TRUE);
        entityClassBooleanMap.put(entity.getClass().getName(), Boolean.TRUE);
   }

    public boolean accept(Object entity) {
        if (!(entity instanceof Entity)) return false;

        return entityClassBooleanMap.containsKey(entity.getClass().getName()) || entityBooleanMap.containsKey(entity);
    }

    public int getFlag() {
        return flag;
    }

    public void clear() {
        entityBooleanMap.clear();
        entityClassBooleanMap.clear();
    }
}