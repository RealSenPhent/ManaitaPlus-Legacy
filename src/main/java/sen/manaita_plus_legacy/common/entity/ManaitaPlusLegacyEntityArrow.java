package sen.manaita_plus_legacy.common.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.entity.PartEntity;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyEntityCore;
import sen.manaita_plus_legacy.common.util.ManaitaPlusLegacyEntityData;

public class ManaitaPlusLegacyEntityArrow extends AbstractArrow {
    public ManaitaPlusLegacyEntityArrow(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
    }

    private ManaitaPlusLegacyEntityArrow(Level p_36866_, LivingEntity p_36867_) {
        super(ManaitaPlusLegacyEntityCore.ManaitaArrow.get(), p_36867_, p_36866_);
    }

    public static ManaitaPlusLegacyEntityArrow create(Level p_36866_, LivingEntity p_36867_) {
        return new ManaitaPlusLegacyEntityArrow(p_36866_,p_36867_);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {
        Entity entity = p_36757_.getEntity();
        super.onHitEntity(p_36757_);
        if (!entity.level().isClientSide) {
            while (entity instanceof PartEntity<?> part) entity = part.getParent();

            if (entity == null) return;
            Entity owner = this.getOwner();
            if (owner instanceof Player living) {
                DamageSource source = entity.damageSources().playerAttack(living);
                entity.hurt(source, 100000);
            } else if (owner instanceof LivingEntity living) {
                DamageSource source = entity.damageSources().mobAttack(living);
                entity.hurt(source, 100000);
            }
            ManaitaPlusLegacyEntityData.death.add(entity);
        }
    }


    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        discard();
    }


    @Override
    protected boolean canHitEntity(Entity p_36743_) {
        return super.canHitEntity(p_36743_);
    }

    @Override
    public double getBaseDamage() {
        return Double.MAX_VALUE;
    }

    @Override
    public void playerTouch(Player p_36766_) {

    }
}
