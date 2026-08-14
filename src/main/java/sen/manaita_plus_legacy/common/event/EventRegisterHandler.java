package sen.manaita_plus_legacy.common.event;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.client.datagen.ManaitaPlusBlockStateProvider;
import sen.manaita_plus_legacy.client.datagen.ManaitaPlusItemModelProvider;
import sen.manaita_plus_legacy.client.datagen.ManaitaPlusItemRecipeProvider;
import sen.manaita_plus_legacy.common.item.curios.CuriosSourceItem;
import sen.manaita_plus_legacy.common.network.Networking;
import sen.manaita_plus_legacy.common.proxy.CommomProxy;
import sen.manaita_plus_legacy.common.recipe.ingredient.ManaitaPlusLegacyNBTIngredient;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ManaitaPlusLegacy.MODID)
public class EventRegisterHandler {

    @SubscribeEvent
    public static void onFMLCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Networking.registerMessage();
            if (CommomProxy.curios) {
                CuriosSourceItem.init();
            }
        });
    }
    @SubscribeEvent
    public static void onRegisters(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            CraftingHelper.register(ManaitaPlusLegacy.rl("nbt"), ManaitaPlusLegacyNBTIngredient.Serializer.INSTANCE);
        }
    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
//        event.getTypes().forEach(entityType -> event.add(entityType, ManaitaPlusAttributeCore.Type.get()));
    }


    @SubscribeEvent
    public static void onGatherData(net.minecraftforge.data.event.GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        PackOutput packOutput = gen.getPackOutput();
//        gen.addProvider(event.includeClient(), new ManaitaPlusBlockStateProvider(packOutput, event.getExistingFileHelper()));
        gen.addProvider(event.includeClient(), new ManaitaPlusItemModelProvider(packOutput, event.getExistingFileHelper()));
        gen.addProvider(event.includeClient(), new ManaitaPlusItemRecipeProvider(packOutput));
    }

}
