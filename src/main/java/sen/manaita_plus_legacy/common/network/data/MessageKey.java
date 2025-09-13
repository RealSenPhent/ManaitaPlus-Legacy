package sen.manaita_plus_legacy.common.network.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusKey;

import java.util.function.Supplier;

public class MessageKey {
    private final byte key;


    public MessageKey(FriendlyByteBuf buffer) {
        key = buffer.readByte();
    }


    public MessageKey(byte key) {
        this.key = key;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeByte(key);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) return;
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;
            switch (key) {
                case 0:
                    ItemStack mainHandItem = sender.getMainHandItem();
                    if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IManaitaPlusKey key) {
                        key.onManaitaKeyPress(mainHandItem, sender);
                    }
                    break;
                case 1:
                    for (ItemStack itemStack : sender.getInventory().armor) {
                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IManaitaPlusKey key) {
                            key.onManaitaKeyPress(itemStack, sender);
                        }
                    }
                    break;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
