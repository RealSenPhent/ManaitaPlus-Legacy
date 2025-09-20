package sen.manaita_plus_legacy.common.item.portabl;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import sen.manaita_plus_legacy.common.core.ManaitaPlusBlockEntityCore;
import sen.manaita_plus_legacy.common.menu.ManaitaPlusFurnaceMenu;

import javax.annotation.Nullable;
import java.util.List;

public class ManaitaPlusFurnacePortabl extends Item {
    public ManaitaPlusFurnacePortabl() {
        super(new Properties().defaultDurability(-1).fireResistant().stacksTo(1));
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.translatable("item.portableFurnace."+ p_41458_.getOrCreateTag().getInt("ManaitaType") +".name");
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if (p_41433_ instanceof ServerPlayer serverPlayer) {
            ItemStack itemInHand = p_41433_.getItemInHand(p_41434_);
            NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("container.furnace_manaita");
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
                    ManaitaPlusFurnaceBlockEntity block = new ManaitaPlusFurnaceBlockEntity(p_39956_,itemInHand);
                    return block.createMenu(p_39954_, p_39955_, p_39956_);
                }
            });
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }


    public class ManaitaPlusFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
        private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
        private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;
        private final Player player;
        private final ItemStack stack;
        protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

        public ManaitaPlusFurnaceBlockEntity(Player player,ItemStack stack) {
            super(ManaitaPlusBlockEntityCore.FURNACE_BLOCK_ENTITY.get(),player.blockPosition(),null, RecipeType.SMELTING);
            this.quickCheck = RecipeManager.createCheck(RecipeType.SMELTING);
            this.player = player;
            this.stack = stack;
            load(stack.getOrCreateTag());
        }

        protected Component getDefaultName() {
            return Component.translatable("container.furnace_manaita");
        }

        protected AbstractContainerMenu createMenu(int p_59293_, Inventory p_59294_) {
            return new ManaitaPlusFurnaceMenu(p_59293_, p_59294_, this, this.dataAccess);
        }

        @Override
        public int getMaxStackSize() {
            return Integer.MAX_VALUE;
        }

        public void load(CompoundTag p_155025_) {
            super.load(p_155025_);
            ContainerHelper.loadAllItems(p_155025_, this.items);
            CompoundTag compoundtag = p_155025_.getCompound("RecipesUsed");

            for(String s : compoundtag.getAllKeys()) {
                this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
            }

        }

        protected void saveAdditional(CompoundTag p_187452_) {
            ContainerHelper.saveAllItems(p_187452_, this.items);
            CompoundTag compoundtag = new CompoundTag();
            this.recipesUsed.forEach((p_187449_, p_187450_) -> compoundtag.putInt(p_187449_.toString(), p_187450_));
            p_187452_.put("RecipesUsed", compoundtag);
        }



        private boolean canBurn(RegistryAccess p_266924_, @Nullable Recipe<?> p_155006_, NonNullList<ItemStack> p_155007_, int p_155008_) {
            if (!p_155007_.get(0).isEmpty() && p_155006_ != null) {
                ItemStack itemstack = ((Recipe<WorldlyContainer>) p_155006_).assemble(this, p_266924_);
                if (itemstack.isEmpty()) {
                    return false;
                } else {
                    ItemStack itemstack1 = p_155007_.get(2);
                    // Forge fix: make furnace respect stack sizes in furnace recipes
                    if (itemstack1.isEmpty()) {
                        return true;
                    } else return ItemStack.isSameItem(itemstack1, itemstack);
                }
            } else {
                return false;
            }
        }

        private boolean burn(RegistryAccess p_266740_, @Nullable Recipe<?> p_266780_, NonNullList<ItemStack> p_267073_, int p_267157_) {
            if (p_266780_ != null && this.canBurn(p_266740_, p_266780_, p_267073_, p_267157_)) {
                ItemStack itemstack = p_267073_.get(0);
                ItemStack itemstack1 = ((Recipe<WorldlyContainer>) p_266780_).assemble(this, p_266740_);
                ItemStack itemstack2 = p_267073_.get(2);
                if (itemstack2.isEmpty()) {
                    ItemStack copy = itemstack1.copy();
                    copy.setCount(copy.getCount() * 64);
                    p_267073_.set(2, copy);
                } else if (itemstack2.is(itemstack1.getItem())) {
                    itemstack2.grow(itemstack1.getCount() * 64);
                }

                itemstack.shrink(1);
                return true;
            } else {
                return false;
            }
        }

        protected int getBurnDuration(ItemStack p_58343_) {
            return 0;
        }



        public boolean canPlaceItemThroughFace(int p_58336_, ItemStack p_58337_, @Nullable Direction p_58338_) {
            return this.canPlaceItem(p_58336_, p_58337_);
        }

        public boolean canTakeItemThroughFace(int p_58392_, ItemStack p_58393_, Direction p_58394_) {
            if (p_58394_ == Direction.DOWN && p_58392_ == 1) {
                return p_58393_.is(Items.WATER_BUCKET) || p_58393_.is(Items.BUCKET);
            } else {
                return true;
            }
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

        public ItemStack getItem(int p_58328_) {
            return this.items.get(p_58328_);
        }

        public ItemStack removeItem(int p_58330_, int p_58331_) {
            return ContainerHelper.removeItem(this.items, p_58330_, p_58331_);
        }

        public ItemStack removeItemNoUpdate(int p_58387_) {
            return ContainerHelper.takeItem(this.items, p_58387_);
        }

        public void setItem(int p_58333_, ItemStack p_58334_) {
            this.items.set(p_58333_, p_58334_);
            if (!this.items.get(0).isEmpty()) {
                Level level = player.level();
                Recipe<?> recipe = this.quickCheck.getRecipeFor(this, level).orElse(null);

                int i = this.getMaxStackSize();
                while (true) {
                    if (!this.canBurn(level.registryAccess(), recipe, this.items, i))
                        break;
                    if (this.burn(level.registryAccess(), recipe, this.items, i)) {
                        this.setRecipeUsed(recipe);
                    }
                }
            }
//            save
            saveAdditional(stack.getOrCreateTag());
        }

        public boolean stillValid(Player p_58340_) {
            return true;
        }

        public boolean canPlaceItem(int p_58389_, ItemStack p_58390_) {
            return p_58389_ != 2;
        }

        public void clearContent() {
            this.items.clear();
        }

        public void setRecipeUsed(@Nullable Recipe<?> p_58345_) {
            if (p_58345_ != null) {
                ResourceLocation resourcelocation = p_58345_.getId();
                this.recipesUsed.addTo(resourcelocation, 1);
            }
        }

        @Nullable
        public Recipe<?> getRecipeUsed() {
            return null;
        }

        public void awardUsedRecipes(Player p_58396_, List<ItemStack> p_282202_) {
        }

        public void awardUsedRecipesAndPopExperience(ServerPlayer p_155004_) {
            List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience(p_155004_.serverLevel(), p_155004_.position());
            p_155004_.awardRecipes(list);

            for(Recipe<?> recipe : list) {
                if (recipe != null) {
                    p_155004_.triggerRecipeCrafted(recipe, this.items);
                }
            }

            this.recipesUsed.clear();
        }

        public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel p_154996_, Vec3 p_154997_) {
            List<Recipe<?>> list = Lists.newArrayList();

            for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
                p_154996_.getRecipeManager().byKey(entry.getKey()).ifPresent((p_155023_) -> {
                    list.add(p_155023_);
                    createExperience(p_154996_, p_154997_, entry.getIntValue(), ((AbstractCookingRecipe)p_155023_).getExperience());
                });
            }

            return list;
        }

        private static void createExperience(ServerLevel p_154999_, Vec3 p_155000_, int p_155001_, float p_155002_) {
            int i = Mth.floor((float)p_155001_ * p_155002_);
            float f = Mth.frac((float)p_155001_ * p_155002_);
            if (f != 0.0F && Math.random() < (double)f) {
                ++i;
            }

            ExperienceOrb.award(p_154999_, p_155000_, i * 64);
        }

        public void fillStackedContents(StackedContents p_58342_) {
            for(ItemStack itemstack : this.items) {
                p_58342_.accountStack(itemstack);
            }

        }
    }
}
