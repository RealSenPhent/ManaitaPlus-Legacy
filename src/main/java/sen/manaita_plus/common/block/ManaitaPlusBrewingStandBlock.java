package sen.manaita_plus.common.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import sen.manaita_plus.common.block.data.Data;
import sen.manaita_plus.common.block.entity.ManaitaPlusBrewingStandBlockEntity;
import sen.manaita_plus.common.core.ManaitaPlusBlockEntityCore;

import javax.annotation.Nullable;
import java.util.List;

public class ManaitaPlusBrewingStandBlock extends BaseEntityBlock {
    public static final BooleanProperty[] HAS_BOTTLE = new BooleanProperty[]{BlockStateProperties.HAS_BOTTLE_0, BlockStateProperties.HAS_BOTTLE_1, BlockStateProperties.HAS_BOTTLE_2};

    public ManaitaPlusBrewingStandBlock() {
        super(Properties.of().forceSolidOff());
        this.registerDefaultState(this.stateDefinition.any().setValue(Data.HOOK, 8).setValue(Data.FACING, Direction.NORTH).setValue(Data.WALL,Direction.DOWN).setValue(Data.TYPES,0).setValue(HAS_BOTTLE[0], Boolean.valueOf(false)).setValue(HAS_BOTTLE[1], Boolean.valueOf(false)).setValue(HAS_BOTTLE[2], Boolean.valueOf(false)));
    }


    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        Direction wall = p_60555_.getValue(Data.WALL);
        Direction direction = p_60555_.getValue(Data.FACING);
        return wall == Direction.EAST ? Data.shapeEAST : wall == Direction.SOUTH ? Data.shapeSOUTH : wall == Direction.NORTH ? Data.shapeNORTH : wall == Direction.WEST ? Data.shapeWEST :  direction == Direction.NORTH || direction == Direction.SOUTH ? wall == Direction.UP ? Data.shapeUNS : Data.shapeDNS : wall == Direction.UP ? Data.shapeUWE :  Data.shapeDWE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_) {
        ItemStack itemStack = new ItemStack(p_287732_.getBlock());
        itemStack.setTag(new CompoundTag());
        CompoundTag p128367 = new CompoundTag();
        int type = p_287732_.getValue(Data.TYPES);
        p128367.putInt("types", type);
        itemStack.getTag().put("BlockStateTag", p128367);
        itemStack.getTag().putInt("ManaitaType",type);
        return Lists.newArrayList(itemStack);
    }

    public InteractionResult use(BlockState p_50930_, Level p_50931_, BlockPos p_50932_, Player p_50933_, InteractionHand p_50934_, BlockHitResult p_50935_) {
        if (p_50931_.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockentity = p_50931_.getBlockEntity(p_50932_);
            if (blockentity instanceof ManaitaPlusBrewingStandBlockEntity) {
                p_50933_.openMenu((ManaitaPlusBrewingStandBlockEntity)blockentity);
                p_50933_.awardStat(Stats.INTERACT_WITH_BREWINGSTAND);
            }

            return InteractionResult.CONSUME;
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
        return this.defaultBlockState().setValue(Data.WALL,p_48689_.getClickedFace().getOpposite()).setValue(Data.FACING, p_48689_.getHorizontalDirection().getOpposite());
    }


    public RenderShape getRenderShape(BlockState p_48727_) {
        return RenderShape.INVISIBLE;
    }

    public BlockState rotate(BlockState p_48722_, Rotation p_48723_) {
        return p_48722_.setValue(Data.FACING, p_48723_.rotate(p_48722_.getValue(Data.FACING)));
    }

    public BlockState mirror(BlockState p_48719_, Mirror p_48720_) {
        return p_48719_.rotate(p_48720_.getRotation(p_48719_.getValue(Data.FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_48725_) {
        p_48725_.add(Data.FACING,Data.TYPES,HAS_BOTTLE[0], HAS_BOTTLE[1], HAS_BOTTLE[2], Data.WALL, Data.HOOK);
    }

    public BlockEntity newBlockEntity(BlockPos p_153277_, BlockState p_153278_) {
        return new ManaitaPlusBrewingStandBlockEntity(p_153277_, p_153278_);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152694_, BlockState p_152695_, BlockEntityType<T> p_152696_) {
        return p_152694_.isClientSide ? null : createTickerHelper(p_152696_, ManaitaPlusBlockEntityCore.BREWING_BLOCK_ENTITY.get(), ManaitaPlusBrewingStandBlockEntity::serverTick);
    }


    public void onRemove(BlockState p_50937_, Level p_50938_, BlockPos p_50939_, BlockState p_50940_, boolean p_50941_) {
        if (!p_50937_.is(p_50940_.getBlock())) {
            BlockEntity blockentity = p_50938_.getBlockEntity(p_50939_);
            if (blockentity instanceof BrewingStandBlockEntity) {
                Containers.dropContents(p_50938_, p_50939_, (BrewingStandBlockEntity)blockentity);
            }

            super.onRemove(p_50937_, p_50938_, p_50939_, p_50940_, p_50941_);
        }
    }

    public boolean hasAnalogOutputSignal(BlockState p_50919_) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState p_50926_, Level p_50927_, BlockPos p_50928_) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(p_50927_.getBlockEntity(p_50928_));
    }

    public boolean isPathfindable(BlockState p_50921_, BlockGetter p_50922_, BlockPos p_50923_, PathComputationType p_50924_) {
        return false;
    }
}
