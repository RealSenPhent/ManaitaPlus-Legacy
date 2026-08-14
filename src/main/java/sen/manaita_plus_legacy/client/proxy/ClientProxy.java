package sen.manaita_plus_legacy.client.proxy;

import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import sen.manaita_plus_legacy.common.proxy.CommomProxy;
import sen.manaita_plus_legacy_core.transform.ManaitaPlusLegacyLaunchPluginService;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommomProxy {
    public static final boolean iris = ModList.get().isLoaded("oculus");
    private static final Supplier<Boolean> isShaderPackInUse = () -> IrisApi.getInstance().isShaderPackInUse();

    static {
        ManaitaPlusLegacyLaunchPluginService.renderLevelRenderer |= !iris;
    }
    @Override
    public boolean isShaderPackInUse() {
        if (iris) {
            return isShaderPackInUse.get();
        }
        return false;
    }
}
