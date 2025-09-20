package sen.manaita_plus_legacy.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusKey;
import sen.manaita_plus_legacy.common.util.ManaitaPlusText;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.List;

public class ManaitaPlusSwordItem extends SwordItem implements IManaitaPlusKey {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public ManaitaPlusSwordItem() {
        super(new ItemManaitaSwordTier(), 0, 0, new Item.Properties().fireResistant());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", Double.POSITIVE_INFINITY, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", Double.POSITIVE_INFINITY, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        p_41404_.setPopTime(0);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof  Player player) {
            int sweep = getSweep(stack);
            for (int i1 = 0; i1 < sweep; i1++) {
                Vec3 vec3 = player.getLookAngle();
                AABB aabb = player.getBoundingBox().expandTowards(3.0D, 3.0D, 3.0D).move(vec3.x * i1, vec3.y * i1, vec3.z * i1);
                for (Entity entity1 : player.level().getEntities(player, aabb, (p_20434_) -> true)) {
                    if (entity1 instanceof  LivingEntity living) {
                        if (!player.level().isClientSide) {
                            living.hurt(living.damageSources().playerAttack(player), (float) Double.MAX_VALUE);
                            living.die(living.damageSources().playerAttack(player));
                            living.setHealth(Float.NaN);
                            living.deathTime = 15;
                        }
                        for (int i = 0; i < 5; i++) {
                            living.handleEntityEvent((byte) 2);
                        }
                        for (int i = 47; i < 53; i++) {
                            living.handleEntityEvent((byte) i);
                        }
                        living.handleEntityEvent((byte) 3);
//                        for (int i = 0; i < 5; ++i)
//                        living.playHurtSound(entity.level().damageSources().genericKill());
//                        living.getDeathSound()
                    }
                }
                double d0 = (-Mth.sin(player.getYRot() * ((float)Math.PI / 180F)));
                double d1 = Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + d0 + vec3.x * i1, player.getY(0.5D) + vec3.y * i1, player.getZ() + d1+ vec3.z * i1, 0, d0, 0.0D, d1, 0.0D);
                }
                player.level().playSound( null, player.getX() + d0 + vec3.x * i1, player.getY(0.5D) + vec3.y * i1, player.getZ() + d1+ vec3.z * i1,  SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
            }

        }
        return false;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_43274_) {
        return p_43274_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43274_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.manaita_sword") + ":"  + getSweep(p_41421_))));
        p_41423_.add(Component.empty());
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_infinity.formatting(I18n.get("info.attack"))));
    }


    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.literal(ManaitaPlusText.manaita_infinity.formatting(I18n.get("item.manaita_sword.name")));
    }

    public void onManaitaKeyPress(ItemStack itemStack, Player player) {
        int sweep = itemStack.getOrCreateTag().getInt("Sweep");
        if (sweep == 0) sweep = 1;
        sweep = (sweep * 4) % 2048;
        setSweep(itemStack,sweep);
  }

    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
        int sweep = itemStack.getOrCreateTag().getInt("Sweep");
        if (sweep == 0) sweep = 1;
        sweep = (sweep * 4) % 2048;
        setSweep(itemStack,sweep);
        ManaitaPlusUtils.chat(Component.literal(String.format("[%s%s] %s%s: %s",ManaitaPlusText.manaita_mode.formatting(I18n.get("item.manaita_sword.name")), ChatFormatting.RESET, ChatFormatting.RESET,I18n.get("mode.manaita_sword"), sweep)));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        entity.hurt(entity.damageSources().playerAttack(player), 10000);
        if (entity instanceof LivingEntity living) {
            living.die(living.damageSources().playerAttack(player));
            living.setHealth(0F);
            living.deathTime = 15;
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    public static int getSweep(ItemStack itemStack) {
        if (itemStack.getTag() == null)
            itemStack.setTag(new CompoundTag());
        int sweep = itemStack.getTag().getInt("Sweep");
        return sweep;
    }

    public static void setSweep(ItemStack itemStack,int sweep) {
        itemStack.getTag().putInt("Sweep", sweep);
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {}
    public static final class ItemManaitaSwordTier implements Tier {
        @Override
        public int getUses() {
            return -1;
        }

        @Override
        public float getSpeed() {
            return Float.MAX_VALUE;
        }

        @Override
        public float getAttackDamageBonus() {
            return Float.MAX_VALUE;
        }

        @Override
        public int getLevel() {
            return Integer.MAX_VALUE;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(Items.NETHERITE_INGOT);
        }

        @Override
        public @Nullable TagKey<Block> getTag() {
            return BlockTags.create(new ResourceLocation("forge", "needs_manaita_tool"));
        }
    }
}
