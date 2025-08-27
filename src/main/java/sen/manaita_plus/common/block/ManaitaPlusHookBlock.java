package sen.manaita_plus.common.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import sen.manaita_plus.common.block.data.Data;
import sen.manaita_plus.common.block.entity.ManaitaPlusCraftingBlockEntity;

import java.util.List;

public class ManaitaPlusHookBlock extends BaseEntityBlock {
    public ManaitaPlusHookBlock() {
        super(Properties.of().forceSolidOff());
        this.registerDefaultState(this.stateDefinition.any().setValue(Data.FACING, Direction.NORTH).setValue(Data.WALL,Direction.DOWN).setValue(Data.TYPES,0));
    }


//    @Override
//    public MutableComponent getName() {
//        return Component.translatable("tile.fixed_hook." + ManaitaPlusUtils.getTypes(p_60555_.getValue(Data.TYPES)) + "name");
//    }

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
        p_48725_.add(Data.FACING,Data.TYPES,Data.WALL);
    }

    public BlockEntity newBlockEntity(BlockPos p_153277_, BlockState p_153278_) {
        return new ManaitaPlusCraftingBlockEntity(p_153277_, p_153278_);
    }
}
