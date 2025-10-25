package sen.manaita_plus_legacy.common.item.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import sen.manaita_plus_legacy.common.util.ManaitaPlusLegacyNBTData;

public interface IManaitaPlusLegacyDoubling {
    default boolean isDoubling(ItemStack itemStack) {
        if (itemStack.getTag() == null)
            itemStack.setTag(new CompoundTag());
        return itemStack.getTag().getBoolean(ManaitaPlusLegacyNBTData.Doubling);
    }

    default void setDoubling(ItemStack itemStack, boolean doubling) {;
        itemStack.getOrCreateTag().putBoolean(ManaitaPlusLegacyNBTData.Doubling, doubling);
    }
}
