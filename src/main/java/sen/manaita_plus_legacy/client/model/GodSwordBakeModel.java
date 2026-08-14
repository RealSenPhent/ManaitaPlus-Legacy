package sen.manaita_plus_legacy.client.model;


import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.client.core.ManaitaPlusLegacyShaderCore;
import sen.manaita_plus_legacy.client.render.ManaitaPlusLegacyRenderStateShard;
import sen.manaita_plus_legacy.client.util.TimeUtil;
import sen.manaita_plus_legacy.client.util.TransformUtils;
import sen.manaita_plus_legacy.common.item.ManaitaPlusLegacyGodSwordItem;

import java.util.*;

public final class GodSwordBakeModel implements BakedModel {
    private static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();
    private static final FaceBakery FACE_BAKERY = new FaceBakery();
    private static final ResourceLocation dynamic = ManaitaPlusLegacy.rl("dynamic");

    private static final ResourceLocation texture = ManaitaPlusLegacy.rl("item/fall/fall_god_sword");
    private static final ResourceLocation texture1 = ManaitaPlusLegacy.rl("item/manaita_sword_god");
    private static final ResourceLocation texture2 = ManaitaPlusLegacy.rl("item/mask/mask");
    private final ItemOverrides overrideList;
    private ModelState parentState;
    private LivingEntity entity;
    private ClientLevel world;
    private ItemStack stack;
    private List<BakedQuad> quads;
    private List<BakedQuad> quads1;
    private List<BakedQuad> quads2;

    public GodSwordBakeModel() {
        this.parentState = TransformUtils.DEFAULT_TOOL;

        // overrides 依然用来捕获实体和世界，以便在渲染时使用
        this.overrideList = new ItemOverrides() {
            @Override
            public BakedModel resolve(@NotNull BakedModel originalModel, @NotNull ItemStack stack,
                                      ClientLevel world, LivingEntity entity, int seed) {
                GodSwordBakeModel.this.entity = entity;
                GodSwordBakeModel.this.world = (world == null && entity != null) ?
                        (ClientLevel) entity.level() : world;
                GodSwordBakeModel.this.stack = stack;
                // 不再关心 wrapped 的覆盖，直接返回自身
                return GodSwordBakeModel.this;
            }
        };
    }

    public void catchQuads() {
        Minecraft mc = Minecraft.getInstance();
        TextureAtlasSprite textureSprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);

        // 将每个蒙版纹理按原版物品模型生成方式拆解成四边形
        LinkedList<BakedQuad> quads = new LinkedList<>();

        // 用纹理生成无光栅的面（BlockElement）
        List<BlockElement> unbaked = ITEM_MODEL_GENERATOR.processFrames(
                0, "layer" + textureSprite, textureSprite.contents());
        for (BlockElement element : unbaked) {
            for (Map.Entry<Direction, BlockElementFace> entry : element.faces.entrySet()) {
                quads.add(FACE_BAKERY.bakeQuad(
                        element.from, element.to, entry.getValue(),
                        textureSprite, entry.getKey(),
                        new PerspectiveModelState(ImmutableMap.of()),
                        element.rotation, element.shade,
                        dynamic
                ));
            }
        }
        this.quads = quads;

        textureSprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture1);
        quads = new LinkedList<>();

        // 用纹理生成无光栅的面（BlockElement）
        unbaked = ITEM_MODEL_GENERATOR.processFrames(
                0, "layer" + textureSprite, textureSprite.contents());
        for (BlockElement element : unbaked) {
            for (Map.Entry<Direction, BlockElementFace> entry : element.faces.entrySet()) {
                quads.add(FACE_BAKERY.bakeQuad(
                        element.from, element.to, entry.getValue(),
                        textureSprite, entry.getKey(),
                        new PerspectiveModelState(ImmutableMap.of()),
                        element.rotation, element.shade,
                        dynamic
                ));
            }
        }
        this.quads1 = quads;

        textureSprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture2);
        quads = new LinkedList<>();

        // 用纹理生成无光栅的面（BlockElement）
        unbaked = ITEM_MODEL_GENERATOR.processFrames(
                0, "layer" + textureSprite, textureSprite.contents());
        for (BlockElement element : unbaked) {
            for (Map.Entry<Direction, BlockElementFace> entry : element.faces.entrySet()) {
                quads.add(FACE_BAKERY.bakeQuad(
                        element.from, element.to, entry.getValue(),
                        textureSprite, entry.getKey(),
                        new PerspectiveModelState(ImmutableMap.of()),
                        element.rotation, element.shade,
                        dynamic
                ));
            }
        }
        this.quads2 = quads;
    }

    /**
     * 现在只渲染蒙版纹理生成的模型，不渲染原有物品模型，也不使用着色器。
     */
    public void renderItem(ItemStack stack, ItemDisplayContext transformType,
                           PoseStack pStack, MultiBufferSource buffers,BakedModel bakedModel,
                           int packedLight, int packedOverlay) {
        if (quads == null || quads1 == null || quads2 == null) {
            catchQuads();
        }
        Minecraft mc = Minecraft.getInstance();
        RenderType p109903 = RenderType.entityCutout(InventoryMenu.BLOCK_ATLAS);
        VertexConsumer cons = buffers.getBuffer(p109903);
        List<BakedQuad> p115165 = isFallSword(stack) ? quads : quads1;

        renderGlowEdge(bakedModel,p115165,transformType,stack,pStack,buffers,packedLight,packedOverlay,0.0075f);
        mc.getItemRenderer().renderQuadList(pStack, cons, p115165, stack, packedLight, packedOverlay);

        if (isZlxxx(stack)) {
            if (buffers instanceof MultiBufferSource.BufferSource bs) {
                bs.endBatch();
            }
            mc.getItemRenderer().renderQuadList(pStack, buffers.getBuffer(ManaitaPlusLegacyRenderStateShard.createItemRenderType()), quads2, stack, packedLight, packedOverlay);
        } else {
            mc.getItemRenderer().renderQuadList(pStack, buffers.getBuffer(RenderType.glint()), p115165, stack, packedLight, packedOverlay);
        }
    }

    public static boolean isFallSword(ItemStack itemStack) {
        return itemStack.getHoverName().getString().contains("陨灭");
    }

    public static boolean isZlxxx(ItemStack itemStack) {
        return itemStack.getHoverName().getString().contains("zlxxx");
    }


    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public @NotNull BakedModel applyTransform(@NotNull ItemDisplayContext context,
                                              @NotNull PoseStack pStack,
                                              boolean leftFlip) {
        PerspectiveModelState modelState = (PerspectiveModelState) this.parentState;
        if (modelState != null) {
            Transformation transform = modelState.getTransform(context);
            Vector3f trans = transform.getTranslation();
            Vector3f scale = transform.getScale();
            pStack.translate(trans.x(), trans.y(), trans.z());
            pStack.mulPose(transform.getLeftRotation());
            pStack.scale(scale.x(), scale.y(), scale.z());
            pStack.mulPose(transform.getRightRotation());
            if (leftFlip) {
                pStack.mulPose(Axis.YN.rotationDegrees(180.0f));
            }
            return this;
        }
        return BakedModel.super.applyTransform(context, pStack, leftFlip);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(BlockState state, Direction side,
                                             @NotNull RandomSource rand) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                    .apply(isFallSword(stack) ? texture : texture1);
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return getParticleIcon();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    private void renderGlowEdge(BakedModel model,List<BakedQuad> p115165,ItemDisplayContext context, ItemStack stack, PoseStack pStack, MultiBufferSource source, int light, int overlay, float offset) {
        if (p115165 == null) {
            return;
        }
        ShaderInstance shader = ManaitaPlusLegacyShaderCore.galaxyShader;
        if (shader == null) {
            return;
        }

        int mode = ManaitaPlusLegacyGodSwordItem.getMode(stack) - 1;
        if (mode < 0) return;
        RenderSystem.assertOnRenderThread();
        RenderType galaxyItemRenderType = ManaitaPlusLegacyRenderStateShard.createGalaxyItemRenderType(mode);


        VertexConsumer consumer = source.getBuffer(galaxyItemRenderType);
        Vector3f[] directions = getVector3fs(context, offset);
        for (Vector3f direction : directions) {
            pStack.pushPose();
            pStack.translate(direction.x(), direction.y(), direction.z());
            int[] offsets = new int[]{direction.x() >= 0 ? 1 : 0, direction.y() >= 0 ? 1 : 0, direction.z() >= 0 ? 1 : 0};

            for (BakedModel bakedModel : model.getRenderPasses(stack, true)) {
                List<BakedQuad> generalQuads = bakedModel.getQuads(null, null, RandomSource.create());
                for (BakedQuad quad2 : generalQuads) {
                    Vec3i quadNormal = quad2.getDirection().getNormal();
                    float dotProduct = (offsets[0] * quadNormal.getX()) + (offsets[1] * quadNormal.getY()) + (offsets[2] * quadNormal.getZ());
                    if (dotProduct > 0.0f) {
                        consumer.putBulkData(pStack.last(), quad2, 1, 1, 1, 1, light, overlay, true);
                    }
                }
            }
            pStack.popPose();
        }
    }

    private static Vector3f[] getVector3fs(ItemDisplayContext context, float offset) {
        Vector3f[] directions;
        if (context == ItemDisplayContext.GUI) {
            float offset_g = offset + 0.0125f;
            directions = new Vector3f[]{new Vector3f(offset_g, offset_g, offset_g), new Vector3f(-offset_g, offset_g, offset_g), new Vector3f(offset_g, -offset_g, offset_g), new Vector3f(offset_g, offset_g, -offset_g), new Vector3f(-offset_g, -offset_g, offset_g), new Vector3f(-offset_g, offset_g, -offset_g), new Vector3f(offset_g, -offset_g, -offset_g), new Vector3f(-offset_g, -offset_g, -offset_g)};
        } else {
            directions = new Vector3f[]{
                    new Vector3f(offset, offset, offset),
                    new Vector3f(-offset, offset, offset),
                    new Vector3f(offset, -offset, offset),
                    new Vector3f(-offset, -offset, offset),
                    new Vector3f(offset, offset, -offset),
                    new Vector3f(-offset, offset, -offset),
                    new Vector3f(offset, -offset, -offset),
                    new Vector3f(-offset, -offset, -offset)
            };
        }
        return directions;
    }


    private boolean shouldRenderQuad(BakedQuad quad, Vector3f offsetDirection) {
        Direction quadDirection = quad.getDirection();
        Vec3i quadNormal = quadDirection.getNormal();
        float dotProduct = (offsetDirection.x() * quadNormal.getX()) + (offsetDirection.y() * quadNormal.getY()) + (offsetDirection.z() * quadNormal.getZ());
        return dotProduct > 0.0f;
    }

}