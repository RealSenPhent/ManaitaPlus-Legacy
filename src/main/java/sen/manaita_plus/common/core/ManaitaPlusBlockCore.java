package sen.manaita_plus.common.core;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus.common.block.ManaitaPlusBrewingStandBlock;
import sen.manaita_plus.common.block.ManaitaPlusFurnaceBlock;
import sen.manaita_plus.common.block.ManaitaPlusCraftingBlock;
import sen.manaita_plus.common.block.item.ManaitaPlusBrewingBlockItem;
import sen.manaita_plus.common.block.item.ManaitaPlusFurnaceBlockItem;
import sen.manaita_plus.common.block.item.ManaitaPlusCraftingBlockItem;

import static sen.manaita_plus.ManaitaPlus.BLOCKS;
import static sen.manaita_plus.ManaitaPlus.ITEMS;

public class ManaitaPlusBlockCore {
    public static final RegistryObject<Block> CraftingBlock = BLOCKS.register("block_crafting_manaita", ManaitaPlusCraftingBlock::new);
    public static final RegistryObject<Item> CraftingBlockItem = ITEMS.register("block_crafting_manaita", ManaitaPlusCraftingBlockItem::new);

    public static final RegistryObject<Block> FurnaceBlock = BLOCKS.register("block_furnace_manaita", ManaitaPlusFurnaceBlock::new);
    public static final RegistryObject<Item> FurnaceBlockItem = ITEMS.register("block_furnace_manaita", ManaitaPlusFurnaceBlockItem::new);

    public static final RegistryObject<Block> BrewingBlock = BLOCKS.register("block_brewing_manaita", ManaitaPlusBrewingStandBlock::new);
    public static final RegistryObject<Item> BrewingBlockItem = ITEMS.register("block_brewing_manaita", ManaitaPlusBrewingBlockItem::new);

    public static void init() {}
}
