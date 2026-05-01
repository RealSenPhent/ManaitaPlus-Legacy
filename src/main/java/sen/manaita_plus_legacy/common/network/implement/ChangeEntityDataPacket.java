package sen.manaita_plus_legacy.common.network.implement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.client.network.ClientPacketHandlers;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;

import java.util.UUID;
import java.util.function.Supplier;

public class ChangeEntityDataPacket {
    public final UUID id;
    public final int flag;

    public ChangeEntityDataPacket(FriendlyByteBuf buffer) {
        this.id = buffer.readUUID();
        this.flag = buffer.readInt();
    }


    public ChangeEntityDataPacket(UUID id, int flag) {
       this.id = id;
       this.flag = flag;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(id);
        buf.writeInt(flag);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.handler(this));
        });
        ctx.get().setPacketHandled(true);
    }
}
