package sen.manaita_plus_legacy.common.core;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus_legacy.common.block.entity.ManaitaPlusBrewingStandBlockEntity;
import sen.manaita_plus_legacy.common.block.entity.ManaitaPlusCraftingBlockEntity;
import sen.manaita_plus_legacy.common.block.entity.ManaitaPlusFurnaceBlockEntity;

import static sen.manaita_plus_legacy.ManaitaPlusLegacy.BLOCK_ENTITY_TYPES;

public class ManaitaPlusLegacyBlockEntityCore {
    public static final RegistryObject<BlockEntityType<ManaitaPlusFurnaceBlockEntity>> FURNACE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("furnace_block_entity", () -> BlockEntityType.Builder.of(ManaitaPlusFurnaceBlockEntity::new, ManaitaPlusLegacyBlockCore.FurnaceBlock.get()).build(null));
    public static final RegistryObject<BlockEntityType<ManaitaPlusBrewingStandBlockEntity>> BREWING_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("brewing_block_entity", () -> BlockEntityType.Builder.of(ManaitaPlusBrewingStandBlockEntity::new, ManaitaPlusLegacyBlockCore.BrewingBlock.get()).build(null));
    public static final RegistryObject<BlockEntityType<ManaitaPlusCraftingBlockEntity>> CRAFTING_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("crafting_entity", () -> BlockEntityType.Builder.of(ManaitaPlusCraftingBlockEntity::new, ManaitaPlusLegacyBlockCore.CraftingBlock.get()).build(null));

    public static void init() {}
}
