package sen.manaita_plus.common.item.portabl;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraftforge.network.NetworkHooks;
import sen.manaita_plus.common.core.ManaitaPlusBlockEntityCore;
import sen.manaita_plus.common.gui.ManaitaPlusBrewingStandMenu;

import javax.annotation.Nullable;
import java.util.Arrays;

public class ManaitaPlusBrewingPortabl extends Item {
    public ManaitaPlusBrewingPortabl() {
        super(new Properties().defaultDurability(-1).fireResistant().stacksTo(1));
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.translatable("item.portableBrewing."+ p_41458_.getOrCreateTag().getInt("ManaitaType") +".name");
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if (p_41433_ instanceof ServerPlayer serverPlayer) {
            ItemStack itemInHand = p_41433_.getItemInHand(p_41434_);
            NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("container.brewing_manaita");
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
                    ManaitaPlusBrewingStandBlockEntity blockEntity = new ManaitaPlusBrewingStandBlockEntity(p_39955_.player, itemInHand);
                    return blockEntity.createMenu(p_39954_, p_39955_, p_39956_);
                }
            });
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }


    public class ManaitaPlusBrewingStandBlockEntity extends BaseContainerBlockEntity  {
        private static final int[] SLOTS_FOR_SIDES = new int[]{0, 1, 2, 4};
        private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY);
        private boolean[] lastPotionCount;
        private final Player player;
        private final ItemStack stack;

        protected final ContainerData dataAccess = new ContainerData() {
            public int get(int p_59038_) {
                return 0;
            }

            public void set(int p_59040_, int p_59041_) {
            }

            public int getCount() {
                return 2;
            }
        };

        public ManaitaPlusBrewingStandBlockEntity(Player player, ItemStack stack) {
            super(ManaitaPlusBlockEntityCore.BREWING_BLOCK_ENTITY.get(), player.blockPosition(),null);
            this.player = player;
            this.stack = stack;
            load(stack.getOrCreateTag());
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

        private void doBrew(Level p_155291_,NonNullList<ItemStack> p_155293_) {
            if (net.minecraftforge.event.ForgeEventFactory.onPotionAttemptBrew(p_155293_)) return;
            ItemStack itemstack = p_155293_.get(3);

            net.minecraftforge.common.brewing.BrewingRecipeRegistry.brewPotions(p_155293_, itemstack, SLOTS_FOR_SIDES);
            for (int slotsForSide : SLOTS_FOR_SIDES) {
                ItemStack itemStack = p_155293_.get(slotsForSide);
                if (itemstack != ItemStack.EMPTY)  itemStack.setCount(itemStack.getCount() * 64);
            }
            net.minecraftforge.event.ForgeEventFactory.onPotionBrewed(p_155293_);
            if (itemstack.hasCraftingRemainingItem()) {
                ItemStack itemstack1 = itemstack.getCraftingRemainingItem().copy();
                itemstack.shrink(1);
                if (itemstack.isEmpty()) {
                    itemstack = itemstack1;
                } else {
                    Containers.dropItemStack(p_155291_, player.getX(), player.getY(), player.getZ(), itemstack1);
                }
            }
            else itemstack.shrink(1);

            p_155293_.set(3, itemstack);
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
                if (isBrewable(this.items)) {
                    doBrew(player.level(), this.items);
                }

                boolean[] aboolean = this.getPotionBits();
                if (!Arrays.equals(aboolean, this.lastPotionCount)) {
                    this.lastPotionCount = aboolean;
                }
            }
            saveAdditional(stack.getOrCreateTag());
        }

        public boolean stillValid(Player p_59000_) {
            return true;
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
            return new ManaitaPlusBrewingStandMenu(p_58990_, p_58991_, this, this.dataAccess);
        }
    }
}
