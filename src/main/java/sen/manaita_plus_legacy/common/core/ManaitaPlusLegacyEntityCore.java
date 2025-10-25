package sen.manaita_plus_legacy.common.core;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus_legacy.common.entity.ManaitaPlusLegacyEntityArrow;
import sen.manaita_plus_legacy.common.entity.ManaitaPlusLegacyLightningBolt;

import static sen.manaita_plus_legacy.ManaitaPlusLegacy.ENTITY_TYPES;

public class ManaitaPlusLegacyEntityCore {
    public static final RegistryObject<EntityType<ManaitaPlusLegacyLightningBolt>> ManaitaLightningBolt = ENTITY_TYPES.register("manaita_lightning_bolt",() -> EntityType.Builder.of(ManaitaPlusLegacyLightningBolt::new, MobCategory.MISC).noSave().sized(0.0F, 0.0F).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE).build("manaita_lightning_bolt"));
    public static final RegistryObject<EntityType<ManaitaPlusLegacyEntityArrow>> ManaitaArrow = ENTITY_TYPES.register("manaita_arrow",() -> EntityType.Builder.of(ManaitaPlusLegacyEntityArrow::new, MobCategory.MISC).noSave().sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("manaita_arrow"));

    public static void init() {}
}
