package sen.manaita_plus_legacy.common.network.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;

import java.util.Set;
import java.util.function.Supplier;

public class ChangeEntitiesIDDataPacket {
    private final int flag;
    private final int[] ids;
    public ChangeEntitiesIDDataPacket(FriendlyByteBuf buffer) {
        this.flag = buffer.readVarInt();
        this.ids = new int[buffer.readVarInt()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = buffer.readVarInt();
        }
    }


    public ChangeEntitiesIDDataPacket(int flag) {
       this.flag = flag;
       this.ids = null;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(flag);
        for (ManaitaPlusLegacyEntityData value : ManaitaPlusLegacyEntityData.values()) {
            if (value.getFlag() == flag) {
                Set<Integer> integers = value.getIdBooleanMap().keySet();
                buf.writeVarInt(integers.size());
                for (Integer integer : integers) {
                        buf.writeVarInt(integer);
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
                        for (int id : ids) {
                            value.getIdBooleanMap().put(id, Boolean.TRUE);
                        }
                    }
                }
                return;
            }
            for (ManaitaPlusLegacyEntityData value : ManaitaPlusLegacyEntityData.values()) {
                if (value.getFlag() == flag) {
                    for (int id : ids) {
                        Entity entity = level.getEntity(id);
                        if (entity == null) {
                            value.getIdBooleanMap().put(id, Boolean.TRUE);
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
