package sen.manaita_plus_legacy.client.shander;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.client.event.RegisterShadersEvent;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.client.shander.comis.CCShaderInstance;

import java.io.IOException;

public class GalaxyShader extends ShaderInstance {
    public GalaxyShader(ResourceProvider p_173336_, ResourceLocation shaderLocation, VertexFormat p_173338_) throws IOException {
        super(p_173336_, shaderLocation, p_173338_);
    }

    public static GalaxyShader create(ResourceProvider resourceProvider, ResourceLocation loc, VertexFormat format) {
        try {
            return new GalaxyShader(resourceProvider, loc, format);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to initialize shader.", ex);
        }
    }

}
