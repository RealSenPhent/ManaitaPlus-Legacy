package sen.manaita_plus_legacy.common.util.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ItemLike;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCore;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class ManaitaPlusLegacyItemStack/* extends ItemStack*/ {
    private static final Item d = ManaitaPlusLegacyItemCore.ManaitaSwordGod.get();
    public static final ItemStack instance = new ItemStack(d);
//    @Nullable
//    private CompoundTag tag;
//    public ManaitaPlusLegacyItemStack() {
//        super(ManaitaPlusLegacyItemCore.ManaitaSwordGod.get());
//    }
//
//    public ManaitaPlusLegacyItemStack(@Nullable CompoundTag p_41606_) {
//        super(ManaitaPlusLegacyItemCore.ManaitaSwordGod.get(),1,p_41606_);
//        this.tag = p_41606_;
//
////        this.capNBT = p_41606_;
////        this.item = p_41604_.asItem();
////        this.delegate = net.minecraftforge.registries.ForgeRegistries.ITEMS.getDelegateOrThrow(p_41604_.asItem());
////        this.count = p_41605_;
////        this.forgeInit();
////        if (this.item.isDamageable(this)) {
////            this.setDamageValue(this.getDamageValue());
////        }
//
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
//
//    @Override
//    public Item getItem() {
//        return d;
//    }
//
//    @Override
//    public ItemStack copy() {
//        ManaitaPlusLegacyItemStack itemstack = new ManaitaPlusLegacyItemStack(this.serializeCaps());
//        itemstack.setPopTime(this.getPopTime());
//        if (this.tag != null) {
//            itemstack.tag = this.tag.copy();
//        }
//
//        return itemstack;
//    }
//
//    @Override
//    public boolean isStackable() {
//        return false;
//    }
//
//    @Override
//    public boolean isDamageableItem() {
//        return false;
//    }
//
//    public boolean hasTag() {
//        return !this.isEmpty() && this.tag != null && !this.tag.isEmpty();
//    }
//
//    @Nullable
//    public CompoundTag getTag() {
//        return this.tag;
//    }
//
//    public CompoundTag getOrCreateTag() {
//        if (this.tag == null) {
//            this.setTag(new CompoundTag());
//        }
//
//        return this.tag;
//    }
//
//    public CompoundTag getOrCreateTagElement(String p_41699_) {
//        if (this.tag != null && this.tag.contains(p_41699_, 10)) {
//            return this.tag.getCompound(p_41699_);
//        } else {
//            CompoundTag compoundtag = new CompoundTag();
//            this.addTagElement(p_41699_, compoundtag);
//            return compoundtag;
//        }
//    }
//
//
//    @Nullable
//    public CompoundTag getTagElement(String p_41738_) {
//        return this.tag != null && this.tag.contains(p_41738_, 10) ? this.tag.getCompound(p_41738_) : null;
//    }
//
//    public void removeTagKey(String p_41750_) {
//        if (this.tag != null && this.tag.contains(p_41750_)) {
//            this.tag.remove(p_41750_);
//            if (this.tag.isEmpty()) {
//                this.tag = null;
//            }
//        }
//
//    }
//
//    public ListTag getEnchantmentTags() {
//        return this.tag != null ? this.tag.getList("Enchantments", 10) : new ListTag();
//    }
//
//    public void setTag(@Nullable CompoundTag p_41752_) {
//        this.tag = p_41752_;
//        if (this.getItem().isDamageable(this)) {
//            this.setDamageValue(this.getDamageValue());
//        }
//
//        if (p_41752_ != null) {
//            this.getItem().verifyTagAfterLoad(p_41752_);
//        }
//
//    }
//
//    public void resetHoverName() {
//        CompoundTag compoundtag = this.getTagElement("display");
//        if (compoundtag != null) {
//            compoundtag.remove("Name");
//            if (compoundtag.isEmpty()) {
//                this.removeTagKey("display");
//            }
//        }
//
//        if (this.tag != null && this.tag.isEmpty()) {
//            this.tag = null;
//        }
//
//    }
//
//    private int getHideFlags() {
//        return this.hasTag() && this.tag.contains("HideFlags", 99) ? this.tag.getInt("HideFlags") : this.getItem().getDefaultTooltipHideFlags(this);
//    }
//
//    public void enchant(Enchantment p_41664_, int p_41665_) {
//        this.getOrCreateTag();
//        if (!this.tag.contains("Enchantments", 9)) {
//            this.tag.put("Enchantments", new ListTag());
//        }
//
//        ListTag listtag = this.tag.getList("Enchantments", 10);
//        listtag.add(EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(p_41664_), (byte)p_41665_));
//    }
//
//    public boolean isEnchanted() {
//        if (this.tag != null && this.tag.contains("Enchantments", 9)) {
//            return !this.tag.getList("Enchantments", 10).isEmpty();
//        } else {
//            return false;
//        }
//    }
//
//    public int getBaseRepairCost() {
//        return this.hasTag() && this.tag.contains("RepairCost", 3) ? this.tag.getInt("RepairCost") : 0;
//    }
//
//
//    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot p_41639_) {
//        Multimap<Attribute, AttributeModifier> multimap;
//        if (this.hasTag() && this.tag.contains("AttributeModifiers", 9)) {
//            multimap = HashMultimap.create();
//            ListTag listtag = this.tag.getList("AttributeModifiers", 10);
//
//            for(int i = 0; i < listtag.size(); ++i) {
//                CompoundTag compoundtag = listtag.getCompound(i);
//                if (!compoundtag.contains("Slot", 8) || compoundtag.getString("Slot").equals(p_41639_.getName())) {
//                    Optional<Attribute> optional = BuiltInRegistries.ATTRIBUTE.getOptional(ResourceLocation.tryParse(compoundtag.getString("AttributeName")));
//                    if (optional.isPresent()) {
//                        AttributeModifier attributemodifier = AttributeModifier.load(compoundtag);
//                        if (attributemodifier != null && attributemodifier.getId().getLeastSignificantBits() != 0L && attributemodifier.getId().getMostSignificantBits() != 0L) {
//                            multimap.put(optional.get(), attributemodifier);
//                        }
//                    }
//                }
//            }
//        } else {
//            multimap = this.getItem().getAttributeModifiers(p_41639_, this);
//        }
//
//        multimap = net.minecraftforge.common.ForgeHooks.getAttributeModifiers(this, p_41639_, multimap);
//        return multimap;
//    }
//
//    public void addAttributeModifier(Attribute p_41644_, AttributeModifier p_41645_, @Nullable EquipmentSlot p_41646_) {
//        this.getOrCreateTag();
//        if (!this.tag.contains("AttributeModifiers", 9)) {
//            this.tag.put("AttributeModifiers", new ListTag());
//        }
//
//        ListTag listtag = this.tag.getList("AttributeModifiers", 10);
//        CompoundTag compoundtag = p_41645_.save();
//        compoundtag.putString("AttributeName", BuiltInRegistries.ATTRIBUTE.getKey(p_41644_).toString());
//        if (p_41646_ != null) {
//            compoundtag.putString("Slot", p_41646_.getName());
//        }
//
//        listtag.add(compoundtag);
//    }
}
