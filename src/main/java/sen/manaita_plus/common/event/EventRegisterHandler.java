package sen.manaita_plus.common.event;

import net.minecraft.data.DataProvider;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sen.manaita_plus.ManaitaPlus;
import sen.manaita_plus.common.loottable.ManaitaPlusLootTable;

import static sen.manaita_plus.common.core.ManaitaPlusKeyBoardInputCore.MESSAGE_ARMOR_KEY;
import static sen.manaita_plus.common.core.ManaitaPlusKeyBoardInputCore.MESSAGE_KEY;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ManaitaPlus.MODID)
public class EventRegisterHandler {
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(MESSAGE_KEY);
        event.register(MESSAGE_ARMOR_KEY);
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                // Tell generator to run only when server data are generating
                event.includeServer(),
                (DataProvider.Factory<DataProvider>) output ->  new ManaitaPlusLootTable()
        );
    }

}
