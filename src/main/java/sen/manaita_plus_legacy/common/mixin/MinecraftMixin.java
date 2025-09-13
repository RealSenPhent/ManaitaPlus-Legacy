package sen.manaita_plus_legacy.common.mixin;

import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sen.manaita_plus_legacy_core.util.EventUtil;

@Mixin(value = net.minecraft.client.Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "setScreen", at = @At("HEAD"),cancellable = true)
    public void setScreen(Screen p_91153_, CallbackInfo ci) {
        if (EventUtil.isNotSafe(p_91153_)) {
            ci.cancel();
        }
    }
}
