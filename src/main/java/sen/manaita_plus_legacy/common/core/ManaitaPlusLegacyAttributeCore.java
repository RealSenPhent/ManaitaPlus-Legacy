package sen.manaita_plus_legacy.common.core;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.RegistryObject;

import static sen.manaita_plus_legacy.ManaitaPlusLegacy.ATTRIBUTE_TYPE;

public class ManaitaPlusLegacyAttributeCore {
    public static final RegistryObject<Attribute> Type = ATTRIBUTE_TYPE.register("type", () -> (new RangedAttribute("entity.type", 0.0D, 0.0D, 13.0D)).setSyncable(true));


    public static void init() {
    }
}
