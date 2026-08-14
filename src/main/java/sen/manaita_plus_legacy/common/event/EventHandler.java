package sen.manaita_plus_legacy.common.event;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.common.config.ManaitaPlusLegacyConfig;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyBlockCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCore;
import sen.manaita_plus_legacy.common.curios.CuriosUtil;
import sen.manaita_plus_legacy.common.item.ManaitaPlusLegacyGodSwordItem;
import sen.manaita_plus_legacy.common.item.curios.CuriosSourceItem;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.common.item.tool.ManaitaPlusLegacyKatarItem;
import sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase;
import sen.manaita_plus_legacy.common.network.Networking;
import sen.manaita_plus_legacy.common.network.implement.ChangeDeathDataPacket;
import sen.manaita_plus_legacy.common.proxy.CommomProxy;
import sen.manaita_plus_legacy.common.trades.ManaitaPlusLegacyBowVillagerTrade;
import sen.manaita_plus_legacy.common.trades.ManaitaPlusLegacySwordGodVillagerTrade;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy.common.util.tag.ManaitaPlusLegacyTagData;
import sen.manaita_plus_legacy_core.util.ClientEventUtil;
import sen.manaita_plus_legacy_core.util.EventUtil;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = ManaitaPlusLegacy.MODID)
public class EventHandler {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        if (item instanceof IManaitaPlusLegacyKey) {
            List<Component> toolTip = event.getToolTip();
            Iterator<Component> iterator = toolTip.iterator();
            while (iterator.hasNext()) {
                Component component = iterator.next();
                if (component instanceof MutableComponent mutableComponent) {
                    ComponentContents contents = mutableComponent.getContents();
                    if (contents instanceof TranslatableContents translatableContents) {
                        if (translatableContents.getKey().startsWith("item.modifiers.")) {
                            while (iterator.hasNext()) {
                                iterator.next();
                                iterator.remove();
                            }
                            break;
                        }
                    }
                }
            }
            toolTip.remove(toolTip.size() - 2);
            toolTip.remove(toolTip.size() - 1);
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        CompoundTag persistentData = player.getPersistentData();
        ItemStack mainHandItem = player.getMainHandItem();
        boolean flag = mainHandItem.getItem() instanceof ManaitaPlusLegacyToolBase &&
                ManaitaPlusLegacyToolBase.isFarDestroy(ManaitaPlusLegacyToolBase.getType(mainHandItem));
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) {
            if (flag) persistentData.putLong(ManaitaPlusLegacyTagData.DTime,0L);
            return;
        }
        BlockPos pos = event.getPos();
        if (flag && player.getEyePosition().distanceToSqr(new Vec3(pos.getX(), pos.getY(), pos.getZ())) >= 24) {
            if (!player.level().isClientSide) {
                int[] p128387 = {pos.getX(), pos.getY(), pos.getZ()};
                if (!Arrays.equals(persistentData.getIntArray(ManaitaPlusLegacyTagData.DPos), p128387)) {
                    persistentData.putIntArray(ManaitaPlusLegacyTagData.DPos, p128387);
                    persistentData.putLong(ManaitaPlusLegacyTagData.DTime, System.currentTimeMillis());
                }
            }
            return;
        }

