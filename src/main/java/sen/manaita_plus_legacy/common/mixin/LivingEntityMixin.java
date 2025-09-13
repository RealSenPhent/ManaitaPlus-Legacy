package sen.manaita_plus_legacy.common.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sen.manaita_plus_legacy.common.util.ManaitaPlusEntityList;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;
import sen.manaita_plus_legacy_core.util.EventUtil;

import static net.minecraft.world.entity.LivingEntity.DATA_HEALTH_ID;
import static net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED;

@Mixin(net.minecraft.world.entity.LivingEntity.class)
public class LivingEntityMixin {
    @Shadow public int hurtTime;

    @Shadow public int deathTime;

    @Inject(method = "getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D", at = @At("HEAD"), cancellable = true)
    public void getAttributeValue(net.minecraft.world.entity.ai.attributes.Attribute p_21134_, CallbackInfoReturnable<Double> cir) {
        if (EventUtil.isManaita((LivingEntity) (Object) this)) {
            double value = ((LivingEntity) (Object) this).getAttributes().getValue(p_21134_);
            if (p_21134_ == Attributes.MAX_HEALTH && value < 20.0D) {
                ((LivingEntity) (Object) this).getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
                cir.setReturnValue(20.0D);
            } else if (p_21134_ == MOVEMENT_SPEED && value < 0.1D) {
                ((LivingEntity) (Object) this).getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.15D);
                cir.setReturnValue(0.15D);
            } else if (p_21134_ == Attributes.FLYING_SPEED && value < 0.05D) {
                ((LivingEntity) (Object) this).getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.075D);
                cir.setReturnValue(0.075D);
            }
        }
    }

    @Inject(method = "getHealth", at = @At("HEAD"), cancellable = true)
    public void getHealth(CallbackInfoReturnable<Float> cir) {
        if (EventUtil.isManaita((LivingEntity) (Object) this)) {
            if ((LivingEntity) (Object) this instanceof Player player && (ManaitaPlusUtils.isManaita(player) || ManaitaPlusEntityList.manaita.accept(player))) {
                player.getEntityData().set(DATA_HEALTH_ID, player.getMaxHealth());
                cir.setReturnValue(player.getMaxHealth());
            }
            if (ManaitaPlusEntityList.death.accept((LivingEntity) (Object) this)) {
                cir.setReturnValue(0.0F);
            }
        }
    }

    @Inject(method = "getMaxHealth", at = @At("HEAD"), cancellable = true)
    public void getMaxHealth(CallbackInfoReturnable<Float> cir) {
        if (EventUtil.isManaita((LivingEntity) (Object) this)) {
            float attributeValue = (float) ((LivingEntity) (Object) this).getAttributeValue(Attributes.MAX_HEALTH);
            if (attributeValue < 20.0F) {
                ((LivingEntity) (Object) this).getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
                cir.setReturnValue(20.0F);
            }
        }
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    public void die(DamageSource p_21014_, CallbackInfo ci) {
        if (EventUtil.isManaita((LivingEntity) (Object) this)) {
            ((LivingEntity) (Object) this).hurtTime = 0;
            ((LivingEntity) (Object) this).hurtDuration = 0;
            ((LivingEntity) (Object) this).deathTime = 0;
            ci.cancel();
        }
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void hurt(DamageSource p_21016_, float p_21017_, CallbackInfoReturnable<Boolean> cir) {
        if (EventUtil.isManaita((LivingEntity) (Object) this)) {
            ((LivingEntity) (Object) this).hurtTime = 0;
            ((LivingEntity) (Object) this).hurtDuration = 0;
            ((LivingEntity) (Object) this).deathTime = 0;
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isDeadOrDying", at = @At("HEAD"), cancellable = true)
    public void isDeadOrDying(CallbackInfoReturnable<Boolean> cir) {
        if (EventUtil.isManaita((LivingEntity) (Object) this)) {
            ((LivingEntity) (Object) this).hurtTime = 0;
            ((LivingEntity) (Object) this).hurtDuration = 0;
            ((LivingEntity) (Object) this).deathTime = 0;
            cir.setReturnValue(false);
        } else if (EventUtil.isDead((LivingEntity) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isAlive", at = @At("HEAD"), cancellable = true)
    public void isAlive(CallbackInfoReturnable<Boolean> cir) {
        if (EventUtil.isManaita((LivingEntity) (Object) this)) {
            ((LivingEntity) (Object) this).hurtTime = 0;
            ((LivingEntity) (Object) this).hurtDuration = 0;
            ((LivingEntity) (Object) this).deathTime = 0;
            cir.setReturnValue(true);
        } else if (EventUtil.isDead((LivingEntity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }
}
