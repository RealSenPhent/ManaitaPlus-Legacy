package sen.manaita_plus_legacy.common.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityLookup;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sen.manaita_plus_legacy_core.util.EventUtil;

@Mixin(value = EntityLookup.class,priority = Integer.MAX_VALUE)
public class EntityLookupMixin {
    @Shadow @Final public Int2ObjectMap byId;

    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    public void add(EntityAccess p_156815_, CallbackInfo ci) {
        if (p_156815_ instanceof Entity entity) {
            if (EventUtil.isRemove(entity)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "getEntities", at = @At("HEAD"), cancellable = true)
    public void getEntities(EntityTypeTest p_261575_, AbortableIterationConsumer p_261925_, CallbackInfo ci) {
        byId.values().removeIf(o -> o instanceof Entity entity && EventUtil.isRemove(entity));
    }


}
