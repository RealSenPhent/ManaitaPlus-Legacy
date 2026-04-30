package sen.manaita_plus_legacy.common.item.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import sen.manaita_plus_legacy.common.util.tag.ManaitaPlusLegacyTagData;

public interface IManaitaPlusLegacyDoubling {
    default boolean isDoubling(int type) {
        return (type & ManaitaPlusLegacyTagData.doubling) != 0;
    }

    default boolean setDoubling(ItemStack itemStack,int type) {
        CompoundTag orCreateTag = itemStack.getOrCreateTag();
        if ((type & ManaitaPlusLegacyTagData.doubling) == 0) {
            orCreateTag.putInt(ManaitaPlusLegacyTagData.Type, type | ManaitaPlusLegacyTagData.doubling);
            return true;
        } else {
            orCreateTag.putInt(ManaitaPlusLegacyTagData.Type, type & ~ManaitaPlusLegacyTagData.doubling);
            return false;
        }
    }
}
