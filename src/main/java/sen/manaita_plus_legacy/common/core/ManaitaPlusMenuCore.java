package sen.manaita_plus_legacy.common.core;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus_legacy.common.gui.ManaitaPlusBrewingStandMenu;
import sen.manaita_plus_legacy.common.gui.ManaitaPlusCraftingMenu;
import sen.manaita_plus_legacy.common.gui.ManaitaPlusFurnaceMenu;
import static sen.manaita_plus_legacy.ManaitaPlus.MENU_TYPES;

public class ManaitaPlusMenuCore {
    public static final RegistryObject<MenuType<ManaitaPlusCraftingMenu>> CraftingManaita = MENU_TYPES.register("manaita_crafting", () -> IForgeMenuType.create(ManaitaPlusCraftingMenu::new));
    public static final RegistryObject<MenuType<ManaitaPlusFurnaceMenu>> FurnaceManaita = MENU_TYPES.register("manaita_furnace", () -> IForgeMenuType.create(ManaitaPlusFurnaceMenu::new));
    public static final RegistryObject<MenuType<ManaitaPlusBrewingStandMenu>> BrewingStandManaita = MENU_TYPES.register("manaita_brewing", () -> IForgeMenuType.create(ManaitaPlusBrewingStandMenu::new));


    public static void init() {}
}
