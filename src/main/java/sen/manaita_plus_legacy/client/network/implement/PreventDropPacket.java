package sen.manaita_plus_legacy.client.network.implement;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy.common.util.item.ManaitaPlusLegacyItemStack;

import java.util.function.Supplier;

public class PreventDropPacket {
    private final int current;
    private final CompoundTag compoundTag;


    public PreventDropPacket(FriendlyByteBuf buffer) {
        current = buffer.readInt();
        compoundTag = buffer.readNbt();
    }


    public PreventDropPacket(int current, CompoundTag compoundTag) {
        this.current = current;
        this.compoundTag = compoundTag;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(current);
        buf.writeNbt(compoundTag);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) return;
            ServerPlayer sender = ctx.get().getSender();
            if (!ManaitaPlusLegacyEntityData.anti.accept(sender)) return;

            ItemStack p36000 = ManaitaPlusLegacyItemStack.instance.copy();
            p36000.setTag(compoundTag);
            sender.getInventory().setItem(current, p36000);
        });
        ctx.get().setPacketHandled(true);
    }
}
