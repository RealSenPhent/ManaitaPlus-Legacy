package sen.manaita_plus.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sen.manaita_plus.client.render.block.RenderBrewingManaitaBlockEntity;
import sen.manaita_plus.client.render.block.RenderFurnaceManaitaBlockEntity;
import sen.manaita_plus.client.render.block.RenderCraftingManaitaBlockEntity;
import sen.manaita_plus.common.core.ManaitaPlusBlockEntityCore;
import sen.manaita_plus.client.render.entity.ManaitaPlusLightningBoltRenderer;
import sen.manaita_plus.client.render.entity.ManaitaPlusLightningPlusBoltRenderer;
import sen.manaita_plus.client.render.entity.RenderManaitaArrow;
import sen.manaita_plus.common.item.ManaitaPlusGodSwordItem;
import sen.manaita_plus.common.core.ManaitaPlusKeyBoardInputCore;
import sen.manaita_plus.common.network.Networking;
import sen.manaita_plus.common.network.data.MessageKey;

import static sen.manaita_plus.common.core.ManaitaPlusEntityCore.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class EventHandler {
    @SubscribeEvent
    public static void onRregisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ManaitaLightningBolt.get(), ManaitaPlusLightningBoltRenderer::new);
        event.registerEntityRenderer(ManaitaLightningBoltPlus.get(), ManaitaPlusLightningPlusBoltRenderer::new);
        event.registerEntityRenderer(ManaitaArrow.get(), RenderManaitaArrow::new);

        BlockEntityRenderers.register(ManaitaPlusBlockEntityCore.FURNACE_BLOCK_ENTITY.get(), RenderFurnaceManaitaBlockEntity::new);
        BlockEntityRenderers.register(ManaitaPlusBlockEntityCore.BREWING_BLOCK_ENTITY.get(), RenderBrewingManaitaBlockEntity::new);
        BlockEntityRenderers.register(ManaitaPlusBlockEntityCore.BLOCK_ENTITY.get(), RenderCraftingManaitaBlockEntity::new);
    }


    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (ManaitaPlusKeyBoardInputCore.MESSAGE_KEY.isDown()) {
            Networking.INSTANCE.sendToServer(new MessageKey((byte)0));
        }
        if (ManaitaPlusKeyBoardInputCore.MESSAGE_ARMOR_KEY.isDown()) {
            Networking.INSTANCE.sendToServer(new MessageKey((byte)1));
       }
    }

    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        ItemStack item = event.getItemStack();
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == event.getHand()) {
            if (item.getItem() instanceof ManaitaPlusGodSwordItem && item.getUseAnimation() == UseAnim.BLOCK) {
                HumanoidArm humanoidarm = event.getHand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
                boolean flag = humanoidarm == HumanoidArm.RIGHT;

                int side = flag ? 1 : -1;
                double f = Mth.sin(event.getSwingProgress() * event.getSwingProgress() * Mth.PI);
                double f1 = Mth.sin(Mth.sqrt(event.getSwingProgress()) * Mth.PI);
                PoseStack poseStack = event.getPoseStack();
                poseStack.translate(side * 0.56, -0.52 + event.getEquipProgress() * -0.6, -0.72);
                poseStack.translate(side * -0.1414214, 0.08, 0.1414214);
                poseStack.mulPose(Axis.XP.rotationDegrees((float) (-102.25F - f1 * 80.0F)));
                poseStack.mulPose(Axis.YP.rotationDegrees((float) (side * 13.365F - f * 20.0F)));
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) (side * 78.050003F - f1 * 20.0F)));
                mc.getEntityRenderDispatcher().getItemInHandRenderer().renderItem(player, item, flag ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !flag, poseStack, event.getMultiBufferSource(), event.getPackedLight());
                event.setCanceled(true);
            }
        }
    }


}
