package sen.manaita_plus_legacy.common.util.tag;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ManaitaPlusLegacyTagData {
    public static final String ItemType = "ManaitaPlusLegacyType";
    public static final String Type = "manaita_plus_legacy_type";
    public static final String Range = "Range";
    public static final String Mode = "Mode";
    public static final String Speed = "Speed";
    public static final String Invisibility = "Invisibility";
    public static final String NightVision = "NightVision";
    public static final String Sweep = "Sweep";
    public static final String DPos = "DPosManaitaPlusLegacy";
    public static final String DTime = "DTimeManaitaPlusLegacy";
    public static final int doubling = (1 << 0);
    public static final int pickItems = (1 << 2);
    public static final int pickExperiences = (1 << 3);
    public static final int antiDisarming = (1 << 4);
    public static final int farDestroyDelay = (1 << 5);

    public static void setMode(ItemStack itemStack, int mode, boolean state) {
        CompoundTag orCreateTag = itemStack.getOrCreateTag();
        int anInt = orCreateTag.getInt(ManaitaPlusLegacyTagData.Type);
        orCreateTag.putInt(ManaitaPlusLegacyTagData.Type, state ? anInt & mode : anInt & ~mode);
    }

    public static void setMode(ItemStack itemStack,int type) {
        itemStack.getOrCreateTag().putInt(ManaitaPlusLegacyTagData.Type, type);
    }

    public static boolean toggleMode(ItemStack itemStack, int mode) {
        CompoundTag orCreateTag = itemStack.getOrCreateTag();
        int anInt = orCreateTag.getInt(ManaitaPlusLegacyTagData.Type);
        boolean state = (anInt & mode) == 0;
        orCreateTag.putInt(ManaitaPlusLegacyTagData.Type, state ? anInt | mode : anInt & ~mode);
        return state;
    }
}
