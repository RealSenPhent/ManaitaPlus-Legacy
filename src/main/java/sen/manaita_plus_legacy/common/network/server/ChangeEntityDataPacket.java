package sen.manaita_plus_legacy.common.network.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.util.ManaitaPlusLegacyEntityData;

import java.util.function.Supplier;

public class ChangeEntityDataPacket {
    private final int id;
    private final int flag;

    public ChangeEntityDataPacket(FriendlyByteBuf buffer) {
        this.id = buffer.readInt();
        this.flag = buffer.readInt();
    }


    public ChangeEntityDataPacket(int id, int flag) {
       this.id = id;
       this.flag = flag;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeInt(flag);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (!ctx.get().getDirection().getReceptionSide().isClient()) return;
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;
            Entity entity = level.getEntity(id);
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
