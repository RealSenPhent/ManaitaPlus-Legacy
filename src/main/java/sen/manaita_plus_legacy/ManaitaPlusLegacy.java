package sen.manaita_plus_legacy;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import sen.manaita_plus_legacy.client.gui.BrewingStandScreen;
import sen.manaita_plus_legacy.client.gui.CraftingManaitaScreen;
import sen.manaita_plus_legacy.client.gui.FurnaceManaitaScreen;
import sen.manaita_plus_legacy.common.config.ManaitaPlusLegacyConfig;
import sen.manaita_plus_legacy.common.core.*;
import sen.manaita_plus_legacy.common.network.Networking;
import sen.manaita_plus_legacy.common.util.ManaitaPlusLegacyNBTData;

import static sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCore.*;

@Mod(ManaitaPlusLegacy.MODID)
public class ManaitaPlusLegacy {
    public static final String MODID = "manaita_plus_legacy";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);
    public static final DeferredRegister<Attribute> ATTRIBUTE_TYPE = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MODID);
    public static final DeferredRegister<RecipeType<?>> REGISTER_TYPES =  DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_DEFERRED_REGISTER =  DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB_TYPES = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> MANAITA_PLUS_TAB = CREATIVE_MODE_TAB_TYPES.register("manaita_plus_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ManaitaPlusLegacyBlockCore.CraftingBlockItem.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.ManaitaPlusTab"))
            .displayItems((parameters, output) -> {
                acceptManaitaPlusLegacyType(ManaitaPlusLegacyBlockCore.CraftingBlockItem.get(), output,8);
                acceptManaitaPlusLegacyType(ManaitaPlusLegacyBlockCore.FurnaceBlockItem.get(), output,8);
                acceptManaitaPlusLegacyType(ManaitaPlusLegacyBlockCore.BrewingBlockItem.get(), output,8);

                output.accept(ManaitaWoodenHook.get());
                acceptManaitaPlusLegacyType(ManaitaPlusLegacyBlockCore.HookBlockItem.get(), output,7);

                acceptManaitaPlusLegacyType(ManaitaCraftingPortable.get(), output,8);
                acceptManaitaPlusLegacyType(ManaitaFurnacePortable.get(), output,8);
                acceptManaitaPlusLegacyType(ManaitaBrewingPortable.get(), output,8);

                output.accept(ManaitaSwordGod.get());
                output.accept(ManaitaSword.get());
                output.accept(ManaitaBow.get());
                output.accept(ManaitaShovel.get());
                output.accept(ManaitaPickaxe.get());
                output.accept(ManaitaAxe.get());
                output.accept(ManaitaPaxel.get());
                output.accept(ManaitaHoe.get());
                output.accept(ManaitaShears.get());

                output.accept(ManaitaHelmet.get());
                output.accept(ManaitaChestplate.get());
                output.accept(ManaitaLeggings.get());
                output.accept(ManaitaBoots.get());
                output.accept(ManaitaSource.get());
            }).build());

    public static void acceptManaitaPlusLegacyType(Item item, CreativeModeTab.Output output, int manaitaType) {
        for (int i = 0; i <= manaitaType; i++) {
            ItemStack itemStack = new ItemStack(item);
            itemStack.setTag(new CompoundTag());
            assert itemStack.getTag() != null;
            itemStack.getTag().putInt(ManaitaPlusLegacyNBTData.ItemType,i);
            output.accept(itemStack);
        }
    }


    public ManaitaPlusLegacy() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onFMLCommonSetup);

        ManaitaPlusLegacyAttributeCore.init();
        ManaitaPlusLegacyItemCore.init();
        ManaitaPlusLegacyBlockCore.init();
        ManaitaPlusLegacyMenuCore.init();
        ManaitaPlusLegacyEntityCore.init();
        ManaitaPlusLegacyBlockEntityCore.init();
        ManaitaPlusLegacyRecipeSerializerCore.init();
        ManaitaPlusLegacySynchedDataCore.init();

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ATTRIBUTE_TYPE.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TAB_TYPES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        RECIPE_SERIALIZER_DEFERRED_REGISTER.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ManaitaPlusLegacyConfig.SPEC);
    }



    private void onFMLCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Networking.registerMessage();

            MenuScreens.register(ManaitaPlusLegacyMenuCore.CraftingManaita.get(), CraftingManaitaScreen::new);
            MenuScreens.register(ManaitaPlusLegacyMenuCore.FurnaceManaita.get(), FurnaceManaitaScreen::new);
            MenuScreens.register(ManaitaPlusLegacyMenuCore.BrewingStandManaita.get(), BrewingStandScreen::new);

            acceptTypePropertyFunction(
                    ManaitaPlusLegacyBlockCore.CraftingBlockItem.get(),
                    ManaitaPlusLegacyBlockCore.FurnaceBlockItem.get(),
                    ManaitaPlusLegacyBlockCore.BrewingBlockItem.get(),
                    ManaitaPlusLegacyBlockCore.HookBlockItem.get(),
                    ManaitaPlusLegacyItemCore.ManaitaCraftingPortable.get(),
                    ManaitaPlusLegacyItemCore.ManaitaFurnacePortable.get(),
                    ManaitaPlusLegacyItemCore.ManaitaBrewingPortable.get()
            );
        });
    }

    public static void acceptTypePropertyFunction(Item... items) {
        ResourceLocation location = new ResourceLocation(ManaitaPlusLegacy.MODID, "manaita_plus_legacy_type");
        ItemPropertyFunction typePropertyFunction = (stack, level, entity, seed) -> {
            if (stack.getTag() != null) {
                return stack.hasTag() ? stack.getTag().getInt(ManaitaPlusLegacyNBTData.ItemType) : 0;
            }
            return 0;
        };
        for (Item item : items) {
            ItemProperties.register(item,location,typePropertyFunction);
        }
    }


}
