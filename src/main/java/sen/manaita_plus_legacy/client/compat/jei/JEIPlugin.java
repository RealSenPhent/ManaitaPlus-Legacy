package sen.manaita_plus_legacy.client.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.*;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.client.gui.CraftingManaitaScreen;
import sen.manaita_plus_legacy.client.gui.FurnaceManaitaScreen;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyBlockCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCurioCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyMenuCore;
import sen.manaita_plus_legacy.common.menu.ManaitaPlusLegacyBrewingStandMenu;
import sen.manaita_plus_legacy.common.menu.ManaitaPlusLegacyCraftingMenu;
import sen.manaita_plus_legacy.common.menu.ManaitaPlusLegacyFurnaceMenu;
import sen.manaita_plus_legacy.common.proxy.CommomProxy;
import sen.manaita_plus_legacy.common.util.tag.ManaitaPlusLegacyTagData;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ManaitaPlusLegacy.rl("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CraftingManaitaScreen.class, 88, 32, 28, 23, RecipeTypes.CRAFTING);
        registration.addRecipeClickArea(FurnaceManaitaScreen.class, 78, 32, 28, 23, RecipeTypes.SMELTING);
        registration.addRecipeClickArea(BrewingStandScreen.class, 97, 16, 14, 30, RecipeTypes.BREWING);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        IIngredientSubtypeInterpreter<ItemStack> typeInterpreter = (stack, context) ->
                stack.hasTag() ? String.valueOf(stack.getTag().getInt(ManaitaPlusLegacyTagData.ItemType)) : IIngredientSubtypeInterpreter.NONE;
        registration.registerSubtypeInterpreter(ManaitaPlusLegacyBlockCore.CraftingBlockItem.get(), typeInterpreter);
        registration.registerSubtypeInterpreter(ManaitaPlusLegacyBlockCore.FurnaceBlockItem.get(), typeInterpreter);
        registration.registerSubtypeInterpreter(ManaitaPlusLegacyBlockCore.BrewingBlockItem.get(), typeInterpreter);
        registration.registerSubtypeInterpreter(ManaitaPlusLegacyBlockCore.HookBlockItem.get(), typeInterpreter);
        registration.registerSubtypeInterpreter(ManaitaPlusLegacyItemCore.ManaitaCraftingPortable.get(), typeInterpreter);
        registration.registerSubtypeInterpreter(ManaitaPlusLegacyItemCore.ManaitaFurnacePortable.get(), typeInterpreter);
        registration.registerSubtypeInterpreter(ManaitaPlusLegacyItemCore.ManaitaBrewingPortable.get(), typeInterpreter);
        if (CommomProxy.curios) {
            registration.registerSubtypeInterpreter(ManaitaPlusLegacyItemCurioCore.ManaitaCreateCurio.get(), typeInterpreter);
            registration.registerSubtypeInterpreter(ManaitaPlusLegacyItemCurioCore.ManaitaFurnaceCurio.get(), typeInterpreter);
            registration.registerSubtypeInterpreter(ManaitaPlusLegacyItemCurioCore.ManaitaBrewingCurio.get(), typeInterpreter);
        }
    }


    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyBlockCore.CraftingBlock.get()), RecipeTypes.CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyItemCore.ManaitaCraftingPortable.get()), RecipeTypes.CRAFTING);
        registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyBlockCore.FurnaceBlock.get()), RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyItemCore.ManaitaFurnacePortable.get()), RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyBlockCore.BrewingBlock.get()), RecipeTypes.BREWING);
        registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyItemCore.ManaitaBrewingPortable.get()), RecipeTypes.BREWING);
        if (CommomProxy.curios) {
            registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyItemCurioCore.ManaitaCreateCurio.get()), RecipeTypes.CRAFTING);
            registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyItemCurioCore.ManaitaFurnaceCurio.get()), RecipeTypes.SMELTING);
            registration.addRecipeCatalyst(new ItemStack(ManaitaPlusLegacyItemCurioCore.ManaitaBrewingCurio.get()), RecipeTypes.BREWING);
        }
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ManaitaPlusLegacyCraftingMenu.class, ManaitaPlusLegacyMenuCore.CraftingManaita.get(), RecipeTypes.CRAFTING, 1, 9, 10, 36);
        registration.addRecipeTransferHandler(ManaitaPlusLegacyFurnaceMenu.class, ManaitaPlusLegacyMenuCore.FurnaceManaita.get(), RecipeTypes.SMELTING, 0, 1, 3, 36);
        registration.addRecipeTransferHandler(ManaitaPlusLegacyBrewingStandMenu.class, ManaitaPlusLegacyMenuCore.BrewingStandManaita.get(), RecipeTypes.BREWING,0, 4, 5, 36);
    }
}
