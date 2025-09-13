package sen.manaita_plus_legacy.common.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sen.manaita_plus_legacy_core.util.EventUtil;

import java.util.function.Consumer;

@Mixin(EntityTickList.class)
public class EntityTickListMixin {
    @Shadow public Int2ObjectMap<Entity> active;

    @Inject(method = "forEach",at = @At("HEAD"), cancellable = true)
    public void forEach(Consumer<Entity> p_156911_, CallbackInfo ci ) {
        active.values().removeIf(EventUtil::isRemove);
    }
}
