package sen.manaita_plus_legacy.client.shander.impl.cosmic;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraftforge.client.event.RegisterShadersEvent;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.client.core.ManaitaPlusLegacyShaderCore;
import sen.manaita_plus_legacy.client.event.EventHandler;
import sen.manaita_plus_legacy.client.shander.comis.CCShaderInstance;
import sen.manaita_plus_legacy.client.shander.comis.CCUniform;

import java.util.Objects;

public class CosmicShaderEventHandler extends RenderStateShard {
    public static CCUniform cosmicTime;
    public static CCUniform cosmicYaw;
    public static CCUniform cosmicPitch;
    public static CCUniform cosmicExternalScale;

    public CosmicShaderEventHandler(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
        super(p_110161_, p_110162_, p_110163_);
    }

    
    public static void onRegisterShaders(RegisterShadersEvent event) {
        event.registerShader(CCShaderInstance.create(event.getResourceProvider(),
                        ManaitaPlusLegacy.rl("cosmic_neo_no_mask"), DefaultVertexFormat.BLOCK),
                e -> {
                    ManaitaPlusLegacyShaderCore.cosmicShader = (CCShaderInstance) e;
                    cosmicTime = Objects.requireNonNull(ManaitaPlusLegacyShaderCore.cosmicShader.getUniform("time"));
                    cosmicYaw = Objects.requireNonNull(ManaitaPlusLegacyShaderCore.cosmicShader.getUniform("yaw"));
                    cosmicPitch = Objects.requireNonNull(ManaitaPlusLegacyShaderCore.cosmicShader.getUniform("pitch"));
                    cosmicExternalScale = Objects.requireNonNull(ManaitaPlusLegacyShaderCore.cosmicShader.getUniform("externalScale"));
                    cosmicTime.set(EventHandler.renderTime + EventHandler.renderFrame);
                    ManaitaPlusLegacyShaderCore.cosmicShader.onApply(() -> cosmicTime.set(EventHandler.renderTime + EventHandler.renderFrame));
                });
    }


}
