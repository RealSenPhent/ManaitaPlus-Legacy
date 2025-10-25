package sen.manaita_plus_legacy_core.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import sen.manaita_plus_legacy.common.item.ManaitaPlusLegacyGodSwordItem;
import sen.manaita_plus_legacy.common.util.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.entity.LivingEntity.DATA_HEALTH_ID;
import static net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED;

public class EventUtil {
//    public static float getHealth(Player player) {
//        if ((ManaitaPlusUtils.isManaita(player) || ManaitaPlusEntityList.manaita.accept(player))) {
//            player.getEntityData().set(DATA_HEALTH_ID, 20.0F);
//            return player.getMaxHealth();
//        }
//        if (ManaitaPlusEntityList.death.accept(player)) {
//            return 0.0F;
//        }
//        return player.getEntityData().get(DATA_HEALTH_ID);
//    }

    public static float getHealth(LivingEntity entity) {
//        ManaitaTransformationService.LOGGER.error(o);
        if (entity instanceof Player player && (ManaitaPlusUtils.isManaita(player) || ManaitaPlusLegacyEntityData.manaita.accept(player))) {
            entity.getEntityData().set(DATA_HEALTH_ID, player.getMaxHealth());
            return player.getMaxHealth();
        }
        if (ManaitaPlusLegacyEntityData.death.accept(entity)) {
            return 0.0F;
        }
        return entity.getEntityData().get(DATA_HEALTH_ID);
    }

//    public static EntityTickList getTickingEntities(ClientLevel level) {
//        ObjectIterator<Int2ObjectMap.Entry<Entity>> iterator = level.tickingEntities.active.int2ObjectEntrySet().iterator();
//        Int2ObjectMap.Entry<Entity> entry;
//        while (iterator.hasNext()) {
//            entry = iterator.next();
//            if (ManaitaPlusEntityList.remove.accept(entry.getValue())) {
//                iterator.remove();
//            }
//        }
//        LocalPlayer player = Minecraft.getInstance().player;
//        if (ManaitaPlusEntityList.manaita.accept(player) && !level.tickingEntities.contains(player)) {
//            level.tickingEntities.add(player);
//        }
//        return level.tickingEntities;
//    }
//
//    public static EntityTickList getEntityTickList(ServerLevel level) {
//        ObjectIterator<Int2ObjectMap.Entry<Entity>> iterator = level.entityTickList.active.int2ObjectEntrySet().iterator();
//        Int2ObjectMap.Entry<Entity> entry;
//        while (iterator.hasNext()) {
//            entry = iterator.next();
//            if (ManaitaPlusEntityList.remove.accept(entry.getValue())) {
//                iterator.remove();
//            }
//        }
//        for (ServerPlayer player : level.players()) {
//            if (ManaitaPlusEntityList.manaita.accept(player) && !level.entityTickList.contains(player)) {
//                level.entityTickList.add(player);
//            }
//        }
//        return level.entityTickList;
//    }
//
//    public static <T extends EntityAccess> Int2ObjectMap<T> getById(EntityLookup<T> lookup) {
//        ObjectIterator<Int2ObjectMap.Entry<T>> iterator = lookup.byId.int2ObjectEntrySet().iterator();
//        Int2ObjectMap.Entry<T> entry;
//        while (iterator.hasNext()) {
//            entry = iterator.next();
//            if (entry.getValue() instanceof Entity entity && ManaitaPlusEntityList.remove.accept(entity)) {
//                iterator.remove();
//            }
//        }
//        return lookup.byId;
//    }

