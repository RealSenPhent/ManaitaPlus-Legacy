package sen.manaita_plus_legacy.common.proxy;

import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraftforge.fml.ModList;

import java.util.function.Supplier;

public class CommomProxy {
    private static final Supplier<Boolean> isShaderPackInUse = () -> IrisApi.getInstance().isShaderPackInUse();
    public static final boolean iris = ModList.get().isLoaded("oculus");

    public static final boolean curios = ModList.get().isLoaded("curios");
    public static final boolean projecte = ModList.get().isLoaded("projecte");
    public boolean isShaderPackInUse() {
        if (iris) {
            return isShaderPackInUse.get();
        }
        return false;
    }
}
