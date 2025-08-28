package sen.manaita_plus;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
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
import sen.manaita_plus.client.gui.BrewingStandScreen;
import sen.manaita_plus.client.gui.CraftingManaitaScreen;
import sen.manaita_plus.client.gui.FurnaceManaitaScreen;
import sen.manaita_plus.common.core.*;
import sen.manaita_plus.common.network.Networking;

import static sen.manaita_plus.common.core.ManaitaPlusItemCore.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ManaitaPlus.MODID)
public class ManaitaPlus {
    public static final String MODID = "manaita_plus";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);
    public static final DeferredRegister<StatType<?>> STAT_TYPES = DeferredRegister.create(ForgeRegistries.STAT_TYPES, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_DEFERRED_REGISTER =  DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> MANAITA_PLUS_TAB = CREATIVE_MODE_TABS.register("manaita_plus_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ManaitaPlusBlockCore.CraftingBlockItem.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.ManaitaPlusTab"))
            .displayItems((parameters, output) -> {
                acceptA(ManaitaPlusBlockCore.CraftingBlockItem.get(), output,8);
                acceptA(ManaitaPlusBlockCore.FurnaceBlockItem.get(), output,8);
                acceptA(ManaitaPlusBlockCore.BrewingBlockItem.get(), output,8);

                output.accept(ManaitaWoodenHook.get());
                acceptA(ManaitaPlusBlockCore.HookBlockItem.get(), output,7);

                acceptA(ManaitaCraftingPortable.get(), output,8);
                acceptA(ManaitaFurnacePortable.get(), output,8);
                acceptA(ManaitaBrewingPortable.get(), output,8);

                output.accept(ManaitaSwordGod.get());
                output.accept(ManaitaSword.get());
                output.accept(ManaitaAxe.get());
                output.accept(ManaitaHoe.get());
                output.accept(ManaitaPaxel.get());
                output.accept(ManaitaPickaxe.get());
                output.accept(ManaitaShears.get());
                output.accept(ManaitaShovel.get());
                output.accept(ManaitaBow.get());
                output.accept(ManaitaHelmet.get());
                output.accept(ManaitaChestplate.get());
                output.accept(ManaitaLeggings.get());
                output.accept(ManaitaBoots.get());
                output.accept(ManaitaSource.get());
            }).build());

    public static void acceptA(Item item, CreativeModeTab.Output output,int manaitaType) {
        for (int i = 0; i <= manaitaType; i++) {
            ItemStack itemStack = new ItemStack(item);
            itemStack.setTag(new CompoundTag());
            itemStack.getTag().putInt("ManaitaType",i);
            output.accept(itemStack);
        }
    }


    public ManaitaPlus() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);


        ManaitaPlusItemCore.init();
        ManaitaPlusBlockCore.init();
        ManaitaPlusStarCore.init();
        ManaitaPlusMenuCore.init();
        ManaitaPlusEntityCore.init();
        ManaitaPlusBlockEntityCore.init();
        ManaitaPlusRecipeSerializerCore.init();
        ManaitaPlusKeyBoardInputCore.init();

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        RECIPE_SERIALIZER_DEFERRED_REGISTER.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Networking.registerMessage();
            MenuScreens.register(ManaitaPlusMenuCore.CraftingManaita.get(), CraftingManaitaScreen::new);
            MenuScreens.register(ManaitaPlusMenuCore.FurnaceManaita.get(), FurnaceManaitaScreen::new);
            MenuScreens.register(ManaitaPlusMenuCore.BrewingStandManaita.get(), BrewingStandScreen::new);

            ItemProperties.register(ManaitaPlusBlockCore.CraftingBlockItem.get(),
                    new ResourceLocation(ManaitaPlus.MODID, "manaita_type"),
                    (stack, level, entity, seed) -> stack.getOrCreateTag().getInt("ManaitaType"));
            ItemProperties.register(ManaitaPlusBlockCore.FurnaceBlockItem.get(),
                    new ResourceLocation(ManaitaPlus.MODID, "manaita_type"),
                    (stack, level, entity, seed) -> stack.getOrCreateTag().getInt("ManaitaType"));
            ItemProperties.register(ManaitaPlusBlockCore.BrewingBlockItem.get(),
                    new ResourceLocation(ManaitaPlus.MODID, "manaita_type"),
                    (stack, level, entity, seed) -> stack.getOrCreateTag().getInt("ManaitaType"));

            ItemProperties.register(ManaitaPlusBlockCore.HookBlockItem.get(),
                    new ResourceLocation(ManaitaPlus.MODID, "manaita_type"),
                    (stack, level, entity, seed) -> stack.getOrCreateTag().getInt("ManaitaType"));

            ItemProperties.register(ManaitaCraftingPortable.get(),
                    new ResourceLocation(ManaitaPlus.MODID, "manaita_type"),
                    (stack, level, entity, seed) -> stack.getOrCreateTag().getInt("ManaitaType"));
            ItemProperties.register(ManaitaFurnacePortable.get(),
                    new ResourceLocation(ManaitaPlus.MODID, "manaita_type"),
                    (stack, level, entity, seed) -> stack.getOrCreateTag().getInt("ManaitaType"));
            ItemProperties.register(ManaitaBrewingPortable.get(),
                    new ResourceLocation(ManaitaPlus.MODID, "manaita_type"),
                    (stack, level, entity, seed) -> stack.getOrCreateTag().getInt("ManaitaType"));
          });
    }


}
