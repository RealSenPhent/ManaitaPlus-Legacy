package sen.manaita_plus_legacy.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.client.core.ManaitaPlusLegacyKeyBoardCore;
import sen.manaita_plus_legacy.client.gui.BrewingStandScreen;
import sen.manaita_plus_legacy.client.gui.CraftingManaitaScreen;
import sen.manaita_plus_legacy.client.gui.FurnaceManaitaScreen;
import sen.manaita_plus_legacy.client.network.implement.*;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyBlockCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCurioCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyMenuCore;
import sen.manaita_plus_legacy.common.curios.CuriosUtil;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.common.item.tool.ManaitaPlusLegacyKatarItem;
import sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase;
import sen.manaita_plus_legacy.common.network.Networking;
import sen.manaita_plus_legacy.common.proxy.CommomProxy;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy.common.util.tag.ManaitaPlusLegacyTagData;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = ManaitaPlusLegacy.MODID,value = Dist.CLIENT)
public class EventHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    public static boolean inventoryRender = false;
    public static int renderTime;
    public static float renderFrame;

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (!Minecraft.getInstance().isPaused() && event.phase == TickEvent.Phase.END) {
            ++renderTime;
        }
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (!Minecraft.getInstance().isPaused() && event.phase == TickEvent.Phase.START) {
            renderFrame = event.renderTickTime;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void drawScreenPre(final ScreenEvent.Render.Pre e) {
        inventoryRender = true;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void drawScreenPost(final ScreenEvent.Render.Post e) {
        inventoryRender = false;
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (mc.player == null) return;
        if (ManaitaPlusLegacyKeyBoardCore.MESSAGE_KEY.isDown()) {
            ItemStack mainHandItem = mc.player.getMainHandItem();
            if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IManaitaPlusLegacyKey iManaitaPlusLegacyKey) {
                iManaitaPlusLegacyKey.onManaitaKeyPressOnClient(mainHandItem, mc.player, 0);
                Networking.sendToServer(new KeyPressPacket((byte) 0));
            }
        }
        if (ManaitaPlusLegacyKeyBoardCore.MESSAGE_ARMOR_KEY.isDown()) {
            for (ItemStack itemStack : mc.player.getInventory().armor) {
                if (!itemStack.isEmpty() && itemStack.getItem() instanceof IManaitaPlusLegacyKey key) {
                    key.onManaitaKeyPressOnClient(itemStack, mc.player, 1);
                }
            }
            Networking.sendToServer(new KeyPressPacket((byte) 1));
        }
        if (ManaitaPlusLegacyKeyBoardCore.MESSAGE_GOD_KEY.isDown()) {
            ItemStack mainHandItem = mc.player.getMainHandItem();
            if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IManaitaPlusLegacyKey iManaitaPlusLegacyKey) {
                iManaitaPlusLegacyKey.onManaitaKeyPressOnClient(mainHandItem, mc.player, 2);
                Networking.sendToServer(new KeyPressPacket((byte) 2));
            }
        }
        if (ManaitaPlusLegacyKeyBoardCore.MESSAGE_ANTI_DISARMING_KEY.isDown()) {
            ItemStack mainHandItem = mc.player.getMainHandItem();
            if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IManaitaPlusLegacyKey iManaitaPlusLegacyKey) {
                iManaitaPlusLegacyKey.onManaitaKeyPressOnClient(mainHandItem, mc.player, 3);
                Networking.sendToServer(new KeyPressPacket((byte) 3));
            }
        }
        if (ManaitaPlusLegacyKeyBoardCore.MESSAGE_ATTACK_KEY.isDown()) {
            if (ManaitaPlusLegacyEntityData.manaita.accept(mc.player)) {
                ManaitaPlusUtils.godKill(0, 3, mc.player.isShiftKeyDown(), mc.player);
                Networking.sendToServer(new KeyPressPacket((byte) 4));
            }
        }
        if (CommomProxy.curios) {
            if (ManaitaPlusLegacyKeyBoardCore.MESSAGE_OPEN_KEY.isDown()) {
                CuriosUtil.onKeyPress();
            }
        }
        if (ManaitaPlusLegacyKeyBoardCore.MESSAGE_ARMOR_TP_KEY.isDown()) {
            if (ManaitaPlusUtils.isManaitaArmor(mc.player)) {
                LocalPlayer entity = mc.player;
                double d0 = 512;
                HitResult hitResult = entity.pick(d0, mc.getPartialTick(), false);

                Vec3 vec3 = entity.getEyePosition(mc.getPartialTick());
                d0 *= d0;
                if (hitResult.getType() != HitResult.Type.MISS) {
                    d0 = hitResult.getLocation().distanceToSqr(vec3);
                }

                Vec3 vec31 = entity.getViewVector(1.0F);
                Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);

                AABB aabb = entity.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
                EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(entity, vec3, vec32, aabb, (p_234237_) -> !p_234237_.isSpectator() &&
                        p_234237_.isPickable(), d0);
                if (entityhitresult == null) {
                    if (hitResult instanceof BlockHitResult) {
                        Networking.sendToServer(new ArmorTPPacket());
                    }
                } else {
                    Networking.sendToServer(new ArmorTPPacket());
                }
            }
        }
    }


    @SubscribeEvent
    public static void onClick(InputEvent.InteractionKeyMappingTriggered event) {
        if (event.getKeyMapping() == mc.options.keyAttack && event.getHand() == InteractionHand.MAIN_HAND && event.isAttack()) {
            LocalPlayer entity = mc.player;
            if (entity != null) {
                ItemStack itemInHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
                Item item = itemInHand.getItem();
                if (item instanceof IManaitaPlusLegacyDoubling doubling) {
                    if (mc.level != null) {
                        mc.crosshairPickEntity = null;
                        double d0 = 512;
                        HitResult hitResult = entity.pick(d0, mc.getPartialTick(), false);

                        Vec3 vec3 = entity.getEyePosition(mc.getPartialTick());
                        d0 *= d0;
                        if (hitResult.getType() != HitResult.Type.MISS) {
                            d0 = hitResult.getLocation().distanceToSqr(vec3);
                        }

                        Vec3 vec31 = entity.getViewVector(1.0F);
                        Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);

                        AABB aabb = entity.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
                        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(entity, vec3, vec32, aabb, (p_234237_) -> !p_234237_.isSpectator() &&
                                p_234237_.isPickable(), d0);
                        if (entityhitresult == null) {
                            if (hitResult instanceof BlockHitResult blockHitResult && hitResult.getLocation().distanceToSqr(vec3) > mc.player.getBlockReach() * mc.player.getBlockReach()) {
                                Networking.sendToServer(new FarDestroyPacket(blockHitResult.getBlockPos()));
                            }
                            return;
                        }
                        Entity entity1 = entityhitresult.getEntity();
                        item.onLeftClickEntity(itemInHand, mc.player,entity1);
                        Networking.sendToServer(new FarAttackEntityPacket(entity1));
                    }
                }
            }
        }
    }

