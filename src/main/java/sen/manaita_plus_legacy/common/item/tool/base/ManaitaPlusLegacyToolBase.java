package sen.manaita_plus_legacy.common.item.tool.base;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
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
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDestroy;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.common.item.tier.ManaitaPlusLegacyToolTier;
import sen.manaita_plus_legacy.common.util.ManaitaPlusLegacyNBTData;
import sen.manaita_plus_legacy.common.util.ManaitaPlusText;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManaitaPlusLegacyToolBase extends DiggerItem implements IManaitaPlusLegacyKey, IManaitaPlusLegacyDestroy, IManaitaPlusLegacyDoubling {
    public ManaitaPlusLegacyToolBase(TagKey<Block> tagKey) {
        super(Float.MAX_VALUE, Float.MAX_VALUE, new ManaitaPlusLegacyToolTier(), tagKey, new Properties().fireResistant());
    }

    @Override
    public boolean accept(BlockState state) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        String range = String.valueOf(getRange(p_41421_));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.manaita_tool") + ": " + range + "x" + range + "x" + range)));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.doubling") + ":" + (isDoubling(p_41421_) ? I18n.get("info.on") : I18n.get("info.off")))));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        ItemStack itemInHand = p_41433_.getItemInHand(p_41434_);
        if (!p_41432_.isClientSide) {
            if (p_41433_.isShiftKeyDown()) {
                setRange(itemInHand,(getRange(itemInHand) + 2) % 21);
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


    public int getRange(ItemStack itemStack) {
        if (!itemStack.hasTag()) return 1;
        assert itemStack.getTag() != null;
        int range = itemStack.getTag().getInt(ManaitaPlusLegacyNBTData.Range);
        if (range == 0) {
            itemStack.getTag().putInt(ManaitaPlusLegacyNBTData.Range, 1);
            return 1;
        }
        return range;
    }

    public void setRange(ItemStack itemStack,int range) {
        if (range == 0) range = 1;
        itemStack.getOrCreateTag().putInt(ManaitaPlusLegacyNBTData.Range, range);
        ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(itemStack.getDisplayName().getString() + " " +I18n.get("mode.range.name") + ": " + range + "x" + range + "x" + range)));
    }

    @Override
    public void onManaitaKeyPress(ItemStack itemStack, Player paramEntityPlayer) {
        boolean doubling = !isDoubling(itemStack);
        setDoubling(itemStack, doubling);
    }

    @Override
    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player paramEntityPlayer) {
        boolean doubling = !isDoubling(itemStack);
        setDoubling(itemStack, doubling);
        ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(itemStack.getDisplayName().getString() + " " + I18n.get("mode.doubling") + ": " + (doubling ? I18n.get("info.on") : I18n.get("info.off")))));
    }
}
