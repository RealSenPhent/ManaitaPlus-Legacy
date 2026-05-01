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

import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class ChangeEntitiesUUIDDataPacket {
    public final int flag;
    public final UUID[] uuids;

    public ChangeEntitiesUUIDDataPacket(FriendlyByteBuf buffer) {
        this.flag = buffer.readVarInt();
        uuids = new UUID[buffer.readVarInt()];
        for (int i = 0; i < uuids.length; i++) {
            uuids[i] = buffer.readUUID();
        }
    }


    public ChangeEntitiesUUIDDataPacket(int flag) {
       this.flag = flag;
       this.uuids = null;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(flag);
        for (ManaitaPlusLegacyEntityData value : ManaitaPlusLegacyEntityData.values()) {
            if (value.getFlag() == flag) {
                Set<UUID> uuids = value.getUuidBooleanMap().keySet();
                buf.writeVarInt(uuids.size());
                for (UUID uuid : uuids) {
                    buf.writeUUID(uuid);
                }
                break;
            }
        }
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.handler(this));
        });
        ctx.get().setPacketHandled(true);
    }
}
