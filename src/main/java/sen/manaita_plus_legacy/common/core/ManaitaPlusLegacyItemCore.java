package sen.manaita_plus_legacy.common.core;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus_legacy.common.item.*;
import sen.manaita_plus_legacy.common.item.armor.ManaitaPlusLegacyArmor;
import sen.manaita_plus_legacy.common.item.portabl.ManaitaPlusLegacyBrewingPortabl;
import sen.manaita_plus_legacy.common.item.portabl.ManaitaPlusLegacyCraftingPortabl;
import sen.manaita_plus_legacy.common.item.portabl.ManaitaPlusLegacyFurnacePortabl;
import sen.manaita_plus_legacy.common.item.tool.*;

import static sen.manaita_plus_legacy.ManaitaPlusLegacy.ITEMS;

public class ManaitaPlusLegacyItemCore {
    public static final RegistryObject<Item> ManaitaSwordGod = ITEMS.register("manaita_sword_god", ManaitaPlusLegacyGodSwordItem::new);
    public static final RegistryObject<Item> ManaitaSword = ITEMS.register("manaita_sword", ManaitaPlusLegacySwordItem::new);

    public static final RegistryObject<Item> ManaitaBow = ITEMS.register("manaita_bow", ManaitaPlusLegacyBowItem::new);


    public static final RegistryObject<Item> ManaitaPickaxe = ITEMS.register("manaita_pickaxe",  ManaitaPlusLegacyPickaxeItem::new);
    public static final RegistryObject<Item> ManaitaAxe = ITEMS.register("manaita_axe",  ManaitaPlusLegacyAxeItem::new);
    public static final RegistryObject<Item> ManaitaHoe = ITEMS.register("manaita_hoe",  ManaitaPlusLegacyHoeItem::new);
    public static final RegistryObject<Item> ManaitaPaxel = ITEMS.register("manaita_paxel",  ManaitaPlusLegacyPaxelItem::new);
    public static final RegistryObject<Item> ManaitaShears = ITEMS.register("manaita_shears",  ManaitaPlusLegacyShearsItem::new);
    public static final RegistryObject<Item> ManaitaShovel = ITEMS.register("manaita_shovel",  ManaitaPlusLegacyShovelItem::new);


    public static final RegistryObject<Item> ManaitaHelmet = ITEMS.register("manaita_helmet", ManaitaPlusLegacyArmor.Helmet::new);
    public static final RegistryObject<Item> ManaitaChestplate = ITEMS.register("manaita_chestplate", ManaitaPlusLegacyArmor.Chestplate::new);
    public static final RegistryObject<Item> ManaitaLeggings = ITEMS.register("manaita_leggings", ManaitaPlusLegacyArmor.Leggings::new);
    public static final RegistryObject<Item> ManaitaBoots = ITEMS.register("manaita_boots", ManaitaPlusLegacyArmor.Boots::new);

    public static final RegistryObject<Item> ManaitaSource = ITEMS.register("manaita_source", ManaitaPlusLegacySourceItem::new);

    public static final RegistryObject<Item> ManaitaWoodenHook = ITEMS.register("manaita_hook", ManaitaPlusLegacyHookItem::new);

    public static final RegistryObject<Item> ManaitaCraftingPortable = ITEMS.register("manaita_crafting_portable", ManaitaPlusLegacyCraftingPortabl::new);
    public static final RegistryObject<Item> ManaitaFurnacePortable = ITEMS.register("manaita_furnace_portable", ManaitaPlusLegacyFurnacePortabl::new);
    public static final RegistryObject<Item> ManaitaBrewingPortable = ITEMS.register("manaita_brewing_portable", ManaitaPlusLegacyBrewingPortabl::new);

    public static void init() {}
}
