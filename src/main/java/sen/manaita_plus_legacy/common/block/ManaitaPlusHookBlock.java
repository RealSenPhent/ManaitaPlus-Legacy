package sen.manaita_plus_legacy.common.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import sen.manaita_plus_legacy.common.block.data.ManaitaPlusLegacyBlockData;
import sen.manaita_plus_legacy.common.util.ManaitaPlusLegacyNBTData;

import java.util.List;

public class ManaitaPlusHookBlock extends Block {
    public ManaitaPlusHookBlock() {
        super(Properties.of().forceSolidOff());
        this.registerDefaultState(this.stateDefinition.any().setValue(ManaitaPlusLegacyBlockData.FACING, Direction.NORTH).setValue(ManaitaPlusLegacyBlockData.TYPES,0));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        Direction direction = p_60555_.getValue(ManaitaPlusLegacyBlockData.FACING);
        return direction == Direction.EAST ? ManaitaPlusLegacyBlockData.shapeE : direction == Direction.SOUTH ? ManaitaPlusLegacyBlockData.shapeS : direction == Direction.NORTH ? ManaitaPlusLegacyBlockData.shapeN : ManaitaPlusLegacyBlockData.shapeW;
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_) {
        ItemStack itemStack = new ItemStack(p_287732_.getBlock());
        itemStack.setTag(new CompoundTag());
        int type = p_287732_.getValue(ManaitaPlusLegacyBlockData.TYPES);
        itemStack.getTag().putInt(ManaitaPlusLegacyNBTData.ItemType,type);
        return Lists.newArrayList(itemStack);
    }


    public BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
        return this.defaultBlockState().setValue(ManaitaPlusLegacyBlockData.FACING, p_48689_.getClickedFace());
    }

    public RenderShape getRenderShape(BlockState p_48727_) {
        return RenderShape.MODEL;
    }

    public BlockState rotate(BlockState p_48722_, Rotation p_48723_) {
        return p_48722_.setValue(ManaitaPlusLegacyBlockData.FACING, p_48723_.rotate(p_48722_.getValue(ManaitaPlusLegacyBlockData.FACING)));
    }

    public BlockState mirror(BlockState p_48719_, Mirror p_48720_) {
        return p_48719_.rotate(p_48720_.getRotation(p_48719_.getValue(ManaitaPlusLegacyBlockData.FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_48725_) {
        p_48725_.add(ManaitaPlusLegacyBlockData.FACING, ManaitaPlusLegacyBlockData.TYPES);
    }
}
