package sen.manaita_plus.common.core;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus.common.recipe.ManaitaPlusCraftingRecipe;
import sen.manaita_plus.common.recipe.ManaitaPlusNBTCraftingRecipe;

import static sen.manaita_plus.ManaitaPlus.*;

public class ManaitaPlusRecipeSerializerCore {
    public static final RegistryObject<RecipeSerializer<?>> CraftingRecipe = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("manaita_crafting", () -> new SimpleCraftingRecipeSerializer<>(ManaitaPlusCraftingRecipe::new));
    public static final RegistryObject<RecipeSerializer<?>> CraftingRecipeType = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("manaita_crafting_type", ManaitaPlusNBTCraftingRecipe.Serializer::new);

    public static void init() {}
}
