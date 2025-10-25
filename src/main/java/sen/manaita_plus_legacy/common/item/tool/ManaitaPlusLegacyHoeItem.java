package sen.manaita_plus_legacy.common.item.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase;

public class ManaitaPlusLegacyHoeItem extends ManaitaPlusLegacyToolBase {
    public ManaitaPlusLegacyHoeItem() {
        super(BlockTags.MINEABLE_WITH_HOE);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_HOE);
    }

    @Override
    public boolean accept(BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_HOE);
    }

    public InteractionResult useOn(UseOnContext p_41341_) {
        Level level = p_41341_.getLevel();
        BlockPos blockpos = p_41341_.getClickedPos();
        int i = getRange(p_41341_.getItemInHand()) >> 1;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int xM = blockpos.getX() + i;
        int yM = blockpos.getY() + i;
        int zM = blockpos.getZ() + i;
        boolean flag = false;
        for (int x = blockpos.getX() - i; x <= xM; x++) {
            for (int y = blockpos.getY() - i; y <= yM; y++) {
                for (int z = blockpos.getZ() - i; z <= zM; z++) {
                    BlockState toolModifiedState = level.getBlockState(mutableBlockPos.set(x, y, z)).getToolModifiedState(p_41341_, net.minecraftforge.common.ToolActions.HOE_TILL, false);
                    if (toolModifiedState != null) {
                        Player player = p_41341_.getPlayer();
                        level.playSound(player, mutableBlockPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        if (!level.isClientSide) {
                            p_41341_.getLevel().setBlock(mutableBlockPos, toolModifiedState, 11);
                            p_41341_.getLevel().gameEvent(GameEvent.BLOCK_CHANGE, mutableBlockPos, GameEvent.Context.of(player, toolModifiedState));
                            if (player != null) {
                                p_41341_.getItemInHand().hurtAndBreak(1, player, (p_150845_) -> {
                                    p_150845_.broadcastBreakEvent(p_41341_.getHand());
                                });
                            }
                        }

                        flag = true;
                    }
                }
            }
        }
        return flag ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
    }


    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_HOE_ACTIONS.contains(toolAction);
    }
}