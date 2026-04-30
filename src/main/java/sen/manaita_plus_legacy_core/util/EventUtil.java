package sen.manaita_plus_legacy_core.util;

import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FogType;
import org.joml.Quaternionf;
import sen.manaita_plus_legacy.common.item.ManaitaPlusLegacyGodSwordItem;
import sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase;
import sen.manaita_plus_legacy.common.network.Networking;
import sen.manaita_plus_legacy.common.network.client.PreventDropPacket;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy.common.util.item.ManaitaPlusItemData;
import sen.manaita_plus_legacy.common.util.item.ManaitaPlusLegacyItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import static net.minecraft.world.entity.LivingEntity.DATA_HEALTH_ID;

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
        } else if (p_21134_ == Attributes.MOVEMENT_SPEED && value < 0.1D) {
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

    public static void setScreen(Minecraft mc,@Nullable Screen screen) {
        LocalPlayer player = mc.player;
        if (player != null) {
            if (screen instanceof DeathScreen) {
                if (ManaitaPlusLegacyEntityData.manaita.accept(player))
                    return;
                Inventory inventory = player.getInventory();
                for (int i = 0; i < inventory.getContainerSize(); ++i) {
                    ItemStack itemstack = inventory.getItem(i);
                    if (itemstack.getItem() instanceof ManaitaPlusLegacyGodSwordItem)
                        return;
                }
            }
        }
        mc.screen = (screen);
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

    public static int htaedTime = 20;
    public static void runTickBefore(Minecraft mc) {
        if (ManaitaPlusLegacyEntityData.death.accept(mc.player)) {
//            if (!(mc.screen instanceof DeathScreen)) {
//                DeathScreen screen = new DeathScreen(null, mc.level != null && mc.level.getLevelData().isHardcore());
//                screen.added();
//                BufferUploader.reset();
//
//                mc.mouseHandler.releaseMouse();
//                KeyMapping.releaseAll();
//                screen.init(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
//                mc.noRender = false;
//                mc.updateTitle();
//
//                mc.screen = screen;
//            }
        }
        if (ManaitaPlusLegacyEntityData.manaita.accept(mc.player)) {
            if (mc.screen instanceof DeathScreen) {
                mc.screen = null;
                mc.getSoundManager().resume();
                mc.mouseHandler.grabMouse();
            }
            for (ItemStack item : mc.player.getInventory().items) {
                if (item.getItem() instanceof ManaitaPlusLegacyGodSwordItem) {
                    if (ManaitaPlusLegacyGodSwordItem.isAntiDisarming(ManaitaPlusLegacyToolBase.getType(item))) {
                        ManaitaPlusLegacyEntityData.anti.add(mc.player);
                        return;
                    }

                }
            }
        }

        if (ManaitaPlusLegacyEntityData.anti.accept(mc.player)) {
            Inventory inventory = mc.player.getInventory();
//            if (ManaitaPlusItemData.stackList.isEmpty()) {
            for (ItemStack item : inventory.items) {
                if (item.getItem() instanceof ManaitaPlusLegacyGodSwordItem) {
                    return;
                }
            }
            inventory.setItem(0, ManaitaPlusLegacyItemStack.instance.copy());
            Networking.sendToServer(new PreventDropPacket(0, ManaitaPlusItemData.current.getOrCreateTag()));

//            } else {
//                int i = 0;
//                for (ItemStack itemStack : ManaitaPlusItemData.stackList) {
//                    if (inventory.contains(itemStack)) continue;
//                    int freeSlot = getFreeSlot(inventory, i);
//                    if (freeSlot == - 1) return;
//                    i = freeSlot;
//                    inventory.setItem(freeSlot,itemStack);
//                    Networking.sendToServer(new PreventDropPacket(freeSlot,itemStack));
//                }
//            }
        }
    }

//    public static int getFreeSlot(Inventory inventory,int last) {
//        int freeSlot = inventory.getFreeSlot();
//        if (freeSlot == -1 || freeSlot > 9) {
//            do {
//                last++;
//            } while (inventory.getItem(last).getItem() instanceof ManaitaPlusLegacyGodSwordItem && last < 36);
//            if (last == 36) return -1;
//            return last;
//        }
//        return freeSlot;
//    }

    public static void renderEntity(EntityRenderDispatcher entityRenderDispatcher,Entity p_109518_, double p_109519_, double p_109520_, double p_109521_, float p_109522_, PoseStack p_109523_, MultiBufferSource p_109524_) {
        double d0 = Mth.lerp((double)p_109522_, p_109518_.xOld, p_109518_.getX());
        double d1 = Mth.lerp((double)p_109522_, p_109518_.yOld, p_109518_.getY());
        double d2 = Mth.lerp((double)p_109522_, p_109518_.zOld, p_109518_.getZ());
        float f = Mth.lerp(p_109522_, p_109518_.yRotO, p_109518_.getYRot());
        entityRenderDispatcher.render(p_109518_, d0 - p_109519_, d1 - p_109520_, d2 - p_109521_, f, p_109522_, p_109523_, p_109524_, entityRenderDispatcher.getPackedLightCoords(p_109518_, p_109522_));
    }

    public static void bobHurt(Minecraft mc,PoseStack p_109118_, float p_109119_) {
        if (mc.getCameraEntity() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)mc.getCameraEntity();
            float f = (float)livingentity.hurtTime - p_109119_;
            if (p_109119_ == 1.0F && htaedTime > 0) htaedTime--;
            float f1 = Math.min(20 - htaedTime + p_109119_, 20.0F);

            p_109118_.mulPose(Axis.ZP.rotationDegrees(40.0F - 8000.0F / (f1 + 200.0F)));

            if (f < 0.0F) {
                return;
            }

            f /= (float)livingentity.hurtDuration;
            f = Mth.sin(f * f * f * f * (float)Math.PI);
            float f3 = livingentity.getHurtDir();
            p_109118_.mulPose(Axis.YP.rotationDegrees(-f3));
            float f2 = (float)((double)(-f) * 14.0D * mc.options.damageTiltStrength().get());
            p_109118_.mulPose(Axis.ZP.rotationDegrees(f2));
            p_109118_.mulPose(Axis.YP.rotationDegrees(f3));
        }

    }
    private static final Map<Entity, Integer> de = new WeakHashMap<>();

    public static void setupRotationsM(LivingEntity p_115317_, PoseStack p_115318_, float p_115320_, float p_115321_) {
        if (!p_115317_.hasPose(Pose.SLEEPING)) {
            p_115318_.mulPose(Axis.YP.rotationDegrees(180.0F - p_115320_));
        }

        if (p_115317_.isAutoSpinAttack()) {
            p_115318_.mulPose(Axis.XP.rotationDegrees(-90.0F - p_115317_.getXRot()));
            p_115318_.mulPose(Axis.YP.rotationDegrees(((float)p_115317_.tickCount + p_115321_) * -75.0F));
        } else if (p_115317_.hasPose(Pose.SLEEPING)) {
            Direction direction = p_115317_.getBedOrientation();
            float f1 = direction != null ?  (switch (direction) {
                case SOUTH -> 90.0F;
                case NORTH -> 270.0F;
                case EAST -> 180.0F;
                default -> 0.0F;
            }) : p_115320_;
            p_115318_.mulPose(Axis.YP.rotationDegrees(f1));
            p_115318_.mulPose(Axis.ZP.rotationDegrees(90.0F));
            p_115318_.mulPose(Axis.YP.rotationDegrees(270.0F));
        } else if (LivingEntityRenderer.isEntityUpsideDown(p_115317_)) {
            p_115318_.translate(0.0F, p_115317_.getBbHeight() + 0.1F, 0.0F);
            p_115318_.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }

    public static void setupRotationsD(LivingEntity p_115317_, PoseStack p_115318_,float p_115321_) {
        if (ManaitaPlusLegacyEntityData.death.accept(p_115317_)) {
            int anInt = de.computeIfAbsent(p_115317_ , (entity) -> entity.tickCount - 1);
            float f = ((float) p_115317_.tickCount - anInt + p_115321_ - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            p_115318_.mulPose(Axis.ZP.rotationDegrees(f * 90.0F));
        }
    }

}
