package sen.manaita_plus_legacy.common.network.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.function.Supplier;

public class MessageRemove {
    private final int id;

    public MessageRemove(FriendlyByteBuf buffer) {
        this.id = buffer.readInt();
    }


    public MessageRemove(int id) {
       this.id = id;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(id);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (!ctx.get().getDirection().getReceptionSide().isClient()) return;
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;
            Entity entity = level.getEntity(id);
            if (entity == null) return;
            ManaitaPlusUtils.removeOnClient(entity);
        });
        ctx.get().setPacketHandled(true);
    }
}