//    @SubscribeEvent
//    public static void onRenderTick(TickEvent.RenderTickEvent event) {
//        LocalPlayer entity = mc.player;
//        if (entity != null) {
//            ItemStack itemInHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
//            Item item = itemInHand.getItem();
//            if (item instanceof IManaitaPlusLegacyDoubling) {
//                if (mc.level != null) {
//                    mc.crosshairPickEntity = null;
//                    double d0 = 4096;
//                    HitResult hitResult = entity.pick(d0, mc.getPartialTick(), false);
//
//                    Vec3 vec3 = entity.getEyePosition(mc.getPartialTick());
//                    d0 *= d0;
//                    if (hitResult.getType() != HitResult.Type.MISS) {
//                        d0 = hitResult.getLocation().distanceToSqr(vec3);
//                    }
//
//                    Vec3 vec31 = entity.getViewVector(1.0F);
//                    Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);
//
//                    AABB aabb = entity.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
//                    EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(entity, vec3, vec32, aabb, (p_234237_) -> !p_234237_.isSpectator() &&
//                            p_234237_.isPickable(), d0);
//                    if (entityhitresult != null) {
//                        mc.hitResult = entityhitresult;
//                        mc.crosshairPickEntity = entityhitresult.getEntity();
//                    } else if (hitResult.getType() == HitResult.Type.BLOCK) {
//                        mc.hitResult = hitResult;
//                    }
//                }
//            }
//        }
//    }

}
