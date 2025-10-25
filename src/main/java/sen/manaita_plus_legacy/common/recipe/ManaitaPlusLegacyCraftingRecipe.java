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
import sen.manaita_plus_legacy.common.util.ManaitaPlusLegacyNBTData;

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
            if (source) return true;
        } else {
            return false;
        }
        boolean source1 = false;
        for (ItemStack itemStack : p_44002_.getItems()) {
            Item item1 = itemStack.getItem();
            if (item1 instanceof ManaitaPlusCraftingBlockItem || item1 instanceof ManaitaPlusFurnaceBlockItem || item1 instanceof ManaitaPlusBrewingBlockItem) {
                source=true;
            } else if (item1 == Items.OAK_PLANKS ||
                    item1 == Items.COBBLESTONE ||
                    item1 == Items.IRON_BLOCK ||
                    item1 == Items.REDSTONE_BLOCK ||
                    item1 == Items.GOLD_BLOCK ||
                    item1 == Items.DIAMOND_BLOCK ||
                    item1 == Items.EMERALD_BLOCK || item1 == Items.NETHERITE_BLOCK || item1 instanceof ManaitaPlusHookBlockItem) {
                source1=true;
            }
        }

        return source & source1;
    }

    @Override
    public ItemStack assemble(CraftingContainer p_44001_, RegistryAccess p_267165_) {
        ItemStack itemStack = null;
        ItemStack itemStack1 = null;
        ItemStack itemStack3 = null;
        Item typeBlock;
        boolean source = false;;
        for (ItemStack itemStack2 : p_44001_.getItems()) {
            Item item1 = itemStack2.getItem();
            if ((itemStack1 != null && item1 instanceof ManaitaPlusHookBlockItem) || (itemStack3 != null && (item1 instanceof ManaitaPlusCraftingBlockItem || item1 instanceof ManaitaPlusFurnaceBlockItem || item1 instanceof ManaitaPlusBrewingBlockItem))) {
                if (itemStack3 == null) itemStack3 = itemStack2;
                if (itemStack1 == null) itemStack1 = itemStack2;
                if (itemStack1.getTag() != null && itemStack1.getTag().getInt(ManaitaPlusLegacyNBTData.ItemType) != 0) continue;
                ItemStack itemStack4;
                Item item = itemStack1.getItem();
                if (item instanceof ManaitaPlusCraftingBlockItem) {
                    itemStack4 = new ItemStack(ManaitaPlusLegacyItemCore.ManaitaCraftingPortable.get());
                } else if (item instanceof ManaitaPlusFurnaceBlockItem) {
                    itemStack4 = new ItemStack(ManaitaPlusLegacyItemCore.ManaitaFurnacePortable.get());
                } else if (item instanceof ManaitaPlusBrewingBlockItem) {
                    itemStack4 = new ItemStack(ManaitaPlusLegacyItemCore.ManaitaBrewingPortable.get());
                } else continue;
                if (itemStack3.hasTag()) itemStack4.getOrCreateTag().putInt(ManaitaPlusLegacyNBTData.ItemType, itemStack3.getTag().getInt(ManaitaPlusLegacyNBTData.ItemType) + 1);
                return itemStack4;
            }
            if ((source && itemStack2 != ItemStack.EMPTY) || (itemStack != null && item1 instanceof ManaitaPlusLegacySourceItem)) {
                if (source) itemStack = itemStack2;
                itemStack = itemStack.copy();
                itemStack.setCount(ManaitaPlusLegacyConfig.source_doubling_value);
                return itemStack;
            }
            if (item1 instanceof ManaitaPlusLegacySourceItem) source = true;
            else if (item1 instanceof ManaitaPlusHookBlockItem) itemStack3 = itemStack2;
            else if (item1 instanceof ManaitaPlusCraftingBlockItem) {
                if (itemStack2.getTag() != null && itemStack2.getTag().getInt(ManaitaPlusLegacyNBTData.ItemType) != 0) continue;
                itemStack1 = new ItemStack(ManaitaPlusLegacyBlockCore.CraftingBlockItem.get());
            } else if (item1 instanceof ManaitaPlusFurnaceBlockItem) {
                if (itemStack2.getTag() != null && itemStack2.getTag().getInt(ManaitaPlusLegacyNBTData.ItemType) != 0)  continue;
                itemStack1 = new ItemStack(ManaitaPlusLegacyBlockCore.FurnaceBlockItem.get());
            } else if (item1 instanceof ManaitaPlusBrewingBlockItem) {
                if (itemStack2.getTag() != null && itemStack2.getTag().getInt(ManaitaPlusLegacyNBTData.ItemType) != 0)  continue;
                itemStack1 = new ItemStack(ManaitaPlusLegacyBlockCore.BrewingBlockItem.get());
            } else if (itemStack2 != ItemStack.EMPTY) itemStack = itemStack2;
        }
        if (itemStack != null && itemStack1 != null) {
            int types;
            typeBlock = itemStack.getItem();
            if (typeBlock == Items.OAK_PLANKS)
                types = 1;
            else if (typeBlock == Items.COBBLESTONE)
                types = 2;
            else if (typeBlock == Items.IRON_BLOCK)
                types = 3;
            else if (typeBlock == Items.REDSTONE_BLOCK)
                types = 7;
            else if (typeBlock == Items.GOLD_BLOCK)
                types = 4;
            else if (typeBlock == Items.DIAMOND_BLOCK)
                types = 5;
            else if (typeBlock == Items.EMERALD_BLOCK)
                types = 6;
            else if (typeBlock == Items.NETHERITE_BLOCK)
                types = 8;
            else return ItemStack.EMPTY;
            itemStack1.getOrCreateTag().putInt(ManaitaPlusLegacyNBTData.ItemType, types);
            return itemStack1;
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
