package sen.manaita_plus_legacy.common.curios;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import sen.manaita_plus_legacy.client.network.implement.KeyPressPacket;
import sen.manaita_plus_legacy.common.item.curio.BrewingCurio;
import sen.manaita_plus_legacy.common.item.curio.CraftingCurio;
import sen.manaita_plus_legacy.common.item.curio.FurnaceCurio;
import sen.manaita_plus_legacy.common.network.Networking;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CuriosUtil {
    public static void onKeyPress() {
        LazyOptional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(Minecraft.getInstance().player);
        curiosInventory.ifPresent(inventory -> {
            Map<String, ICurioStacksHandler> curios = inventory.getCurios();
            ICurioStacksHandler ring = curios.get("ring");
            IDynamicStackHandler stacks = ring.getStacks();
            for (int i = 0; i < stacks.getSlots(); i++) {
                ItemStack stackInSlot = stacks.getStackInSlot(i);
                if (!stackInSlot.isEmpty()) {
                    if (stackInSlot.getItem() instanceof CraftingCurio) {
                        Networking.sendToServer(new KeyPressPacket((byte) 5));
                    }  else if (stackInSlot.getItem() instanceof FurnaceCurio) {
                        Networking.sendToServer(new KeyPressPacket((byte) 6));
                    } else if (stackInSlot.getItem() instanceof BrewingCurio) {
                        Networking.sendToServer(new KeyPressPacket((byte) 7));
                    }
                }
            }
        });
    }
}
