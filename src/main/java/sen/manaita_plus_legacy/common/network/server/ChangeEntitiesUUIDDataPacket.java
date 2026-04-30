package sen.manaita_plus_legacy.common.network.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;

import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class ChangeEntitiesUUIDDataPacket {
    private final int flag;
    private final UUID uuids[];

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
            if (!ctx.get().getDirection().getReceptionSide().isClient()) return;
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) {
                for (ManaitaPlusLegacyEntityData value : ManaitaPlusLegacyEntityData.values()) {
                    if (value.getFlag() == flag) {
                        for (UUID uuid : uuids) {
                            value.getUuidBooleanMap().put(uuid, Boolean.TRUE);
                        }
                        break;
                    }
                }
                return;
            }
            for (ManaitaPlusLegacyEntityData value : ManaitaPlusLegacyEntityData.values()) {
                if (value.getFlag() == flag) {
                    for (UUID uuid : uuids) {
                        Entity entity = level.entityStorage.entityGetter.get(uuid);
                        if (entity == null) {
                            value.getUuidBooleanMap().put(uuid, Boolean.TRUE);
                            continue;
                        }
                        value.add(entity);
                    }
                    break;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
