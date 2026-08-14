package sen.manaita_plus_legacy.common.core;

import moze_intel.projecte.PECore;
import moze_intel.projecte.gameObjs.EnumMatterType;
import moze_intel.projecte.gameObjs.items.tools.PEKatar;
import moze_intel.projecte.gameObjs.registration.impl.ItemDeferredRegister;
import moze_intel.projecte.gameObjs.registration.impl.ItemRegistryObject;
import moze_intel.projecte.gameObjs.registries.PEItems;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus_legacy.common.item.curio.BrewingCurio;
import sen.manaita_plus_legacy.common.item.curio.CraftingCurio;
import sen.manaita_plus_legacy.common.item.curio.FurnaceCurio;
import sen.manaita_plus_legacy.common.item.tool.ManaitaPlusLegacyKatarItem;

import static sen.manaita_plus_legacy.ManaitaPlusLegacy.ITEMS;

public class ManaitaPlusLegacyItemProjecteCore {
    public static final ItemRegistryObject<ManaitaPlusLegacyKatarItem> KATAR = PEItems.ITEMS.registerNoStackFireImmune("manaita_plus_legacy_katar", properties -> new ManaitaPlusLegacyKatarItem(64, properties));
//    public static final ItemRegistryObject<Item> Katar = ITEMS.register("manaita_plus_legacy_katar", ManaitaPlusLegacyKatarItem::new);
    public static void init() {}
}
