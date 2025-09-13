package sen.manaita_plus_legacy.common.entity;

import net.minecraft.util.Mth;
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
import net.minecraft.world.phys.Vec3;
import sen.manaita_plus_legacy.common.core.ManaitaPlusEntityCore;

import java.util.Optional;

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
        if (!entity.level().isClientSide && entity.isAlive()) {
            DamageSource source = entity.damageSources().fellOutOfWorld();
            entity.hurt(source, Float.MAX_VALUE);
            if (entity instanceof LivingEntity living) {
                living.die(source);
                living.deathTime = 20;
            }
        }
    }


    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        discard();
//        for (int i = 0; i < 500; i++) {
//            double angle = random.nextDouble() * 20.0D * Math.PI;
//            double distance = random.nextGaussian() * 100.0D;
//            Vec3 position = position();
//            AbstractArrow abstractarrow = ManaitaPlusEntityCore.ManaitaArrow.get().create(level);
//            if (abstractarrow != null) {
//                double x = Math.sin(angle) * distance + position.x;
//                double z = Math.cos(angle) * distance + position.z;
//                int y = level().getHeight(Heightmap.Types.WORLD_SURFACE, (int) x, (int) z) + 5;
//                abstractarrow.setCritArrow(true);
//                abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
//                level().addFreshEntity(abstractarrow);
//            }
//            if (bolt != null) {
//
//                bolt.setPos(new Vec3(x,y,z));
//                p_41432_.addFreshEntity(bolt);
//            }
//        }
//        Vec3 mob = player.position();
//        Vec3 mob = player.position();
//        this.getYRotD().ifPresent((p_287447_) -> {
//            this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, p_287447_, this.yMaxRotSpeed);
//        });
//        this.getXRotD().ifPresent((p_289400_) -> {
//            this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), p_289400_, this.xMaxRotAngle));
//        });
    }

    public void shootFromRotation(float p_37253_, float p_37254_, float p_37255_, float p_37256_, float p_37257_) {
        float f = -Mth.sin(p_37254_ * ((float)Math.PI / 180F)) * Mth.cos(p_37253_ * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((p_37253_ + p_37255_) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(p_37254_ * ((float)Math.PI / 180F)) * Mth.cos(p_37253_ * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, p_37256_, p_37257_);
        Vec3 vec3 = Vec3.ZERO;
        this.setDeltaMovement(this.getDeltaMovement().add(vec3.x, vec3.y, vec3.z));
    }
    private Optional<Float> getXRotD(Vec3 mob, Vec3 want) {
        double d0 = want.x - mob.x;
        double d1 = want.y - mob.y;
        double d2 = want.z - mob.z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        return !(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d3) > (double)1.0E-5F) ? Optional.empty() : Optional.of((float)(-(Mth.atan2(d1, d3) * (double)(180F / (float)Math.PI))));
    }

    private Optional<Float> getYRotD(Vec3 mob,Vec3 want) {
        double d0 = want.x - mob.x;
        double d1 = want.z - mob.z;
        return !(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d0) > (double)1.0E-5F) ? Optional.empty() : Optional.of((float)(Mth.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F);
    }

    private float rotateTowards(float p_24957_, float p_24958_, float p_24959_) {
        float f = Mth.degreesDifference(p_24957_, p_24958_);
        float f1 = Mth.clamp(f, -p_24959_, p_24959_);
        return p_24957_ + f1;
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
