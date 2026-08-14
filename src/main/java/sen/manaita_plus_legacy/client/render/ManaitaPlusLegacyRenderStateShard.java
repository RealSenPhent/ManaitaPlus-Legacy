package sen.manaita_plus_legacy.client.render;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import sen.manaita_plus_legacy.client.core.ManaitaPlusLegacyShaderCore;
import sen.manaita_plus_legacy.client.shander.impl.cosmic.CosmicShaderEventHandler;
import sen.manaita_plus_legacy.client.shander.impl.galaxy.GalaxyShaderHandle;
import sen.manaita_plus_legacy.client.shander.impl.item.ItemShaderHandle;

import java.util.function.Function;

public class ManaitaPlusLegacyRenderStateShard extends RenderStateShard {

    public ManaitaPlusLegacyRenderStateShard(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
        super(p_110161_, p_110162_, p_110163_);
    }

    public static RenderType entityCutout(ResourceLocation p_110453_) {
        return ENTITY_CUTOUT.apply(p_110453_);
    }

    private static final Function<ResourceLocation, RenderType> ENTITY_CUTOUT = Util.memoize((p_286173_) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_CUTOUT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(p_286173_, false, false)).setTransparencyState(NO_TRANSPARENCY).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(true);
        return RenderType.create("entity_cutout", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
    });

    public static RenderType createCosmicRenderType(float scale) {
        return RenderType.create("cosmic_render_type_entity",
                DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS,
                2097149, true, false,
                RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> {
                            if (ManaitaPlusLegacyShaderCore.cosmicShader != null) {
                                Minecraft mc = Minecraft.getInstance();
                                float yaw = 0.0f;
                                float pitch = 0.0f;
                                if (mc.player != null) {
                                    yaw = (float) (((mc.player.getYRot() * 2.0f) * 3.141592653589793d) / 360.0d);
                                    pitch = -((float) (((mc.player.getXRot() * 2.0f) * 3.141592653589793d) / 360.0d));
                                }
                                CosmicShaderEventHandler.cosmicYaw.set(yaw);
                                CosmicShaderEventHandler.cosmicPitch.set(pitch);
                                CosmicShaderEventHandler.cosmicExternalScale.set(scale);
                            }
                            return ManaitaPlusLegacyShaderCore.cosmicShader;
                        })).
                        setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false)).
                        setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY).
                        setWriteMaskState(COLOR_DEPTH_WRITE).
                        setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST).
                        setLightmapState(RenderStateShard.LIGHTMAP).
                        setCullState(RenderStateShard.NO_CULL).
                        createCompositeState(true));
    }

    public static RenderType createItemRenderType() {
        return RenderType.create("item_manaita_sword",
                DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS,
                2097149, true, false,
                RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> {
                            if (ManaitaPlusLegacyShaderCore.itemShader != null) {
                                Minecraft mc = Minecraft.getInstance();
                                float yaw = 0.0f;
                                float pitch = 0.0f;
                                if (mc.player != null) {
                                    yaw = (float) (((mc.player.getYRot() * 2.0f) * 3.141592653589793d) / 360.0d);
                                    pitch = -((float) (((mc.player.getXRot() * 2.0f) * 3.141592653589793d) / 360.0d));
                                }
                                ItemShaderHandle.yaw.set(yaw);
                                ItemShaderHandle.pitch.set(pitch);
                            }
                            return ManaitaPlusLegacyShaderCore.itemShader;
                        })).
                        setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED).
                        setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).
                        setWriteMaskState(COLOR_DEPTH_WRITE).
                        setCullState(RenderStateShard.NO_CULL).
                        setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST).
                        setLightmapState(RenderStateShard.LIGHTMAP).
                        createCompositeState(true));
    }

    public static final float[][] colors = new float[][] {
            {0.98f, 0.70f, 0.25f},
            {0.95f, 0.30f, 0.60f},
            {0.45f, 0.60f, 0.95f},
            {0.05f, 0.25f, 0.70f},
            {0.00f, 0.70f, 0.75f},
            {0.20f, 0.85f, 0.55f},
            {0.10f, 0.80f, 0.70f},
            {0.25f, 0.35f, 0.90f},
            {0.70f, 0.25f, 0.80f}
    };

    public static RenderType createGalaxyItemRenderType(int color) {
        return RenderType.create("item_manaita_sword_galaxy",
                DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS,
                2097149, true, false,
                RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> {
                            if (ManaitaPlusLegacyShaderCore.galaxyShader != null) {
                                GalaxyShaderHandle.screenSize.set(getScreenSize());
                                int i = color * 3;
                                GalaxyShaderHandle.color1.set(colors[i]);
                                GalaxyShaderHandle.color2.set(colors[i + 1]);
                                GalaxyShaderHandle.color3.set(colors[i + 2]);
                            }
                            return ManaitaPlusLegacyShaderCore.galaxyShader;
                        })).
                        setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED).
                        setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).
                        setWriteMaskState(RenderStateShard.COLOR_WRITE).
                        setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST).
                        setLightmapState(RenderStateShard.LIGHTMAP).
                        setCullState(RenderStateShard.NO_CULL).
                        createCompositeState(false));
    }



    public static float[] color(double r, double g, double b) {
        return new float[] { (float) r, (float) g, (float) b };
    }


    public static float[] getScreenSize() {
        GLFWVidMode vidMode;
        Minecraft mc = Minecraft.getInstance();
        try {
            Window wh = mc.getWindow();
            int width = wh.getWidth();
            int height = wh.getHeight();
            if (width > 0 && height > 0) {
                return new float[] {width, height};
            }
        } catch (Exception e) {
            System.err.println("Failed to get screen size from Minecraft window: " + e.getMessage());
        }
        try {
            long monitor = GLFW.glfwGetPrimaryMonitor();
            if (monitor != 0 && (vidMode = GLFW.glfwGetVideoMode(monitor)) != null && vidMode.width() > 0 && vidMode.height() > 0) {
                return new float[] {vidMode.width(), vidMode.height()};
            }
        } catch (Exception e2) {
            System.err.println("Failed to get screen size from monitor: " + e2.getMessage());
        }
        System.err.println("All methods to get screen size failed, using default: 1920x1080");
        return new float[]{1920.0f, 1080.0f};
    }

}