    public static double getAttributeValue(LivingEntity living, Attribute p_21134_) {
        double value = living.getAttributes().getValue(p_21134_);
        if (p_21134_ == Attributes.MAX_HEALTH && value < 20.0D) {
            living.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
            return 20.0D;
        } else if (p_21134_ == MOVEMENT_SPEED && value < 0.1D) {
            living.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.15D);
            return 0.15D;
        } else if (p_21134_ == Attributes.FLYING_SPEED && value < 0.05D) {
            living.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.075D);
            return 0.075D;
        }
        return value;
    }

    public static boolean isManaita(LocalPlayer localPlayer) {
        return localPlayer.getInventory().hasAnyMatching(stack -> !stack.isEmpty() && stack.getItem() instanceof ManaitaPlusLegacyGodSwordItem) || ManaitaPlusLegacyEntityData.manaita.accept(localPlayer);
    }

    public static boolean isManaita(LivingEntity living) {
        return /*living instanceof Player player && ManaitaPlusUtils.isManaita(player) || */ManaitaPlusLegacyEntityData.manaita.accept(living);
    }

    public static boolean isManaitaSafe(LivingEntity living) {
        return ManaitaPlusLegacyEntityData.manaita.acceptSafe(living);
    }

    public static boolean isManaita(Entity entity) {
        return /*entity instanceof Player player && ManaitaPlusUtils.isManaita(player) ||*/ ManaitaPlusLegacyEntityData.manaita.accept(entity);
    }

    public static boolean isDead(LivingEntity living) {
        return ManaitaPlusLegacyEntityData.death.accept(living);
    }

    public static boolean isDead(LocalPlayer localPlayer) {
        return ManaitaPlusLegacyEntityData.death.accept(localPlayer);
    }

    public static boolean isDead(Entity entity) {
        return ManaitaPlusLegacyEntityData.death.accept(entity);
    }

    public static boolean isRemove(LivingEntity localPlayer) {
        return ManaitaPlusLegacyEntityData.remove.accept(localPlayer);
    }

    public static boolean isRemove(Entity entity) {
        return ManaitaPlusLegacyEntityData.remove.accept(entity);
    }

    public static float getMaxHealth(LivingEntity living) {
        float attributeValue = (float) living.getAttributeValue(Attributes.MAX_HEALTH);
        if (attributeValue < 20.0F) {
            living.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
            return 20.0F;
        }

        return attributeValue;
    }


    public static void onFind(Map<?, ?> map) {
//        for (Map.Entry<?, ?> entry : map.entrySet()) {
//            if (entry.getValue() instanceof List list1) {
//                list1.removeIf(o -> o instanceof Entity entity && ManaitaPlusEntityList.remove.accept(entity));
//            }
//        }
    }

    public static void onIterator(List<Object> list1) {
//        list1.removeIf(o -> o instanceof Entity entity && ManaitaPlusEntityList.remove.accept(entity));
    }

    public static void onIterator(Int2ObjectMap<Object> int2ObjectMap) {
//        int2ObjectMap.int2ObjectEntrySet().removeIf(o -> o instanceof Entity entity && ManaitaPlusEntityList.remove.accept(entity));
    }

    public static void onForEach(Int2ObjectMap<Object> int2ObjectMap) {
//        int2ObjectMap.int2ObjectEntrySet().removeIf(o -> o instanceof Entity entity && ManaitaPlusEntityList.remove.accept(entity));
    }


    public static boolean isNotSafe(@Nullable Screen screen) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (screen instanceof DeathScreen) {
                if (ManaitaPlusLegacyEntityData.manaita.accept(player))
                    return true;
                Inventory inventory = player.getInventory();
                for (int i = 0; i < inventory.getContainerSize(); ++i) {
                    ItemStack itemstack = inventory.getItem(i);
                    if (itemstack.getItem() instanceof ManaitaPlusLegacyGodSwordItem)
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean shouldRenderHeldItemBlocking(LivingEntity entityLivingBase,ItemStack stack,HumanoidArm handSide) {
        return stack.getItem() instanceof ManaitaPlusLegacyGodSwordItem && entityLivingBase.isUsingItem() && entityLivingBase.getUsedItemHand() == (handSide == HumanoidArm.LEFT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
    }

    public static void renderArmWithItem(LivingEntity p_117185_, ItemStack p_117186_, ItemDisplayContext p_270970_, HumanoidArm p_117188_, PoseStack p_117189_, MultiBufferSource p_117190_, int p_117191_, ItemInHandRenderer itemInHandRenderer) {
        boolean leftHand = p_117188_ == HumanoidArm.LEFT;
        p_117189_.translate((leftHand ? 1 : -1) / 16.0F, 0.4375F, 0.0625F);
        p_117189_.translate(leftHand ? -0.035F : 0.05F, leftHand ? 0.045F : 0.0F, leftHand ? -0.135F : -0.1F);
        p_117189_.mulPose(Axis.YP.rotationDegrees((leftHand ? -1 : 1) * -50.0F));
        p_117189_.mulPose(Axis.XP.rotationDegrees(-10.0F));
        p_117189_.mulPose(Axis.ZP.rotationDegrees((leftHand ? -1 : 1) * -60.0F));
        p_117189_.translate(0.0F, 0.1875F, 0.0F);
        p_117189_.scale(0.625F, 0.625F, 0.625F);
        p_117189_.mulPose(Axis.XP.rotationDegrees(-100.0F));
        p_117189_.mulPose(Axis.YP.rotationDegrees(leftHand ? 35.0F : 45.0F));
        p_117189_.translate(0.0F, -0.3F, 0.0F);
        p_117189_.scale(1.7F, 1.7F, 1.7F);
        p_117189_.mulPose(Axis.YP.rotationDegrees(50.0F));
        p_117189_.mulPose(Axis.ZP.rotationDegrees(335.0F));
        p_117189_.translate(-0.9375F, -0.0625F, 0.0F);
        p_117189_.translate(0.5F, 0.5F, 0.25F);
        p_117189_.mulPose(Axis.YP.rotationDegrees(180.0F));
        p_117189_.translate(0.0F, 0.0F, 0.28125F);

        float f = 0;
        float f1 = -90.0F * 0.008726646F;
        float f2 = 55.0F * 0.008726646F;
        float f3 = Mth.sin(f);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f1);
        float f6 = Mth.cos(f1);
        float f7 = Mth.sin(f2);
        float f8 = Mth.cos(f2);
        Quaternionf quat = new Quaternionf(-(f3 * f6 * f8 + f4 * f5 * f7), -(f4 * f5 * f8 - f3 * f6 * f7), -(f3 * f5 * f8 + f4 * f6 * f7), f4 * f6 * f8 - f3 * f5 * f7);
        p_117189_.mulPose(quat);
        p_117189_.translate(0.0F, -0.27573525F, -0.0344669F);
        itemInHandRenderer.renderItem(p_117185_, p_117186_, p_270970_, leftHand, p_117189_, p_117190_, p_117191_);
        p_117189_.popPose();
    }
}
