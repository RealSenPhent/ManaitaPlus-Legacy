package sen.manaita_plus_legacy.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import sen.manaita_plus_legacy.common.core.ManaitaPlusBlockEntityCore;

public class ManaitaPlusCraftingBlockEntity extends BlockEntity {
    public ManaitaPlusCraftingBlockEntity(BlockPos p_155545_, BlockState p_155546_) {
        super(ManaitaPlusBlockEntityCore.CRAFTING_BLOCK_ENTITY.get(), p_155545_, p_155546_);
    }
}