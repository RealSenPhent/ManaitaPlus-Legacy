package sen.manaita_plus_legacy.common.network.server;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.function.Supplier;

public class MessageRemoveEntities {
    private final boolean unsafe;

    public MessageRemoveEntities(FriendlyByteBuf buffer) {
        this.unsafe = buffer.readBoolean();
    }


    public MessageRemoveEntities(boolean unsafe) {
       this.unsafe = unsafe;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(unsafe);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (!ctx.get().getDirection().getReceptionSide().isClient()) return;
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            ManaitaPlusUtils.godKill(mc.player,true,unsafe);
        });
        ctx.get().setPacketHandled(true);
    }
}
