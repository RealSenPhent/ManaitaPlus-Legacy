package sen.manaita_plus_legacy.common.event;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.common.config.ManaitaPlusLegacyConfig;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyBlockCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCore;
import sen.manaita_plus_legacy.common.item.armor.ManaitaPlusLegacyArmor;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.common.trades.ManaitaPlusLegacyBowVillagerTrade;
import sen.manaita_plus_legacy.common.trades.ManaitaPlusLegacySwordGodVillagerTrade;
import sen.manaita_plus_legacy.common.util.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = ManaitaPlusLegacy.MODID)
public class EventHandler {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        if (item instanceof ManaitaPlusLegacyArmor) {
            List<Component> toolTip = event.getToolTip();
            Iterator<Component> iterator = toolTip.iterator();
            Component component;
            while (iterator.hasNext()) {
                component = iterator.next();
                if (component instanceof MutableComponent mutableComponent) {
                    for (Component sibling : mutableComponent.getSiblings()) {
                        if (sibling instanceof MutableComponent mutableComponent1) {
                            if (mutableComponent1.getContents() instanceof TranslatableContents) {
                                iterator.remove();
                                break;
                            }
                        }
                    }
                }
            }
        } else if (item instanceof IManaitaPlusLegacyKey) {
            List<Component> toolTip = event.getToolTip();
            Iterator<Component> iterator = toolTip.iterator();
            while1 : while (iterator.hasNext()) {
                Component component = iterator.next();
                if (component instanceof MutableComponent mutableComponent) {
                    for (Component sibling : mutableComponent.getSiblings()) {
                        if (sibling instanceof MutableComponent mutableComponent1) {
                            if (mutableComponent1.getContents() instanceof TranslatableContents translatableContents) {
                                if (translatableContents.getKey().startsWith("item.modifiers.")) {
                                    while (iterator.hasNext()) {
                                        iterator.next();
                                        iterator.remove();
                                    }
                                    break while1;
                                }
                            }
                        }
                    }
                }
            }
            toolTip.remove(toolTip.size() - 1);
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) return;
        Player player = event.getEntity();
        ManaitaPlusUtils.destroyBlocks(player.getMainHandItem(),event.getLevel(),event.getPos(),player);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        ManaitaPlusLegacyEntityData.death.remove(event.getEntity());
        ManaitaPlusLegacyEntityData.remove.remove(event.getEntity());
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            ItemStack mainHandItem = player.getMainHandItem();
            if (mainHandItem.getItem() instanceof IManaitaPlusLegacyDoubling doublingItem) {
                if (doublingItem.isDoubling(mainHandItem)) {
                    int magnification = ManaitaPlusLegacyConfig.drops_doubling_value;
                    for (ItemEntity drop : event.getDrops()) {
                        ItemStack dropStack = drop.getItem();
                        dropStack.setCount(dropStack.getCount() * magnification);
                        player.getInventory().add(dropStack);
                    }
                } else {
                    for (ItemEntity drop : event.getDrops()) {
                        player.getInventory().add(drop.getItem());
                    }
                }
                event.getDrops().clear();
            }
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ManaitaPlusUtils.isManaitaArmorPart(player) || ManaitaPlusUtils.isManaita(player)) {
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
                player.fallDistance = 0;
                player.hurtTime = 0;
                player.deathTime = 0;
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ManaitaPlusUtils.isManaitaArmor(player) || ManaitaPlusUtils.isManaita(player)) {
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
                player.fallDistance = 0;
                player.hurtTime = 0;
                player.deathTime = 0;
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ManaitaPlusUtils.isManaitaArmor(player) || ManaitaPlusUtils.isManaita(player)) {
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
                player.fallDistance = 0;
                player.hurtTime = 0;
                player.deathTime = 0;
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ManaitaPlusUtils.isManaitaArmor(player) || ManaitaPlusUtils.isManaita(player))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.WEAPONSMITH) {
            List<VillagerTrades.ItemListing> tradesTier = event.getTrades().get(5);

            tradesTier.add(new ManaitaPlusLegacyBowVillagerTrade(
                    new ItemStack(ManaitaPlusLegacyBlockCore.CraftingBlockItem.get(), 64),
                    new ItemStack(ManaitaPlusLegacyItemCore.ManaitaBow.get(), 1),
                    1, 0, 1));
            tradesTier.add(new ManaitaPlusLegacyBowVillagerTrade(
                    new ItemStack(ManaitaPlusLegacyBlockCore.FurnaceBlockItem.get(), 64),
                    new ItemStack(ManaitaPlusLegacyItemCore.ManaitaBow.get(), 1),
                    1, 0, 1));
            tradesTier.add(new ManaitaPlusLegacyBowVillagerTrade(
                    new ItemStack(ManaitaPlusLegacyBlockCore.BrewingBlock.get(), 64),
                    new ItemStack(ManaitaPlusLegacyItemCore.ManaitaBow.get(), 1),
                    1, 0, 1));


            tradesTier.add(new ManaitaPlusLegacySwordGodVillagerTrade(
                    new ItemStack(ManaitaPlusLegacyItemCore.ManaitaBow.get(), 1),
                    new ItemStack(ManaitaPlusLegacyItemCore.ManaitaSwordGod.get(), 1),
                    1, 0, 1));
        }
    }




}
