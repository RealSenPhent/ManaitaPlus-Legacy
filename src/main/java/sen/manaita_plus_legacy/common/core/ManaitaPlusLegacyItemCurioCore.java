package sen.manaita_plus_legacy.common.core;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus_legacy.common.item.curio.BrewingCurio;
import sen.manaita_plus_legacy.common.item.curio.CraftingCurio;
import sen.manaita_plus_legacy.common.item.curio.FurnaceCurio;

import static sen.manaita_plus_legacy.ManaitaPlusLegacy.ITEMS;

public class ManaitaPlusLegacyItemCurioCore {
    public static final RegistryObject<Item> ManaitaCreateCurio = ITEMS.register("manaita_crafting_ring", CraftingCurio::new);
    public static final RegistryObject<Item> ManaitaFurnaceCurio = ITEMS.register("manaita_furnace_ring", FurnaceCurio::new);
    public static final RegistryObject<Item> ManaitaBrewingCurio = ITEMS.register("manaita_brewing_ring", BrewingCurio::new);

    public static void init() {}
}
