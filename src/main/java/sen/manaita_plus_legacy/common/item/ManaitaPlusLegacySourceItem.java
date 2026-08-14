package sen.manaita_plus_legacy.common.item;

import com.sun.jna.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;
import sen.manaita_plus_legacy.common.util.text.ManaitaPlusText;
import sen.manaita_plus_legacy_core.util.BacktrackingUtils;

import java.util.List;

public class ManaitaPlusLegacySourceItem extends Item {
    public ManaitaPlusLegacySourceItem() {
        super(new Properties().defaultDurability(-1).fireResistant().stacksTo(64));
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("item.source.name")));
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        if (p_41421_.hasTag() && p_41421_.getItem() instanceof ManaitaPlusLegacySourceItem) {
            CompoundTag tag = p_41421_.getTag();
            assert tag != null;
            ListTag listtag = tag.getList("Items", 10);
            if (!listtag.isEmpty()) {
                for (int i = 0; i < listtag.size(); ++i) {
                    CompoundTag compoundtag = listtag.getCompound(i);
                    ItemStack itemStack = ItemStack.of(compoundtag);
                    itemStack.setCount(compoundtag.getInt("RealCount"));
                    p_41423_.add(Component.literal(ChatFormatting.GOLD + itemStack.getDisplayName().getString() + " x" + itemStack.getCount()));
                }
                p_41423_.add(Component.empty());
            }
        }
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_infinity.formatting(I18n.get("info.source.1"))));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_infinity.formatting(I18n.get("info.source.2"))));
    }

    @OnlyIn(net.minecraftforge.api.distmarker.Dist.CLIENT)
    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        ItemStack itemInHand = p_41433_.getItemInHand(p_41434_);
//
//        if (p_41433_.isShiftKeyDown()) {
//            ManaitaPlusUtils.Client.chat("backtrack");
//            BacktrackingUtils.backtrack();
//        } else {
//            System.err.println("Tracking count: " + BacktrackingUtils.liveTrackers.size());
//            ManaitaPlusUtils.Client.chat("record");
//            BacktrackingUtils.record();
//        }
        if (itemInHand.hasTag() && itemInHand.getItem() instanceof ManaitaPlusLegacySourceItem) {
            CompoundTag tag = itemInHand.getTag();
            assert tag != null;
            ListTag listtag = tag.getList("Items", 10);
            Vec3 blockPos = ManaitaPlusUtils.predictPlayerPosition(p_41433_, 10);
            for(int i = 0; i < listtag.size(); ++i) {
                CompoundTag compoundtag = listtag.getCompound(i);
                ItemStack itemStack = ItemStack.of(compoundtag);
                itemStack.setCount(compoundtag.getInt("RealCount"));
                ManaitaPlusUtils.popResource(p_41432_,blockPos, itemStack,p_41433_.getDeltaMovement());
            }
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }

    private static final ResourceLocation zlxxx = ManaitaPlusLegacy.rl("adventure/zlxxx");
    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        if (p_41406_ instanceof ServerPlayer player) {
            Inventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack itemstack = inventory.getItem(i);
                if (itemstack.isDamageableItem()) {
                    itemstack.setDamageValue(0);
                }
            }
        }
        CompoundTag display = p_41404_.getTagElement("display");
        if (display != null && display.contains("Name", 8)) {
            try {
                Component component = Component.Serializer.fromJson(display.getString("Name"));
                if (component != null) {
                    if (component.getString().contains("zlxxx")) {
                        if (p_41406_ instanceof ServerPlayer serverPlayer) {
                            Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(zlxxx);
                            if (advancement != null) {
//                for (String criterion : advancement.getCriteria().keySet()) {
//                    serverPlayer.getAdvancements().award(advancement, criterion);
//                    System.err.println(criterion);
//                }
                                serverPlayer.getAdvancements().award(advancement,"who_is_he");
                            }
                        }
                    }
                }

            } catch (Exception exception) {}
        }
    }


}
