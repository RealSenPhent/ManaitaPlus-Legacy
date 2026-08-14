package sen.manaita_plus_legacy_core.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.client.model.GodSwordBakeModel;
import sen.manaita_plus_legacy.client.network.implement.PreventDropPacket;
import sen.manaita_plus_legacy.common.entity.ManaitaPlusLegacyLightningBolt;
import sen.manaita_plus_legacy.common.item.ManaitaPlusLegacyGodSwordItem;
import sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase;
import sen.manaita_plus_legacy.common.network.Networking;
import sen.manaita_plus_legacy.common.proxy.CommomProxy;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy.common.util.item.ManaitaPlusItemData;
import sen.manaita_plus_legacy.common.util.item.ManaitaPlusLegacyItemStack;

import javax.annotation.Nullable;
import java.util.*;

public class ClientEventUtil {
    public static boolean isManaita(LocalPlayer localPlayer) {
        return localPlayer.getInventory().hasAnyMatching(stack -> !stack.isEmpty() && stack.getItem() instanceof ManaitaPlusLegacyGodSwordItem) || ManaitaPlusLegacyEntityData.manaita.accept(localPlayer);
    }

    public static boolean isDead(LocalPlayer localPlayer) {
        return ManaitaPlusLegacyEntityData.death.accept(localPlayer);
    }

