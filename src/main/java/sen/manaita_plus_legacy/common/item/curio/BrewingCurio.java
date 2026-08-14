package sen.manaita_plus_legacy.common.item.curio;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.common.item.portabl.ManaitaPlusLegacyBrewingPortabl;
import sen.manaita_plus_legacy.common.util.tag.ManaitaPlusLegacyTagData;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class BrewingCurio extends Item implements ICurioItem {
    public BrewingCurio() {
        super(new Properties().defaultDurability(-1).fireResistant().stacksTo(1));
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.translatable("item.ring_brewing_manaita." + p_41458_.getOrCreateTag().getInt(ManaitaPlusLegacyTagData.ItemType) + ".name");
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return CuriosApi.createCurioProvider(new ICurio() {

            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public void curioTick(SlotContext slotContext) {

            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        if (p_41433_ instanceof ServerPlayer serverPlayer) {
            ItemStack itemInHand = p_41433_.getItemInHand(p_41434_);
            NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("container.brewing_manaita");
                }

                @javax.annotation.Nullable
                @Override
                public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
                    ManaitaPlusLegacyBrewingPortabl.ManaitaPlusBrewingStandBlockEntity blockEntity = new ManaitaPlusLegacyBrewingPortabl.ManaitaPlusBrewingStandBlockEntity(p_39955_.player, itemInHand);
                    return blockEntity.createMenu(p_39954_, p_39955_, p_39956_);
                }
            });
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }

}

