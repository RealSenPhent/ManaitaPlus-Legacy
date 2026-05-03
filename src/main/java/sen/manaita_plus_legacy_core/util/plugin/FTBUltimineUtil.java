package sen.manaita_plus_legacy_core.util.plugin;

import dev.architectury.event.EventResult;
import dev.architectury.utils.value.IntValue;
import dev.ftb.mods.ftbultimine.CooldownTracker;
import dev.ftb.mods.ftbultimine.FTBUltimine;
import dev.ftb.mods.ftbultimine.FTBUltiminePlayerData;
import dev.ftb.mods.ftbultimine.ItemCollection;
import dev.ftb.mods.ftbultimine.config.FTBUltimineServerConfig;
import dev.ftb.mods.ftbultimine.integration.acceldecay.AcceleratedDecay;
import dev.ftb.mods.ftbultimine.integration.acceldecay.LogBreakTracker;
import dev.ftb.mods.ftbultimine.net.SendShapePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.common.config.ManaitaPlusLegacyConfig;
import sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static sen.manaita_plus_legacy.common.util.ManaitaPlusUtils.setBlock;


public class FTBUltimineUtil {
    public static boolean canUltimine(ServerPlayer player) {
        return player.getMainHandItem().getItem() instanceof ManaitaPlusLegacyToolBase;
    }

    public static EventResult blockBroken(FTBUltimine ftbUltimine, Level world, BlockPos pos, ServerPlayer player) {
        if (CooldownTracker.isOnCooldown(player)) {
            return EventResult.pass();
        }
        FTBUltiminePlayerData data = ftbUltimine.getOrCreatePlayerData(player);

        if (!data.isPressed()) {
            return EventResult.pass();
        }

        HitResult result = FTBUltiminePlayerData.rayTrace(player);

        if (!(result instanceof BlockHitResult) || result.getType() != HitResult.Type.BLOCK) {
            return EventResult.pass();
        }

        data.clearCache();
        data.updateBlocks(player, pos, ((BlockHitResult) result).getDirection(), false, FTBUltimineServerConfig.getMaxBlocks(player));

        if (!data.hasCachedPositions()) {
            return EventResult.pass();
        }

        if (player.totalExperience < data.cachedPositions().size() * FTBUltimineServerConfig.getExperiencePerBlock(player)) {
            return EventResult.pass();
        }

        int exp = 0;
        Map<Integer, List<ItemStack>> itemDrops = new HashMap<>();
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof ManaitaPlusLegacyToolBase des) {
            int type = ManaitaPlusLegacyToolBase.getType(stack);
            boolean doubling = des.isDoubling(type);
            for (BlockPos p : data.cachedPositions()) {
                BlockState state1 = world.getBlockState(p);
                if (!des.accept(state1)) continue;
                Block block = state1.getBlock();
                if (world instanceof ServerLevel serverLevel) {
                        List<ItemStack> drops = Block.getDrops(state1, serverLevel, p, serverLevel.getBlockEntity(p), player, stack);
                        if (drops.isEmpty()) {
                            ItemStack itemStack = new ItemStack(block,1);
                            List<ItemStack> itemStacks = itemDrops.computeIfAbsent(itemStack.getItem().hashCode(), ys -> new ArrayList<>());
                            boolean flag = false;
                            for (ItemStack itemStack1 : itemStacks) {
                                if (itemStack1.areShareTagsEqual(itemStack)) {
                                    itemStack1.setCount(itemStack1.getCount() + 1);
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) itemStacks.add(itemStack);
                        } else {
                            drops.forEach((p_49859_) -> {
                                List<ItemStack> itemStacks = itemDrops.computeIfAbsent(p_49859_.getItem().hashCode(), ys -> new ArrayList<>());
                                for (ItemStack itemStack : itemStacks) {
                                    if (itemStack.areShareTagsEqual(p_49859_)) {
                                        itemStack.setCount(itemStack.getCount() + p_49859_.getCount());
                                        return;
                                    }
                                }
                                itemStacks.add(p_49859_);
                            });
                        }
                        exp += state1.getExpDrop(serverLevel, serverLevel.random, p, stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE), stack.getEnchantmentLevel(Enchantments.SILK_TOUCH));
                    }
                boolean removed = setBlock(world, p, world.getFluidState(p).createLegacyBlock(), 2);
                block.playerWillDestroy(world, p, state1, player);
                if (removed) {
                    block.destroy(world, p, state1);
                }
            }
            if (world instanceof ServerLevel serverLevel) {
                if (doubling)
                    exp *= ManaitaPlusLegacyConfig.destroy_doubling_value;
                player.level().addFreshEntity(new ExperienceOrb(player.level(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, exp));
                for (List<ItemStack> value : itemDrops.values()) {
                    if (value.isEmpty()) continue;
                    for (ItemStack itemStack : value) {
                        if (doubling) itemStack.setCount(itemStack.getCount() * ManaitaPlusLegacyConfig.destroy_doubling_value);
                        ManaitaPlusUtils.popResource(serverLevel, pos, itemStack);
                    }
                }
            }
            CooldownTracker.setLastUltimineTime(player, System.currentTimeMillis());

            data.clearCache();
            new SendShapePacket(data.getCurrentShapeIndex(), Collections.emptyList()).sendTo(player);
        }
        return EventResult.interruptFalse();
    }


}
