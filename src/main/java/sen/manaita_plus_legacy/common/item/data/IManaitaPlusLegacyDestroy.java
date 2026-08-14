package sen.manaita_plus_legacy.common.item.data;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;
import sen.manaita_plus_legacy.common.util.tag.ManaitaPlusLegacyTagData;
import sen.manaita_plus_legacy.common.util.text.ManaitaPlusText;

public interface IManaitaPlusLegacyDestroy {
    boolean accept(BlockState state);
    default int getRange(ItemStack itemStack) {
        if (!itemStack.hasTag()) return 1;
        assert itemStack.getTag() != null;
        return itemStack.getTag().getInt(ManaitaPlusLegacyTagData.Range) | 1;
    }

    default void setRange(ItemStack itemStack,int range) {
        itemStack.getOrCreateTag().putInt(ManaitaPlusLegacyTagData.Range, range);
    }
}
