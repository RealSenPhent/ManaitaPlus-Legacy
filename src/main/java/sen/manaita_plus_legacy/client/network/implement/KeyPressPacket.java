package sen.manaita_plus_legacy.client.network.implement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import sen.manaita_plus_legacy.common.curios.CuriosUtil;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.common.item.portabl.ManaitaPlusLegacyBrewingPortabl;
import sen.manaita_plus_legacy.common.item.portabl.ManaitaPlusLegacyFurnacePortabl;
import sen.manaita_plus_legacy.common.menu.ManaitaPlusLegacyCraftingMenu;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;

import javax.annotation.Nullable;
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
                        ManaitaPlusUtils.godKill(0, true, sender.isShiftKeyDown(), sender);
                    }
                    break;
                case 5:
                    if (ModList.get().isLoaded("curios")) {
                        if (CuriosUtil.getCurios() != null) {
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
                    if (ModList.get().isLoaded("curios")) {
                        ItemStack curios = CuriosUtil.getCurios();
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
                    if (ModList.get().isLoaded("curios")) {
                        ItemStack curios = CuriosUtil.getCurios();
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
}
