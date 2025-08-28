package sen.manaita_plus.common.event;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sen.manaita_plus.common.core.ManaitaPlusBlockCore;
import sen.manaita_plus.common.core.ManaitaPlusItemCore;
import sen.manaita_plus.common.item.ManaitaPlusGodSwordItem;
import sen.manaita_plus.common.item.armor.ManaitaPlusArmor;
import sen.manaita_plus.common.trades.ManaitaPlusBowVillagerTrade;
import sen.manaita_plus.common.util.ManaitaPlusUtils;

import java.util.List;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        if (item instanceof ManaitaPlusGodSwordItem) {
            List<Component> toolTip = event.getToolTip();
            for (Component component : toolTip) {
                if (component instanceof MutableComponent mutableComponent) {                    for (Component sibling : mutableComponent.getSiblings()) {
                        if (sibling instanceof MutableComponent mutableComponent1) {
                            if (mutableComponent1.getContents() instanceof TranslatableContents translatableContents) {
                                if (translatableContents.getKey().startsWith("attribute.modifier.equals.")) {
                                    if (translatableContents.getArgs()[0] != null)  translatableContents.getArgs()[0] = "Infinity";
                                }
                            }
                        }
                    }
                }
            }
        } else if (item instanceof ManaitaPlusArmor) {
            List<Component> toolTip = event.getToolTip();
            for (Component component : toolTip) {
                if (component instanceof MutableComponent mutableComponent) {
                    for (Component sibling : mutableComponent.getSiblings()) {
                        if (sibling instanceof MutableComponent mutableComponent1) {
                            if (mutableComponent1.getContents() instanceof TranslatableContents translatableContents) {
                                mutableComponent.getSiblings().remove(translatableContents);
                            }
                        }
                    }
                }
            }
        }
    }

//    @SubscribeEvent
//    public static void registerAttributes(EntityAttributeCreationEvent event) {
//        event.put(ManaitaPlusEntityCore.ManaitaLightningBolt.get(), ManaitaPlusLightningBolt.createAttributes().b());
//    }


    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        ItemStack mainHandItem = player.getMainHandItem();
        ManaitaPlusUtils.desBlocks(mainHandItem,level,event.getPos(),player);
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            if (player.getMainHandItem().getItem() instanceof ManaitaPlusGodSwordItem && player.isShiftKeyDown()) {
                for (ItemEntity drop : event.getDrops()) {
                    ItemStack copy = drop.getItem().copy();
                    player.getInventory().add(copy);
                }
                event.getDrops().clear();
            }
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ManaitaPlusUtils.isManaitaArmorPart(player))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ManaitaPlusUtils.isManaitaArmor(player))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ManaitaPlusUtils.isManaitaArmor(player)) {
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ManaitaPlusUtils.isManaitaArmor(player))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.WEAPONSMITH) {
            List<VillagerTrades.ItemListing> tradesTier = event.getTrades().get(1);

            tradesTier.add(new ManaitaPlusBowVillagerTrade(
                    new ItemStack(ManaitaPlusBlockCore.CraftingBlockItem.get(), 64),
                    new ItemStack(ManaitaPlusItemCore.ManaitaBow.get(), 1),
                    1, 10, 1));
            tradesTier.add(new ManaitaPlusBowVillagerTrade(
                    new ItemStack(ManaitaPlusBlockCore.FurnaceBlockItem.get(), 64),
                    new ItemStack(ManaitaPlusItemCore.ManaitaBow.get(), 1),
                    1, 10, 1));
            tradesTier.add(new ManaitaPlusBowVillagerTrade(
                    new ItemStack(ManaitaPlusBlockCore.BrewingBlock.get(), 64),
                    new ItemStack(ManaitaPlusItemCore.ManaitaBow.get(), 1),
                    1, 10, 1));
        }
    }



//
//    private static final Field field;
//
//    static {
//        try {
//            field = ClientLevel.class.getDeclaredField("blockStatePredictionHandler");
//            field.setAccessible(true);
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private void startPrediction(ClientLevel p_233730_, PredictiveAction p_233731_) {
//        try (BlockStatePredictionHandler blockstatepredictionhandler = ((BlockStatePredictionHandler)field.get(p_233730_)).startPredicting()) {
//            int i = blockstatepredictionhandler.currentSequence();
//            Packet<ServerGamePacketListener> packet = p_233731_.predict(i);
//            mc.getConnection().send(packet);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

}
