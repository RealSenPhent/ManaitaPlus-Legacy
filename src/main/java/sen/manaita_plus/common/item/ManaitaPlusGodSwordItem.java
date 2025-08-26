package sen.manaita_plus.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus.common.entity.ManaitaPlusLightningBolt;
import sen.manaita_plus.common.item.data.IManaitaPlusKey;
import sen.manaita_plus.common.util.ManaitaPlusText;
import sen.manaita_plus.common.util.ManaitaPlusUtils;

import java.util.List;
import java.util.Random;

import static sen.manaita_plus.common.core.ManaitaPlusEntityCore.ManaitaLightningBolt;

public class ManaitaPlusGodSwordItem extends SwordItem implements IManaitaPlusKey {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public ManaitaPlusGodSwordItem() {
        super(new ManaitaPlusGodItemTier(), 0, 0, new Item.Properties().fireResistant());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", Double.POSITIVE_INFINITY, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", Double.POSITIVE_INFINITY, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
    }


    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        if (p_41406_ instanceof  Player player) {
            player.getAbilities().mayfly = true;
        }
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && player.isShiftKeyDown()) {
            ManaitaPlusUtils.godKill(player,isRemove(stack));
        }
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        ManaitaPlusUtils.kill(entity, player.isShiftKeyDown(),isRemove(stack));
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level p_41432_, Player player, InteractionHand p_41434_) {
        ItemStack itemstack = player.getItemInHand(p_41434_);
        player.startUsingItem(p_41434_);
        if (!p_41432_.isClientSide) {
            Random random = new Random();
            Vec3 position = player.position();
            for (int i = 0; i < 100; i++) {
                ManaitaPlusLightningBolt bolt = ManaitaLightningBolt.get().create(p_41432_);
                if (bolt != null) {
                    double angle = random.nextDouble() * 20.0D * Math.PI;
                    double distance = random.nextGaussian() * 100.0D + 3;
                    double x = Math.sin(angle) * distance + position.x;
                    double z = Math.cos(angle) * distance + position.z;

                    int y = p_41432_.getHeight(Heightmap.Types.WORLD_SURFACE_WG, (int) x, (int) z);
                    bolt.setPos(x, y, z);
                    p_41432_.addFreshEntity(bolt);
                }
            }
        }
        ManaitaPlusUtils.godKill(player,isRemove(itemstack));
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_43274_) {
        return p_43274_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43274_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.doubling") + ":" + (getDoubling(p_41421_) ? I18n.get("info.on") : I18n.get("info.off")))));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.remove.name") + ":" + (isRemove(p_41421_) ? I18n.get("info.on") : I18n.get("info.off")))));
        p_41423_.add(Component.empty());
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_enchantment.formatting(I18n.get("info.item.manaita_sword_god.1"))));
    }


    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.literal(ManaitaPlusText.manaita_infinity.formatting(I18n.get("item.manaita_sword_god.name")));
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.BLOCK;
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return true;
    }

    public void onManaitaKeyPress(ItemStack itemStack, Player player) {
        if (player.isShiftKeyDown()) {
            boolean remove = !isRemove(itemStack);
            setRemove(itemStack, remove);
            ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(String.format("[%s] %s: %s", I18n.get("item.manaita_sword_god.name"), I18n.get("mode.remove.name"), (remove ? I18n.get("info.on") : I18n.get("info.off"))))));
        } else {
            boolean doubling = !getDoubling(itemStack);
            setDoubling(itemStack, doubling);
            ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(String.format("[%s] %s: %s", I18n.get("item.manaita_sword_god.name"), I18n.get("mode.doubling"), (doubling ? I18n.get("info.on") : I18n.get("info.off"))))));
        }
    }

    public static boolean getDoubling(ItemStack itemStack) {
        if (itemStack.getTag() == null)
            itemStack.setTag(new CompoundTag());
        return itemStack.getTag().getBoolean("Doubling");
    }

    public static boolean isRemove(ItemStack itemStack) {
        if (itemStack.getTag() == null)
            itemStack.setTag(new CompoundTag());
        return itemStack.getTag().getBoolean("Remove");
    }

    public static void setRemove(ItemStack itemStack,boolean remove) {
        if (itemStack.getTag() == null)
            itemStack.setTag(new CompoundTag());
        itemStack.getTag().putBoolean("Remove", remove);
    }

    public static void setDoubling(ItemStack itemStack, boolean doubling) {
        if (itemStack.getTag() == null)
            itemStack.setTag(new CompoundTag());
        itemStack.getTag().putBoolean("Doubling", doubling);
    }


    public static final class ManaitaPlusGodItemTier implements Tier {
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
