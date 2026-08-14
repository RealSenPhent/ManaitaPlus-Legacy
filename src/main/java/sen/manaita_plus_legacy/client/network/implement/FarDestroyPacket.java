package sen.manaita_plus_legacy.client.network.implement;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.function.Supplier;

public class FarDestroyPacket {
    private final BlockPos targetPos;

    public FarDestroyPacket(FriendlyByteBuf buffer) {
        targetPos = buffer.readBlockPos();
    }


    public FarDestroyPacket(BlockPos target) {
        this.targetPos = target;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(targetPos);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) return;
            ServerPlayer sender = ctx.get().getSender();
            ItemStack itemInHand = sender.getItemInHand(InteractionHand.MAIN_HAND);
            if (itemInHand.getItem() instanceof IManaitaPlusLegacyDoubling doubling) {
                ManaitaPlusUtils.destroyBlocks(itemInHand,sender.level(), targetPos, sender);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
