package sen.manaita_plus_legacy.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyKeyBoardCore;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.common.network.Networking;
import sen.manaita_plus_legacy.common.network.client.KeyPressPacket;

@Mod.EventBusSubscriber(modid = ManaitaPlusLegacy.MODID,value = Dist.CLIENT)
public class EventHandler {
    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (mc.player == null) return;
        if (ManaitaPlusLegacyKeyBoardCore.MESSAGE_KEY.isDown()) {
            ItemStack mainHandItem = mc.player.getMainHandItem();
            if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IManaitaPlusLegacyKey iManaitaPlusLegacyKey) {
               iManaitaPlusLegacyKey.onManaitaKeyPressOnClient(mainHandItem, mc.player);
           }
            Networking.sendToServer(new KeyPressPacket((byte)0));
        }
        if (ManaitaPlusLegacyKeyBoardCore.MESSAGE_ARMOR_KEY.isDown()) {
            for (ItemStack itemStack : mc.player.getInventory().armor) {
                if (!itemStack.isEmpty() && itemStack.getItem() instanceof IManaitaPlusLegacyKey key) {
                    key.onManaitaKeyPressOnClient(itemStack, mc.player);
                }
            }
            Networking.sendToServer(new KeyPressPacket((byte)1));
        }
    }

//    @SubscribeEvent
//    public static void onRenderHand(RenderHandEvent event) {
//        ItemStack item = event.getItemStack();
//        LocalPlayer player = mc.player;
//        if (player == null) return;
//        if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == event.getHand()) {
//            if (item.getItem() instanceof ManaitaPlusGodSwordItem) {
//                HumanoidArm humanoidarm = event.getHand() == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();
//                boolean flag = humanoidarm == HumanoidArm.RIGHT;
//
//                int side = flag ? 1 : -1;
//                double f = Mth.sin(event.getSwingProgress() * event.getSwingProgress() * Mth.PI);
//                double f1 = Mth.sin(Mth.sqrt(event.getSwingProgress()) * Mth.PI);
//                PoseStack poseStack = event.getPoseStack();
//                poseStack.translate(side * 0.56, -0.52 + event.getEquipProgress() * -0.6, -0.72);
//                poseStack.translate(side * -0.1414214, 0.08, 0.1414214);
//                poseStack.mulPose(Axis.XP.rotationDegrees((float) (-102.25F - f1 * 80.0F)));
//                poseStack.mulPose(Axis.YP.rotationDegrees((float) (side * 13.365F - f * 20.0F)));
//                poseStack.mulPose(Axis.ZP.rotationDegrees((float) (side * 78.050003F - f1 * 20.0F)));
//                mc.getEntityRenderDispatcher().getItemInHandRenderer().renderItem(player, item, flag ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !flag, poseStack, event.getMultiBufferSource(), event.getPackedLight());
//                event.setCanceled(true);
//            }
//        }
//    }

}
