package sen.manaita_plus_legacy.common.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sen.manaita_plus_legacy.common.block.data.ManaitaPlusLegacyBlockData;
import sen.manaita_plus_legacy.common.block.entity.ManaitaPlusCraftingBlockEntity;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyBlockCore;
import sen.manaita_plus_legacy.common.menu.ManaitaPlusLegacyCraftingMenu;
import sen.manaita_plus_legacy.common.util.ManaitaPlusLegacyNBTData;

import java.util.List;

public class ManaitaPlusCraftingBlock extends BaseEntityBlock {

    private static final Component CONTAINER_TITLE = Component.translatable("container.crafting_manaita");


    public ManaitaPlusCraftingBlock() {
        super(BlockBehaviour.Properties.of().forceSolidOff());
        this.registerDefaultState(this.stateDefinition.any().setValue(ManaitaPlusLegacyBlockData.HOOK, 8).setValue(ManaitaPlusLegacyBlockData.FACING, Direction.NORTH).setValue(ManaitaPlusLegacyBlockData.WALL,Direction.DOWN).setValue(ManaitaPlusLegacyBlockData.TYPES,0));
    }


    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        Direction wall = p_60555_.getValue(ManaitaPlusLegacyBlockData.WALL);
        Direction direction = p_60555_.getValue(ManaitaPlusLegacyBlockData.FACING);
        boolean hasHook = p_60555_.getValue(ManaitaPlusLegacyBlockData.HOOK) != 8;
        return wall == Direction.EAST ? hasHook ? ManaitaPlusLegacyBlockData.shapeAndE : ManaitaPlusLegacyBlockData.shapeEAST : wall == Direction.SOUTH
                ? hasHook ? ManaitaPlusLegacyBlockData.shapeAndS : ManaitaPlusLegacyBlockData.shapeSOUTH :
                wall == Direction.NORTH ? hasHook ? ManaitaPlusLegacyBlockData.shapeAndN : ManaitaPlusLegacyBlockData.shapeNORTH :
                        wall == Direction.WEST ? hasHook ? ManaitaPlusLegacyBlockData.shapeAndW : ManaitaPlusLegacyBlockData.shapeWEST :
                                direction == Direction.NORTH || direction == Direction.SOUTH ? wall == Direction.UP ? ManaitaPlusLegacyBlockData.shapeUNS :
                                        ManaitaPlusLegacyBlockData.shapeDNS : wall == Direction.UP ? ManaitaPlusLegacyBlockData.shapeUWE :  ManaitaPlusLegacyBlockData.shapeDWE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_287732_, LootParams.Builder p_287596_) {
        List<ItemStack> list = Lists.newArrayList();
        ItemStack itemStack = new ItemStack(p_287732_.getBlock());
        itemStack.setTag(new CompoundTag());
        itemStack.getTag().putInt(ManaitaPlusLegacyNBTData.ItemType,p_287732_.getValue(ManaitaPlusLegacyBlockData.TYPES));
        list.add(itemStack);
        int hook = p_287732_.getValue(ManaitaPlusLegacyBlockData.HOOK);
        if (hook != 8) {
            itemStack = new ItemStack(ManaitaPlusLegacyBlockCore.HookBlockItem.get());
            itemStack.setTag(new CompoundTag());
            itemStack.getTag().putInt(ManaitaPlusLegacyNBTData.ItemType,hook);
            list.add(itemStack);
        }
        return list;
    }


    public @NotNull InteractionResult use(BlockState p_52233_, Level p_52234_, BlockPos p_52235_, Player p_52236_, InteractionHand p_52237_, BlockHitResult p_52238_) {
        if (p_52234_.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            p_52236_.openMenu(p_52233_.getMenuProvider(p_52234_, p_52235_));
//            p_52236_.awardStat(((ResourceLocation) ManaitaPlusStarCore.INTERACT_WITH_CRAFTING_MANAITA_TABLE.get()));
            return InteractionResult.CONSUME;
        }

    }
//
    public MenuProvider getMenuProvider(BlockState p_52240_, Level p_52241_, BlockPos p_52242_) {
        return new SimpleMenuProvider((p_52229_, p_52230_, p_52231_) -> new ManaitaPlusLegacyCraftingMenu(p_52229_, p_52230_, ContainerLevelAccess.create(p_52241_, p_52242_)), CONTAINER_TITLE);
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_48689_) {
        return this.defaultBlockState().setValue(ManaitaPlusLegacyBlockData.WALL,p_48689_.getClickedFace().getOpposite()).setValue(ManaitaPlusLegacyBlockData.FACING, p_48689_.getHorizontalDirection().getOpposite());
    }


    public RenderShape getRenderShape(BlockState p_48727_) {
        return RenderShape.INVISIBLE;
    }

    public BlockState rotate(BlockState p_48722_, Rotation p_48723_) {
        return p_48722_.setValue(ManaitaPlusLegacyBlockData.FACING, p_48723_.rotate(p_48722_.getValue(ManaitaPlusLegacyBlockData.FACING)));
    }

    public BlockState mirror(BlockState p_48719_, Mirror p_48720_) {
        return p_48719_.rotate(p_48720_.getRotation(p_48719_.getValue(ManaitaPlusLegacyBlockData.FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_48725_) {
        p_48725_.add(ManaitaPlusLegacyBlockData.FACING, ManaitaPlusLegacyBlockData.TYPES, ManaitaPlusLegacyBlockData.WALL, ManaitaPlusLegacyBlockData.HOOK);
    }

    public BlockEntity newBlockEntity(BlockPos p_153277_, BlockState p_153278_) {
        return new ManaitaPlusCraftingBlockEntity(p_153277_, p_153278_);
    }
}
