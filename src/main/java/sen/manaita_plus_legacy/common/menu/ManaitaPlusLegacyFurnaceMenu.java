package sen.manaita_plus_legacy.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyMenuCore;
import sen.manaita_plus_legacy.common.item.portabl.ManaitaPlusLegacyFurnacePortabl;
import sen.manaita_plus_legacy.common.menu.slot.ManaitaPlusLegacyFurnaceFuelSlot;

public class ManaitaPlusLegacyFurnaceMenu extends RecipeBookMenu<Container> {
    public ManaitaPlusLegacyFurnaceMenu(int p_39532_, Inventory p_39533_, FriendlyByteBuf extraData) {
        this(ManaitaPlusLegacyMenuCore.FurnaceManaita.get(), RecipeType.SMELTING, RecipeBookType.FURNACE, p_39532_, p_39533_);
    }

    public ManaitaPlusLegacyFurnaceMenu(int p_39535_, Inventory p_39536_, Container p_39537_, ContainerData p_39538_) {
        this(ManaitaPlusLegacyMenuCore.FurnaceManaita.get(), RecipeType.SMELTING, RecipeBookType.FURNACE, p_39535_, p_39536_, p_39537_);
    }

    private final Container container;
    protected final Level level;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final RecipeBookType recipeBookType;

    protected ManaitaPlusLegacyFurnaceMenu(MenuType<?> p_38960_, RecipeType<? extends AbstractCookingRecipe> p_38961_, RecipeBookType p_38962_, int p_38963_, Inventory p_38964_) {
        this(p_38960_, p_38961_, p_38962_, p_38963_, p_38964_, new SimpleContainer(3));
    }

    protected ManaitaPlusLegacyFurnaceMenu(MenuType<?> p_38966_, RecipeType<? extends AbstractCookingRecipe> p_38967_, RecipeBookType p_38968_, int p_38969_, Inventory p_38970_, Container p_38971_) {
        super(p_38966_, p_38969_);
        this.recipeType = p_38967_;
        this.recipeBookType = p_38968_;
        checkContainerSize(p_38971_, 3);
        this.container = p_38971_;
        this.level = p_38970_.player.level();
        this.addSlot(new Slot(p_38971_, 0, 56, 17));
        this.addSlot(new ManaitaPlusLegacyFurnaceFuelSlot(this, p_38971_, 1, 56, 53));
        this.addSlot(new FurnaceResultSlot(p_38970_.player, p_38971_, 2, 116, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(p_38970_, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(p_38970_, k, 8 + k * 18, 142));
        }
    }

    public void fillCraftSlotsStackedContents(StackedContents p_38976_) {
        if (this.container instanceof StackedContentsCompatible) {
            ((StackedContentsCompatible)this.container).fillStackedContents(p_38976_);
        }

    }

    public void clearCraftingContent() {
        this.getSlot(0).set(ItemStack.EMPTY);
        this.getSlot(2).set(ItemStack.EMPTY);
    }

    public boolean recipeMatches(Recipe<? super Container> p_38980_) {
        return p_38980_.matches(this.container, this.level);
    }

    public int getResultSlotIndex() {
        return 2;
    }

    public int getGridWidth() {
        return 1;
    }

    public int getGridHeight() {
        return 1;
    }

    public int getSize() {
        return 3;
    }

    public boolean stillValid(Player p_38974_) {
        return this.container.stillValid(p_38974_);
    }

    public ItemStack quickMoveStack(Player p_38986_, int p_38987_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_38987_);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (p_38987_ == 2) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (p_38987_ != 1 && p_38987_ != 0) {
                if (this.canSmelt(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (p_38987_ >= 3 && p_38987_ < 30) {
                    if (!this.moveItemStackTo(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (p_38987_ >= 30 && p_38987_ < 39 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(p_38986_, itemstack1);
        }

        return itemstack;
    }

    protected boolean canSmelt(ItemStack p_38978_) {
        return this.level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>)this.recipeType, new SimpleContainer(p_38978_), this.level).isPresent();
    }

    public boolean isFuel(ItemStack p_38989_) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(p_38989_, this.recipeType) > 0;
    }

    public RecipeBookType getRecipeBookType() {
        return this.recipeBookType;
    }

    public boolean shouldMoveToInventory(int p_150463_) {
        return p_150463_ != 1;
   }


    @Override
    public void removed(Player player) {
        super.removed(player);
        if (container instanceof ManaitaPlusLegacyFurnacePortabl.ManaitaPlusFurnaceBlockEntity block) {
            block.saveToItem(block.stack);
        }
    }
}