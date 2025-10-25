package sen.manaita_plus_legacy.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = ManaitaPlusLegacy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ManaitaPlusLegacyConfig
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue drops_doubling = BUILDER
            .comment("DropsDoubling")
            .defineInRange("drops_doubling_value", 4, 1, Integer.MAX_VALUE);
    
    private static final ForgeConfigSpec.IntValue crafting_doubling = BUILDER
            .comment("CraftingDoubling")
            .defineInRange("crafting_doubling_value", 64, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue furnace_doubling = BUILDER
            .comment("FurnaceDoubling")
            .defineInRange("furnace_doubling_value", 64, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue brewing_doubling = BUILDER
            .comment("BrewingDoubling")
            .defineInRange("brewing_doubling_value", 64, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue destroy_doubling = BUILDER
            .comment("DestroyDoubling")
            .defineInRange("destroy_doubling_value", 4, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue source_doubling = BUILDER
            .comment("SourceDoubling")
            .defineInRange("source_doubling_value", 64, 1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int drops_doubling_value;
    public static int crafting_doubling_value;
    public static int furnace_doubling_value;
    public static int brewing_doubling_value;
    public static int destroy_doubling_value;
    public static int source_doubling_value;
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        drops_doubling_value = drops_doubling.get();
        crafting_doubling_value = crafting_doubling.get();
        furnace_doubling_value = furnace_doubling.get();
        brewing_doubling_value = brewing_doubling.get();
        destroy_doubling_value = destroy_doubling.get();
        source_doubling_value = source_doubling.get();
    }
}
