package sen.manaita_plus.common.block.item;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import sen.manaita_plus.common.util.ManaitaPlusUtils;

import static sen.manaita_plus.common.core.ManaitaPlusBlockCore.FurnaceBlock;

public class ManaitaPlusFurnaceBlockItem extends BlockItem {
    public ManaitaPlusFurnaceBlockItem() {
        super(FurnaceBlock.get(), new Item.Properties().fireResistant());
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.literal(I18n.get("block.furnace."+ ManaitaPlusUtils.getTypes(p_41458_.getOrCreateTag().getInt("ManaitaType")) +"name"));
    }
}
