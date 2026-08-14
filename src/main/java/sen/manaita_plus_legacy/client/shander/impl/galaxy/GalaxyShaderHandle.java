package sen.manaita_plus_legacy.client.shander.impl.galaxy;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraftforge.client.event.RegisterShadersEvent;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.client.core.ManaitaPlusLegacyShaderCore;
import sen.manaita_plus_legacy.client.event.EventHandler;
import sen.manaita_plus_legacy.client.shander.comis.CCShaderInstance;
import sen.manaita_plus_legacy.client.shander.comis.CCUniform;

import java.util.Objects;

public class GalaxyShaderHandle {
    public static CCUniform time;
    public static CCUniform screenSize;
    public static CCUniform color1;
    public static CCUniform color2;
    public static CCUniform color3;

    public static void onRegisterShaders(RegisterShadersEvent event) {
        event.registerShader(CCShaderInstance.create(event.getResourceProvider(),
                        ManaitaPlusLegacy.rl("galaxy"), DefaultVertexFormat.BLOCK),
                e -> {
                    ManaitaPlusLegacyShaderCore.galaxyShader = (CCShaderInstance) e;
                    time = Objects.requireNonNull(ManaitaPlusLegacyShaderCore.galaxyShader.getUniform("time"));
                    screenSize = Objects.requireNonNull(ManaitaPlusLegacyShaderCore.galaxyShader.getUniform("screenSize"));
                    color1 = Objects.requireNonNull(ManaitaPlusLegacyShaderCore.galaxyShader.getUniform("color1"));
                    color2 = Objects.requireNonNull(ManaitaPlusLegacyShaderCore.galaxyShader.getUniform("color2"));
                    color3 = Objects.requireNonNull(ManaitaPlusLegacyShaderCore.galaxyShader.getUniform("color3"));

                    ManaitaPlusLegacyShaderCore.galaxyShader.onApply(() -> time.set(EventHandler.renderTime + EventHandler.renderFrame));
                });
    }
}
