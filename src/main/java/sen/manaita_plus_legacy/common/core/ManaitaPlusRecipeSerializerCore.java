package sen.manaita_plus_legacy.common.core;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus_legacy.common.recipe.ManaitaPlusCraftingRecipe;
import sen.manaita_plus_legacy.common.recipe.ManaitaPlusNBTCraftingRecipe;

import static sen.manaita_plus_legacy.ManaitaPlus.RECIPE_SERIALIZER_DEFERRED_REGISTER;

public class ManaitaPlusRecipeSerializerCore {
    public static final RegistryObject<RecipeSerializer<?>> CraftingRecipe = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("manaita_crafting", ManaitaPlusCraftingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> NBTCraftingRecipe = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("manaita_crafting_type", ManaitaPlusNBTCraftingRecipe.Serializer::new);

    public static void init() {}
}
