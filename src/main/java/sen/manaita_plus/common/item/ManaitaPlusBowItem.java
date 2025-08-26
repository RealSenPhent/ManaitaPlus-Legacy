package sen.manaita_plus.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus.common.entity.ManaitaPlusEntityArrow;
import sen.manaita_plus.common.item.data.IManaitaPlusKey;
import sen.manaita_plus.common.util.ManaitaPlusText;

import java.util.List;

public class ManaitaPlusBowItem extends Item implements IManaitaPlusKey {
    public ManaitaPlusBowItem() {
        super(new Properties().defaultDurability(-1).fireResistant());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        ItemStack itemstack = player.getItemInHand(p_41434_);

        if (!level.isClientSide) {
            AbstractArrow abstractarrow = ManaitaPlusEntityArrow.craft(level,player);
            if (abstractarrow != null) {
                abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 100.0F, 1.0F);
                abstractarrow.setCritArrow(true);

                level.addFreshEntity(abstractarrow);
            }
        }
        return super.use(level, player, p_41434_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.doubling") + ":" + (getDoubling(p_41421_) ? I18n.get("info.on") : I18n.get("info.off")))));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_infinity.formatting(I18n.get("info.attack"))));
    }
    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("item.manaita_bow.name")));
    }

    public void onManaitaKeyPress(ItemStack itemStack, Player player) {
        boolean doubling = !getDoubling(itemStack);
        setDoubling(itemStack, doubling);
        Minecraft.getInstance().gui.setOverlayMessage(Component.literal(String.format("[%s%s] %s%s: %s", ManaitaPlusText.manaita_mode.formatting(I18n.get("item.manaita_bow.name")), ChatFormatting.RESET, ChatFormatting.RESET,I18n.get("mode.manaita_sword_god"), (getDoubling(itemStack) ? I18n.get("info.on") : I18n.get("info.off")))), false);
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


}
