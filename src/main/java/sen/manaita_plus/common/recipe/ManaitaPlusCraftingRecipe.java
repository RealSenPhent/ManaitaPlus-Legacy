package sen.manaita_plus.common.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import sen.manaita_plus.common.block.item.ManaitaPlusBrewingBlockItem;
import sen.manaita_plus.common.block.item.ManaitaPlusFurnaceBlockItem;
import sen.manaita_plus.common.block.item.ManaitaPlusCraftingBlockItem;
import sen.manaita_plus.common.block.item.ManaitaPlusHookBlockItem;
import sen.manaita_plus.common.core.ManaitaPlusBlockCore;
import sen.manaita_plus.common.core.ManaitaPlusItemCore;
import sen.manaita_plus.common.core.ManaitaPlusRecipeSerializerCore;
import sen.manaita_plus.common.item.ManaitaPlusSourceItem;

public class ManaitaPlusCraftingRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    private final CraftingBookCategory category;
    public ManaitaPlusCraftingRecipe(ResourceLocation p_252125_, CraftingBookCategory p_249010_) {
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
            if (itemStack.getItem() instanceof ManaitaPlusSourceItem || itemStack.getItem() instanceof ManaitaPlusCraftingBlockItem || itemStack.getItem() instanceof ManaitaPlusFurnaceBlockItem || itemStack.getItem() instanceof ManaitaPlusBrewingBlockItem) {
                source=true;
            } else if (itemStack == ItemStack.EMPTY) {
                continue;
            }
            ++item;
        }
        if (source && item == 2) return true;
        source = false;
        item = 0;
        boolean source1 = false;
        for (ItemStack itemStack : p_44002_.getItems()) {
            if (itemStack.getItem() instanceof ManaitaPlusCraftingBlockItem) {
                source=true;
            } else if (itemStack.getItem() == Items.OAK_PLANKS ||
                    itemStack.getItem() == Items.COBBLESTONE ||
                    itemStack.getItem() == Items.IRON_BLOCK ||
                    itemStack.getItem() == Items.REDSTONE_BLOCK ||
                    itemStack.getItem() == Items.GOLD_BLOCK ||
                    itemStack.getItem() == Items.DIAMOND_BLOCK ||
                    itemStack.getItem() == Items.EMERALD_BLOCK || itemStack.getItem() instanceof ManaitaPlusHookBlockItem) {
                source1=true;
            } else if (itemStack == ItemStack.EMPTY) continue;
            item++;
        }
        return source & source1 & item == 2;
    }

    @Override
    public ItemStack assemble(CraftingContainer p_44001_, RegistryAccess p_267165_) {
        ItemStack itemStack = null;
        ItemStack itemStack1 = null;
        ItemStack itemStack3 = null;
        Item typeBlock;
        boolean source = false;;
        for (ItemStack itemStack2 : p_44001_.getItems()) {
            if ((itemStack1 != null && itemStack2.getItem() instanceof ManaitaPlusHookBlockItem) || (itemStack3 != null && (itemStack2.getItem() instanceof ManaitaPlusCraftingBlockItem || itemStack2.getItem() instanceof ManaitaPlusFurnaceBlockItem || itemStack2.getItem() instanceof ManaitaPlusBrewingBlockItem))) {
                if (itemStack3 == null) itemStack3 = itemStack2;
                if (itemStack1 == null) itemStack1 = itemStack2;
                if (itemStack1.getTag() != null && itemStack1.getTag().getInt("ManaitaType") != 0) continue;
                ItemStack itemStack4;
                if (itemStack1.getItem() instanceof ManaitaPlusCraftingBlockItem) {
                    itemStack4 = new ItemStack(ManaitaPlusItemCore.ManaitaCraftingPortable.get());
                } else if (itemStack1.getItem() instanceof ManaitaPlusFurnaceBlockItem) {
                    itemStack4 = new ItemStack(ManaitaPlusItemCore.ManaitaFurnacePortable.get());
                } else if (itemStack1.getItem() instanceof ManaitaPlusCraftingBlockItem) {
                    itemStack4 = new ItemStack(ManaitaPlusItemCore.ManaitaBrewingPortable.get());
                } else continue;
                if (itemStack3.hasTag()) itemStack4.getOrCreateTag().putInt("ManaitaType", itemStack3.getTag().getInt("ManaitaType") + 1);
                return itemStack4;
            }
            if ((source && itemStack2 != ItemStack.EMPTY) || (itemStack != null && itemStack2.getItem() instanceof ManaitaPlusSourceItem)) {
                if (source) {
                    itemStack = itemStack2;
                }
                itemStack = itemStack.copy();
                itemStack.setCount(64);
                return itemStack;
            }
            if (itemStack2.getItem() instanceof ManaitaPlusSourceItem) source = true;
            else if (itemStack2.getItem() instanceof ManaitaPlusHookBlockItem) itemStack3 = itemStack2;
            else if (itemStack2.getItem() instanceof ManaitaPlusCraftingBlockItem) {
                if (itemStack2.getTag() != null && itemStack2.getTag().getInt("ManaitaType") != 0) continue;
                itemStack1 = new ItemStack(ManaitaPlusBlockCore.CraftingBlockItem.get());
            } else if (itemStack2.getItem() instanceof ManaitaPlusFurnaceBlockItem) {
                if (itemStack2.getTag() != null && itemStack2.getTag().getInt("ManaitaType") != 0)  continue;
                itemStack1 = new ItemStack(ManaitaPlusBlockCore.FurnaceBlockItem.get());
            }else if (itemStack2.getItem() instanceof ManaitaPlusBrewingBlockItem) {
                if (itemStack2.getTag() != null && itemStack2.getTag().getInt("ManaitaType") != 0)  continue;
                itemStack1 = new ItemStack(ManaitaPlusBlockCore.BrewingBlockItem.get());
            }else if (itemStack2 != ItemStack.EMPTY) itemStack = itemStack2;
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
                types = 4;
            else if (typeBlock == Items.GOLD_BLOCK)
                types = 5;
            else if (typeBlock == Items.DIAMOND_BLOCK)
                types = 6;
            else if (typeBlock == Items.EMERALD_BLOCK)
                types = 7;
            else if (typeBlock == Items.NETHERITE_BLOCK)
                types = 8;
            else return ItemStack.EMPTY;
            itemStack1.setTag(new CompoundTag());
            CompoundTag p128367 = new CompoundTag();
            p128367.putInt("types", types);
            itemStack1.getTag().put("BlockStateTag", p128367);
            itemStack1.getTag().putInt("ManaitaType", types);
            return itemStack1;
        }
        return ItemStack.EMPTY;
    }


    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return p_43999_ * p_44000_ >= 2;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ManaitaPlusRecipeSerializerCore.CraftingRecipe.get();
    }
}
