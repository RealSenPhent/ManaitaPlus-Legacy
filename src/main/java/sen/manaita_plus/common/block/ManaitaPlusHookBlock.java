package sen.manaita_plus.common.block;

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
import sen.manaita_plus.common.block.data.Data;

import java.util.List;

public class ManaitaPlusHookBlock extends Block {
    public ManaitaPlusHookBlock() {
        super(Properties.of().forceSolidOff());
        this.registerDefaultState(this.stateDefinition.any().setValue(Data.FACING, Direction.NORTH).setValue(Data.TYPES,0));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        Direction direction = p_60555_.getValue(Data.FACING);
        return direction == Direction.EAST ? Data.shapeE : direction == Direction.SOUTH ? Data.shapeS : direction == Direction.NORTH ? Data.shapeN : Data.shapeW;
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_) {
        ItemStack itemStack = new ItemStack(p_287732_.getBlock());
        itemStack.setTag(new CompoundTag());
        int type = p_287732_.getValue(Data.TYPES);
        itemStack.getTag().putInt("ManaitaType",type);
        return Lists.newArrayList(itemStack);
    }


    public BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
        return this.defaultBlockState().setValue(Data.FACING, p_48689_.getHorizontalDirection().getOpposite());
    }

    public RenderShape getRenderShape(BlockState p_48727_) {
        return RenderShape.MODEL;
    }

    public BlockState rotate(BlockState p_48722_, Rotation p_48723_) {
        return p_48722_.setValue(Data.FACING, p_48723_.rotate(p_48722_.getValue(Data.FACING)));
    }

    public BlockState mirror(BlockState p_48719_, Mirror p_48720_) {
        return p_48719_.rotate(p_48720_.getRotation(p_48719_.getValue(Data.FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_48725_) {
        p_48725_.add(Data.FACING,Data.TYPES);
    }
}
