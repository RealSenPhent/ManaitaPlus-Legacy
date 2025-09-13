package sen.manaita_plus_legacy.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sen.manaita_plus_legacy_core.util.EventUtil;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow @Final private static AABB INITIAL_AABB;

    @Inject(method = "isRemoved",at = @At("HEAD"), cancellable = true)
    public void isRemoved(CallbackInfoReturnable<Boolean> cir) {
        if (EventUtil.isManaita((Entity) (Object) this)) {
            cir.setReturnValue(false);
        } else if (EventUtil.isRemove((Entity) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getBoundingBoxForCulling",at = @At("HEAD"), cancellable = true)
    public void getBoundingBoxForCulling(CallbackInfoReturnable<AABB> cir) {
         if (EventUtil.isRemove((Entity) (Object) this)) {
            cir.setReturnValue(INITIAL_AABB);
        }
    }

    @Inject(method = "blockPosition",at = @At("HEAD"), cancellable = true)
    public void blockPosition(CallbackInfoReturnable<BlockPos> cir) {
         if (EventUtil.isRemove((Entity) (Object) this)) {
            cir.setReturnValue(BlockPos.ZERO);
        }
    }
}
