package sen.manaita_plus_legacy.client.shander.impl.item;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraftforge.client.event.RegisterShadersEvent;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.client.core.ManaitaPlusLegacyShaderCore;
import sen.manaita_plus_legacy.client.event.EventHandler;
import sen.manaita_plus_legacy.client.shander.comis.CCShaderInstance;
import sen.manaita_plus_legacy.client.shander.comis.CCUniform;

import java.util.Objects;

public class ItemShaderHandle {
    public static CCUniform time;
    public static CCUniform yaw;
    public static CCUniform pitch;
    
    public static void onRegisterShaders(RegisterShadersEvent event) {
        event.registerShader(CCShaderInstance.create(event.getResourceProvider(),
                ManaitaPlusLegacy.rl("item"), DefaultVertexFormat.BLOCK),
                e -> {
            ManaitaPlusLegacyShaderCore.itemShader = (CCShaderInstance) e;
            time = Objects.requireNonNull(ManaitaPlusLegacyShaderCore.itemShader.getUniform("time1"));
            yaw = Objects.requireNonNull(ManaitaPlusLegacyShaderCore.itemShader.getUniform("yaw"));
            pitch = Objects.requireNonNull(ManaitaPlusLegacyShaderCore.itemShader.getUniform("pitch"));
            ManaitaPlusLegacyShaderCore.itemShader.onApply(() -> time.set((EventHandler.renderTime + EventHandler.renderFrame) / 10));
        });
    }
}