    public static boolean isNotSafe(@Nullable Screen screen) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if (screen instanceof DeathScreen) {
                if (ManaitaPlusLegacyEntityData.anti.accept(player))
                    return true;
                Inventory inventory = player.getInventory();
                for (int i = 0; i < inventory.getContainerSize(); ++i) {
                    ItemStack itemstack = inventory.getItem(i);
                    if (itemstack.getItem() instanceof ManaitaPlusLegacyGodSwordItem)
                        return true;
                }
            }
        }
        return false;
    }

    public static void setScreen(Minecraft mc,@Nullable Screen screen) {
        LocalPlayer player = mc.player;
        if (player != null) {
            if (screen instanceof DeathScreen) {
                if (ManaitaPlusLegacyEntityData.manaita.accept(player))
                    return;
                Inventory inventory = player.getInventory();
                for (int i = 0; i < inventory.getContainerSize(); ++i) {
                    ItemStack itemstack = inventory.getItem(i);
                    if (itemstack.getItem() instanceof ManaitaPlusLegacyGodSwordItem)
                        return;
                }
            }
        }
        mc.screen = screen;
    }

    public static boolean shouldRenderHeldItemBlocking(LivingEntity entityLivingBase,ItemStack stack,HumanoidArm handSide) {
        return stack.getItem() instanceof ManaitaPlusLegacyGodSwordItem && entityLivingBase.isUsingItem() && entityLivingBase.getUsedItemHand() == (handSide == HumanoidArm.LEFT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
    }

    public static void renderArmWithItem(LivingEntity p_117185_, ItemStack p_117186_, ItemDisplayContext p_270970_, HumanoidArm p_117188_, PoseStack p_117189_, MultiBufferSource p_117190_, int p_117191_, ItemInHandRenderer itemInHandRenderer) {
        boolean leftHand = p_117188_ == HumanoidArm.LEFT;

        p_117189_.translate((leftHand ? 1 : -1) / 16.0F, 0.4375F, 0.0625F);
        p_117189_.translate(leftHand ? -0.035F : 0.05F, leftHand ? 0.045F : 0.0F, leftHand ? -0.135F : -0.1F);
        p_117189_.mulPose(Axis.YP.rotationDegrees((leftHand ? -1 : 1) * -50.0F));
        p_117189_.mulPose(Axis.XP.rotationDegrees(-10.0F));
        p_117189_.mulPose(Axis.ZP.rotationDegrees((leftHand ? -1 : 1) * -60.0F));
        p_117189_.translate(0.0F, 0.1875F, 0.0F);
        p_117189_.scale(0.625F, -0.625F, 0.625F);
        p_117189_.mulPose(Axis.XP.rotationDegrees(-100.0F));
        p_117189_.mulPose(Axis.YP.rotationDegrees(leftHand ? 35.0F : 45.0F));
        p_117189_.translate(0.0F, -0.3F, 0.0F);
        p_117189_.scale(1.5F, 1.5F, 1.5F);
        p_117189_.mulPose(Axis.YP.rotationDegrees(50.0F));
        p_117189_.mulPose(Axis.ZP.rotationDegrees(335.0F));
        p_117189_.translate(-0.9375F, -0.0625F, 0.0F);
        p_117189_.translate(0.5F, 0.5F, 0.25F);
        p_117189_.mulPose(Axis.YP.rotationDegrees(180.0F));
        p_117189_.translate(0.0F, 0.0F, 0.28125F);
        float scale = 1.0F / 0.85F;
        p_117189_.scale(scale, scale, scale);
        float f = 0;
        float f1 = -90.0F * 0.008726646F;
        float f2 = 55.0F * 0.008726646F;
        float f3 = Mth.sin(f);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f1);
        float f6 = Mth.cos(f1);
        float f7 = Mth.sin(f2);
        float f8 = Mth.cos(f2);
        Quaternionf quat = new Quaternionf(-(f3 * f6 * f8 + f4 * f5 * f7), -(f4 * f5 * f8 - f3 * f6 * f7), -(f3 * f5 * f8 + f4 * f6 * f7), f4 * f6 * f8 - f3 * f5 * f7);
        p_117189_.mulPose(quat);
        p_117189_.translate(0.0F, -0.27573525F, -0.0344669F);
        InvokeMethod.renderItem(p_117185_, p_117186_, p_270970_, leftHand, p_117189_, p_117190_, p_117191_);
        p_117189_.popPose();
    }


    public static int htaedTime = 20;
    public static void runTickBefore(Minecraft mc) {
        if (ManaitaPlusLegacyEntityData.death.accept(mc.player)) {
//            if (!(mc.screen instanceof DeathScreen)) {
//                DeathScreen screen = new DeathScreen(null, mc.level != null && mc.level.getLevelData().isHardcore());
//                screen.added();
//                BufferUploader.reset();
//
//                mc.mouseHandler.releaseMouse();
//                KeyMapping.releaseAll();
//                screen.init(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
//                mc.noRender = false;
//                mc.updateTitle();
//
//                mc.screen = screen;
//            }
        }
        if (ManaitaPlusLegacyEntityData.manaita.accept(mc.player)) {
            if (mc.screen instanceof DeathScreen) {
                mc.screen = null;
                mc.getSoundManager().resume();
                mc.mouseHandler.grabMouse();
            }
            for (ItemStack item : mc.player.getInventory().items) {
                if (item.getItem() instanceof ManaitaPlusLegacyGodSwordItem) {
                    if (ManaitaPlusLegacyGodSwordItem.isAntiDisarming(ManaitaPlusLegacyToolBase.getType(item))) {
                        ManaitaPlusLegacyEntityData.anti.add(mc.player);
                        return;
                    }

                }
            }
        }

        if (ManaitaPlusLegacyEntityData.anti.accept(mc.player)) {
            Inventory inventory = mc.player.getInventory();
//            if (ManaitaPlusItemData.stackList.isEmpty()) {
            for (ItemStack item : inventory.items) {
                if (item.getItem() instanceof ManaitaPlusLegacyGodSwordItem) {
                    return;
                }
            }
            inventory.setItem(0, ManaitaPlusLegacyItemStack.instance.copy());
            Networking.sendToServer(new PreventDropPacket(0, ManaitaPlusItemData.current.getOrCreateTag()));

//            } else {
//                int i = 0;
//                for (ItemStack itemStack : ManaitaPlusItemData.stackList) {
//                    if (inventory.contains(itemStack)) continue;
//                    int freeSlot = getFreeSlot(inventory, i);
//                    if (freeSlot == - 1) return;
//                    i = freeSlot;
//                    inventory.setItem(freeSlot,itemStack);
//                    Networking.sendToServer(new PreventDropPacket(freeSlot,itemStack));
//                }
//            }
        }
    }

