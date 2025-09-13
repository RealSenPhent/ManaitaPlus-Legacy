package sen.manaita_plus_legacy.common.item;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.common.util.ManaitaPlusText;

import java.util.List;

public class ManaitaPlusSourceItem extends Item {
    public ManaitaPlusSourceItem() {
        super(new Properties().defaultDurability(-1).fireResistant().stacksTo(64));
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("item.source.name")));
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_infinity.formatting(I18n.get("info.source.1"))));
    }

    @OnlyIn(net.minecraftforge.api.distmarker.Dist.CLIENT)
    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
//        if (p_41433_ instanceof ServerPlayer serverPlayer)
//        NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
//            @Override
//            public Component getDisplayName() {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
//                return new CraftingManaitaMenu(p_39954_, p_39955_, ContainerLevelAccess.NULL);
//            }
//        });
//        return super.use(p_41432_, p_41433_, p_41434_);
//    }
}
