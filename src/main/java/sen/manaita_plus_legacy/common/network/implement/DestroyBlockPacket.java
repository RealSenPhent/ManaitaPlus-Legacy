package sen.manaita_plus_legacy.common.network.implement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.client.network.ClientPacketHandlers;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDestroy;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.function.Supplier;

public class DestroyBlockPacket {
    public final BlockPos blockPos;
    public final int range;
    public final Item item;

    public DestroyBlockPacket(FriendlyByteBuf buffer) {
        blockPos = buffer.readBlockPos();
        range = buffer.readVarInt();
        item = buffer.readById(BuiltInRegistries.ITEM);
    }


    public DestroyBlockPacket(BlockPos blockPos, int range, Item item) {
        this.blockPos = blockPos;
        this.range = range;
        this.item = item;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeVarInt(range);
        buf.writeId(BuiltInRegistries.ITEM, item);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandlers.handler(this)));
        ctx.get().setPacketHandled(true);
    }
}
