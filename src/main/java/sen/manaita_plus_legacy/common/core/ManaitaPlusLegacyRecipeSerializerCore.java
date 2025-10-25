package sen.manaita_plus_legacy.common.core;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus_legacy.common.recipe.ManaitaPlusLegacyCraftingRecipe;
import sen.manaita_plus_legacy.common.recipe.ManaitaPlusLegacyNBTCraftingRecipe;

import static sen.manaita_plus_legacy.ManaitaPlusLegacy.RECIPE_SERIALIZER_DEFERRED_REGISTER;

public class ManaitaPlusLegacyRecipeSerializerCore {
    public static final RegistryObject<RecipeSerializer<?>> CraftingRecipe = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("manaita_crafting", ManaitaPlusLegacyCraftingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<?>> NBTCraftingRecipe = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("manaita_crafting_type", ManaitaPlusLegacyNBTCraftingRecipe.Serializer::new);

    public static void init() {}
}
