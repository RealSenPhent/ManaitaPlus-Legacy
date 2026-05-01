package sen.manaita_plus_legacy_core.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.math.MatrixUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;

import javax.annotation.Nullable;

import static net.minecraft.client.renderer.entity.ItemRenderer.*;
import static net.minecraft.client.renderer.entity.ItemRenderer.getFoilBuffer;

public class InvokeMethod {
    private static final ModelResourceLocation TRIDENT_MODEL = ModelResourceLocation.vanilla("trident", "inventory");
    private static final ModelResourceLocation SPYGLASS_MODEL = ModelResourceLocation.vanilla("spyglass", "inventory");

    public static void renderItem(@Nullable LivingEntity p_270101_,ItemStack p_115144_,ItemDisplayContext p_270188_,boolean p_115146_,PoseStack p_115147_,MultiBufferSource p_115148_,int p_115149_) {
        if (p_115144_.isEmpty()) return;
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        int p_115150_ = OverlayTexture.NO_OVERLAY;
        int p_270845_ = p_270101_.getId() + p_270188_.ordinal();
        @Nullable Level p_270641_ = p_270101_.level();
        BakedModel p_115151_ = itemRenderer.getModel(p_115144_, p_270641_, p_270101_, p_270845_);

        if (!p_115144_.isEmpty()) {
            p_115147_.pushPose();
            boolean flag = p_270188_ == ItemDisplayContext.GUI || p_270188_ == ItemDisplayContext.GROUND || p_270188_ == ItemDisplayContext.FIXED;
            if (flag) {
                if (p_115144_.is(Items.TRIDENT)) {
                    p_115151_ = itemRenderer.getItemModelShaper().getModelManager().getModel(TRIDENT_MODEL);
                } else if (p_115144_.is(Items.SPYGLASS)) {
                    p_115151_ = itemRenderer.getItemModelShaper().getModelManager().getModel(SPYGLASS_MODEL);
                }
            }

            p_115151_ = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(p_115147_, p_115151_, p_270188_, p_115146_);
            p_115147_.translate(-0.5F, -0.5F, -0.5F);
            if (!p_115151_.isCustomRenderer() && (!p_115144_.is(Items.TRIDENT) || flag)) {
                boolean flag1;
                if (p_270188_ != ItemDisplayContext.GUI && !p_270188_.firstPerson() && p_115144_.getItem() instanceof BlockItem) {
                    Block block = ((BlockItem)p_115144_.getItem()).getBlock();
                    flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                } else {
                    flag1 = true;
                }
                for (var model : p_115151_.getRenderPasses(p_115144_, flag1)) {
                    for (var rendertype : model.getRenderTypes(p_115144_, flag1)) {
                        VertexConsumer vertexconsumer;
                        if ((p_115144_.is(ItemTags.COMPASSES) || p_115144_.is(Items.CLOCK)) && p_115144_.hasFoil()) {
                            p_115147_.pushPose();
                            PoseStack.Pose posestack$pose = p_115147_.last();
                            if (p_270188_ == ItemDisplayContext.GUI) {
                                MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.5F);
                            } else if (p_270188_.firstPerson()) {
                                MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.75F);
                            }

                            if (flag1) {
                                vertexconsumer = getCompassFoilBufferDirect(p_115148_, rendertype, posestack$pose);
                            } else {
                                vertexconsumer = getCompassFoilBuffer(p_115148_, rendertype, posestack$pose);
                            }

                            p_115147_.popPose();
                        } else if (flag1) {
                            vertexconsumer = getFoilBufferDirect(p_115148_, rendertype, true, p_115144_.hasFoil());
                        } else {
                            vertexconsumer = getFoilBuffer(p_115148_, rendertype, true, p_115144_.hasFoil());
                        }

                        itemRenderer.renderModelLists(model, p_115144_, p_115149_, p_115150_, p_115147_, vertexconsumer);
                    }
                }
            } else {
                net.minecraftforge.client.extensions.common.IClientItemExtensions.of(p_115144_).getCustomRenderer().renderByItem(p_115144_, p_270188_, p_115147_, p_115148_, p_115149_, p_115150_);
            }

            p_115147_.popPose();
        }
    }

}
