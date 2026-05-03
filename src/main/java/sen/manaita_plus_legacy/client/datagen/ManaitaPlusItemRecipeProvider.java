package sen.manaita_plus_legacy.client.datagen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
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
        for (int i = 0; i <= 8; i++) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ManaitaPlusLegacyItemCurioCore.ManaitaCreateCurio.get(),i,1).
                    define('0',ManaitaPlusLegacyItemCore.ManaitaCraftingPortable.get(),i).
                    define('1',Items.DIAMOND).
                    pattern("01").
                    pattern("11").
                    save(p_251297_,"manaita_plus_legacy:crafting_ring" + getTypeName(i));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ManaitaPlusLegacyItemCurioCore.ManaitaFurnaceCurio.get(),i,1).
                    define('0',ManaitaPlusLegacyItemCore.ManaitaFurnacePortable.get(),i).
                    define('1',Items.DIAMOND).
                    pattern("01").
                    pattern("11").
                    save(p_251297_,"manaita_plus_legacy:furnace_ring" + getTypeName(i));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ManaitaPlusLegacyItemCurioCore.ManaitaBrewingCurio.get(),i,1).
                    define('0',ManaitaPlusLegacyItemCore.ManaitaBrewingPortable.get(),i).
                    define('1',Items.DIAMOND).
                    pattern("01").
                    pattern("11").
                    save(p_251297_,"manaita_plus_legacy:brewing_ring" + getTypeName(i));
        }
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

        public ShapedRecipeBuilder(RecipeCategory p_249996_, ItemLike p_251475_, int p_248948_,int type) {
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

        public static Ingredient of(int type,ItemLike... p_43930_) {
            return Ingredient.fromValues(Arrays.stream(p_43930_).map(ItemStack::new).filter((p_43944_) -> !p_43944_.isEmpty()).map(itemStack -> new ItemValue(itemStack,type)));
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
            p_126141_.accept(new ShapedRecipeBuilder.Result(p_126142_, this.result, this.count, this.group == null ? "" : this.group, determineBookCategory(this.category), this.rows, this.key, this.showNotification,this.type));
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
            private final boolean showNotification;

            public Result(ResourceLocation p_273548_, Item p_273530_, int p_272738_, String p_273549_, CraftingBookCategory p_273500_, List<String> p_273744_, Map<Character, Ingredient> p_272991_, boolean p_272862_,int type) {
                super(p_273500_);
                this.id = p_273548_;
                this.result = p_273530_;
                this.count = p_272738_;
                this.group = p_273549_;
                this.pattern = p_273744_;
                this.key = p_272991_;
                this.showNotification = p_272862_;
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