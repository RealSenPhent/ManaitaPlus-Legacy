package sen.manaita_plus_legacy.common.item.tool;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusKey;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusDestroy;
import sen.manaita_plus_legacy.common.item.tier.ManaitaPlusItemTier;
import sen.manaita_plus_legacy.common.util.ManaitaPlusText;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManaitaPlusPickaxeItem extends DiggerItem implements IManaitaPlusKey, IManaitaPlusDestroy {
    public ManaitaPlusPickaxeItem() {
        super(ManaitaPlusItemTier.MAX, ManaitaPlusItemTier.MAX,new ManaitaPlusItemTier(),BlockTags.MINEABLE_WITH_PICKAXE, new Properties().fireResistant());
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {}

    @Override
    public boolean mineBlock(ItemStack p_40998_, Level p_40999_, BlockState p_41000_, BlockPos p_41001_, LivingEntity p_41002_) {
        return true;
    }

    @Override
    public boolean canAttackBlock(BlockState p_41441_, Level p_41442_, BlockPos p_41443_, Player p_41444_) {
        return true;
    }


    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        String range = String.valueOf(getRange(p_41421_));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.manaita_tool") + ": " + range + "x" + range + "x" + range)));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.doubling") + ":" + (getDoubling(p_41421_) ? I18n.get("info.on") : I18n.get("info.off")))));
    }


    public void onManaitaKeyPress(ItemStack itemStack, Player player) {
        boolean doubling = !getDoubling(itemStack);
        setDoubling(itemStack, doubling);
    }

    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
        boolean doubling = !getDoubling(itemStack);
        setDoubling(itemStack, doubling);
        ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(String.format("[%s] %s: %s", I18n.get("item.manaita_plus.manaita_pickaxe"),I18n.get("mode.doubling"), (doubling ? I18n.get("info.on") : I18n.get("info.off"))))));
    }

    public static boolean getDoubling(ItemStack itemStack) {
        if (itemStack.getTag() == null)
            itemStack.setTag(new CompoundTag());
        return itemStack.getTag().getBoolean("Doubling");
    }

    public static void setDoubling(ItemStack itemStack, boolean invisibility) {
        if (itemStack.getTag() == null)
            itemStack.setTag(new CompoundTag());
        itemStack.getTag().putBoolean("Doubling", invisibility);
    }


    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        return false;
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

    public int getRange(ItemStack itemStack) {
        if (itemStack.getTag() == null)
            itemStack.setTag(new CompoundTag());

        int range = itemStack.getTag().getInt("Range");
        if (range == 0) {
            itemStack.getTag().putInt("Range", 1);
            return 1;
        }
        return range;
    }

    public void setRange(ItemStack itemStack,int range) {
        if (range == 0) range = 1;
        itemStack.getTag().putInt("Range", range);
        ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting("[" + I18n.get("item.manaita_plus.manaita_pickaxe") + "] " +I18n.get("mode.range.name") + ": " + range + "x" + range + "x" + range)));
    }

    @Override
    public boolean accept(BlockState state) {
        return state.is(BlockTags.MINEABLE_WITH_PICKAXE);
    }
}
