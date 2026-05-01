package sen.manaita_plus_legacy_core.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;

import static net.minecraft.world.entity.LivingEntity.DATA_HEALTH_ID;

public class CallBackHelper {
    public static float getHealth(LivingEntity entity) {
//        ManaitaTransformationService.LOGGER.error(o);
        if (entity instanceof Player player && (ManaitaPlusLegacyEntityData.manaita.accept(player))) {
            float max = Math.max(player.getMaxHealth(), 20.0F);
            entity.getEntityData().set(DATA_HEALTH_ID, max);

            return max;
        }
        if (ManaitaPlusLegacyEntityData.death.accept(entity)) {
            return 0.0F;
        }
        return entity.getEntityData().get(DATA_HEALTH_ID);
    }

    public static boolean isAlive(LivingEntity entity) {
        if (ManaitaPlusLegacyEntityData.manaita.accept(entity)) return true;
        else if (ManaitaPlusLegacyEntityData.death.accept(entity)) return false;
        return entity.isAlive();
    }

    public static boolean isDeadOrDying(LivingEntity entity) {
        if (ManaitaPlusLegacyEntityData.manaita.accept(entity)) return false;
        else if (ManaitaPlusLegacyEntityData.death.accept(entity)) return true;
        return entity.isDeadOrDying();
    }


}
