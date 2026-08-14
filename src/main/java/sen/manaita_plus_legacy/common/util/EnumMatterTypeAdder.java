package sen.manaita_plus_legacy.common.util;

import moze_intel.projecte.gameObjs.EnumMatterType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import sen.manaita_plus_legacy_core.util.Helper;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

public final class EnumMatterTypeAdder {
    /**
     * 反射添加一个新的 EnumMatterType 枚举常量。
     *
     * @param constantName  枚举常量名，例如 "BLUE_MATTER"
     * @param serializedName getSerializedName() 返回的名字，例如 "blue_matter"
     * @param attackDamage  攻击伤害加成
     * @param efficiency    挖掘效率
     * @param chargeModifier 充能倍率
     * @param harvestLevel  挖掘等级
     * @param neededTag     需要的方块 Tag，可为 null
     * @param previous      前一个 Tier，例如 EnumMatterType.RED_MATTER
     * @param next          后一个 Tier 的 ResourceLocation，可为 null
     * @param mapColor      地图颜色
     */
    public static EnumMatterType addMatterType(
            String constantName,
            String serializedName,
            float attackDamage,
            float efficiency,
            float chargeModifier,
            int harvestLevel,
            TagKey<Block> neededTag,
            Tier previous,
            ResourceLocation next,
            MapColor mapColor
    ) {

        try {
            Constructor<EnumMatterType> ctor = EnumMatterType.class.getDeclaredConstructor(
                    String.class, int.class,          // 隐式：枚举常量名、ordinal
                    String.class,                     // 源码构造器第一个参数：serializedName
                    float.class, float.class, float.class,
                    int.class,
                    TagKey.class, Tier.class, ResourceLocation.class, MapColor.class
            );
            ctor.setAccessible(true);

            // 2. 新常量的 ordinal 应该是当前 values 的长度。
            int newOrdinal = EnumMatterType.values().length;

            EnumMatterType newMatter = ctor.newInstance(
                    constantName,
                    newOrdinal,
                    serializedName,
                    attackDamage,
                    efficiency,
                    chargeModifier,
                    harvestLevel,
                    neededTag,
                    previous,
                    next,
                    mapColor
            );
            Field valuesField = EnumMatterType.class.getDeclaredField("$VALUES");

            Object base = Helper.UNSAFE.staticFieldBase(valuesField);
            long offset = Helper.UNSAFE.staticFieldOffset(valuesField);

            EnumMatterType[] oldValues = (EnumMatterType[]) Helper.UNSAFE.getObject(base, offset);
            EnumMatterType[] newValues = Arrays.copyOf(oldValues, oldValues.length + 1);
            newValues[oldValues.length] = newMatter;

            Helper.UNSAFE.putObject(base, offset, newValues);

            // 5. 清空 Class 中的枚举缓存，否则 valueOf() / getEnumConstants() 可能拿到旧值。
            clearEnumCaches(EnumMatterType.class);
            return newMatter;
        } catch (Throwable e) {
            return EnumMatterType.RED_MATTER;
        }
    }

    private static void clearEnumCaches(Class<?> enumClass) throws Throwable {
        // Class.enumConstantDirectory：valueOf() 会使用
        Field dirField = Class.class.getDeclaredField("enumConstantDirectory");
        Object dir = Helper.UNSAFE.getObject(enumClass, Helper.UNSAFE.objectFieldOffset(dirField));
        if (dir instanceof Map<?, ?> map) {
            map.clear();
        }

        // Class.enumConstants：getEnumConstantsShared() 会使用
        Field constantsField = Class.class.getDeclaredField("enumConstants");
        Helper.UNSAFE.putObject(enumClass, Helper.UNSAFE.objectFieldOffset(constantsField), null);
    }
}