package sen.manaita_plus_legacy.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.client.render.block.RenderBrewingManaitaBlockEntity;
import sen.manaita_plus_legacy.client.render.block.RenderCraftingManaitaBlockEntity;
import sen.manaita_plus_legacy.client.render.block.RenderFurnaceManaitaBlockEntity;
import sen.manaita_plus_legacy.client.render.entity.ManaitaPlusLightningBoltRenderer;
import sen.manaita_plus_legacy.client.render.entity.RenderManaitaArrow;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyBlockEntityCore;
import sen.manaita_plus_legacy.client.core.ManaitaPlusLegacyKeyBoardCore;

import static sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyEntityCore.ManaitaArrow;
import static sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyEntityCore.ManaitaLightningBolt;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

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
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_GOD_KEY = new KeyMapping("key.manaita.god_sword", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 86, ManaitaPlusLegacyKeyBoardCore.CATEGORY);
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_ANTI_DISARMING_KEY = new KeyMapping("key.manaita.anti_disarming", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 66, ManaitaPlusLegacyKeyBoardCore.CATEGORY);
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_ATTACK_KEY = new KeyMapping("key.manaita.attack", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, -1, ManaitaPlusLegacyKeyBoardCore.CATEGORY);
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_KEY);
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_ARMOR_KEY);
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_GOD_KEY);
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_ANTI_DISARMING_KEY);
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_ATTACK_KEY);
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_OPEN_KEY = new KeyMapping("key.manaita.open", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 71, ManaitaPlusLegacyKeyBoardCore.CATEGORY);
        if (ModList.get().isLoaded("curios")) {
            event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_OPEN_KEY);
        }
    }
}

