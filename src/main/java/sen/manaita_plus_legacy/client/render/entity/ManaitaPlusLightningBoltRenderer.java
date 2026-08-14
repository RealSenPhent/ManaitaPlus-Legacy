package sen.manaita_plus_legacy.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import sen.manaita_plus_legacy.client.render.ManaitaPlusLegacyRenderStateShard;
import sen.manaita_plus_legacy.common.entity.ManaitaPlusLegacyLightningBolt;
import sen.manaita_plus_legacy_core.transform.ManaitaPlusLegacyLaunchPluginService;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class ManaitaPlusLightningBoltRenderer extends EntityRenderer<ManaitaPlusLegacyLightningBolt> {
    public ManaitaPlusLightningBoltRenderer(EntityRendererProvider.Context p_174286_) {
        super(p_174286_);
    }


    public void render(ManaitaPlusLegacyLightningBolt p_115266_, float p_115267_, float p_115268_, PoseStack p_115269_, MultiBufferSource p_115270_, int p_115271_) {
        float[] afloat = new float[8];
        float[] afloat1 = new float[8];
        float f = 0.0F;
        float f1 = 0.0F;

        RandomSource randomsource = RandomSource.create(p_115266_.seed);
        for(int i = 7; i >= 0; --i) {
            afloat[i] = f;
            afloat1[i] = f1;
            f += (float)(randomsource.nextInt(11) - 5);
            f1 += (float)(randomsource.nextInt(11) - 5);
        }

        VertexConsumer vertexconsumer = p_115270_.getBuffer(ManaitaPlusLegacyLaunchPluginService.renderLevelRenderer ? ManaitaPlusLegacyRenderStateShard.createCosmicRenderType(1) : RenderType.lightning());
        Matrix4f matrix4f = p_115269_.last().pose();
        Random random = new Random(p_115266_.seed);
        float r = random.nextFloat();
        float g = random.nextFloat();
        float b = random.nextFloat();
        float a = 0.07F;
        for(int j = 0; j < 4; ++j) {
            RandomSource randomsource1 = RandomSource.create(p_115266_.seed);

            for(int k = 0; k < 3; ++k) {
                int l = 7;
                int i1 = 0;
                if (k > 0) {
                    l = 7 - k;
                }

                if (k > 0) {
                    i1 = l - 2;
                }

                float f2 = afloat[l] - f;
                float f3 = afloat1[l] - f1;

                for(int j1 = l; j1 >= i1; --j1) {
                    float f4 = f2;
                    float f5 = f3;
                    if (k == 0) {
                        f2 += (float)(randomsource1.nextInt(11) - 5);
                        f3 += (float)(randomsource1.nextInt(11) - 5);
                    } else {
                        f2 += (float)(randomsource1.nextInt(31) - 15);
                        f3 += (float)(randomsource1.nextInt(31) - 15);
                    }


                    float f10 = 0.1F + (float)j * 0.2F;
                    if (k == 0) {
                        f10 *= (float)j1 * 0.1F + 1.0F;
                    }

                    float f11 = 0.1F + (float)j * 0.2F;
                    if (k == 0) {
                        f11 *= ((float)j1 - 1.0F) * 0.1F + 1.0F;
                    }


                    quad(matrix4f, vertexconsumer, f2, f3, j1, f4, f5, r, g, b, a, f10, f11, false, false, true, false);
                    quad(matrix4f, vertexconsumer, f2, f3, j1, f4, f5, r, g, b, a, f10, f11, true, false, true, true);
                    quad(matrix4f, vertexconsumer, f2, f3, j1, f4, f5, r, g, b, a, f10, f11, true, true, false, true);
                    quad(matrix4f, vertexconsumer, f2, f3, j1, f4, f5, r, g, b, a, f10, f11, false, true, false, false);
                }
            }
        }

    }


    private static void quad(Matrix4f mat, VertexConsumer consumer,
                             float x1, float y1, int step, float x2, float y2,
                             float r, float g, float b, float a, float w1, float w2,
                             boolean p1, boolean p2, boolean p3, boolean p4) {

        consumer.vertex(mat, x1 + (p1 ? w2 : -w2), step * 16, y1 + (p2 ? w2 : -w2))
                .color(r, g, b, a)
                .endVertex();

        consumer.vertex(mat, x2 + (p1 ? w1 : -w1), (step + 1) * 16, y2 + (p2 ? w1 : -w1))
                .color(r, g, b, a)
                .endVertex();

        consumer.vertex(mat, x2 + (p3 ? w1 : -w1), (step + 1) * 16, y2 + (p4 ? w1 : -w1))
                .color(r, g, b, a)
                .endVertex();

        consumer.vertex(mat, x1 + (p3 ? w2 : -w2), step * 16, y1 + (p4 ? w2 : -w2))
                .color(r, g, b, a)
                .endVertex();
    }

    public @NotNull ResourceLocation getTextureLocation(ManaitaPlusLegacyLightningBolt p_115264_) {
        return InventoryMenu.BLOCK_ATLAS;
    }



}