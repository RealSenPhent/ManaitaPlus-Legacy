package sen.manaita_plus.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sen.manaita_plus.common.gui.ManaitaPlusFurnaceMenu;

@OnlyIn(Dist.CLIENT)
public class FurnaceManaitaScreen extends AbstractFurnaceScreen<ManaitaPlusFurnaceMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");


    public FurnaceManaitaScreen(ManaitaPlusFurnaceMenu p_98776_, Inventory p_98777_, Component p_98778_) {
        super(p_98776_, new SmeltingRecipeBookComponent(), p_98777_, p_98778_, TEXTURE);
    }


    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {
        p_281635_.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        p_281635_.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
        p_281635_.drawString(this.font, "64x",118, 22, 4210752);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}