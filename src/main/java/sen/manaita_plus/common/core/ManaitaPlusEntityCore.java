package sen.manaita_plus.common.core;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import sen.manaita_plus.common.entity.ManaitaPlusEntityArrow;
import sen.manaita_plus.common.entity.ManaitaPlusLightningBolt;
import sen.manaita_plus.common.entity.ManaitaPlusLightningPlusBolt;

import static sen.manaita_plus.ManaitaPlus.ENTITY_TYPES;

public class ManaitaPlusEntityCore {
    public static final RegistryObject<EntityType<ManaitaPlusLightningBolt>> ManaitaLightningBolt = ENTITY_TYPES.register("manaita_lightning_bolt",() -> EntityType.Builder.of(ManaitaPlusLightningBolt::new, MobCategory.MISC).noSave().sized(0.0F, 0.0F).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE).build("manaita_lightning_bolt"));
    public static final RegistryObject<EntityType<ManaitaPlusLightningPlusBolt>> ManaitaLightningBoltPlus = ENTITY_TYPES.register("manaita_lightning_bolt_plus",() -> EntityType.Builder.of(ManaitaPlusLightningPlusBolt::new, MobCategory.MISC).noSave().sized(0.0F, 0.0F).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE).build("manaita_lightning_bolt"));
    public static final RegistryObject<EntityType<ManaitaPlusEntityArrow>> ManaitaArrow = ENTITY_TYPES.register("manaita_arrow",() -> EntityType.Builder.of(ManaitaPlusEntityArrow::new, MobCategory.MISC).noSave().sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("manaita_arrow"));

    public static void init() {}
}
