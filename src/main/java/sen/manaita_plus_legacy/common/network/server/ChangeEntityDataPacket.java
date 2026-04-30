package sen.manaita_plus_legacy.common.network.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;

import java.util.UUID;
import java.util.function.Supplier;

public class ChangeEntityDataPacket {
    private final UUID id;
    private final int flag;

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
            if (!ctx.get().getDirection().getReceptionSide().isClient()) return;
            if (id == null && flag == 0) {
                for (ManaitaPlusLegacyEntityData value : ManaitaPlusLegacyEntityData.values()) {
                    value.clear();
                }
            }
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null || id == null) return;
            Entity entity = level.entityStorage.entityGetter.get(id);
            if (entity == null) return;
            boolean remove = flag < 0;
            for (ManaitaPlusLegacyEntityData entityList : ManaitaPlusLegacyEntityData.values()) {
                if ((entityList.getFlag() & flag) != 0) {
                    if (remove) {
                        entityList.remove(entity);
                    } else {
                        entityList.add(entity);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
