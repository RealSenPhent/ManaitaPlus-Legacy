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

    @Override
    public void render(GuiGraphics p_282573_, int p_97859_, int p_97860_, float p_97861_) {
        super.render(p_282573_, p_97859_, p_97860_, p_97861_);
        p_282573_.drawCenteredString(this.font, "64x",112, 20, 4210752);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}