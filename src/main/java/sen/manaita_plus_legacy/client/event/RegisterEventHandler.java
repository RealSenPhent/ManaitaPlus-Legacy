package sen.manaita_plus_legacy.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.client.gui.BrewingStandScreen;
import sen.manaita_plus_legacy.client.gui.CraftingManaitaScreen;
import sen.manaita_plus_legacy.client.gui.FurnaceManaitaScreen;
import sen.manaita_plus_legacy.client.render.block.RenderBrewingManaitaBlockEntity;
import sen.manaita_plus_legacy.client.render.block.RenderCraftingManaitaBlockEntity;
import sen.manaita_plus_legacy.client.render.block.RenderFurnaceManaitaBlockEntity;
import sen.manaita_plus_legacy.client.render.entity.ManaitaPlusLightningBoltRenderer;
import sen.manaita_plus_legacy.client.render.entity.RenderManaitaArrow;
import sen.manaita_plus_legacy.client.shander.impl.cosmic.CosmicShaderEventHandler;
import sen.manaita_plus_legacy.client.shander.impl.galaxy.GalaxyShaderHandle;
import sen.manaita_plus_legacy.client.shander.impl.item.ItemShaderHandle;
import sen.manaita_plus_legacy.common.core.*;
import sen.manaita_plus_legacy.client.core.ManaitaPlusLegacyKeyBoardCore;

import static sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyEntityCore.ManaitaArrow;
import static sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyEntityCore.ManaitaLightningBolt;

import net.minecraftforge.client.event.RegisterShadersEvent;
import sen.manaita_plus_legacy.common.proxy.CommomProxy;
import sen.manaita_plus_legacy.common.util.tag.ManaitaPlusLegacyTagData;

import java.nio.file.Path;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ManaitaPlusLegacy.MODID, value = Dist.CLIENT)
public class RegisterEventHandler {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ManaitaLightningBolt.get(), ManaitaPlusLightningBoltRenderer::new);
        event.registerEntityRenderer(ManaitaArrow.get(), RenderManaitaArrow::new);

        BlockEntityRenderers.register(ManaitaPlusLegacyBlockEntityCore.FURNACE_BLOCK_ENTITY.get(), RenderFurnaceManaitaBlockEntity::new);
        BlockEntityRenderers.register(ManaitaPlusLegacyBlockEntityCore.BREWING_BLOCK_ENTITY.get(), RenderBrewingManaitaBlockEntity::new);
        BlockEntityRenderers.register(ManaitaPlusLegacyBlockEntityCore.CRAFTING_BLOCK_ENTITY.get(), RenderCraftingManaitaBlockEntity::new);
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event)
    {
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_KEY = new KeyMapping("key.manaita", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 88, ManaitaPlusLegacyKeyBoardCore.CATEGORY);
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_ARMOR_KEY = new KeyMapping("key.manaita.armor", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 67, ManaitaPlusLegacyKeyBoardCore.CATEGORY);
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_ARMOR_TP_KEY = new KeyMapping("key.manaita.tp.armor", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 78, ManaitaPlusLegacyKeyBoardCore.CATEGORY);
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_GOD_KEY = new KeyMapping("key.manaita.god_sword", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 86, ManaitaPlusLegacyKeyBoardCore.CATEGORY);
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_ANTI_DISARMING_KEY = new KeyMapping("key.manaita.anti_disarming", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 66, ManaitaPlusLegacyKeyBoardCore.CATEGORY);
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_ATTACK_KEY = new KeyMapping("key.manaita.attack", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, -1, ManaitaPlusLegacyKeyBoardCore.CATEGORY);
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_KEY);
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_ARMOR_KEY);
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_ARMOR_TP_KEY);
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_GOD_KEY);
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_ANTI_DISARMING_KEY);
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_ATTACK_KEY);
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_OPEN_KEY = new KeyMapping("key.manaita.open", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 71, ManaitaPlusLegacyKeyBoardCore.CATEGORY);
        if (CommomProxy.curios) {
            event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_OPEN_KEY);
        }
    }


    public static void acceptTypePropertyFunction(Item... items) {
        ResourceLocation location = ManaitaPlusLegacy.rl(ManaitaPlusLegacyTagData.Type);
        ItemPropertyFunction typePropertyFunction = (stack, level, entity, seed) -> {
            if (stack.getTag() != null) {
                return stack.hasTag() ? stack.getTag().getInt(ManaitaPlusLegacyTagData.ItemType) : 0;
            }
            return 0;
        };
        for (Item item : items) {
            ItemProperties.register(item,location,typePropertyFunction);
        }
    }

    @SubscribeEvent
    public static void onFMLCommonSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

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
            if (CommomProxy.curios) {
                acceptTypePropertyFunction(ManaitaPlusLegacyItemCurioCore.ManaitaCreateCurio.get());
                acceptTypePropertyFunction(ManaitaPlusLegacyItemCurioCore.ManaitaFurnaceCurio.get());
                acceptTypePropertyFunction(ManaitaPlusLegacyItemCurioCore.ManaitaBrewingCurio.get());
            }
        });
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        CosmicShaderEventHandler.onRegisterShaders(event);
        ItemShaderHandle.onRegisterShaders(event);
        GalaxyShaderHandle.onRegisterShaders(event);
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            Path resourcePath = ModList.get().getModFileById(ManaitaPlusLegacy.MODID).getFile().findResource(
                    "resourcepacks", "ManaitaPlusClassicalTexturePack");
            event.addRepositorySource(consumer -> {
                Pack pack = Pack.readMetaAndCreate("manaita_plus_legacy:classical_texture", Component.literal("ManaitaPlusClassicalTexturePack"), false, path -> new PathPackResources(path, resourcePath, false), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN);
                if (pack != null) {
                    consumer.accept(pack);
                }
            });
        }
    }

}

