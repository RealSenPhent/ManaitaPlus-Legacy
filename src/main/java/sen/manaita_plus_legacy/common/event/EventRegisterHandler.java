package sen.manaita_plus_legacy.common.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.common.recipe.ingredient.ManaitaPlusLegacyNBTIngredient;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ManaitaPlusLegacy.MODID)
public class EventRegisterHandler {
    @SubscribeEvent
    public static void onRegisters(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            CraftingHelper.register(new ResourceLocation("manaita_plus_legacy", "nbt"), ManaitaPlusLegacyNBTIngredient.Serializer.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
//        event.getTypes().forEach(entityType -> event.add(entityType, ManaitaPlusAttributeCore.Type.get()));
    }


//    @SubscribeEvent
//    public static void onGatherData(net.minecraftforge.data.event.GatherDataEvent event) {
//        DataGenerator gen = event.getGenerator();
//
//        gen.addProvider(event.includeServer(), new ManaitaPlusLootTable(gen.getPackOutput()));
//
//        PackOutput packOutput = gen.getPackOutput();
//        gen.addProvider(event.includeClient(), new ManaitaPlusBlockStateProvider(packOutput, event.getExistingFileHelper()));
//        gen.addProvider(event.includeClient(), new ManaitaPlusItemModelProvider(packOutput, event.getExistingFileHelper()));
//    }

}
