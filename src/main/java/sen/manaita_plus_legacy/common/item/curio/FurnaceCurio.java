package sen.manaita_plus_legacy.common.item.curio;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.common.util.tag.ManaitaPlusLegacyTagData;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class FurnaceCurio extends Item implements ICurioItem {
    public FurnaceCurio() {
        super(new Properties().defaultDurability(-1).fireResistant().stacksTo(1));
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.translatable("item.ring_furnace_manaita." + p_41458_.getOrCreateTag().getInt(ManaitaPlusLegacyTagData.ItemType) + ".name");
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
}
