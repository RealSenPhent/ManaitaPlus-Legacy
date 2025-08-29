package sen.manaita_plus.common.network.data;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus.common.item.data.IManaitaPlusDestroy;
import sen.manaita_plus.common.util.ManaitaPlusUtils;

import java.util.function.Supplier;

public class MessageDestroy {
    private final BlockPos blockPos;
    private final int range;

    public MessageDestroy(FriendlyByteBuf buffer) {
        blockPos = buffer.readBlockPos();
        range = buffer.readInt();
    }


    public MessageDestroy(BlockPos blockPos, int range) {
        this.blockPos = blockPos;
        this.range = range;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(range);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level == null || mc.player == null) return;
            ItemStack mainHandItem = mc.player.getMainHandItem();
            if (mainHandItem.getItem() instanceof IManaitaPlusDestroy des) {
                int xM = blockPos.getX() + range;
                int yM = Math.min(blockPos.getY() + range, level.getMaxBuildHeight());
                int zM = blockPos.getZ() + range;
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                for (int x = blockPos.getX() - range / 2; x < xM; x++) {
                    for (int y = Math.max(blockPos.getY() - range / 2, level.getMinBuildHeight()); y < yM; y++) {
                        for (int z = blockPos.getZ() - range / 2; z < zM; z++) {
                            mutableBlockPos.set(x, y, z);
                            BlockState blockState = level.getBlockState(mutableBlockPos);
                            if (blockState == null || !des.accept(blockState))
                                continue;
//                            Block block = blockState.getBlock();
//                            block.playerWillDestroy(level, mutableBlockPos, blockState, mc.player);

                            ManaitaPlusUtils.setBlock(level, mutableBlockPos, level.getFluidState(mutableBlockPos).createLegacyBlock());

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
