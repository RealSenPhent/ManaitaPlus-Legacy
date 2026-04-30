package sen.manaita_plus_legacy.common.item;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.common.entity.ManaitaPlusLegacyEntityArrow;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;
import sen.manaita_plus_legacy.common.util.tag.ManaitaPlusLegacyTagData;
import sen.manaita_plus_legacy.common.util.text.ManaitaPlusText;

import java.util.List;

import static sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase.getPickDesc;
import static sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase.getType;

public class ManaitaPlusLegacyBowItem extends Item implements IManaitaPlusLegacyKey, IManaitaPlusLegacyDoubling {
    public ManaitaPlusLegacyBowItem() {
        super(new Properties().defaultDurability(-1).fireResistant());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        ItemStack itemstack = player.getItemInHand(p_41434_);

        if (!level.isClientSide) {
            AbstractArrow abstractarrow = ManaitaPlusLegacyEntityArrow.create(level,player);
            if (abstractarrow != null) {
                abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 10.0F, 1.0F);
                abstractarrow.setCritArrow(true);

                level.addFreshEntity(abstractarrow);
            }
        }
        return super.use(level, player, p_41434_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.doubling.name") + ":" + (isDoubling(ManaitaPlusLegacyToolBase.getType(p_41421_)) ? I18n.get("info.on") : I18n.get("info.off")))));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(getPickDesc(ManaitaPlusLegacyToolBase.getType(p_41421_)))));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_infinity.formatting(I18n.get("info.attack"))));
    }
    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("item.manaita_bow.name")));
    }

    @Override
    public void onManaitaKeyPress(ItemStack itemStack, Player paramEntityPlayer,int i) {
        if (i == 0) {
            int type = ManaitaPlusLegacyToolBase.getType(itemStack);
            setDoubling(itemStack, type);
        } else  if (i == 2) {
            int type = ManaitaPlusLegacyToolBase.getType(itemStack);
            ManaitaPlusLegacyToolBase.setPickMode(itemStack, paramEntityPlayer.isShiftKeyDown(), type);
        }
    }

    @Override
    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player paramEntityPlayer,int i) {
        if (i == 0) {
            ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(itemStack.getDisplayName().getString() + " " + I18n.get("mode.doubling.name") + ": " + (setDoubling(itemStack, ManaitaPlusLegacyToolBase.getType(itemStack)) ? I18n.get("info.on") : I18n.get("info.off")))));
        } else if (i == 2) {
            ManaitaPlusLegacyToolBase.setPickMode(itemStack, paramEntityPlayer.isShiftKeyDown(), ManaitaPlusLegacyToolBase.getType(itemStack));
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

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        entity.hurt(entity.damageSources().playerAttack(player), 10000);
        return super.onLeftClickEntity(stack, player, entity);
    }
}
