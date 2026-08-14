package sen.manaita_plus_legacy.client.network.implement;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import sen.manaita_plus_legacy.common.curios.CuriosUtil;
import sen.manaita_plus_legacy.common.item.curio.BrewingCurio;
import sen.manaita_plus_legacy.common.item.curio.CraftingCurio;
import sen.manaita_plus_legacy.common.item.curio.FurnaceCurio;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.common.item.portabl.ManaitaPlusLegacyBrewingPortabl;
import sen.manaita_plus_legacy.common.item.portabl.ManaitaPlusLegacyFurnacePortabl;
import sen.manaita_plus_legacy.common.menu.ManaitaPlusLegacyCraftingMenu;
import sen.manaita_plus_legacy.common.proxy.CommomProxy;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class KeyPressPacket {
    private final byte key;


    public KeyPressPacket(FriendlyByteBuf buffer) {
        key = buffer.readByte();
    }


    public KeyPressPacket(byte key) {
        this.key = key;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeByte(key);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) return;
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;
            switch (key) {
                case 0:
                case 2:
                case 3:
                    ItemStack mainHandItem = sender.getMainHandItem();
                    if (!mainHandItem.isEmpty() && mainHandItem.getItem() instanceof IManaitaPlusLegacyKey key) {
                        key.onManaitaKeyPress(mainHandItem, sender, this.key);
                    }
                    break;
                case 1:
                    for (ItemStack itemStack : sender.getInventory().armor) {
                        if (!itemStack.isEmpty() && itemStack.getItem() instanceof IManaitaPlusLegacyKey key) {
                            key.onManaitaKeyPress(itemStack, sender, this.key);
                        }
                    }
                    break;
                case 4:
                    if (ManaitaPlusLegacyEntityData.manaita.accept(sender)) {
                        ManaitaPlusUtils.godKill(0, 3, sender.isShiftKeyDown(), sender);
                    }
                    break;
                case 5:
                    if (CommomProxy.curios) {
                        if (getCurios(sender) != null) {
                            NetworkHooks.openScreen(sender, new MenuProvider() {
                                @Override
                                public Component getDisplayName() {
                                    return Component.translatable("container.crafting_manaita");
                                }

                                @Nullable
                                @Override
                                public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
                                    return new ManaitaPlusLegacyCraftingMenu(p_39954_, p_39955_, p_39956_.level());
                                }
                            });
                        }
                    }
                    break;
                case 6:
                    if (CommomProxy.curios) {
                        ItemStack curios = getCurios(sender);
                        if (curios != null) {
                            NetworkHooks.openScreen(sender, new MenuProvider() {
                                @Override
                                public Component getDisplayName() {
                                    return Component.translatable("container.furnace_manaita");
                                }

                                @Nullable
                                @Override
                                public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
                                    ManaitaPlusLegacyFurnacePortabl.ManaitaPlusFurnaceBlockEntity block = new ManaitaPlusLegacyFurnacePortabl.ManaitaPlusFurnaceBlockEntity(p_39956_, curios);
                                    return block.createMenu(p_39954_, p_39955_, p_39956_);
                                }
                            });
                        }
                    }
                    break;
                case 7:
                    if (CommomProxy.curios) {
                        ItemStack curios = getCurios(sender);
                        if (curios != null) {
                            NetworkHooks.openScreen(sender, new MenuProvider() {
                                @Override
                                public Component getDisplayName() {
                                    return Component.translatable("container.brewing_manaita");
                                }

                                @Nullable
                                @Override
                                public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
                                    ManaitaPlusLegacyBrewingPortabl.ManaitaPlusBrewingStandBlockEntity blockEntity = new ManaitaPlusLegacyBrewingPortabl.ManaitaPlusBrewingStandBlockEntity(p_39955_.player, curios);
                                    return blockEntity.createMenu(p_39954_, p_39955_, p_39956_);
                                }
                            });
                        }
                    }
                    break;
            }
        });
        ctx.get().setPacketHandled(true);
    }


    public static ItemStack getCurios(Player player) {
        LazyOptional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);
        AtomicReference<ItemStack> stack = new AtomicReference<>();
        curiosInventory.ifPresent(inventory -> {
            Map<String, ICurioStacksHandler> curios = inventory.getCurios();
            ICurioStacksHandler ring = curios.get("ring");
            IDynamicStackHandler stacks = ring.getStacks();
            for (int i = 0; i < stacks.getSlots(); i++) {
                stack.set(stacks.getStackInSlot(i));
                if (stack.get() != null && !stack.get().isEmpty() && (stack.get().getItem() instanceof CraftingCurio || stack.get().getItem() instanceof FurnaceCurio || stack.get().getItem() instanceof BrewingCurio)) {
                    return;
                }
            }
        });
        return stack.get();
    }
}
