package sen.manaita_plus.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sen.manaita_plus.common.block.ManaitaPlusBrewingStandBlock;
import sen.manaita_plus.common.block.data.Data;
import sen.manaita_plus.common.block.entity.ManaitaPlusBrewingStandBlockEntity;
import sen.manaita_plus.common.core.ManaitaPlusBlockCore;

@OnlyIn(Dist.CLIENT)
public class RenderBrewingManaitaBlockEntity implements BlockEntityRenderer<ManaitaPlusBrewingStandBlockEntity> {
    private final EntityRenderDispatcher entityRenderer;
    private final ItemStack stack;

    public RenderBrewingManaitaBlockEntity(BlockEntityRendererProvider.Context p_173673_) {
        this.entityRenderer = p_173673_.getEntityRenderer();
        stack = new ItemStack(ManaitaPlusBlockCore.BrewingBlockItem.get());
    }



    @Override
    public void render(ManaitaPlusBrewingStandBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState blockState = blockEntity.getBlockState();

        Direction direction = blockState.getValue(Data.FACING);
        Direction wall = blockState.getValue(Data.WALL);

        poseStack.pushPose();

        switch (wall) {
            case NORTH:
                poseStack.translate(0.5F, 0.5F, 0.0F);
                poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-90.0F));
                break;
            case SOUTH:
                poseStack.translate(0.5F, 0.5F, 1F);
                poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90.0F));
                break;
            case WEST:
                poseStack.translate(0.0F, 0.5F, 0.5F);
                poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(90.0F));
                break;
            case EAST:
                poseStack.translate(1F, 0.5F, 0.5F);
                poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(90.0F));
                break;
            case UP:
                poseStack.translate(0.5F, 1F, 0.5F);
                switch (direction) {
                    case NORTH:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                        break;
                    case SOUTH:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-90.0F));
                        break;
                    case WEST:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90.0F));
                        break;
                    case EAST:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-90.0F));
                        break;
                }
                break;
            case DOWN:
                poseStack.translate(0.5F, 0.0F, 0.5F);
                switch (direction) {
                    case NORTH:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                        break;
                    case SOUTH:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-90.0F));
                        break;
                    case WEST:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90.0F));
                        break;
                    case EAST:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-90.0F));
                        break;
                }
                break;
        }


        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        Block block = blockEntity.getBlockState().getBlock();
        if (block instanceof ManaitaPlusBrewingStandBlock) {
            CompoundTag p41752 = new CompoundTag();
            p41752.putInt("ManaitaType",blockEntity.getBlockState().getValue(Data.TYPES));
            stack.setTag(p41752);
            BakedModel bakedModel = itemRenderer.getModel(stack,blockEntity.getLevel(),null,0);
            itemRenderer.render(stack, ItemDisplayContext.FIXED,true,poseStack,bufferSource,packedLight,packedOverlay,bakedModel);
        }

        poseStack.popPose();
    }


    @Override
    public boolean shouldRenderOffScreen(ManaitaPlusBrewingStandBlockEntity p_112306_) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(p_112306_);
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(ManaitaPlusBrewingStandBlockEntity p_173568_, Vec3 p_173569_) {
        return BlockEntityRenderer.super.shouldRender(p_173568_, p_173569_);
    }
}
