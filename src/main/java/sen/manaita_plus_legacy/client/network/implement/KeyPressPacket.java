package sen.manaita_plus_legacy.client.network.implement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;

import java.util.function.Supplier;

public class KeyPressPacket {
    private final byte key;


    public KeyPressPacket(FriendlyByteBuf buffer) {
        key = buffer.readByte();
    }


    public KeyPressPacket(byte key) {
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
                case 2:
                case 3:
                    ItemStack mainHandItem = sender.getMainHandItem();
                    if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IManaitaPlusLegacyKey key) {
                        key.onManaitaKeyPress(mainHandItem, sender, this.key);
                    }
                    break;
                case 1:
                    for (ItemStack itemStack : sender.getInventory().armor) {
                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IManaitaPlusLegacyKey key) {
                            key.onManaitaKeyPress(itemStack, sender, this.key);
                        }
                    }
                    break;
                case 4:
                    if (ManaitaPlusLegacyEntityData.manaita.accept(sender)) {
                        ManaitaPlusUtils.godKill(0,true,sender.isShiftKeyDown(),sender);
                    }
                    break;
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