//    public static int getFreeSlot(Inventory inventory,int last) {
//        int freeSlot = inventory.getFreeSlot();
//        if (freeSlot == -1 || freeSlot > 9) {
//            do {
//                last++;
//            } while (inventory.getItem(last).getItem() instanceof ManaitaPlusLegacyGodSwordItem && last < 36);
//            if (last == 36) return -1;
//            return last;
//        }
//        return freeSlot;
//    }

    public static void renderEntity(EntityRenderDispatcher entityRenderDispatcher,Entity p_109518_, double p_109519_, double p_109520_, double p_109521_, float p_109522_, PoseStack p_109523_, MultiBufferSource p_109524_) {
        double d0 = Mth.lerp((double)p_109522_, p_109518_.xOld, p_109518_.getX());
        double d1 = Mth.lerp((double)p_109522_, p_109518_.yOld, p_109518_.getY());
        double d2 = Mth.lerp((double)p_109522_, p_109518_.zOld, p_109518_.getZ());
        float f = Mth.lerp(p_109522_, p_109518_.yRotO, p_109518_.getYRot());
        entityRenderDispatcher.render(p_109518_, d0 - p_109519_, d1 - p_109520_, d2 - p_109521_, f, p_109522_, p_109523_, p_109524_, entityRenderDispatcher.getPackedLightCoords(p_109518_, p_109522_));
    }

    public static void bobHurt(Minecraft mc,PoseStack p_109118_, float p_109119_) {
        if (mc.getCameraEntity() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)mc.getCameraEntity();
            float f = (float)livingentity.hurtTime - p_109119_;
            if (p_109119_ == 1.0F && htaedTime > 0) htaedTime--;
            float f1 = Math.min(20 - htaedTime + p_109119_, 20.0F);

            p_109118_.mulPose(Axis.ZP.rotationDegrees(40.0F - 8000.0F / (f1 + 200.0F)));

            if (f < 0.0F) {
                return;
            }

            f /= (float)livingentity.hurtDuration;
            f = Mth.sin(f * f * f * f * (float)Math.PI);
            float f3 = livingentity.getHurtDir();
            p_109118_.mulPose(Axis.YP.rotationDegrees(-f3));
            float f2 = (float)((double)(-f) * 14.0D * mc.options.damageTiltStrength().get());
            p_109118_.mulPose(Axis.ZP.rotationDegrees(f2));
            p_109118_.mulPose(Axis.YP.rotationDegrees(f3));
        }

    }
    private static final Map<Entity, Integer> de = new WeakHashMap<>();

    public static void setupRotationsM(LivingEntity p_115317_, PoseStack p_115318_, float p_115320_, float p_115321_) {
        if (!p_115317_.hasPose(Pose.SLEEPING)) {
            p_115318_.mulPose(Axis.YP.rotationDegrees(180.0F - p_115320_));
        }

        if (p_115317_.isAutoSpinAttack()) {
            p_115318_.mulPose(Axis.XP.rotationDegrees(-90.0F - p_115317_.getXRot()));
            p_115318_.mulPose(Axis.YP.rotationDegrees(((float)p_115317_.tickCount + p_115321_) * -75.0F));
        } else if (p_115317_.hasPose(Pose.SLEEPING)) {
            Direction direction = p_115317_.getBedOrientation();
            float f1 = direction != null ?  (switch (direction) {
                case SOUTH -> 90.0F;
                case NORTH -> 270.0F;
                case EAST -> 180.0F;
                default -> 0.0F;
            }) : p_115320_;
            p_115318_.mulPose(Axis.YP.rotationDegrees(f1));
            p_115318_.mulPose(Axis.ZP.rotationDegrees(90.0F));
            p_115318_.mulPose(Axis.YP.rotationDegrees(270.0F));
        } else if (LivingEntityRenderer.isEntityUpsideDown(p_115317_)) {
            p_115318_.translate(0.0F, p_115317_.getBbHeight() + 0.1F, 0.0F);
            p_115318_.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }

    public static void setupRotationsD(LivingEntity p_115317_, PoseStack p_115318_,float p_115321_) {
        if (ManaitaPlusLegacyEntityData.death.accept(p_115317_)) {
            int anInt = de.computeIfAbsent(p_115317_ , (entity) -> entity.tickCount - 1);
            float f = ((float) p_115317_.tickCount - anInt + p_115321_ - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            p_115318_.mulPose(Axis.ZP.rotationDegrees(f * 90.0F));
        }
    }

    public static void renderLevelAfter(Minecraft mc,float pT,PoseStack poseStack) {
        if (ManaitaPlusLegacy.proxy.isShaderPackInUse()) {
            render(mc,pT);
            renderAllPendingItems(pT,poseStack);
        }
    }

//    public static void renderAllPendingItems(float pt, PoseStack worldPoseStack) {
//        if (PENDING_RENDERS.isEmpty()) {
//            return;
//        }
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.level == null || mc.player == null) {
//            return;
//        }
//        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
//        List<RenderItemData> firstPersonItems = new ArrayList<>();
//        List<RenderItemData> otherItems = new ArrayList<>();
//        for (RenderItemData data : PENDING_RENDERS) {
//            if (data.transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || data.transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
//                firstPersonItems.add(data);
//            } else {
//                otherItems.add(data);
//            }
//        }
//        if (!otherItems.isEmpty()) {
//            for (RenderItemData data2 : otherItems) {
//                renderSingleItem(data2, pt, mc, bufferSource, worldPoseStack);
//            }
//        }
//        if (!firstPersonItems.isEmpty()) {
//            for (RenderItemData data3 : firstPersonItems) {
//                RenderSystem.clear(256, Minecraft.ON_OSX);
//                renderSingleItem(data3, pt, mc, bufferSource, worldPoseStack);
//            }
//        }
//        PENDING_RENDERS.clear();
//    }
//
//    private static void renderSingleItem(RenderItemData data, float pt, Minecraft mc, MultiBufferSource.BufferSource bufferSource, PoseStack worldPoseStack) {
//        PoseStack poseStack = new PoseStack();
//        try {
//            data.renderState.restore();
//            poseStack.pushPose();
//            for (RenderItemData.PoseEntry entry : data.poseStackEntries) {
//                poseStack.last().pose().set(entry.poseMatrix);
//                poseStack.last().normal().set(entry.normalMatrix);
//            }
//            CosmicBakeModelNeo renderer = new CosmicBakeModelNeo(data.wrapped, data.maskSprite);
//            renderer.entity = data.entity;
//            renderer.world = data.world;
//            renderer.renderItemDirect(data.stack, data.transformType, poseStack, bufferSource, data.packedLight, data.packedOverlay, data.config);
//            bufferSource.endBatch();
//            poseStack.popPose();
//            data.renderState.cleanup();
//        } catch (Throwable th) {
//            bufferSource.endBatch();
//            poseStack.popPose();
//            data.renderState.cleanup();
//            throw th;
//        }
//    }



    public static void render(Minecraft minecraft,float partialTicks) {
        if (minecraft.level == null || minecraft.player == null) {
            return;
        }
        RenderSystem.backupProjectionMatrix();
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        try {
            Camera camera = minecraft.gameRenderer.getMainCamera();
            Vec3 cameraPos = camera.getPosition();
            double fov = minecraft.gameRenderer.getFov(camera, partialTicks, true);
            Matrix4f projectionMatrix = minecraft.gameRenderer.getProjectionMatrix(fov);
            RenderSystem.setProjectionMatrix(projectionMatrix, VertexSorting.DISTANCE_TO_ORIGIN);
            PoseStack viewPoseStack = new PoseStack();
            if (minecraft.options.bobView().get()) {
                minecraft.gameRenderer.bobView(viewPoseStack, partialTicks);
            }
            minecraft.gameRenderer.bobHurt(viewPoseStack, partialTicks);
            viewPoseStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
            viewPoseStack.mulPose(Axis.YP.rotationDegrees(camera.getYRot() + 180.0f));
            modelViewStack.setIdentity();
            modelViewStack.mulPoseMatrix(viewPoseStack.last().pose());
            RenderSystem.applyModelViewMatrix();
            RenderSystem.enableDepthTest();
            RenderSystem.depthFunc(515);
            RenderSystem.depthMask(true);
            RenderSystem.enableCull();
            MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
            renderEntities(minecraft, cameraPos, partialTicks, bufferSource);
            bufferSource.endBatch();
            modelViewStack.popPose();
            RenderSystem.restoreProjectionMatrix();
            RenderSystem.applyModelViewMatrix();
        } catch (Throwable th) {
            modelViewStack.popPose();
            RenderSystem.restoreProjectionMatrix();
            RenderSystem.applyModelViewMatrix();
            throw th;
        }


    }


    public static Set<Class<? extends Entity>> RENDERABLE_ENTITY_TYPES = Set.of(ManaitaPlusLegacyLightningBolt.class);

    private static void renderEntities(Minecraft minecraft, Vec3 cameraPos, float partialTicks, MultiBufferSource.BufferSource bufferSource) {
        EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        if (minecraft.level != null) {
            for (Entity entity : minecraft.level.entitiesForRendering()) {
                if (entity != null && RENDERABLE_ENTITY_TYPES.contains(entity.getClass())) {
                    double x = Mth.lerp(partialTicks, entity.xOld, entity.getX());
                    double y = Mth.lerp(partialTicks, entity.yOld, entity.getY());
                    double z = Mth.lerp(partialTicks, entity.zOld, entity.getZ());
                    double relativeX = x - cameraPos.x;
                    double relativeY = y - cameraPos.y;
                    double relativeZ = z - cameraPos.z;
                    PoseStack entityPoseStack = new PoseStack();
                    entityPoseStack.pushPose();
                    entityPoseStack.translate(relativeX, relativeY, relativeZ);
                    float entityYaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
                    EntityRenderer<Entity> renderer = (EntityRenderer<Entity>) entityRenderDispatcher.getRenderer(entity);
                    renderer.render(entity, entityYaw, partialTicks, entityPoseStack, bufferSource, LevelRenderer.getLightColor(minecraft.level, entity.blockPosition()));
                    entityPoseStack.popPose();
                }
            }
        }
    }

    public static GodSwordBakeModel godSwordBakeModel = new GodSwordBakeModel();


    public static boolean onRenderItem(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack mStack, MultiBufferSource buffers, int packedLight, int packedOverlay, BakedModel modelIn) {
        if (!(stack.getItem() instanceof ManaitaPlusLegacyGodSwordItem godSwordItem)) return false;
        mStack.pushPose();

        if (!ManaitaPlusLegacy.proxy.isShaderPackInUse()) {
            final GodSwordBakeModel renderer = (GodSwordBakeModel) ForgeHooksClient.handleCameraTransforms(mStack, godSwordBakeModel, context, leftHand);
            mStack.translate(-0.5D, -0.5D, -0.5D);
            renderer.renderItem(stack, context, mStack, buffers,modelIn, packedLight, packedOverlay);
        } else {
            if (context != ItemDisplayContext.GUI) {
                collectData(modelIn,stack, context, leftHand, mStack, buffers, packedLight, packedOverlay);
            } else {
                final GodSwordBakeModel renderer = (GodSwordBakeModel) ForgeHooksClient.handleCameraTransforms(mStack, godSwordBakeModel, context, leftHand);
                mStack.translate(-0.5D, -0.5D, -0.5D);
                renderer.renderItem(stack, context, mStack, buffers,modelIn, packedLight, packedOverlay);
            }
        }
        mStack.popPose();

        return true;
    }

    private static final List<RenderItemData> PENDING_RENDERS = new ArrayList<RenderItemData>();

    public static void collectData(BakedModel bakedModel,ItemStack stack, ItemDisplayContext transformType, boolean leftHand, PoseStack pStack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
        RenderStateSnapshot renderState = RenderStateSnapshot.capture();
        RenderItemData renderData = new RenderItemData(bakedModel, stack, transformType, leftHand, copyPoseStack(pStack), packedLight, packedOverlay, buffers, true, renderState, pStack, pStack.last().pose());
        PENDING_RENDERS.add(renderData);
    }

    public record RenderItemData(BakedModel model,ItemStack stack, ItemDisplayContext transformType, boolean leftHand, List<PoseEntry> poseEntries, int packedLight, int packedOverlay,  MultiBufferSource buffers, boolean is3D, RenderStateSnapshot renderState, PoseStack poseStack, Matrix4f matrix) {}


    public static void renderAllPendingItems(float pt, PoseStack worldPoseStack) {
        if (PENDING_RENDERS.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        List<RenderItemData> firstPersonItems = new ArrayList<>();
        List<RenderItemData> otherItems = new ArrayList<>();
        for (RenderItemData data : PENDING_RENDERS) {
            if (data.transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || data.transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
                firstPersonItems.add(data);
            } else {
                otherItems.add(data);
            }
        }
        if (!otherItems.isEmpty()) {
            for (RenderItemData data2 : otherItems) {
                renderSingleItem(data2, pt, mc, bufferSource, worldPoseStack);
            }
        }
        if (!firstPersonItems.isEmpty()) {
            for (RenderItemData data3 : firstPersonItems) {
                RenderSystem.clear(256, Minecraft.ON_OSX);
                renderSingleItem(data3, pt, mc, bufferSource, worldPoseStack);
            }
        }
        PENDING_RENDERS.clear();
    }

    private static void renderSingleItem(RenderItemData data, float pt, Minecraft mc, MultiBufferSource.BufferSource bufferSource, PoseStack worldPoseStack) {
        PoseStack poseStack = new PoseStack();
        try {
            data.renderState.restore();
            poseStack.pushPose();
            for (PoseEntry entry : data.poseEntries) {
                poseStack.last().pose().set(entry.poseMatrix);
                poseStack.last().normal().set(entry.normalMatrix);
            }
            poseStack.pushPose();
            final GodSwordBakeModel renderer = (GodSwordBakeModel) ForgeHooksClient.handleCameraTransforms(poseStack, godSwordBakeModel, data.transformType, data.leftHand);
            poseStack.translate(-0.5D, -0.5D, -0.5D);
            renderer.renderItem(data.stack, data.transformType, poseStack, bufferSource,data.model, data.packedLight, data.packedOverlay);
            poseStack.popPose();

            bufferSource.endBatch();
            poseStack.popPose();
            data.renderState.cleanup();
        } catch (Throwable th) {
            bufferSource.endBatch();
            poseStack.popPose();
            data.renderState.cleanup();
            throw th;
        }
    }

    private static List<PoseEntry> copyPoseStack(PoseStack stack) {
        List<PoseEntry> entries = new ArrayList<>();
        Deque<PoseStack.Pose> poseDeque = stack.poseStack;
        for (PoseStack.Pose pose : poseDeque) {
            entries.add(new PoseEntry(pose.pose(), pose.normal()));
        }
        return entries;
    }

    public static class PoseEntry {
        public final Matrix4f poseMatrix;
        public final Matrix3f normalMatrix;

        public PoseEntry(Matrix4f pose, Matrix3f normal) {
            this.poseMatrix = new Matrix4f(pose);
            this.normalMatrix = new Matrix3f(normal);
        }
    }



}
