package sen.manaita_plus.common.block.item;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import static sen.manaita_plus.common.core.ManaitaPlusBlockCore.BrewingBlock;

public class ManaitaPlusBrewingBlockItem extends BlockItem {
    public ManaitaPlusBrewingBlockItem() {
        super(BrewingBlock.get(), new Properties().fireResistant());
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.literal(I18n.get("block.brewing.name"));
    }

}
