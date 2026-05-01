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
import java.util.function.Supplier;

public class ChangeEntitiesIDDataPacket {
    public final int flag;
    public final int[] ids;
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
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.handler(this));
        });
        ctx.get().setPacketHandled(true);
    }
}
