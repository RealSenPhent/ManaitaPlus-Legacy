package sen.manaita_plus_legacy.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.common.config.ManaitaPlusLegacyConfig;
import sen.manaita_plus_legacy.common.menu.ManaitaPlusLegacyFurnaceMenu;

@OnlyIn(Dist.CLIENT)
public class FurnaceManaitaScreen extends AbstractContainerScreen<ManaitaPlusLegacyFurnaceMenu> implements RecipeUpdateListener {
    private static final ResourceLocation TEXTURE = ManaitaPlusLegacy.rl("minecraft","textures/gui/container/furnace.png");
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = ManaitaPlusLegacy.rl("minecraft","textures/gui/recipe_button.png");
    public final AbstractFurnaceRecipeBookComponent recipeBookComponent;
    private boolean widthTooNarrow;
    private final ResourceLocation texture;
    private final String doubling_text;

    public FurnaceManaitaScreen(ManaitaPlusLegacyFurnaceMenu p_98776_, Inventory p_98777_, Component p_98778_) {
        super(p_98776_, p_98777_, p_98778_);
        this.recipeBookComponent = new SmeltingRecipeBookComponent();
        this.texture = TEXTURE;
        doubling_text = ManaitaPlusLegacyConfig.furnace_doubling_value + "x";
    }

    protected void renderLabels(GuiGraphics p_281635_, int p_282681_, int p_283686_) {
        p_281635_.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        p_281635_.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
        p_281635_.drawString(this.font, doubling_text,118 - font.width(doubling_text) / 2, 22, 4210752,false);
    }

    public void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
        this.addRenderableWidget(new ImageButton(this.leftPos + 20, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (p_289628_) -> {
            this.recipeBookComponent.toggleVisibility();
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.width, this.imageWidth);
            p_289628_.setPosition(this.leftPos + 20, this.height / 2 - 49);
        }));
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void containerTick() {
        super.containerTick();
        this.recipeBookComponent.tick();
    }

    public void render(GuiGraphics p_282573_, int p_97859_, int p_97860_, float p_97861_) {
        this.renderBackground(p_282573_);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(p_282573_, p_97861_, p_97859_, p_97860_);
            this.recipeBookComponent.render(p_282573_, p_97859_, p_97860_, p_97861_);
        } else {
            this.recipeBookComponent.render(p_282573_, p_97859_, p_97860_, p_97861_);
            super.render(p_282573_, p_97859_, p_97860_, p_97861_);
            this.recipeBookComponent.renderGhostRecipe(p_282573_, this.leftPos, this.topPos, true, p_97861_);
        }

        this.renderTooltip(p_282573_, p_97859_, p_97860_);
        this.recipeBookComponent.renderTooltip(p_282573_, this.leftPos, this.topPos, p_97859_, p_97860_);
    }

    protected void renderBg(GuiGraphics p_282928_, float p_281631_, int p_281252_, int p_281891_) {
        int i = this.leftPos;
        int j = this.topPos;
        p_282928_.blit(this.texture, i, j, 0, 0, this.imageWidth, this.imageHeight);

        p_282928_.blit(this.texture, i + 79, j + 34, 176, 14, 1, 16);
    }

    public boolean mouseClicked(double p_97834_, double p_97835_, int p_97836_) {
        if (this.recipeBookComponent.mouseClicked(p_97834_, p_97835_, p_97836_)) {
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(p_97834_, p_97835_, p_97836_);
        }
    }

    protected void slotClicked(Slot p_97848_, int p_97849_, int p_97850_, ClickType p_97851_) {
        super.slotClicked(p_97848_, p_97849_, p_97850_, p_97851_);
        this.recipeBookComponent.slotClicked(p_97848_);
    }

    public boolean keyPressed(int p_97844_, int p_97845_, int p_97846_) {
        return !this.recipeBookComponent.keyPressed(p_97844_, p_97845_, p_97846_) && super.keyPressed(p_97844_, p_97845_, p_97846_);
    }

    protected boolean hasClickedOutside(double p_97838_, double p_97839_, int p_97840_, int p_97841_, int p_97842_) {
        boolean flag = p_97838_ < (double)p_97840_ || p_97839_ < (double)p_97841_ || p_97838_ >= (double)(p_97840_ + this.imageWidth) || p_97839_ >= (double)(p_97841_ + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(p_97838_, p_97839_, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, p_97842_) && flag;
    }

    public boolean charTyped(char p_97831_, int p_97832_) {
        return this.recipeBookComponent.charTyped(p_97831_, p_97832_) || super.charTyped(p_97831_, p_97832_);
    }

    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    public RecipeBookComponent getRecipeBookComponent() {
        return this.recipeBookComponent;
   }
}