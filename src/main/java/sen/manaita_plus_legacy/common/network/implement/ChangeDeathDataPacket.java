package sen.manaita_plus_legacy.common.network.implement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;

import java.util.function.Supplier;

public class ChangeDeathDataPacket {
    public final int flag;

    public ChangeDeathDataPacket(FriendlyByteBuf buffer) {
        this.flag = buffer.readInt();
    }


    public ChangeDeathDataPacket(int flag) {
       this.flag = flag;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(flag);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (!ctx.get().getDirection().getReceptionSide().isClient()) return;
            Entity entity = Minecraft.getInstance().player;
            if (entity == null) return;
            if (flag == 1) {
                ManaitaPlusLegacyEntityData.death.add(entity);
            } else {
                ManaitaPlusLegacyEntityData.death.remove(entity);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
