package sen.manaita_plus_legacy.common.core;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus_legacy.common.item.*;
import sen.manaita_plus_legacy.common.item.armor.ManaitaPlusArmor;
import sen.manaita_plus_legacy.common.item.tool.*;

import static sen.manaita_plus_legacy.ManaitaPlus.ITEMS;

public class ManaitaPlusItemCore {
    public static final RegistryObject<Item> ManaitaSwordGod = ITEMS.register("manaita_sword_god", ManaitaPlusGodSwordItem::new);
    public static final RegistryObject<Item> ManaitaSword = ITEMS.register("manaita_sword", ManaitaPlusSwordItem::new);

    public static final RegistryObject<Item> ManaitaBow = ITEMS.register("manaita_bow", ManaitaPlusBowItem::new);


    public static final RegistryObject<Item> ManaitaPickaxe = ITEMS.register("manaita_pickaxe",  ManaitaPlusPickaxeItem::new);
    public static final RegistryObject<Item> ManaitaAxe = ITEMS.register("manaita_axe",  ManaitaPlusAxeItem::new);
    public static final RegistryObject<Item> ManaitaHoe = ITEMS.register("manaita_hoe",  ManaitaPlusHoeItem::new);
    public static final RegistryObject<Item> ManaitaPaxel = ITEMS.register("manaita_paxel",  ManaitaPlusPaxelItem::new);
    public static final RegistryObject<Item> ManaitaShears = ITEMS.register("manaita_shears",  ManaitaPlusShearsItem::new);
    public static final RegistryObject<Item> ManaitaShovel = ITEMS.register("manaita_shovel",  ManaitaPlusShovelItem::new);


    public static final RegistryObject<Item> ManaitaHelmet = ITEMS.register("manaita_helmet", ManaitaPlusArmor.Helmet::new);
    public static final RegistryObject<Item> ManaitaChestplate = ITEMS.register("manaita_chestplate", ManaitaPlusArmor.Chestplate::new);
    public static final RegistryObject<Item> ManaitaLeggings = ITEMS.register("manaita_leggings", ManaitaPlusArmor.Leggings::new);
    public static final RegistryObject<Item> ManaitaBoots = ITEMS.register("manaita_boots", ManaitaPlusArmor.Boots::new);

    public static final RegistryObject<Item> ManaitaSource = ITEMS.register("manaita_source", ManaitaPlusSourceItem::new);

    public static final RegistryObject<Item> ManaitaWoodenHook = ITEMS.register("manaita_wood_hook", ManaitaPlusHookItem::new);

    public static final RegistryObject<Item> ManaitaCraftingPortable = ITEMS.register("manaita_crafting_portable", sen.manaita_plus_legacy.common.item.portabl.ManaitaPlusCraftingPortabl::new);
    public static final RegistryObject<Item> ManaitaFurnacePortable = ITEMS.register("manaita_furnace_portable", sen.manaita_plus_legacy.common.item.portabl.ManaitaPlusFurnacePortabl::new);
    public static final RegistryObject<Item> ManaitaBrewingPortable = ITEMS.register("manaita_brewing_portable", sen.manaita_plus_legacy.common.item.portabl.ManaitaPlusBrewingPortabl::new);


    public static void init() {}
}
