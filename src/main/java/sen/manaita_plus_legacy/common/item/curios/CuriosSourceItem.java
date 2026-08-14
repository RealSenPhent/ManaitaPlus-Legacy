package sen.manaita_plus_legacy.common.item.curios;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import sen.manaita_plus_legacy.client.network.implement.KeyPressPacket;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCore;
import sen.manaita_plus_legacy.common.item.ManaitaPlusLegacySourceItem;
import sen.manaita_plus_legacy.common.item.curio.BrewingCurio;
import sen.manaita_plus_legacy.common.item.curio.CraftingCurio;
import sen.manaita_plus_legacy.common.item.curio.FurnaceCurio;
import sen.manaita_plus_legacy.common.network.Networking;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;
import java.util.Optional;

public class CuriosSourceItem implements ICurioItem {
    public static void init() {
        CuriosApi.registerCurio(ManaitaPlusLegacyItemCore.ManaitaSource.get(),new CuriosSourceItem());
    }

    public static boolean inCurios(ServerPlayer serverPlayer) {
        LazyOptional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(Minecraft.getInstance().player);
        Optional<ICuriosItemHandler> resolve = curiosInventory.resolve();
        if (resolve.isEmpty()) return false;
        ICuriosItemHandler iCuriosItemHandler = resolve.get();
        for (ICurioStacksHandler value : iCuriosItemHandler.getCurios().values()) {
            IDynamicStackHandler stacks = value.getStacks();
            for (int i = 0; i < stacks.getSlots(); i++) {
                ItemStack stackInSlot = stacks.getStackInSlot(i);
                if (!stackInSlot.isEmpty()) {
                    if (stackInSlot.getItem() instanceof ManaitaPlusLegacySourceItem) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof ServerPlayer player) {
            Inventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack itemstack = inventory.getItem(i);
                if (itemstack.isDamageableItem()) {
                    itemstack.setDamageValue(0);
                }
            }
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {

    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}
