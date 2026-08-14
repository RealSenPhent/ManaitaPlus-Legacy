package sen.manaita_plus_legacy.client.shander;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.client.event.RegisterShadersEvent;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;

import java.io.IOException;

public class LightShader extends ShaderInstance {
    public LightShader(RegisterShadersEvent event) throws IOException {
        super(event.getResourceProvider(), ManaitaPlusLegacy.rl("rendertype_light"), DefaultVertexFormat.POSITION_COLOR_NORMAL);
    }
}
