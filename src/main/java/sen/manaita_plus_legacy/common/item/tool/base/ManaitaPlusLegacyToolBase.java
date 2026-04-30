package sen.manaita_plus_legacy.common.item.tool.base;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDestroy;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.common.item.tier.ManaitaPlusLegacyToolTier;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;
import sen.manaita_plus_legacy.common.util.tag.ManaitaPlusLegacyTagData;
import sen.manaita_plus_legacy.common.util.text.ManaitaPlusText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class ManaitaPlusLegacyToolBase extends DiggerItem implements IManaitaPlusLegacyKey, IManaitaPlusLegacyDestroy, IManaitaPlusLegacyDoubling {
    public static final UUID BASE_ENTITY_REACH_UUID = UUID.fromString("835233d0-782e-4c8e-9131-6fe4dd0e40d2");
    public static final UUID BASE_BLOCK_REACH_UUID = UUID.fromString("22537a6a-04c4-4104-821b-be51bd4005e3");

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public ManaitaPlusLegacyToolBase(TagKey<Block> tagKey) {
        super(Float.MAX_VALUE, Float.MAX_VALUE, new ManaitaPlusLegacyToolTier(), tagKey, new Properties().fireResistant());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ENTITY_REACH_UUID, "Tool modifier", Double.POSITIVE_INFINITY, AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(BASE_BLOCK_REACH_UUID, "Tool modifier", Double.POSITIVE_INFINITY, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_43274_) {
        return p_43274_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43274_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        String range = String.valueOf(getRange(p_41421_));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.manaita_tool.name") + ": " + range + "x" + range + "x" + range)));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.doubling.name") + ":" + (isDoubling(getType(p_41421_)) ? I18n.get("info.on") : I18n.get("info.off")))));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.farDestroyDelay.name") + ":" + (isFarDestroyDelay(getType(p_41421_)) ? I18n.get("info.on") : I18n.get("info.off")))));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(getPickDesc(ManaitaPlusLegacyToolBase.getType(p_41421_)))));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        ItemStack itemInHand = p_41433_.getItemInHand(p_41434_);
        if (!p_41432_.isClientSide) {
            if (p_41433_.isShiftKeyDown()) {
                setRange(itemInHand,((getRange(itemInHand) + 2) % 21) | 1);
            } else {
                Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
                enchantmentMap.put(Enchantments.BLOCK_FORTUNE, 10);
                String s =  I18n.get("enchantments.fortune");
                if (!EnchantmentHelper.hasSilkTouch(itemInHand)) {
                    enchantmentMap.put(Enchantments.SILK_TOUCH, 1);
                    s = I18n.get("enchantments.silktouch");
                }
                EnchantmentHelper.setEnchantments(enchantmentMap, itemInHand);
                ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_enchantment.formatting(itemInHand.getDisplayName().getString() + I18n.get("info.enchantment") + ": " + s)));
            }
        }
        return InteractionResultHolder.pass(itemInHand);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        entity.hurt(entity.damageSources().playerAttack(player), 10000);
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public boolean mineBlock(ItemStack p_40998_, Level p_40999_, BlockState p_41000_, BlockPos p_41001_, LivingEntity p_41002_) {
        return true;
    }

    @Override
    public boolean canAttackBlock(BlockState p_41441_, Level p_41442_, BlockPos p_41443_, Player p_41444_) {
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        return false;
    }

    @Override
    public void onManaitaKeyPress(ItemStack itemStack, Player player,int i) {
        if (i == 0) {
            int type = ManaitaPlusLegacyToolBase.getType(itemStack);
            if (player.isShiftKeyDown()) {
                setDoubling(itemStack, type);
            } else {
                setFarDestroyDelay(itemStack,type);
            }
        } else if (i == 2) {
            int type = ManaitaPlusLegacyToolBase.getType(itemStack);
            ManaitaPlusLegacyToolBase.setPickMode(itemStack, player.isShiftKeyDown(), type);
        }
    }

    @Override
    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player,int i) {
        if (i == 0) {
            if (player.isShiftKeyDown()) {
                ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(itemStack.getDisplayName().getString() + " " + I18n.get("mode.doubling.name") + ": " + (setDoubling(itemStack, ManaitaPlusLegacyToolBase.getType(itemStack)) ? I18n.get("info.on") : I18n.get("info.off")))));
            } else {
                ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(itemStack.getDisplayName().getString() + " " + I18n.get("mode.farDestroyDelay.name") + ": " + (setFarDestroyDelay(itemStack, ManaitaPlusLegacyToolBase.getType(itemStack)) ? I18n.get("info.on") : I18n.get("info.off")))));
            }
        } else if (i == 2) {
            ManaitaPlusLegacyToolBase.setPickMode(itemStack, player.isShiftKeyDown(), ManaitaPlusLegacyToolBase.getType(itemStack));
            int type = ManaitaPlusLegacyToolBase.getType(itemStack);
            StringBuilder sb = new StringBuilder(I18n.get("mode.pick.name") + ": ");
            if (ManaitaPlusLegacyToolBase.canPick(type,true)) {
                sb.append(I18n.get("info.pick_items.name"));
                if (ManaitaPlusLegacyToolBase.canPick(type,false)) sb.append("&").append(I18n.get("info.pick_experience.name"));
            } else if (ManaitaPlusLegacyToolBase.canPick(type,false)) {
                sb.append(I18n.get("info.pick_experience.name"));
            } else {
                sb.append("None");
            }
            ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(itemStack.getDisplayName().getString() + " " + sb)));
        }
    }

    public static int getType(ItemStack itemStack) {
        if (!itemStack.hasTag()) return 0;
        assert itemStack.getTag() != null;

        return itemStack.getTag().getInt(ManaitaPlusLegacyTagData.Type);
    }

    public static boolean canPick(int type,boolean isItems) {
        if (isItems) {
            return (type & ManaitaPlusLegacyTagData.pickItems) != 0;
        } else {
            return (type & ManaitaPlusLegacyTagData.pickExperiences) != 0;
        }
    }

    public static boolean setPickMode(ItemStack itemStack,boolean isExperiences,int type) {
        if (isExperiences) {
            if ((type & ManaitaPlusLegacyTagData.pickExperiences) == 0) {
                ManaitaPlusLegacyTagData.setMode(itemStack,type | ManaitaPlusLegacyTagData.pickExperiences);
                return true;
            } else {
                ManaitaPlusLegacyTagData.setMode(itemStack,type & ~ManaitaPlusLegacyTagData.pickExperiences);
                return false;
            }
        } else {
            if ((type & ManaitaPlusLegacyTagData.pickItems) == 0) {
                ManaitaPlusLegacyTagData.setMode(itemStack,type | ManaitaPlusLegacyTagData.pickItems);
                return true;
            } else {
                ManaitaPlusLegacyTagData.setMode(itemStack,type & ~ManaitaPlusLegacyTagData.pickItems);
                return false;
            }
        }
    }

    public static String getPickDesc(int type) {
        StringBuilder sb = new StringBuilder(I18n.get("mode.pick.name") + ": ");
        if (canPick(type,true)) {
            sb.append(I18n.get("info.pick_items.name"));
            if (canPick(type,false)) sb.append("&").append(I18n.get("info.pick_experience.name"));
        } else if (canPick(type,false)) {
            sb.append(I18n.get("info.pick_experience.name"));
        } else {
            sb.append("None");
        }

        return sb.toString();
    }

    public static boolean isFarDestroyDelay(int type) {
        return (type & ManaitaPlusLegacyTagData.farDestroyDelay) != 0;
    }

    public static boolean setFarDestroyDelay(ItemStack itemStack, int type) {
        CompoundTag orCreateTag = itemStack.getOrCreateTag();
        if ((type & ManaitaPlusLegacyTagData.farDestroyDelay) == 0) {
            orCreateTag.putInt(ManaitaPlusLegacyTagData.Type, type | ManaitaPlusLegacyTagData.farDestroyDelay);
            return true;
        } else {
            orCreateTag.putInt(ManaitaPlusLegacyTagData.Type, type & ~ManaitaPlusLegacyTagData.farDestroyDelay);
            return false;
        }
    }
}
