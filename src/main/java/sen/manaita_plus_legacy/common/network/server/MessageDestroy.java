package sen.manaita_plus_legacy.common.network.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusDestroy;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.function.Supplier;

public class MessageDestroy {
    private final BlockPos blockPos;
    private final int range;
    private final Item item;

    public MessageDestroy(FriendlyByteBuf buffer) {
        blockPos = buffer.readBlockPos();
        range = buffer.readInt();
        item = buffer.readById(BuiltInRegistries.ITEM);
    }


    public MessageDestroy(BlockPos blockPos, int range,Item item) {
        this.blockPos = blockPos;
        this.range = range;
        this.item = item;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(range);
        buf.writeId(BuiltInRegistries.ITEM, item);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (!ctx.get().getDirection().getReceptionSide().isClient()) return;
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level == null || mc.player == null) return;
            if (item instanceof IManaitaPlusDestroy des) {
                int xM = blockPos.getX() + range;
                int yM = blockPos.getY() + range;
                int zM = blockPos.getZ() + range;
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                for (int x = blockPos.getX() - range; x <= xM; x++) {
                    for (int y = blockPos.getY() - range; y <= yM; y++) {
                        for (int z = blockPos.getZ() - range; z <= zM; z++) {
                            BlockState blockState = level.getBlockState(mutableBlockPos.set(x, y, z));
                            if (blockState == null || !des.accept(blockState))
                                continue;
//                            Block block = blockState.getBlock();
//                            block.playerWillDestroy(level, mutableBlockPos, blockState, mc.player);

                            ManaitaPlusUtils.setBlock(level, mutableBlockPos, level.getFluidState(mutableBlockPos).createLegacyBlock(), 10);

                            SoundType soundtype = blockState.getSoundType(level, mutableBlockPos, mc.player);
                            mc.getSoundManager().play(new SimpleSoundInstance(soundtype.getHitSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 8.0F, soundtype.getPitch() * 0.5F, SoundInstance.createUnseededRandom(), mutableBlockPos));
//                            blockState.onDestroyedByPlayer(level, pos, mc.player, false, level.getFluidState(pos));
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
