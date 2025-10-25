package sen.manaita_plus_legacy.common.util;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacySynchedDataCore;

public enum ManaitaPlusLegacyEntityData {
    manaita(),
    death(),
    remove();

    public static final String cName = "manaita_plus_legacy_type";
    public static final EntityDataAccessor<Integer> Type = ManaitaPlusLegacySynchedDataCore.get();

    ManaitaPlusLegacyEntityData() {
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


    private final String name;
    private final int flag;

    public void add(Entity entity) {
        if (entity == null)
            return;
        entity.addTag(name);

        if (entity.getEntityData().hasItem(Type)) entity.getEntityData().set(Type, entity.getEntityData().get(Type) | flag);
        entity.getPersistentData().putInt(cName, flag);

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

    public void remove(Entity entity) {
        if (entity == null) return;
        entity.removeTag(name);
        if (entity.getEntityData().hasItem(Type)) entity.getEntityData().set(Type, entity.getEntityData().get(Type) & ~flag);
        entity.getPersistentData().putInt(cName, entity.getPersistentData().getInt(cName) & ~flag);

//        if (entity.level() instanceof ServerLevel serverLevel) {
//            serverLevel.getPlayers(p -> {
//                Networking.INSTANCE.send(
//                        PacketDistributor.PLAYER.with(() -> p),
//                        new MessageEntityData(entity.getId(), -flag));
//                return false;
//            });
//        }
//        if (entity instanceof LivingEntity livingEntity && livingEntity.getAttributes().hasAttribute(ManaitaPlusAttributeCore.Type.get())) {
//            try {
//                AttributeInstance attribute = livingEntity.getAttribute(ManaitaPlusAttributeCore.Type.get());
//                if (attribute != null) {
//                    int baseValue = (int) attribute.getBaseValue();
//                    if ((baseValue & flag) != 0) {
//                        attribute.setBaseValue(baseValue - flag);
//                    }
//                }
//            } catch (RuntimeException e) {
//            }
//        }
    }


    public boolean accept(Entity entity) {
        if (entity == null) return false;
        return entity.getTags().contains(name) || (entity.getPersistentData().getInt(cName) & flag) != 0 ||
                (entity.getEntityData().hasItem(Type) && (entity.getEntityData().get(Type) & flag) != 0);

//        if (entity instanceof LivingEntity livingEntity) {
//            return livingEntity.getAttributes() != null && livingEntity.getAttributes().hasAttribute(ManaitaPlusAttributeCore.Type.get()) && (((int) livingEntity.getAttribute(ManaitaPlusAttributeCore.Type.get()).getBaseValue()) & this.flag) != 0;
//        }
    }

    public boolean acceptSafe(Entity entity) {
        if (entity == null) return false;
        try {
            return entity.getTags().contains(name) || (entity.getPersistentData().getInt(cName) & flag) != 0;
        } catch (Exception ex) {
            return false;
        }
    }

    public int getFlag() {
        return flag;
    }
}