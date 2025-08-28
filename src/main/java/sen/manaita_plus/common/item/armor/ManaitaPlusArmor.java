package sen.manaita_plus.common.item.armor;

import com.google.common.collect.Lists;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus.common.item.data.IManaitaPlusKey;
import sen.manaita_plus.common.util.ManaitaPlusText;
import sen.manaita_plus.common.util.ManaitaPlusUtils;

import java.util.List;

public class ManaitaPlusArmor extends ArmorItem {
    public ManaitaPlusArmor(Type p_266831_) {
        super(new ManaitaPlusArmorMaterial(), p_266831_, new Item.Properties().fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_infinity.formatting(I18n.get("info.armor"))));
    }

    static class ManaitaPlusArmorMaterial implements ArmorMaterial {

        @Override
        public int getDurabilityForType(Type p_266807_) {
            return -1;
        }

        @Override
        public int getDefenseForType(Type p_267168_) {
            return 0;
        }

        @Override
        public int getEnchantmentValue() {
            return 0;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_TURTLE;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return null;
        }

        @Override
        public String getName() {
            return "manaita_armor";
        }

        @Override
        public float getToughness() {
            return 0;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    }

    public static class Helmet extends ManaitaPlusArmor implements IManaitaPlusKey {
        public Helmet() {
            super(ArmorItem.Type.HELMET);
        }


//        @Override
//        public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
//            super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
//            p_41423_.add(Component.literal(ManaitaPlusText.manaita_infinity.formatting("")));
//        }


        @Override
        public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
            super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
            p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.nightvision") + ": " +  (getNightVision(p_41421_) ? I18n.get("info.on") : I18n.get("info.off") ))));
        }

        @Override
        public Component getName(ItemStack p_41458_) {
            return Component.literal(I18n.get("item.helmet.name"));
        }

        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "manaita_plus:textures/models/armor/manaita_armor_layer_1.png";
        }

        @Override
        public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
            if (p_41407_ == 3 && p_41406_ instanceof Player player) {
                player.setAirSupply(300);
                FoodData foodData = player.getFoodData();
                if (foodData.getFoodLevel() < 20) {
                    foodData.setFoodLevel(20);
                }
                if (foodData.getSaturationLevel() < 20) {
                    foodData.setSaturation(20);
                }
                foodData.setExhaustion(0);
                if (getNightVision(p_41404_)) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 0, false, false));
                }
            }
        }

        public static boolean getNightVision(ItemStack itemStack) {
            return itemStack.getOrCreateTag().getBoolean("NightVision");
        }

        public static void toggleNightVision(ItemStack itemStack) {
            boolean nightVision = !itemStack.getOrCreateTag().getBoolean("NightVision");
            itemStack.getTag().putBoolean("NightVision", nightVision);
            ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(String.format("[%s] %s: %b",  I18n.get("item.helmet.name"), I18n.get("mode.nightvision"), nightVision))));
        }

        @Override
        public void onManaitaKeyPress(ItemStack paramItemStack, Player paramEntityPlayer) {
            if (paramEntityPlayer.isShiftKeyDown()) toggleNightVision(paramItemStack);
        }
    }
    
    public static class Chestplate extends ManaitaPlusArmor {
        public Chestplate() {
            super(ArmorItem.Type.CHESTPLATE);
        }


        @Override
        public Component getName(ItemStack p_41458_) {
            return Component.literal(I18n.get("item.chestplate.name"));
        }


        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "manaita_plus:textures/models/armor/manaita_armor_layer_1.png";
        }

        @Override
        public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
            if (p_41407_ == 2 && p_41406_ instanceof Player player) {
                List<MobEffect> badEffects = Lists.newArrayList();
                for (MobEffectInstance effect : player.getActiveEffects()) {
                    if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                        badEffects.add(effect.getEffect());
                    }
                }
                for (MobEffect effect : badEffects) {
                    player.removeEffect(effect);
                }
            }
        }
    }

    public static class Leggings extends ManaitaPlusArmor implements IManaitaPlusKey {
        public Leggings() {
            super(ArmorItem.Type.LEGGINGS);
        }

        @Override
        public Component getName(ItemStack p_41458_) {
            return Component.literal(I18n.get("item.leggings.name"));
        }


        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "manaita_plus:textures/models/armor/manaita_armor_layer_2.png";
        }

        @Override
        public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
            if (p_41407_ == 1 && p_41406_ instanceof Player player) {
                player.setRemainingFireTicks(0);
                if (getInvisibility(p_41404_)) {
                    player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 400, 0, false, false));
                    player.setInvisible(true);
                } else {
                    player.setInvisible(false);
                }
            }
        }

        @Override
        public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
            super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
            p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.invisibility") + ": " + (getInvisibility(p_41421_) ? I18n.get("info.on") : I18n.get("info.off") ))));
        }

        public static boolean getInvisibility(ItemStack itemStack) {
            return itemStack.getOrCreateTag().getBoolean("Invisibility");
        }

        public static void toggleInvisibility(ItemStack itemStack) {
            boolean nightVision = !itemStack.getOrCreateTag().getBoolean("Invisibility");
            itemStack.getTag().putBoolean("Invisibility", nightVision);
            ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(String.format("[%s] %s: %b",  I18n.get("item.leggings.name"), I18n.get("mode.invisibility"), nightVision))));
        }

        @Override
        public void onManaitaKeyPress(ItemStack paramItemStack, Player paramEntityPlayer) {
            if (paramEntityPlayer.isShiftKeyDown()) toggleInvisibility(paramItemStack);
        }
    }

    public static class Boots extends ManaitaPlusArmor implements IManaitaPlusKey {
        public Boots() {
            super(ArmorItem.Type.BOOTS);
        }

        @Override
        public Component getName(ItemStack p_41458_) {
            return Component.literal(I18n.get("item.boots.name"));
        }

        public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
            return "manaita_plus:textures/models/armor/manaita_armor_layer_2.png";
        }

        @Override
        public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
            if (p_41407_ == 0 && p_41406_ instanceof  Player player) {
                player.getAbilities().mayfly = true;
                int speed = getSpeed(p_41404_);
                float p22101 = 0.1F * speed;
                player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(p22101);
                player.getAbilities().setWalkingSpeed(p22101);
                player.getAbilities().setFlyingSpeed(p22101 / 2);
            }
        }

        @Override
        public void onManaitaKeyPress(ItemStack paramItemStack, Player paramEntityPlayer) {
            if (!paramEntityPlayer.isShiftKeyDown()) {
                setSpeed(paramItemStack);
            }

        }

        public static int getSpeed(ItemStack itemStack) {
            return Math.max(itemStack.getOrCreateTag().getInt("Speed"),1);
        }

        public static void setSpeed(ItemStack itemStack) {
            int speed = Math.max(1,itemStack.getOrCreateTag().getInt("Speed") + 1) % 9;
            itemStack.getTag().putInt("Speed", speed);
            ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(String.format("[%s] %s: %d", I18n.get("item.boots.name"), I18n.get("mode.speed"), speed))));
        }
    }

}
