package sen.manaita_plus.common.network.data;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus.common.item.data.IManaitaPlusKey;

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
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;
            switch (key) {
                case 0:
                    ItemStack mainHandItem = sender.getMainHandItem();
//                    LOGGER.error(mainHandItem.getItem().getClass().getName());
                    if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IManaitaPlusKey key) {
                        key.onManaitaKeyPress(mainHandItem, sender);
//                        LOGGER.error("azz");
                    }
                    break;
                case 1:
                    for (ItemStack itemStack : sender.getInventory().armor) {
                        if (itemStack.getItem() instanceof IManaitaPlusKey key) {
                            key.onManaitaKeyPress(itemStack, sender);
                        }
                    }
                    break;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
