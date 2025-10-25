package sen.manaita_plus_legacy.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.client.render.block.RenderBrewingManaitaBlockEntity;
import sen.manaita_plus_legacy.client.render.block.RenderCraftingManaitaBlockEntity;
import sen.manaita_plus_legacy.client.render.block.RenderFurnaceManaitaBlockEntity;
import sen.manaita_plus_legacy.client.render.entity.ManaitaPlusLightningBoltRenderer;
import sen.manaita_plus_legacy.client.render.entity.RenderManaitaArrow;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyBlockEntityCore;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyKeyBoardCore;

import static sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyEntityCore.ManaitaArrow;
import static sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyEntityCore.ManaitaLightningBolt;

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
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_KEY = new KeyMapping("key.manaita", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 88, "key.categories.misc");
        ManaitaPlusLegacyKeyBoardCore.MESSAGE_ARMOR_KEY = new KeyMapping("key.manaita.armor", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 89, "key.categories.misc");
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_KEY);
        event.register(ManaitaPlusLegacyKeyBoardCore.MESSAGE_ARMOR_KEY);
    }
}

