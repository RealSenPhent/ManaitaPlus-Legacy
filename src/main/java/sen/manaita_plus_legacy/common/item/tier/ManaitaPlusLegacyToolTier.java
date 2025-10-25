package sen.manaita_plus_legacy.common.item.tier;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyBlockCore;

public class ManaitaPlusLegacyToolTier implements Tier {
    @Override
    public int getUses() {
        return -1;
    }

    @Override
    public float getSpeed() {
        return Float.POSITIVE_INFINITY;
    }

    @Override
    public float getAttackDamageBonus() {
        return Float.POSITIVE_INFINITY;
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
        return Ingredient.of(ManaitaPlusLegacyBlockCore.CraftingBlockItem.get(), ManaitaPlusLegacyBlockCore.FurnaceBlockItem.get(), ManaitaPlusLegacyBlockCore.BrewingBlockItem.get());
    }

    @Override
    public @Nullable TagKey<Block> getTag() {
        return null;
    }
}