package sen.manaita_plus_legacy.client.event;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sen.manaita_plus_legacy.ManaitaPlus;
import sen.manaita_plus_legacy.client.render.block.RenderBrewingManaitaBlockEntity;
import sen.manaita_plus_legacy.client.render.block.RenderCraftingManaitaBlockEntity;
import sen.manaita_plus_legacy.client.render.block.RenderFurnaceManaitaBlockEntity;
import sen.manaita_plus_legacy.client.render.entity.ManaitaPlusLightningBoltRenderer;
import sen.manaita_plus_legacy.client.render.entity.RenderManaitaArrow;
import sen.manaita_plus_legacy.common.core.ManaitaPlusBlockEntityCore;

import static sen.manaita_plus_legacy.common.core.ManaitaPlusEntityCore.ManaitaArrow;
import static sen.manaita_plus_legacy.common.core.ManaitaPlusEntityCore.ManaitaLightningBolt;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ManaitaPlus.MODID, value = Dist.CLIENT)
public class RegisterEventHandler {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ManaitaLightningBolt.get(), ManaitaPlusLightningBoltRenderer::new);
        event.registerEntityRenderer(ManaitaArrow.get(), RenderManaitaArrow::new);

        BlockEntityRenderers.register(ManaitaPlusBlockEntityCore.FURNACE_BLOCK_ENTITY.get(), RenderFurnaceManaitaBlockEntity::new);
        BlockEntityRenderers.register(ManaitaPlusBlockEntityCore.BREWING_BLOCK_ENTITY.get(), RenderBrewingManaitaBlockEntity::new);
        BlockEntityRenderers.register(ManaitaPlusBlockEntityCore.CRAFTING_BLOCK_ENTITY.get(), RenderCraftingManaitaBlockEntity::new);
    }
}

