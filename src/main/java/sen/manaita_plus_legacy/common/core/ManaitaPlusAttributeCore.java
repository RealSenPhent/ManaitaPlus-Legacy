package sen.manaita_plus_legacy.common.core;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;

import static sen.manaita_plus_legacy.ManaitaPlus.ATTRIBUTE_TYPE;

public class ManaitaPlusAttributeCore {
    public static final RegistryObject<Attribute> Type = ATTRIBUTE_TYPE.register("type", () -> (new RangedAttribute("entity.type", 0.0D, 0.0D, 13.0D)).setSyncable(true));

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> event.add(entityType, Type.get()));
    }

    public static void init() {
    }
}
