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
import sen.manaita_plus_legacy.common.core.ManaitaPlusEntityCore;
import sen.manaita_plus_legacy.common.util.ManaitaPlusEntityList;

public class ManaitaPlusEntityArrow extends AbstractArrow {
    public ManaitaPlusEntityArrow(EntityType<? extends AbstractArrow> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
    }

    private ManaitaPlusEntityArrow(Level p_36866_, LivingEntity p_36867_) {
        super(ManaitaPlusEntityCore.ManaitaArrow.get(), p_36867_, p_36866_);
    }
    @Override
    public void tick() {
        super.tick();
    }

    public static ManaitaPlusEntityArrow craft(Level p_36866_, LivingEntity p_36867_) {
        return new ManaitaPlusEntityArrow(p_36866_,p_36867_);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {
        Entity entity = p_36757_.getEntity();
        super.onHitEntity(p_36757_);
        System.err.println(entity.getClass().getName());
        if (!entity.level().isClientSide) {
            while (entity instanceof PartEntity<?> part) entity = part.getParent();

            if (entity == null) return;
            DamageSource source = entity.damageSources().fellOutOfWorld();
            entity.hurt(source, 10000);
            ManaitaPlusEntityList.death.add(entity);

            if (entity instanceof LivingEntity living) {
                living.die(source);
                living.setHealth(0.0F);
            }
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
