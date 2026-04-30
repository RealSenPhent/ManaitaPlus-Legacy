package sen.manaita_plus_legacy.client.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;

@Mod.EventBusSubscriber(modid = ManaitaPlusLegacy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ManaitaPlusLegacyConfig
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();


    public static final ForgeConfigSpec SPEC = BUILDER.build();

}
