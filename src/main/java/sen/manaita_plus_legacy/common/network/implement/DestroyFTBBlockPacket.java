package sen.manaita_plus_legacy.common.network.implement;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.client.network.ClientPacketHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DestroyFTBBlockPacket {
    public final List<Integer> blockPos;
    public final Item item;

    public DestroyFTBBlockPacket(FriendlyByteBuf buffer) {
        blockPos = buffer.readList(FriendlyByteBuf::readVarInt);
        item = buffer.readById(BuiltInRegistries.ITEM);
    }


    public DestroyFTBBlockPacket(List<BlockPos> blockPos, Item item) {
        ArrayList<Integer> p236829 = new ArrayList<>(blockPos.size() * 3);
        for (int i = 0; i < blockPos.size(); i++) {
            p236829.add(blockPos.get(i).getX());
            p236829.add(blockPos.get(i).getY());
            p236829.add(blockPos.get(i).getZ());
        }
        this.blockPos = p236829;
        this.item = item;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeCollection(blockPos, FriendlyByteBuf::writeVarInt);
        buf.writeId(BuiltInRegistries.ITEM, item);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.handler(this)));
        ctx.get().setPacketHandled(true);
    }
}
