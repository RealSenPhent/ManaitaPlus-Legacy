package sen.manaita_plus.common.event;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sen.manaita_plus.ManaitaPlus;
import sen.manaita_plus.client.datagen.ManaitaPlusBlockStateProvider;
import sen.manaita_plus.client.datagen.ManaitaPlusItemModelProvider;
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
    public static void gatherData(net.minecraftforge.data.event.GatherDataEvent event) {  // 确保参数正确
        DataGenerator gen = event.getGenerator();


        gen.addProvider(event.includeServer(), new ManaitaPlusLootTable(gen.getPackOutput()));


        PackOutput packOutput = gen.getPackOutput();
        gen.addProvider(event.includeClient(), new ManaitaPlusBlockStateProvider(packOutput, event.getExistingFileHelper()));
        gen.addProvider(event.includeClient(), new ManaitaPlusItemModelProvider(packOutput, event.getExistingFileHelper()));
    }

}
