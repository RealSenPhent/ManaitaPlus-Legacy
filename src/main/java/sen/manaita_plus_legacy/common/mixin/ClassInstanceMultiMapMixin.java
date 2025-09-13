package sen.manaita_plus_legacy.common.mixin;

import net.minecraft.util.ClassInstanceMultiMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sen.manaita_plus_legacy_core.util.EventUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(ClassInstanceMultiMap.class)
public class ClassInstanceMultiMapMixin {
    @Shadow @Final private List<Object> allInstances;

    @Shadow @Final public Map<Class<?>, List> byClass;

    @Inject(method = "iterator", at = @At("HEAD"))
    public void onIterator(CallbackInfoReturnable cir) {
        EventUtil.onIterator(allInstances);
    }

    @Inject(method = "find", at = @At("HEAD"))
    public void find(Class p_13534_, CallbackInfoReturnable<Collection> cir) {
        EventUtil.onFind(byClass);
    }
}
