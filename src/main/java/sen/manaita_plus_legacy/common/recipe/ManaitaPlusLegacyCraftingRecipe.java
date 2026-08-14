package sen.manaita_plus_legacy.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import sen.manaita_plus_legacy.common.config.ManaitaPlusLegacyConfig;
import sen.manaita_plus_legacy.common.block.item.ManaitaPlusBrewingBlockItem;
import sen.manaita_plus_legacy.common.block.item.ManaitaPlusCraftingBlockItem;
import sen.manaita_plus_legacy.common.block.item.ManaitaPlusFurnaceBlockItem;
import sen.manaita_plus_legacy.common.block.item.ManaitaPlusHookBlockItem;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyBlockCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyRecipeSerializerCore;
import sen.manaita_plus_legacy.common.item.ManaitaPlusLegacySourceItem;
import sen.manaita_plus_legacy.common.util.tag.ManaitaPlusLegacyTagData;

public class ManaitaPlusLegacyCraftingRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    private final CraftingBookCategory category;
    public ManaitaPlusLegacyCraftingRecipe(ResourceLocation p_252125_, CraftingBookCategory p_249010_) {
        this.id = p_252125_;
        this.category = p_249010_;
    }
    @Override
    public CraftingBookCategory category() {
        return category;
    }

    @Override
    public boolean matches(CraftingContainer p_44002_, Level p_44003_) {
        boolean source = false;
        int item = 0;
        for (ItemStack itemStack : p_44002_.getItems()) {
            if (itemStack.getItem() instanceof ManaitaPlusLegacySourceItem) {
                source=true;
            } else if (itemStack == ItemStack.EMPTY) {
                continue;
            }
            ++item;
        }
        if (item == 2) {
            return source;
        }
        return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer p_44001_, RegistryAccess p_267165_) {
        ItemStack itemStack;
        boolean source = false;
        for (ItemStack itemStack2 : p_44001_.getItems()) {
            Item item1 = itemStack2.getItem();
            if (source && itemStack2 != ItemStack.EMPTY) {
                itemStack = itemStack2.copy();
                itemStack.setCount(ManaitaPlusLegacyConfig.source_doubling_value);
                return itemStack;
            }
            if (item1 instanceof ManaitaPlusLegacySourceItem) source = true;
        }
        return ItemStack.EMPTY;
    }


    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return p_43999_ * p_44000_ >= 2;
    }

    public static final ItemStack source = new ItemStack(ManaitaPlusLegacyItemCore.ManaitaSource.get());
    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return source;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ManaitaPlusLegacyRecipeSerializerCore.CraftingRecipe.get();
    }

    public static class Serializer implements RecipeSerializer<ManaitaPlusLegacyCraftingRecipe> {
        public ManaitaPlusLegacyCraftingRecipe fromJson(ResourceLocation p_44236_, JsonObject p_44237_) {
            CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(p_44237_, "category", (String)null), CraftingBookCategory.MISC);
            return new ManaitaPlusLegacyCraftingRecipe(p_44236_, craftingbookcategory);
        }


        public ManaitaPlusLegacyCraftingRecipe fromNetwork(ResourceLocation p_44239_, FriendlyByteBuf p_44240_) {
            CraftingBookCategory craftingbookcategory = p_44240_.readEnum(CraftingBookCategory.class);
            return new ManaitaPlusLegacyCraftingRecipe(p_44239_, craftingbookcategory);
        }

        public void toNetwork(FriendlyByteBuf p_44227_, ManaitaPlusLegacyCraftingRecipe p_44228_) {
            p_44227_.writeEnum(p_44228_.category);
        }
    }
}
