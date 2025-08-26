package sen.manaita_plus.common.core;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus.common.recipe.ManaitaPlusCraftingRecipe;

import static sen.manaita_plus.ManaitaPlus.*;

public class ManaitaPlusRecipeSerializerCore {
    public static final RegistryObject<RecipeSerializer<?>> CraftingManaitaBlock = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("manaita_crafting", () -> new SimpleCraftingRecipeSerializer<>(ManaitaPlusCraftingRecipe::new));

    public static void init() {}
}
