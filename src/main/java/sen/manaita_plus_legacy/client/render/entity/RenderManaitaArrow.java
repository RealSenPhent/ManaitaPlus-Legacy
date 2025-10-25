package sen.manaita_plus_legacy.client.render.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sen.manaita_plus_legacy.common.entity.ManaitaPlusLegacyEntityArrow;

@OnlyIn(Dist.CLIENT)
public class RenderManaitaArrow extends ArrowRenderer<ManaitaPlusLegacyEntityArrow> {
    public static final ResourceLocation NORMAL_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/arrow.png");

    public RenderManaitaArrow(EntityRendererProvider.Context p_174422_) {
        super(p_174422_);
    }

    public ResourceLocation getTextureLocation(ManaitaPlusLegacyEntityArrow p_116140_) {
        return NORMAL_ARROW_LOCATION;
    }
}