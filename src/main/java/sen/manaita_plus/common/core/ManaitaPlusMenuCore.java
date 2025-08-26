package sen.manaita_plus.common.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus.ManaitaPlus;
import sen.manaita_plus.common.gui.ManaitaPlusBrewingStandMenu;
import sen.manaita_plus.common.gui.ManaitaPlusCraftingMenu;
import sen.manaita_plus.common.gui.ManaitaPlusFurnaceMenu;
import static sen.manaita_plus.ManaitaPlus.MENU_TYPES;

public class ManaitaPlusMenuCore {
    public static final RegistryObject<MenuType<ManaitaPlusCraftingMenu>> CraftingManaita = MENU_TYPES.register("manaita_crafting", () -> IForgeMenuType.create(ManaitaPlusCraftingMenu::new));
    public static final RegistryObject<MenuType<ManaitaPlusFurnaceMenu>> FurnaceManaita = MENU_TYPES.register("manaita_furnace", () -> IForgeMenuType.create(ManaitaPlusFurnaceMenu::new));
    public static final RegistryObject<MenuType<ManaitaPlusBrewingStandMenu>> BrewingStandManaita = MENU_TYPES.register("manaita_brewing", () -> IForgeMenuType.create(ManaitaPlusBrewingStandMenu::new));


    public static void init() {}
}
