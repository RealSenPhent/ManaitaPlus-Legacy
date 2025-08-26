package sen.manaita_plus_core.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityLookup;
import net.minecraft.world.level.entity.EntityTickList;
import sen.manaita_plus.common.item.ManaitaPlusGodSwordItem;
import sen.manaita_plus.common.util.ManaitaPlusEntityList;
import sen.manaita_plus.common.util.ManaitaPlusUtils;

import static net.minecraft.world.entity.LivingEntity.DATA_HEALTH_ID;
import static net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED;

public class EventUtil {
    public static float getHealth(Player player) {
        if ((ManaitaPlusUtils.isManaita(player) || ManaitaPlusEntityList.player.accept(player))) {
            player.setHealth(20.0F);
            return player.getMaxHealth();
        }
        if (ManaitaPlusEntityList.death.accept(player)) {
            return 0.0F;
        }
        return player.getEntityData().get(DATA_HEALTH_ID);
    }

    public static float getHealth(LivingEntity entity) {
//        ManaitaTransformationService.LOGGER.error(o);
        if (entity instanceof Player player && (ManaitaPlusUtils.isManaita(player) || ManaitaPlusEntityList.player.accept(player))) {
            player.setHealth(20.0F);
            return player.getMaxHealth();
        }
        if (ManaitaPlusEntityList.death.accept(entity)) {
            return 0.0F;
        }
        return entity.getEntityData().get(DATA_HEALTH_ID);
    }

    public static EntityTickList getTickingEntities(ClientLevel level) {
        ObjectIterator<Int2ObjectMap.Entry<Entity>> iterator = level.tickingEntities.active.int2ObjectEntrySet().iterator();
        Int2ObjectMap.Entry<Entity> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (ManaitaPlusEntityList.remove.accept(entry.getValue())) {
                iterator.remove();
            }
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (ManaitaPlusEntityList.player.accept(player) && !level.tickingEntities.contains(player)) {
            level.tickingEntities.add(player);
        }
        return level.tickingEntities;
    }

    public static EntityTickList getEntityTickList(ServerLevel level) {
        ObjectIterator<Int2ObjectMap.Entry<Entity>> iterator = level.entityTickList.active.int2ObjectEntrySet().iterator();
        Int2ObjectMap.Entry<Entity> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (ManaitaPlusEntityList.remove.accept(entry.getValue())) {
                iterator.remove();
            }
        }
        for (ServerPlayer player : level.players()) {
            if (ManaitaPlusEntityList.player.accept(player) && !level.entityTickList.contains(player)) {
                level.entityTickList.add(player);
            }
        }
        return level.entityTickList;
    }

    public static <T extends EntityAccess> Int2ObjectMap<T> getById(EntityLookup<T> lookup) {
        ObjectIterator<Int2ObjectMap.Entry<T>> iterator =  lookup.byId.int2ObjectEntrySet().iterator();
        Int2ObjectMap.Entry<T> entry;
        while (iterator.hasNext()) {
            entry = iterator.next();
            if (entry.getValue() instanceof Entity entity && ManaitaPlusEntityList.remove.accept(entity)) {
                iterator.remove();
            }
        }
        return lookup.byId;
    }

    public static double getAttributeValue(LivingEntity living,Attribute p_21134_) {
        double value = living.getAttributes().getValue(p_21134_);
        if (p_21134_ == Attributes.MAX_HEALTH && value < 20.0D) {
            living.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
            return 20.0D;
        } else if (p_21134_ == MOVEMENT_SPEED && value < 0.1D) {
            living.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.15D);
            return 0.15D;
        }
        return value;
    }
    public static boolean isManaita(LocalPlayer localPlayer) {
        return localPlayer.getInventory().hasAnyMatching(stack -> !stack.isEmpty() && stack.getItem() instanceof ManaitaPlusGodSwordItem) || ManaitaPlusEntityList.player.accept(localPlayer);
    }

    public static boolean isManaita(LivingEntity living) {
        return living instanceof Player player && (ManaitaPlusUtils.isManaita(player) || ManaitaPlusEntityList.player.accept(player));
    }

    public static boolean isManaita(Entity entity) {
        return entity instanceof Player player && (ManaitaPlusUtils.isManaita(player) || ManaitaPlusEntityList.player.accept(player));
    }

    public static boolean isDead(LivingEntity living) {
        return ManaitaPlusEntityList.death.accept(living);
    }

    public static boolean isDead(LocalPlayer localPlayer) {
        return ManaitaPlusEntityList.death.accept(localPlayer);
    }

    public static boolean isDead(Entity entity) {
        return ManaitaPlusEntityList.death.accept(entity);
    }

    public static boolean isRemove(LivingEntity localPlayer) {
        return ManaitaPlusEntityList.remove.accept(localPlayer);
    }

    public static boolean isRemove(Entity entity) {
        return ManaitaPlusEntityList.remove.accept(entity);
    }

    public static float getMaxHealth(LivingEntity living) {
        float attributeValue = (float) living.getAttributeValue(Attributes.MAX_HEALTH);
        if (attributeValue > 20.0F) {
            living.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
            return 20.0F;
        }

        return attributeValue;
    }

}
