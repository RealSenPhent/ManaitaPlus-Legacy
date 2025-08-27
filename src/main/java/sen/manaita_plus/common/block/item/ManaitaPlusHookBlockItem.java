package sen.manaita_plus.common.block.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import static sen.manaita_plus.common.core.ManaitaPlusBlockCore.HookBlock;

public class ManaitaPlusHookBlockItem extends BlockItem {
    public ManaitaPlusHookBlockItem() {
        super(HookBlock.get(), new Properties().fireResistant());
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.translatable("tile.fixed_hook." + p_41458_.getOrCreateTag().getInt("ManaitaType") + ".name");
    }

}
