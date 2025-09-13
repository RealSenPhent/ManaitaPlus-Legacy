package sen.manaita_plus_legacy.common.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import sen.manaita_plus_legacy.common.block.data.Data;
import sen.manaita_plus_legacy.common.block.entity.ManaitaPlusFurnaceBlockEntity;
import sen.manaita_plus_legacy.common.core.ManaitaPlusBlockCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusBlockEntityCore;

import javax.annotation.Nullable;
import java.util.List;

public class ManaitaPlusFurnaceBlock extends AbstractFurnaceBlock {
    public ManaitaPlusFurnaceBlock() {
        super(BlockBehaviour.Properties.of().forceSolidOff());
        this.registerDefaultState(this.stateDefinition.any().setValue(Data.HOOK, 8).setValue(FACING, Direction.NORTH).setValue(Data.WALL,Direction.DOWN).setValue(LIT, Boolean.FALSE).setValue(Data.TYPES,0));
    }

    protected void openContainer(Level p_53631_, BlockPos p_53632_, Player p_53633_) {
        BlockEntity blockentity = p_53631_.getBlockEntity(p_53632_);
        if (blockentity instanceof ManaitaPlusFurnaceBlockEntity) {
            p_53633_.openMenu((MenuProvider) blockentity);
            p_53633_.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    public RenderShape getRenderShape(BlockState p_48727_) {
        return RenderShape.INVISIBLE;
    }


    @Override
    public List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_) {
        List<ItemStack> list = Lists.newArrayList();
        ItemStack itemStack = new ItemStack(p_287732_.getBlock());
        itemStack.setTag(new CompoundTag());
        itemStack.getTag().putInt("ManaitaType",p_287732_.getValue(Data.TYPES));
        list.add(itemStack);
        int hook = p_287732_.getValue(Data.HOOK);
        if (hook != 8) {
            itemStack = new ItemStack(ManaitaPlusBlockCore.HookBlockItem.get());
            itemStack.setTag(new CompoundTag());
            itemStack.getTag().putInt("ManaitaType",hook);
        }
        return list;
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
        return this.defaultBlockState().setValue(FACING, p_48689_.getHorizontalDirection().getOpposite()).setValue(Data.WALL,p_48689_.getClickedFace().getOpposite()).setValue(Data.FACING, p_48689_.getHorizontalDirection().getOpposite());
    }


    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        Direction wall = p_60555_.getValue(Data.WALL);
        Direction direction = p_60555_.getValue(Data.FACING);
        return wall == Direction.EAST ? Data.shapeEAST : wall == Direction.SOUTH ? Data.shapeSOUTH : wall == Direction.NORTH ? Data.shapeNORTH : wall == Direction.WEST ? Data.shapeWEST :  direction == Direction.NORTH || direction == Direction.SOUTH ? wall == Direction.UP ? Data.shapeUNS : Data.shapeDNS : wall == Direction.UP ? Data.shapeUWE :  Data.shapeDWE;
    }

    public BlockState rotate(BlockState p_154354_, Rotation p_154355_) {
        return p_154354_.setValue(FACING, p_154355_.rotate(p_154354_.getValue(FACING)));
    }

    public BlockState mirror(BlockState p_154351_, Mirror p_154352_) {
        return p_154351_.setValue(FACING, p_154352_.mirror(p_154351_.getValue(FACING)));
    }

    public BlockEntity newBlockEntity(BlockPos p_153277_, BlockState p_153278_) {
        return new ManaitaPlusFurnaceBlockEntity(p_153277_, p_153278_);
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_48725_) {
        p_48725_.add(FACING, LIT, Data.TYPES, Data.WALL, Data.HOOK);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153273_, BlockState p_153274_, BlockEntityType<T> p_153275_) {
        return p_153273_.isClientSide ? null : createTickerHelper(p_153275_, ManaitaPlusBlockEntityCore.FURNACE_BLOCK_ENTITY.get(), ManaitaPlusFurnaceBlockEntity::serverTick);
    }
}