        ManaitaPlusUtils.destroyBlocks(player.getMainHandItem(),event.getLevel(), pos,player);
    }

    @SubscribeEvent
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) return;
        if (event.player instanceof ServerPlayer serverPlayer) {
            ItemStack mainHandItem = serverPlayer.getMainHandItem();
            if (mainHandItem.getItem() instanceof ManaitaPlusLegacyToolBase) {
                if (!ManaitaPlusLegacyToolBase.isFarDestroy(ManaitaPlusLegacyToolBase.getType(mainHandItem))) return;
                if (serverPlayer.gameMode.isDestroyingBlock) {
                    BlockPos pos = serverPlayer.gameMode.destroyPos;
                    CompoundTag persistentData = serverPlayer.getPersistentData();
                    if (serverPlayer.getEyePosition().distanceToSqr(new Vec3(pos.getX(), pos.getY(), pos.getZ())) >= 24) {
                        if (!serverPlayer.level().isClientSide) {
                            int[] p128387 = {pos.getX(), pos.getY(), pos.getZ()};
                            if (!Arrays.equals(persistentData.getIntArray(ManaitaPlusLegacyTagData.DPos), p128387) || persistentData.getLong(ManaitaPlusLegacyTagData.DTime) == 0L) {
                                persistentData.putIntArray(ManaitaPlusLegacyTagData.DPos, p128387);
                                persistentData.putLong(ManaitaPlusLegacyTagData.DTime, System.currentTimeMillis());
                            }
                        }
                        if ((System.currentTimeMillis() - persistentData.getLong(ManaitaPlusLegacyTagData.DTime)) >= 200) {
                            ManaitaPlusUtils.destroyBlocks(mainHandItem, serverPlayer.level(), pos, serverPlayer);
                        }
                        return;
                    }
                    ManaitaPlusUtils.destroyBlocks(mainHandItem, serverPlayer.level(), pos, serverPlayer);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        ManaitaPlusLegacyEntityData.death.remove(event.getEntity());
        ManaitaPlusLegacyEntityData.remove.remove(event.getEntity());
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            Networking.sendToPlayer(serverPlayer,new ChangeDeathDataPacket(0));
        }
        ManaitaPlusLegacyEntityData.death.putInt(event.getEntity(), 0);
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getDrops() == null) return;
        Player player;
        if (event.getSource().getEntity() instanceof Player) {
            player = (Player) event.getSource().getEntity();
        } else {
            LivingEntity killCredit = event.getEntity().getKillCredit();
            if (killCredit instanceof Player) {
                player = (Player) killCredit;
            } else {
                return;
            }
        }
        ItemStack mainHandItem = player.getMainHandItem();
        if (mainHandItem.getItem() instanceof IManaitaPlusLegacyDoubling doublingItem) {
            int type = ManaitaPlusLegacyToolBase.getType(mainHandItem);
            if (doublingItem.isDoubling(type)) {
                int magnification = ManaitaPlusLegacyConfig.item_drops_doubling_value;
                for (ItemEntity drop : event.getDrops()) {
                    ItemStack dropStack = drop.getItem();
                    dropStack.setCount(dropStack.getCount() * magnification);
                }
            }
            if (ManaitaPlusLegacyToolBase.canPick(type,true)) {
                event.getDrops().forEach(item -> player.getInventory().add(item.getItem()));
                event.getDrops().clear();
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingExperienceDrop(LivingExperienceDropEvent event) {
        Player attackingPlayer = event.getAttackingPlayer();

        if (attackingPlayer == null)
            return;
        ItemStack mainHandItem = attackingPlayer.getMainHandItem();
        if (mainHandItem.getItem() instanceof IManaitaPlusLegacyDoubling doublingItem) {
            int type = ManaitaPlusLegacyToolBase.getType(mainHandItem);
            if (doublingItem.isDoubling(type)) {
                event.setDroppedExperience(event.getDroppedExperience() * ManaitaPlusLegacyConfig.experience_drops_doubling_value);
            }
            if (ManaitaPlusLegacyToolBase.canPick(type, false)) {
                attackingPlayer.giveExperiencePoints(event.getDroppedExperience());
                event.setDroppedExperience(0);
                event.setCanceled(true);
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
    public static void onLivingFall(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getMainHandItem().getItem() instanceof ManaitaPlusLegacyGodSwordItem) {
            ManaitaPlusLegacyEntityData.manaita.add(entity);
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


    @SubscribeEvent
    public static void onPlayerLogin(NetworkEvent.LoginPayloadEvent event) {
        NetworkEvent.Context context = event.getSource().get();
        if (context.getSender() == null || event.isCanceled()) return;
//        Networking.sendToPlayer(context.getSender(), new ChangeEntityDataPacket(null,0));
//        Networking.sendToPlayer(context.getSender(), new ChangeEntitiesIDDataPacket(ManaitaPlusLegacyEntityData.manaita.getFlag()));
//        Networking.sendToPlayer(context.getSender(), new ChangeEntitiesUUIDDataPacket(ManaitaPlusLegacyEntityData.manaita.getFlag()));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (ManaitaPlusLegacyEntityData.manaita.accept(player) && !ManaitaPlusLegacyEntityData.anti.accept(player)) {
            boolean flag = false;
            for (ItemStack item : player.getInventory().items) {
                if (item.getItem() instanceof ManaitaPlusLegacyGodSwordItem) {
                        if (ManaitaPlusLegacyGodSwordItem.isAntiDisarming(ManaitaPlusLegacyToolBase.getType(item))) {
//                            if (!ManaitaPlusItemData.stackList.contains(item)) ManaitaPlusItemData.stackList.add(item);
                            ManaitaPlusLegacyEntityData.anti.add(player);
                        }
                        flag = true;
                }
            }
            if (flag) return;
            ManaitaPlusLegacyEntityData.manaita.remove(player);
        }
    }
}
