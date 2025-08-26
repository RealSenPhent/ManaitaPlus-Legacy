package sen.manaita_plus.common.item.tier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class ManaitaPlusItemTier implements Tier {
    public static final float MAX = (float) Double.MAX_VALUE;
//    static {
//        TierSortingRegistry.registerTier()
//    }
    @Override
    public int getUses() {
        return -1;
    }

    @Override
    public float getSpeed() {
        return MAX;
    }

    @Override
    public float getAttackDamageBonus() {
        return MAX;
    }

    @Override
    public int getLevel() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(Items.NETHERITE_INGOT);
    }

    @Override
    public @Nullable TagKey<Block> getTag() {
        return BlockTags.create(new ResourceLocation("manaita_tool"));
    }
}