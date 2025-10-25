package sen.manaita_plus_legacy.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import sen.manaita_plus_legacy.common.config.ManaitaPlusLegacyConfig;
import sen.manaita_plus_legacy.common.block.ManaitaPlusBrewingStandBlock;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyBlockEntityCore;
import sen.manaita_plus_legacy.common.menu.ManaitaPlusLegacyBrewingStandMenu;

import javax.annotation.Nullable;
import java.util.Arrays;

public class ManaitaPlusBrewingStandBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    private static final int[] SLOTS_FOR_UP = new int[]{3};
    private static final int[] SLOTS_FOR_DOWN = new int[]{0, 1, 2, 3};
    private static final int[] SLOTS_FOR_SIDES = new int[]{0, 1, 2, 4};
    private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
    int brewTime;
    private boolean[] lastPotionCount;
    int fuel;
    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int p_59038_) {
            switch (p_59038_) {
                case 0:
                    return ManaitaPlusBrewingStandBlockEntity.this.brewTime;
                case 1:
                    return ManaitaPlusBrewingStandBlockEntity.this.fuel;
                default:
                    return 0;
            }
        }

        public void set(int p_59040_, int p_59041_) {
            switch (p_59040_) {
                case 0:
                    ManaitaPlusBrewingStandBlockEntity.this.brewTime = p_59041_;
                    break;
                case 1:
                    ManaitaPlusBrewingStandBlockEntity.this.fuel = p_59041_;
            }

        }

        public int getCount() {
            return 2;
        }
    };

    public ManaitaPlusBrewingStandBlockEntity(BlockPos p_155283_, BlockState p_155284_) {
        super(ManaitaPlusLegacyBlockEntityCore.BREWING_BLOCK_ENTITY.get(), p_155283_, p_155284_);
    }

    protected Component getDefaultName() {
        return Component.translatable("container.brewing_manaita");
    }

    public int getContainerSize() {
        return this.items.size();
    }

    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public static void serverTick(Level p_155286_, BlockPos p_155287_, BlockState p_155288_, ManaitaPlusBrewingStandBlockEntity p_155289_) {
        if (isBrewable(p_155289_.items)) {
            doBrew(p_155286_, p_155287_, p_155289_.items);
            setChanged(p_155286_, p_155287_, p_155288_);
        }

        boolean[] aboolean = p_155289_.getPotionBits();
        if (!Arrays.equals(aboolean, p_155289_.lastPotionCount)) {
            p_155289_.lastPotionCount = aboolean;
            BlockState blockstate = p_155288_;
            if (!(p_155288_.getBlock() instanceof ManaitaPlusBrewingStandBlock)) {
                return;
            }

            for (int i = 0; i < ManaitaPlusBrewingStandBlock.HAS_BOTTLE.length; ++i) {
                blockstate = blockstate.setValue(ManaitaPlusBrewingStandBlock.HAS_BOTTLE[i], aboolean[i]);
            }

            p_155286_.setBlock(p_155287_, blockstate, 2);
        }
    }

    private boolean[] getPotionBits() {
        boolean[] aboolean = new boolean[3];

        for(int i = 0; i < 3; ++i) {
            if (!this.items.get(i).isEmpty()) {
                aboolean[i] = true;
            }
        }

        return aboolean;
    }

    private static boolean isBrewable(NonNullList<ItemStack> p_155295_) {
        ItemStack itemstack = p_155295_.get(3);
        if (!itemstack.isEmpty()) return net.minecraftforge.common.brewing.BrewingRecipeRegistry.canBrew(p_155295_, itemstack, SLOTS_FOR_SIDES); // divert to VanillaBrewingRegistry
        if (itemstack.isEmpty()) {
            return false;
        } else if (!PotionBrewing.isIngredient(itemstack)) {
            return false;
        } else {
            for(int i = 0; i < 3; ++i) {
                ItemStack itemstack1 = p_155295_.get(i);
                if (!itemstack1.isEmpty() && PotionBrewing.hasMix(itemstack1, itemstack)) {
                    return true;
                }
            }

            return false;
        }
    }

    private static void doBrew(Level p_155291_, BlockPos p_155292_, NonNullList<ItemStack> p_155293_) {
        if (net.minecraftforge.event.ForgeEventFactory.onPotionAttemptBrew(p_155293_)) return;
        ItemStack itemstack = p_155293_.get(3);

        net.minecraftforge.common.brewing.BrewingRecipeRegistry.brewPotions(p_155293_, itemstack, SLOTS_FOR_SIDES);
        for (int slotsForSide : SLOTS_FOR_SIDES) {
            ItemStack itemStack = p_155293_.get(slotsForSide);
            if (itemstack != ItemStack.EMPTY)  itemStack.setCount(itemStack.getCount() * ManaitaPlusLegacyConfig.brewing_doubling_value);
        }
        net.minecraftforge.event.ForgeEventFactory.onPotionBrewed(p_155293_);
        if (itemstack.hasCraftingRemainingItem()) {
            ItemStack itemstack1 = itemstack.getCraftingRemainingItem().copy();
            itemstack.shrink(1);
            if (itemstack.isEmpty()) {
                itemstack = itemstack1;
            } else {
                Containers.dropItemStack(p_155291_, (double)p_155292_.getX(), (double)p_155292_.getY(), (double)p_155292_.getZ(), itemstack1);
            }
        }
        else itemstack.shrink(1);

        p_155293_.set(3, itemstack);
        p_155291_.levelEvent(1035, p_155292_, 0);
    }

    public void load(CompoundTag p_155297_) {
        super.load(p_155297_);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(p_155297_, this.items);
    }

    protected void saveAdditional(CompoundTag p_187484_) {
        super.saveAdditional(p_187484_);
        ContainerHelper.saveAllItems(p_187484_, this.items);
    }

    public ItemStack getItem(int p_58985_) {
        return p_58985_ >= 0 && p_58985_ < this.items.size() ? this.items.get(p_58985_) : ItemStack.EMPTY;
    }

    public ItemStack removeItem(int p_58987_, int p_58988_) {
        return ContainerHelper.removeItem(this.items, p_58987_, p_58988_);
    }

    public ItemStack removeItemNoUpdate(int p_59015_) {
        return ContainerHelper.takeItem(this.items, p_59015_);
    }

    public void setItem(int p_58993_, ItemStack p_58994_) {
        if (p_58993_ >= 0 && p_58993_ < this.items.size()) {
            this.items.set(p_58993_, p_58994_);
        }

    }

    public boolean stillValid(Player p_59000_) {
        return Container.stillValidBlockEntity(this, p_59000_);
    }

    public boolean canPlaceItem(int p_59017_, ItemStack p_59018_) {
        if (p_59017_ == 3) {
            return net.minecraftforge.common.brewing.BrewingRecipeRegistry.isValidIngredient(p_59018_);
        } else if (p_59017_ == 4) {
            return p_59018_.is(Items.BLAZE_POWDER);
        } else {
            return net.minecraftforge.common.brewing.BrewingRecipeRegistry.isValidInput(p_59018_) && this.getItem(p_59017_).isEmpty();
        }
    }

    public int[] getSlotsForFace(Direction p_59010_) {
        if (p_59010_ == Direction.UP) {
            return SLOTS_FOR_UP;
        } else {
            return p_59010_ == Direction.DOWN ? SLOTS_FOR_DOWN : SLOTS_FOR_SIDES;
        }
    }

    public boolean canPlaceItemThroughFace(int p_58996_, ItemStack p_58997_, @Nullable Direction p_58998_) {
        return this.canPlaceItem(p_58996_, p_58997_);
    }

    public boolean canTakeItemThroughFace(int p_59020_, ItemStack p_59021_, Direction p_59022_) {
        return p_59020_ == 3 ? p_59021_.is(Items.GLASS_BOTTLE) : true;
    }

    public void clearContent() {
        this.items.clear();
    }

    protected AbstractContainerMenu createMenu(int p_58990_, Inventory p_58991_) {
        return new ManaitaPlusLegacyBrewingStandMenu(p_58990_, p_58991_, this, this.dataAccess);
    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.handlers = net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
    }
}
