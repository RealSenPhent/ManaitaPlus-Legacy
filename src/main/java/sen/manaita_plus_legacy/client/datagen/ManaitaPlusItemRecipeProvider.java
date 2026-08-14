package sen.manaita_plus_legacy.client.datagen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyBlockCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCurioCore;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class ManaitaPlusItemRecipeProvider extends RecipeProvider {
    public ManaitaPlusItemRecipeProvider(PackOutput p_248933_) {
        super(p_248933_);
    }
    private String getTypeName(int type) {
        return switch (type) {
            case 0 -> "";
            case 1 -> "_wooden";
            case 2 -> "_stone";
            case 3 -> "_iron";
            case 4 -> "_gold";
            case 5 -> "_diamond";
            case 6 -> "_emerald";
            case 7 -> "_redstone";
            case 8 -> "_netherite";
            default -> "_wooden";
        };
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> p_251297_) {
        Item[] manaita = new Item[]{ManaitaPlusLegacyBlockCore.CraftingBlockItem.get(), ManaitaPlusLegacyBlockCore.FurnaceBlockItem.get(), ManaitaPlusLegacyBlockCore.BrewingBlockItem.get()};
        Item[] items = {ManaitaPlusLegacyItemCore.ManaitaCraftingPortable.get(), ManaitaPlusLegacyItemCore.ManaitaFurnacePortable.get(), ManaitaPlusLegacyItemCore.ManaitaBrewingPortable.get()};
        for (int i = 0; i <= 8; i++) {
            if (i == 0) {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ManaitaPlusLegacyItemCurioCore.ManaitaCreateCurio.get(), i, 1).
                        define('0', ManaitaPlusLegacyItemCore.ManaitaCraftingPortable.get()).
                        define('1', Items.DIAMOND).
                        pattern("01").
                        pattern("11").
                        save(p_251297_, "manaita_plus_legacy:crafting_ring" + getTypeName(i));
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ManaitaPlusLegacyItemCurioCore.ManaitaFurnaceCurio.get(), i, 1).
                        define('0', ManaitaPlusLegacyItemCore.ManaitaFurnacePortable.get()).
                        define('1', Items.DIAMOND).
                        pattern("01").
                        pattern("11").
                        save(p_251297_, "manaita_plus_legacy:furnace_ring" + getTypeName(i));
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ManaitaPlusLegacyItemCurioCore.ManaitaBrewingCurio.get(), i, 1).
                        define('0', ManaitaPlusLegacyItemCore.ManaitaBrewingPortable.get()).
                        define('1', Items.DIAMOND).
                        pattern("01").
                        pattern("11").
                        save(p_251297_, "manaita_plus_legacy:brewing_ring" + getTypeName(i));


                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, items[0], 0, 1).
                        requires(manaita[0]).
                        requires(ManaitaPlusLegacyItemCore.ManaitaHook.get(), 1).
                        save(p_251297_, "manaita_plus_legacy:" + items[0].getDescriptionId().substring(35).toLowerCase(Locale.ROOT) + getTypeName(i));
                ;
                continue;
            }
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ManaitaPlusLegacyItemCurioCore.ManaitaCreateCurio.get(), i, 1).
                    define('0', ManaitaPlusLegacyItemCore.ManaitaCraftingPortable.get(), i).
                    define('1', Items.DIAMOND).
                    pattern("01").
                    pattern("11").
                    save(p_251297_, "manaita_plus_legacy:crafting_ring" + getTypeName(i));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ManaitaPlusLegacyItemCurioCore.ManaitaFurnaceCurio.get(), i, 1).
                    define('0', ManaitaPlusLegacyItemCore.ManaitaFurnacePortable.get(), i).
                    define('1', Items.DIAMOND).
                    pattern("01").
                    pattern("11").
                    save(p_251297_, "manaita_plus_legacy:furnace_ring" + getTypeName(i));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ManaitaPlusLegacyItemCurioCore.ManaitaBrewingCurio.get(), i, 1).
                    define('0', ManaitaPlusLegacyItemCore.ManaitaBrewingPortable.get(), i).
                    define('1', Items.DIAMOND).
                    pattern("01").
                    pattern("11").
                    save(p_251297_, "manaita_plus_legacy:brewing_ring" + getTypeName(i));

            for (int i1 = 0; i1 < items.length; i1++) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, items[i1], i, 1).
                        requires(ManaitaPlusLegacyBlockCore.HookBlockItem.get(), i - 1, 1).
                        requires(manaita[i1], 1).
                        save(p_251297_, "manaita_plus_legacy:" + items[i1].getDescriptionId().substring(35).toLowerCase(Locale.ROOT) + getTypeName(i));
                ;
            }
        }
        Item[] blockItems = new Item[]{
                Items.OAK_PLANKS, Items.COBBLESTONE, Items.IRON_BLOCK, Items.GOLD_BLOCK, Items.DIAMOND_BLOCK, Items.EMERALD_BLOCK, Items.REDSTONE_BLOCK, Items.NETHERITE_BLOCK
        };

        for (Item item : manaita) {
            for (int i1 = 0; i1 < blockItems.length; i1++) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, item, i1 + 1, 1).
                        requires(blockItems[i1]).
                        requires(item, 1).
                        save(p_251297_, "manaita_plus_legacy:" + item.getDescriptionId().substring(35).toLowerCase(Locale.ROOT) + getTypeName(1 + i1));
                ;
            }
        }
    }


    public static class ShapelessRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
        private final RecipeCategory category;
        private final Item result;
        private final int count;
        private final int type;
        private final List<Ingredient> ingredients = Lists.newArrayList();
        private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
        @Nullable
        private String group;

        public ShapelessRecipeBuilder(RecipeCategory p_250837_, ItemLike p_251897_,int type,int p_252227_) {
            this.category = p_250837_;
            this.result = p_251897_.asItem();
            this.count = p_252227_;
            this.type = type;
        }

        public static ShapelessRecipeBuilder shapeless(RecipeCategory p_252339_, ItemLike p_250836_,int type, int p_249928_) {
            return new ShapelessRecipeBuilder(p_252339_, p_250836_,type, p_249928_);
        }

        public ShapelessRecipeBuilder requires(TagKey<Item> p_206420_) {
            return this.requires(Ingredient.of(p_206420_));
        }

        public ShapelessRecipeBuilder requires(ItemLike p_126210_) {
            return this.requires(p_126210_, 1);
        }

        public ShapelessRecipeBuilder requires(ItemLike p_126212_, int p_126213_) {
            for(int i = 0; i < p_126213_; ++i) {
                this.requires(Ingredient.of(p_126212_));
            }

            return this;
        }

        public ShapelessRecipeBuilder requires(ItemLike p_126212_,int type, int p_126213_) {
            for(int i = 0; i < p_126213_; ++i) {
                this.requires(of(type,p_126212_));
            }

            return this;
        }

        public ShapelessRecipeBuilder requires(Ingredient p_126185_) {
            return this.requires(p_126185_, 1);
        }

        public ShapelessRecipeBuilder requires(Ingredient p_126187_, int p_126188_) {
            for(int i = 0; i < p_126188_; ++i) {
                this.ingredients.add(p_126187_);
            }

            return this;
        }

        public ShapelessRecipeBuilder unlockedBy(String p_126197_, CriterionTriggerInstance p_126198_) {
            this.advancement.addCriterion(p_126197_, p_126198_);
            return this;
        }

        public ShapelessRecipeBuilder group(@Nullable String p_126195_) {
            this.group = p_126195_;
            return this;
        }

        public Item getResult() {
            return this.result;
        }

        public void save(Consumer<FinishedRecipe> p_126205_, ResourceLocation p_126206_) {
            this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(p_126206_)).rewards(AdvancementRewards.Builder.recipe(p_126206_)).requirements(RequirementsStrategy.OR);
            p_126205_.accept(new ShapelessRecipeBuilder.Result(p_126206_, this.result, this.count, this.group == null ? "" : this.group, determineBookCategory(this.category), this.ingredients,type));
        }

        public static class Result extends CraftingRecipeBuilder.CraftingResult {
            private final ResourceLocation id;
            private final Item result;
            private final int count;
            private final int type;
            private final String group;
            private final List<Ingredient> ingredients;

            public Result(ResourceLocation p_249007_, Item p_248667_, int p_249014_, String p_248592_, CraftingBookCategory p_249485_, List<Ingredient> p_252312_,int type) {
                super(p_249485_);
                this.id = p_249007_;
                this.result = p_248667_;
                this.count = p_249014_;
                this.group = p_248592_;
                this.ingredients = p_252312_;
                this.type = type;
            }

            @Override
            public JsonObject serializeRecipe() {
                JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("type", "manaita_plus_legacy:manaita_crafting_less_type");
                this.serializeRecipeData(jsonobject);
                return jsonobject;
            }


            public void serializeRecipeData(JsonObject p_126230_) {
                super.serializeRecipeData(p_126230_);
                if (!this.group.isEmpty()) {
                    p_126230_.addProperty("group", this.group);
                }

                JsonArray jsonarray = new JsonArray();

                for(Ingredient ingredient : this.ingredients) {
                    jsonarray.add(ingredient.toJson());
                }

                p_126230_.add("ingredients", jsonarray);
                JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());

                JsonObject nbt = new JsonObject();
                nbt.addProperty("ManaitaPlusLegacyType", type);
                jsonobject.add("nbt", nbt);

                if (this.count > 1) {
                    jsonobject.addProperty("count", this.count);
                }

                p_126230_.add("result", jsonobject);
            }

            public RecipeSerializer<?> getType() {
                return RecipeSerializer.SHAPELESS_RECIPE;
            }

            public ResourceLocation getId() {
                return this.id;
            }

            @Nullable
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable
            public ResourceLocation getAdvancementId() {
                return null;
            }
        }
    }

    public static Ingredient of(int type,ItemLike... p_43930_) {
        return Ingredient.fromValues(Arrays.stream(p_43930_).map(ItemStack::new).filter((p_43944_) -> !p_43944_.isEmpty()).map(itemStack -> new ItemValue(itemStack,type)));
    }

    public static class ShapedRecipeBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
        private final RecipeCategory category;
        private final Item result;
        private final int count;
        private int type;
        private final List<String> rows = Lists.newArrayList();
        private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
        @Nullable
        private String group;
        private boolean showNotification = true;

        public ShapedRecipeBuilder(RecipeCategory p_249996_, ItemLike p_251475_, int p_248948_, int type) {
            this.category = p_249996_;
            this.result = p_251475_.asItem();
            this.count = p_248948_;
            this.type = type;
        }

        public static ShapedRecipeBuilder shaped(RecipeCategory p_251325_, ItemLike p_250636_,int type,int p_249081_) {
            return new ShapedRecipeBuilder(p_251325_, p_250636_, p_249081_,type);
        }

        public ShapedRecipeBuilder define(Character p_206417_, TagKey<Item> p_206418_) {
            return this.define(p_206417_, Ingredient.of(p_206418_));
        }

        public ShapedRecipeBuilder define(Character p_126128_, ItemLike p_126129_) {
            return this.define(p_126128_, Ingredient.of(p_126129_));
        }

        public ShapedRecipeBuilder define(Character p_126128_, ItemLike p_126129_,int type) {
            return this.define(p_126128_, of(type,p_126129_));
        }


        public ShapedRecipeBuilder define(Character p_126125_, Ingredient p_126126_) {
            if (this.key.containsKey(p_126125_)) {
                throw new IllegalArgumentException("Symbol '" + p_126125_ + "' is already defined!");
            } else if (p_126125_ == ' ') {
                throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
            } else {
                this.key.put(p_126125_, p_126126_);
                return this;
            }
        }

        public ShapedRecipeBuilder pattern(String p_126131_) {
            if (!this.rows.isEmpty() && p_126131_.length() != this.rows.get(0).length()) {
                throw new IllegalArgumentException("Pattern must be the same width on every line!");
            } else {
                this.rows.add(p_126131_);
                return this;
            }
        }

        @Override
        public RecipeBuilder unlockedBy(String p_176496_, CriterionTriggerInstance p_176497_) {
            return null;
        }

        public ShapedRecipeBuilder group(@Nullable String p_126146_) {
            this.group = p_126146_;
            return this;
        }

        public ShapedRecipeBuilder showNotification(boolean p_273326_) {
            this.showNotification = p_273326_;
            return this;
        }

        public Item getResult() {
            return this.result;
        }

        public void save(Consumer<FinishedRecipe> p_126141_, ResourceLocation p_126142_) {
            this.ensureValid(p_126142_);
            p_126141_.accept(new ShapedRecipeBuilder.Result(p_126142_, this.result, this.count, this.group == null ? "" : this.group, determineBookCategory(this.category), this.rows, this.key,this.type));
        }

        private void ensureValid(ResourceLocation p_126144_) {
            if (this.rows.isEmpty()) {
                throw new IllegalStateException("No pattern is defined for shaped recipe " + p_126144_ + "!");
            } else {
                Set<Character> set = Sets.newHashSet(this.key.keySet());
                set.remove(' ');

                for(String s : this.rows) {
                    for(int i = 0; i < s.length(); ++i) {
                        char c0 = s.charAt(i);
                        if (!this.key.containsKey(c0) && c0 != ' ') {
                            throw new IllegalStateException("Pattern in recipe " + p_126144_ + " uses undefined symbol '" + c0 + "'");
                        }

                        set.remove(c0);
                    }
                }

                if (!set.isEmpty()) {
                    throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + p_126144_);
                } else if (this.rows.size() == 1 && this.rows.get(0).length() == 1) {
                    throw new IllegalStateException("Shaped recipe " + p_126144_ + " only takes in a single item - should it be a shapeless recipe instead?");
                }
            }
        }

        public static class Result extends CraftingRecipeBuilder.CraftingResult {
            private final ResourceLocation id;
            private final int type;
            private final Item result;
            private final int count;
            private final String group;
            private final List<String> pattern;
            private final Map<Character, Ingredient> key;

            public Result(ResourceLocation p_273548_, Item p_273530_, int p_272738_, String p_273549_, CraftingBookCategory p_273500_, List<String> p_273744_, Map<Character, Ingredient> p_272991_, int type) {
                super(p_273500_);
                this.id = p_273548_;
                this.result = p_273530_;
                this.count = p_272738_;
                this.group = p_273549_;
                this.pattern = p_273744_;
                this.key = p_272991_;
                this.type = type;
            }

            @Override
            public JsonObject serializeRecipe() {
                JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("type", "manaita_plus_legacy:manaita_crafting_type");
                this.serializeRecipeData(jsonobject);
                return jsonobject;
            }

            public void serializeRecipeData(JsonObject p_126167_) {
                super.serializeRecipeData(p_126167_);
                if (!this.group.isEmpty()) {
                    p_126167_.addProperty("group", this.group);
                }

                JsonArray jsonarray = new JsonArray();

                for(String s : this.pattern) {
                    jsonarray.add(s);
                }

                p_126167_.add("pattern", jsonarray);
                JsonObject jsonobject = new JsonObject();

                for(Map.Entry<Character, Ingredient> entry : this.key.entrySet()) {
                    jsonobject.add(String.valueOf(entry.getKey()), entry.getValue().toJson());
                }

                p_126167_.add("key", jsonobject);
                JsonObject jsonobject1 = new JsonObject();
                jsonobject1.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());


                JsonObject nbt = new JsonObject();
                nbt.addProperty("ManaitaPlusLegacyType", type);
                jsonobject1.add("nbt", nbt);

                jsonobject1.addProperty("count", this.count);


                p_126167_.add("result", jsonobject1);
            }

            public RecipeSerializer<?> getType() {
                return RecipeSerializer.SHAPED_RECIPE;
            }

            public ResourceLocation getId() {
                return this.id;
            }

            @Nullable
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable
            public ResourceLocation getAdvancementId() {
                return null;
            }
        }
    }


    public static class ItemValue implements Ingredient.Value {
        private final ItemStack item;
        private final int type;
        public ItemValue(ItemStack p_43953_,int type) {
            this.item = p_43953_;
            this.type = type;
        }

        public Collection<ItemStack> getItems() {
            return Collections.singleton(this.item);
        }

        public JsonObject serialize() {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.item.getItem()).toString());
            jsonobject.addProperty("type", this.type);
            return jsonobject;
        }
    }
}