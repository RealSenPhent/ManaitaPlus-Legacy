package sen.manaita_plus_legacy.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;

public class ManaitaPlusForgeGui extends ForgeGui {
    public ManaitaPlusForgeGui(Minecraft mc) {
        super(mc);
    }

    @Override
    public void renderHealth(int width, int height, GuiGraphics guiGraphics) {
        super.renderHealth(width, height, guiGraphics);
    }

}
