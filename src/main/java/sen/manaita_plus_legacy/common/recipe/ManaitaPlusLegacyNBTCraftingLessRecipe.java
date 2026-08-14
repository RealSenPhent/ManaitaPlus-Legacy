package sen.manaita_plus_legacy.common.recipe;

import com.google.gson.*;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import sen.manaita_plus_legacy.common.block.ManaitaPlusCraftingBlock;
import sen.manaita_plus_legacy.common.block.item.ManaitaPlusCraftingBlockItem;
import sen.manaita_plus_legacy.common.block.item.ManaitaPlusHookBlockItem;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyRecipeSerializerCore;
import sen.manaita_plus_legacy.common.recipe.ingredient.ManaitaPlusLegacyNBTIngredient;
import sen.manaita_plus_legacy.common.util.tag.ManaitaPlusLegacyTagData;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ManaitaPlusLegacyNBTCraftingLessRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    final String group;
    final CraftingBookCategory category;
    final ItemStack result;
    final NonNullList<Ingredient> ingredients;

    public ManaitaPlusLegacyNBTCraftingLessRecipe(ResourceLocation p_251840_, String p_249640_, CraftingBookCategory p_249390_, ItemStack p_252071_, NonNullList<Ingredient> p_250689_) {
        this.id = p_251840_;
        this.group = p_249640_;
        this.category = p_249390_;
        this.result = p_252071_;
        this.ingredients = p_250689_;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return ManaitaPlusLegacyRecipeSerializerCore.NBTCraftingLessRecipe.get();
    }

    public String getGroup() {
        return this.group;
    }

    public CraftingBookCategory category() {
        return this.category;
    }

    public ItemStack getResultItem(RegistryAccess p_267111_) {
        return this.result;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public boolean matches(CraftingContainer container, Level level) {
        int size = container.getContainerSize();
        boolean[] used = new boolean[size];

        // 为每个原料寻找一个未使用的匹配物品
        for (Ingredient ingredient : ingredients) {
            boolean found = false;
            for (int i = 0; i < size; i++) {
                if (!used[i]) {
                    ItemStack stack = container.getItem(i);
                    if (!stack.isEmpty() && ingredient.test(stack)) {
                        used[i] = true;
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                return false;  // 有原料无法匹配到任何剩余物品
            }
        }

        // 检查容器中是否还有未被匹配的非空物品
        for (int i = 0; i < size; i++) {
            if (!used[i] && !container.getItem(i).isEmpty()) {
                return false;  // 存在多余的物品
            }
        }

        return true;  // 所有原料且仅有这些原料被完全匹配
    }

    public ItemStack assemble(CraftingContainer p_44260_, RegistryAccess p_266797_) {
        return this.result.copy();
    }

    public boolean canCraftInDimensions(int p_44252_, int p_44253_) {
        return p_44252_ * p_44253_ >= this.ingredients.size();
    }

    public static class Serializer implements RecipeSerializer<ManaitaPlusLegacyNBTCraftingLessRecipe> {
        public ManaitaPlusLegacyNBTCraftingLessRecipe fromJson(ResourceLocation p_44290_, JsonObject p_44291_) {
            String s = GsonHelper.getAsString(p_44291_, "group", "");
            CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(p_44291_, "category", (String)null), CraftingBookCategory.MISC);
            NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(p_44291_, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 9) {
                throw new JsonParseException("Too many ingredients for shapeless recipe. The maximum is " + 9);
            } else {
                ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(p_44291_, "result"));
                return new ManaitaPlusLegacyNBTCraftingLessRecipe(p_44290_, s, craftingbookcategory, itemstack, nonnulllist);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray p_44276_) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < p_44276_.size(); ++i) {
                Ingredient ingredient = fromJson(p_44276_.get(i), false);
                nonnulllist.add(ingredient);
            }

            return nonnulllist;
        }

        public ManaitaPlusLegacyNBTCraftingLessRecipe fromNetwork(ResourceLocation p_44293_, FriendlyByteBuf p_44294_) {
            String s = p_44294_.readUtf();
            CraftingBookCategory craftingbookcategory = p_44294_.readEnum(CraftingBookCategory.class);
            int i = p_44294_.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            for(int j = 0; j < nonnulllist.size(); ++j) {
                nonnulllist.set(j, Ingredient.fromNetwork(p_44294_));
            }

            ItemStack itemstack = p_44294_.readItem();
            return new ManaitaPlusLegacyNBTCraftingLessRecipe(p_44293_, s, craftingbookcategory, itemstack, nonnulllist);
        }

        public void toNetwork(FriendlyByteBuf p_44281_, ManaitaPlusLegacyNBTCraftingLessRecipe p_44282_) {
            p_44281_.writeUtf(p_44282_.group);
            p_44281_.writeEnum(p_44282_.category);
            p_44281_.writeVarInt(p_44282_.ingredients.size());

            for(Ingredient ingredient : p_44282_.ingredients) {
                ingredient.toNetwork(p_44281_);
            }

            p_44281_.writeItem(p_44282_.result);
        }

        public static Ingredient fromJson(@Nullable JsonElement p_289022_, boolean p_288974_) {
            if (p_289022_ != null && !p_289022_.isJsonNull()) {
                if (p_289022_.isJsonObject()) {
                    return fromValues(Stream.of(valueFromJson(p_289022_.getAsJsonObject())));
                } else if (p_289022_.isJsonArray()) {
                    JsonArray jsonarray = p_289022_.getAsJsonArray();
                    if (jsonarray.size() == 0 && !p_288974_) {
                        throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
                    } else {
                        return fromValues(StreamSupport.stream(jsonarray.spliterator(), false).map((p_289756_) -> valueFromJson(GsonHelper.convertToJsonObject(p_289756_, "item"))));
                    }
                } else {
                    throw new JsonSyntaxException("Expected item to be object or array of objects");
                }
            } else {
                throw new JsonSyntaxException("Item cannot be null");
            }
        }

        public static final ManaitaPlusLegacyNBTIngredient EMPTY = new ManaitaPlusLegacyNBTIngredient(Stream.empty());
        public static ManaitaPlusLegacyNBTIngredient fromValues(Stream<? extends Ingredient.Value> p_43939_) {
            ManaitaPlusLegacyNBTIngredient ingredient = new ManaitaPlusLegacyNBTIngredient(p_43939_);
            return ingredient.isEmpty() ? EMPTY : ingredient;
        }


        public static Ingredient.Value valueFromJson(JsonObject p_289797_) {
            if (p_289797_.has("item")) {
                Item item = ShapedRecipe.itemFromJson(p_289797_);
                ItemStack p43953 = new ItemStack(item);
                if (p_289797_.has("type")) {
                    int type = GsonHelper.getAsInt(p_289797_, "type");
                    p43953.getOrCreateTag().putInt(ManaitaPlusLegacyTagData.ItemType, type);
                }
                return new Ingredient.ItemValue(p43953);
            }
            throw new JsonParseException("An ingredient entry needs  an item");
        }
    }
}

