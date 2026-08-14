package sen.manaita_plus_legacy_core.util;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexSorting;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

/* loaded from: ArcaneVortex [Alpha]-0.6.8-1.20.1-Forge-deMcpObf.jar:com/erchien/arcanevortex/Content/Render/RenderStateSnapshot.class */
public class RenderStateSnapshot {
    private final boolean depthTest;
    private final boolean depthMask;
    private final int depthFunc;
    private final boolean blend;
    private final int blendSrcRgb;
    private final int blendDstRgb;
    private final int blendSrcAlpha;
    private final int blendDstAlpha;
    private final boolean cull;
    private final int cullFace;
    private final boolean colorMask;
    private final float[] colorMaskValues;
    private final int polygonMode;
    private final Matrix4f modelViewMatrix;
    private final Matrix4f projectionMatrix;
    private final VertexSorting vertexSorting;

    private RenderStateSnapshot(boolean depthTest, boolean depthMask, int depthFunc, boolean blend, int blendSrcRgb, int blendDstRgb, int blendSrcAlpha, int blendDstAlpha, boolean cull, int cullFace, boolean colorMask, float[] colorMaskValues, int polygonMode, Matrix4f modelViewMatrix, Matrix4f projectionMatrix, VertexSorting vertexSorting) {
        this.depthTest = depthTest;
        this.depthMask = depthMask;
        this.depthFunc = depthFunc;
        this.blend = blend;
        this.blendSrcRgb = blendSrcRgb;
        this.blendDstRgb = blendDstRgb;
        this.blendSrcAlpha = blendSrcAlpha;
        this.blendDstAlpha = blendDstAlpha;
        this.cull = cull;
        this.cullFace = cullFace;
        this.colorMask = colorMask;
        this.colorMaskValues = colorMaskValues;
        this.polygonMode = polygonMode;
        this.modelViewMatrix = new Matrix4f(modelViewMatrix);
        this.projectionMatrix = new Matrix4f(projectionMatrix);
        this.vertexSorting = vertexSorting;
    }

    public static RenderStateSnapshot capture() {
        boolean depthTest = GL11.glIsEnabled(2929);
        boolean depthMask = GL11.glGetBoolean(2930);
        int depthFunc = GL11.glGetInteger(2932);
        boolean blend = GL11.glIsEnabled(3042);
        int blendSrcRgb = GL11.glGetInteger(32969);
        int blendDstRgb = GL11.glGetInteger(32968);
        int blendSrcAlpha = GL11.glGetInteger(32971);
        int blendDstAlpha = GL11.glGetInteger(32970);
        boolean cull = GL11.glIsEnabled(2884);
        int cullFace = GL11.glGetInteger(2885);
        boolean colorMask = GL11.glGetBoolean(3107);
        float[] colorMaskValues = new float[4];
        GL11.glGetFloatv(3107, colorMaskValues);
        int polygonMode = GL11.glGetInteger(2880);
        Matrix4f modelViewMatrix = new Matrix4f(RenderSystem.getModelViewMatrix());
        Matrix4f projectionMatrix = new Matrix4f(RenderSystem.getProjectionMatrix());
        VertexSorting vertexSorting = RenderSystem.getVertexSorting();
        return new RenderStateSnapshot(depthTest, depthMask, depthFunc, blend, blendSrcRgb, blendDstRgb, blendSrcAlpha, blendDstAlpha, cull, cullFace, colorMask, colorMaskValues, polygonMode, modelViewMatrix, projectionMatrix, vertexSorting);
    }

    public void restore() {
        if (this.depthTest) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }
        RenderSystem.depthMask(this.depthMask);
        RenderSystem.depthFunc(this.depthFunc);
        if (this.blend) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(this.blendSrcRgb, this.blendDstRgb, this.blendSrcAlpha, this.blendDstAlpha);
        } else {
            RenderSystem.disableBlend();
        }
        if (this.cull) {
            RenderSystem.enableCull();
        } else {
            RenderSystem.disableCull();
        }
        RenderSystem.colorMask(this.colorMaskValues[0] > 0.5f, this.colorMaskValues[1] > 0.5f, this.colorMaskValues[2] > 0.5f, this.colorMaskValues[3] > 0.5f);
        RenderSystem.polygonMode(1032, this.polygonMode);
        RenderSystem.getModelViewStack().pushPose();
        RenderSystem.getModelViewStack().setIdentity();
        RenderSystem.getModelViewStack().mulPoseMatrix(this.modelViewMatrix);
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setProjectionMatrix(this.projectionMatrix, this.vertexSorting);
    }

    public void cleanup() {
        RenderSystem.getModelViewStack().popPose();
        RenderSystem.applyModelViewMatrix();
    }
}