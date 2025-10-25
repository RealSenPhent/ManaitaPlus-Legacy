package sen.manaita_plus_legacy.common.block.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import sen.manaita_plus_legacy.common.block.ManaitaPlusFurnaceBlock;
import sen.manaita_plus_legacy.common.block.ManaitaPlusHookBlock;
import sen.manaita_plus_legacy.common.block.data.ManaitaPlusLegacyBlockData;
import sen.manaita_plus_legacy.common.util.ManaitaPlusLegacyNBTData;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import static sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyBlockCore.FurnaceBlock;

public class ManaitaPlusFurnaceBlockItem extends BlockItem {
    public ManaitaPlusFurnaceBlockItem() {
        super(FurnaceBlock.get(), new Item.Properties().fireResistant());
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.literal(I18n.get("block.furnace."+ ManaitaPlusUtils.getTypes(p_41458_.getOrCreateTag().getInt(ManaitaPlusLegacyNBTData.ItemType)) +"name"));
    }

    public InteractionResult place(BlockPlaceContext p_40577_) {
        if (!this.getBlock().isEnabled(p_40577_.getLevel().enabledFeatures())) {
            return InteractionResult.FAIL;
        } else if (!p_40577_.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext blockplacecontext = this.updatePlacementContext(p_40577_);
            if (blockplacecontext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockstate = this.getPlacementState(blockplacecontext);
                if (blockstate == null) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockpos = blockplacecontext.getClickedPos();
                    Level level = blockplacecontext.getLevel();
                    Player player = blockplacecontext.getPlayer();
                    ItemStack itemstack = blockplacecontext.getItemInHand();
                    BlockPos relative = blockpos.relative(blockplacecontext.getClickedFace().getOpposite());
                    BlockState blockState = level.getBlockState(relative);
                    if (blockState.getBlock() instanceof ManaitaPlusHookBlock) {
                        blockstate = blockstate.setValue(ManaitaPlusLegacyBlockData.HOOK, blockState.getValue(ManaitaPlusLegacyBlockData.TYPES)).setValue(ManaitaPlusLegacyBlockData.WALL, blockState.getValue(ManaitaPlusLegacyBlockData.FACING)).setValue(ManaitaPlusLegacyBlockData.FACING, blockState.getValue(ManaitaPlusLegacyBlockData.FACING));
                        blockpos = relative;
                    } else if (blockplacecontext.getClickedFace() != Direction.UP && blockplacecontext.getClickedFace() != Direction.DOWN || !p_40577_.getLevel().isUnobstructed(blockState, p_40577_.getClickedPos(), player == null ? CollisionContext.empty() : CollisionContext.of(player))) {
                        return InteractionResult.FAIL;
                    }
                    if (!level.setBlock(blockpos, blockstate, 11)){
                        return InteractionResult.FAIL;
                    } else {
                        BlockState blockstate1 = level.getBlockState(blockpos);

                        if (blockstate1.is(blockstate.getBlock())) {
                            blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
                            this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
                            blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                            if (player instanceof ServerPlayer) {
                                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
                            }
                        }

                        SoundType soundtype = blockstate1.getSoundType(level, blockpos, p_40577_.getPlayer());
                        level.playSound(player, blockpos, this.getPlaceSound(blockstate1, level, blockpos, p_40577_.getPlayer()), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                        level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
                        if (player == null || !player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }

                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
        }
    }

    protected boolean canPlace(BlockPlaceContext p_40611_, BlockState p_40612_) {
        return (!this.mustSurvive() || p_40612_.canSurvive(p_40611_.getLevel(), p_40611_.getClickedPos()));
    }

    private BlockState updateBlockStateFromTag(BlockPos pos, Level level, ItemStack p_40605_, BlockState p_40606_) {
        BlockState blockstate = p_40606_;
        if (blockstate.getBlock() instanceof ManaitaPlusFurnaceBlock && p_40605_.getTag() != null) {
            BlockState manaitaType = blockstate.setValue(ManaitaPlusLegacyBlockData.TYPES, p_40605_.getTag().getInt(ManaitaPlusLegacyNBTData.ItemType));
            level.setBlock(pos, manaitaType, 2);
            return manaitaType;
        }

        return blockstate;
    }
}
