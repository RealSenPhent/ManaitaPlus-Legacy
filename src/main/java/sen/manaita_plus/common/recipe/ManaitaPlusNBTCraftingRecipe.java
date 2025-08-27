package sen.manaita_plus.common.recipe;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import sen.manaita_plus.common.core.ManaitaPlusRecipeSerializerCore;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ManaitaPlusNBTCraftingRecipe implements CraftingRecipe, net.minecraftforge.common.crafting.IShapedRecipe<CraftingContainer> {
    static int MAX_WIDTH = 3;
    static int MAX_HEIGHT = 3;
    /**
     * Expand the max width and height allowed in the deserializer.
     * This should be called by modders who add custom crafting tables that are larger than the vanilla 3x3.
     * @param width your max recipe width
     * @param height your max recipe height
     */
    public static void setCraftingSize(int width, int height) {
        if (MAX_WIDTH < width) MAX_WIDTH = width;
        if (MAX_HEIGHT < height) MAX_HEIGHT = height;
    }

    final int width;
    final int height;
    final NonNullList<Ingredient> recipeItems;
    final ItemStack result;
    private final ResourceLocation id;
    final String group;
    final CraftingBookCategory category;
    final boolean showNotification;

    public ManaitaPlusNBTCraftingRecipe(ResourceLocation p_273203_, String p_272759_, CraftingBookCategory p_273506_, int p_272952_, int p_272920_, NonNullList<Ingredient> p_273650_, ItemStack p_272852_, boolean p_273122_) {
        this.id = p_273203_;
        this.group = p_272759_;
        this.category = p_273506_;
        this.width = p_272952_;
        this.height = p_272920_;
        this.recipeItems = p_273650_;
        this.result = p_272852_;
        this.showNotification = p_273122_;
    }

    public ManaitaPlusNBTCraftingRecipe(ResourceLocation p_250963_, String p_250221_, CraftingBookCategory p_250716_, int p_251480_, int p_251980_, NonNullList<Ingredient> p_252150_, ItemStack p_248581_) {
        this(p_250963_, p_250221_, p_250716_, p_251480_, p_251980_, p_252150_, p_248581_, true);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public RecipeSerializer<?> getSerializer() {
        return ManaitaPlusRecipeSerializerCore.CraftingRecipeType.get();
    }

    public String getGroup() {
        return this.group;
    }

    public CraftingBookCategory category() {
        return this.category;
    }

    public ItemStack getResultItem(RegistryAccess p_266881_) {
        return this.result;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }

    public boolean showNotification() {
        return this.showNotification;
    }

    public boolean canCraftInDimensions(int p_44161_, int p_44162_) {
        return p_44161_ >= this.width && p_44162_ >= this.height;
    }

    public boolean matches(CraftingContainer p_44176_, Level p_44177_) {
        for(int i = 0; i <= p_44176_.getWidth() - this.width; ++i) {
            for(int j = 0; j <= p_44176_.getHeight() - this.height; ++j) {
                if (this.matches(p_44176_, i, j, true)) {
                    return true;
                }

                if (this.matches(p_44176_, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matches(CraftingContainer p_44171_, int p_44172_, int p_44173_, boolean p_44174_) {
        for(int i = 0; i < p_44171_.getWidth(); ++i) {
            for(int j = 0; j < p_44171_.getHeight(); ++j) {
                int k = i - p_44172_;
                int l = j - p_44173_;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (p_44174_) {
                        ingredient = this.recipeItems.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.recipeItems.get(k + l * this.width);
                    }
                }

                if (!ingredient.test(p_44171_.getItem(i + j * p_44171_.getWidth()))) {
                    return false;
                }
            }
        }

        return true;
    }

    public ItemStack assemble(CraftingContainer p_266686_, RegistryAccess p_266725_) {
        return this.getResultItem(p_266725_).copy();
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    public int getRecipeWidth() {
        return getWidth();
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public int getRecipeHeight() {
        return getHeight();
    }

    static NonNullList<Ingredient> dissolvePattern(String[] p_44203_, Map<String, Ingredient> p_44204_, int p_44205_, int p_44206_) {
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(p_44205_ * p_44206_, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(p_44204_.keySet());
        set.remove(" ");

        for(int i = 0; i < p_44203_.length; ++i) {
            for(int j = 0; j < p_44203_[i].length(); ++j) {
                String s = p_44203_[i].substring(j, j + 1);
                Ingredient ingredient = p_44204_.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonnulllist.set(j + p_44205_ * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonnulllist;
        }
    }

    @VisibleForTesting
    static String[] shrink(String... p_44187_) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for(int i1 = 0; i1 < p_44187_.length; ++i1) {
            String s = p_44187_[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (p_44187_.length == l) {
            return new String[0];
        } else {
            String[] astring = new String[p_44187_.length - l - k];

            for(int k1 = 0; k1 < astring.length; ++k1) {
                astring[k1] = p_44187_[k1 + k].substring(i, j + 1);
            }

            return astring;
        }
    }

    public boolean isIncomplete() {
        NonNullList<Ingredient> nonnulllist = this.getIngredients();
        return nonnulllist.isEmpty() || nonnulllist.stream().filter((p_151277_) -> !p_151277_.isEmpty()).anyMatch(ForgeHooks::hasNoElements);
    }

    private static int firstNonSpace(String p_44185_) {
        int i;
        for(i = 0; i < p_44185_.length() && p_44185_.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int lastNonSpace(String p_44201_) {
        int i;
        for(i = p_44201_.length() - 1; i >= 0 && p_44201_.charAt(i) == ' '; --i) {
        }

        return i;
    }

    static String[] patternFromJson(JsonArray p_44197_) {
        String[] astring = new String[p_44197_.size()];
        if (astring.length > MAX_HEIGHT) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < astring.length; ++i) {
                String s = GsonHelper.convertToString(p_44197_.get(i), "pattern[" + i + "]");
                if (s.length() > MAX_WIDTH) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
                }

                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }

    static Map<String, Ingredient> keyFromJson(JsonObject p_44211_) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for(Map.Entry<String, JsonElement> entry : p_44211_.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), fromJson(entry.getValue(), false));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
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
                    return fromValues(StreamSupport.stream(jsonarray.spliterator(), false).map((p_289756_) -> {
                        return valueFromJson(GsonHelper.convertToJsonObject(p_289756_, "item"));
                    }));
                }
            } else {
                throw new JsonSyntaxException("Expected item to be object or array of objects");
            }
        } else {
            throw new JsonSyntaxException("Item cannot be null");
        }
    }

    public static Ingredient fromValues(Stream<? extends Ingredient.Value> p_43939_) {
        Ingredient ingredient = new NBTIngredient(p_43939_);
        return ingredient.isEmpty() ? Ingredient.EMPTY : ingredient;
    }

    public static class NBTIngredient extends Ingredient{

        protected NBTIngredient(Stream<? extends Value> p_43907_) {
            super(p_43907_);
        }

        @Override
        public boolean test(@org.jetbrains.annotations.Nullable ItemStack p_43914_) {
            if (p_43914_ == null) {
                return false;
            } else if (this.isEmpty()) {
                return p_43914_.isEmpty();
            } else {
                for(ItemStack itemstack : this.getItems()) {
                    if (itemstack.hasTag()) {
                        int manaitaType = itemstack.getTag().getInt("ManaitaType");
                        if (manaitaType != 0 && (!p_43914_.hasTag() || manaitaType != p_43914_.getTag().getInt("ManaitaType"))) continue;
                    }
                    if (itemstack.is(p_43914_.getItem())) {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public static Ingredient.Value valueFromJson(JsonObject p_289797_) {
        if (p_289797_.has("item")) {
            Item item = ShapedRecipe.itemFromJson(p_289797_);
            ItemStack p43953 = new ItemStack(item);
            if (p_289797_.has("type")) {
                int type = GsonHelper.getAsInt(p_289797_, "type");
                p43953.getOrCreateTag().putInt("ManaitaType", type);
            }
            return new Ingredient.ItemValue(p43953);
        }
        throw new JsonParseException("An ingredient entry needs  an item");
    }

    public static class Serializer implements RecipeSerializer<ManaitaPlusNBTCraftingRecipe> {
        private static final ResourceLocation NAME = new ResourceLocation( "type_crafting_shaped");
        public ManaitaPlusNBTCraftingRecipe fromJson(ResourceLocation p_44236_, JsonObject p_44237_) {
            String s = GsonHelper.getAsString(p_44237_, "group", "");
            CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(p_44237_, "category", (String)null), CraftingBookCategory.MISC);
            Map<String, Ingredient> map = ManaitaPlusNBTCraftingRecipe.keyFromJson(GsonHelper.getAsJsonObject(p_44237_, "key"));
            String[] astring = ManaitaPlusNBTCraftingRecipe.shrink(ManaitaPlusNBTCraftingRecipe.patternFromJson(GsonHelper.getAsJsonArray(p_44237_, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = ManaitaPlusNBTCraftingRecipe.dissolvePattern(astring, map, i, j);
            ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(p_44237_, "result"));
            boolean flag = GsonHelper.getAsBoolean(p_44237_, "show_notification", true);
            return new ManaitaPlusNBTCraftingRecipe(p_44236_, s, craftingbookcategory, i, j, nonnulllist, itemstack, flag);
        }



        public ManaitaPlusNBTCraftingRecipe fromNetwork(ResourceLocation p_44239_, FriendlyByteBuf p_44240_) {
            int i = p_44240_.readVarInt();
            int j = p_44240_.readVarInt();
            String s = p_44240_.readUtf();
            CraftingBookCategory craftingbookcategory = p_44240_.readEnum(CraftingBookCategory.class);
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.fromNetwork(p_44240_));
            }

            ItemStack itemstack = p_44240_.readItem();
            boolean flag = p_44240_.readBoolean();
            return new ManaitaPlusNBTCraftingRecipe(p_44239_, s, craftingbookcategory, i, j, nonnulllist, itemstack, flag);
        }

        public void toNetwork(FriendlyByteBuf p_44227_, ManaitaPlusNBTCraftingRecipe p_44228_) {
            p_44227_.writeVarInt(p_44228_.width);
            p_44227_.writeVarInt(p_44228_.height);
            p_44227_.writeUtf(p_44228_.group);
            p_44227_.writeEnum(p_44228_.category);

            for(Ingredient ingredient : p_44228_.recipeItems) {
                ingredient.toNetwork(p_44227_);
            }

            p_44227_.writeItem(p_44228_.result);
            p_44227_.writeBoolean(p_44228_.showNotification);
        }
    }
}

