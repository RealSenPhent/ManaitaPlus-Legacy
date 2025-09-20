package sen.manaita_plus_legacy.common.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import sen.manaita_plus_legacy.common.core.ManaitaPlusAttributeCore;

public enum ManaitaPlusEntityList {
    manaita,
    death,
    remove;


    ManaitaPlusEntityList() {
        flag = pow(2,ordinal());
        name = name();
    }

    public static int pow(int a, int b) {
        if(b == 0) return 1;
        int pow = 1;
        for (int i = 0; i < b; i++) {
            pow *= a;
        }
        return pow;
    }

//    private final Map<Entity, Object> entities = new WeakHashMap<>();
//    private final Map<UUID, Object> uuids = new WeakHashMap<>();

    private final String name;
    private final int flag;

    public void add(Entity entity) {
        if (entity == null) return;
        entity.addTag(name);

//        uuids.put(entity.getUUID(),Boolean.TRUE);
//        entities.put(entity,Boolean.TRUE);
        if (entity instanceof LivingEntity livingEntity) {
            AttributeInstance attribute = livingEntity.getAttribute(ManaitaPlusAttributeCore.Type.get());

            if (attribute != null) {
                int baseValue = (int) attribute.getBaseValue();
                if ((baseValue & flag) == 0) {
                    attribute.setBaseValue(baseValue | flag);
                }
            }
        }
    }

    public void remove(Entity entity) {
        if (entity == null) return;
        entity.removeTag(name);

//        uuids.remove(entity.getUUID());
//        entities.remove(entity);
        if (entity instanceof LivingEntity livingEntity) {
            AttributeInstance attribute = livingEntity.getAttribute(ManaitaPlusAttributeCore.Type.get());
            if (attribute != null) {
                int baseValue = (int) attribute.getBaseValue();
                if ((baseValue & flag) != 0) {
                    attribute.setBaseValue(baseValue - flag);
                }
            }
        }
    }


    public boolean accept(Entity entity) {
        if (entity == null) return false;
        if(/*entities.containsKey(entity) || uuids.containsKey(entity.getUUID()) ||*/ entity.getTags().contains(name)) return true;

        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.getAttributes() != null && livingEntity.getAttributes().hasAttribute(ManaitaPlusAttributeCore.Type.get())) {
                return ((int) livingEntity.getAttribute(ManaitaPlusAttributeCore.Type.get()).getBaseValue() & flag) != 0;
            }
        }
        return false;
    }

//    public Set<Entity> getEntities() {
//        return entities.keySet();
//    }
}
