package sen.manaita_plus_legacy.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeType;
import sen.manaita_plus_legacy.common.core.ManaitaPlusMenuCore;

public class ManaitaPlusFurnaceMenu extends AbstractFurnaceMenu {
    public ManaitaPlusFurnaceMenu(int p_39532_, Inventory p_39533_, FriendlyByteBuf extraData) {
        super(ManaitaPlusMenuCore.FurnaceManaita.get(), RecipeType.SMELTING, RecipeBookType.FURNACE, p_39532_, p_39533_);
    }

    public ManaitaPlusFurnaceMenu(int p_39535_, Inventory p_39536_, Container p_39537_, ContainerData p_39538_) {
        super(ManaitaPlusMenuCore.FurnaceManaita.get(), RecipeType.SMELTING, RecipeBookType.FURNACE, p_39535_, p_39536_, p_39537_, p_39538_);
    }


}