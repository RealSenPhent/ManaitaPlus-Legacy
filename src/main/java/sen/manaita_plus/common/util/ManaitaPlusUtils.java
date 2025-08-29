package sen.manaita_plus.common.util;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import sen.manaita_plus.common.entity.ManaitaPlusLightningBolt;
import sen.manaita_plus.common.item.ManaitaPlusGodSwordItem;
import sen.manaita_plus.common.item.armor.ManaitaPlusArmor;
import sen.manaita_plus.common.item.data.IManaitaPlusDestroy;
import sen.manaita_plus.common.network.Networking;
import sen.manaita_plus.common.network.data.MessageDestroy;
import sen.manaita_plus_core.util.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ManaitaPlusUtils {
    public static final Minecraft mc = Minecraft.getInstance();
//    public static final Style style = Style.EMPTY.withFont(new ResourceLocation("rainbow_text","awa")).withBold(true);

    public static Entity getEntity(Entity entity) {
        if (entity instanceof Arrow arrow)
            return arrow.getOwner();
        if (entity instanceof DragonFireball fireball)
            return fireball.getOwner();
        return entity;
    }

    public static void godKill(Player player,boolean remove) {
        List<Entity> allEntities;
        Level level = player.level();
        boolean shiftKeyDown = player.isShiftKeyDown();
        if (shiftKeyDown && level.isClientSide) {
            mc.getSoundManager().stop();
            mc.particleEngine.setLevel(mc.level);
        }

        if (level instanceof ClientLevel client) {
            allEntities = Lists.newArrayList(client.entitiesForRendering());
        } else if (level instanceof ServerLevel server) {
            allEntities = Lists.newArrayList(server.getAllEntities());
        } else
            return;

        List<Entity> tntities = new ArrayList<>();

        for (Entity entity : allEntities) {
            Entity target = getEntity(entity);
            if (target == null || target instanceof Player || target instanceof ManaitaPlusLightningBolt) {
                continue;
            }
            if (target instanceof ItemEntity || target instanceof ExperienceOrb) {
                tntities.add(target);
                continue;
            }
            if (!shiftKeyDown && !(target instanceof Monster)) continue;
            kill(target, shiftKeyDown,remove);
        }

        if (!level.isClientSide) {
            for (Entity tntity : tntities) {
                if (tntity instanceof ItemEntity item) {
                    item.setNoPickUpDelay();
                    item.playerTouch(player);
                    continue;
                }
                if (tntity instanceof ExperienceOrb xp) {
                    player.takeXpDelay = 0;
                    xp.playerTouch(player);
                    continue;
                }
            }
        }
    }

    public static void kill(Entity target,boolean isSnk,boolean remove) {
        if (!target.level().isClientSide) {
            if (!ManaitaPlusEntityList.attack.accept(target)) {
                ManaitaPlusEntityList.attack.add(target);
                if (target instanceof LivingEntity living) {
                    DamageSource damageSource = target.damageSources().fellOutOfWorld();
                    living.hurt(damageSource , Float.MAX_VALUE);
                    living.setHealth(0.0F);
                    living.die(damageSource);
                }
            } else {
                ManaitaPlusEntityList.attack.remove(target);
                if (target instanceof LivingEntity living) {
                    living.hurtDuration += 10;
                    living.hurtTime += 10;
                    living.deathTime += 10;
//                for (AttributeInstance syncableAttribute : living.getAttributes().getSyncableAttributes()) Objects.requireNonNull(living.getAttribute(syncableAttribute.getAttribute())).setBaseValue(0.0D);
                }
            }
        }
        ManaitaPlusEntityList.death.add(target);
        if (remove) {
            ManaitaPlusEntityList.remove.add(target);
            if (!target.level().isClientSide) target.remove(Entity.RemovalReason.DISCARDED);
        }
        if (isSnk) {
            Class<?> wrapper = ManaitaPlusClassLoaderFactory.createWrapper(target.getClass());
            if (wrapper != null) {
                Helper.setFieldValue(target, wrapper);
            }
        }
    }


    public static boolean hasItem(Entity entity, ItemStack stack) {
        if (entity instanceof Player player && player.getInventory() != null) {
            return player.getInventory().contains(stack);
        }
        return false;
    }

    public static boolean hasItem(Entity entity, Item item) {
        if (entity instanceof Player player && player.getInventory() != null) {
            return player.getInventory().hasAnyMatching(stack -> !stack.isEmpty() && stack.getItem().getClass() == item.getClass());
        }
        return false;
    }

    public static boolean isManaita(Player player) {
        if (player.getInventory() != null) {
            return player.getInventory().hasAnyMatching(stack -> !stack.isEmpty() && stack.getItem() instanceof ManaitaPlusGodSwordItem);
        }
        return false;
    }


    public static void dropResources(BlockState p_49882_, Level p_49883_, BlockPos p_49884_, @javax.annotation.Nullable BlockEntity p_49885_, @javax.annotation.Nullable Entity p_49886_, ItemStack p_49887_, boolean dropXp) {
        if (p_49883_ instanceof ServerLevel) {
            List<ItemStack> drops = getDrops(p_49882_, (ServerLevel) p_49883_, p_49884_, p_49885_, p_49886_, p_49887_);
            if (drops.isEmpty()) drops.add(new ItemStack(p_49882_.getBlock()));
            drops.forEach((p_49944_) -> popResource(p_49883_, p_49884_, p_49944_));
            p_49882_.spawnAfterBreak((ServerLevel)p_49883_, p_49884_, p_49887_, dropXp);
        }
    }

    public static void popResource(Level p_49841_, BlockPos p_49842_, ItemStack p_49843_) {
        double d0 = (double) EntityType.ITEM.getHeight() / 2.0D;
        double d1 = (double)p_49842_.getX() + 0.5D + Mth.nextDouble(p_49841_.random, -0.25D, 0.25D);
        double d2 = (double)p_49842_.getY() + 0.5D + Mth.nextDouble(p_49841_.random, -0.25D, 0.25D) - d0;
        double d3 = (double)p_49842_.getZ() + 0.5D + Mth.nextDouble(p_49841_.random, -0.25D, 0.25D);
        popResource(p_49841_, () -> new ItemEntity(p_49841_, d1, d2, d3, p_49843_), p_49843_);
    }


    public static List<ItemStack> getDrops(BlockState p_49875_, ServerLevel p_49876_, BlockPos p_49877_, @javax.annotation.Nullable BlockEntity p_49878_, @javax.annotation.Nullable Entity p_49879_, ItemStack p_49880_) {
        LootParams.Builder lootparams$builder = (new LootParams.Builder(p_49876_)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(p_49877_)).withParameter(LootContextParams.TOOL, p_49880_).withOptionalParameter(LootContextParams.THIS_ENTITY, p_49879_).withOptionalParameter(LootContextParams.BLOCK_ENTITY, p_49878_);
        return p_49875_.getDrops(lootparams$builder);
    }


    private static void popResource(Level p_152441_, Supplier<ItemEntity> p_152442_, ItemStack p_152443_) {
        if (!p_152441_.isClientSide && !p_152443_.isEmpty()) {
            ItemEntity itementity = p_152442_.get();
            itementity.setDefaultPickUpDelay();
            p_152441_.addFreshEntity(itementity);
        }
    }

    public static void popExperience(ServerLevel p_49806_, BlockPos p_49807_, int p_49808_) {
        if (p_49806_.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !p_49806_.restoringBlockSnapshots) {
            ExperienceOrb.award(p_49806_, Vec3.atCenterOf(p_49807_), p_49808_);
        }

    }

    public static boolean isManaitaArmor(Player player) {
        for (ItemStack itemStack : player.getInventory().armor) {
            if (itemStack == null || !(itemStack.getItem() instanceof ManaitaPlusArmor))
                return false;
        }
        return true;
    }

    public static boolean isManaitaArmorPart(Player player) {
        for (ItemStack itemStack : player.getInventory().armor) {
            if (itemStack != null && itemStack.getItem() instanceof ManaitaPlusArmor)
                return true;
        }
        return false;
    }

    public static void desBlocks(ItemStack stack,Level level,BlockPos blockPos,Player player) {
        if (stack.getItem() instanceof IManaitaPlusDestroy des) {
            int range = des.getRange(stack) - 1;
            desBlock(stack, level, blockPos, player);
            if (range == 0) {
                return;
            }
            if (level instanceof ServerLevel serverLevel) {
                int xM = blockPos.getX() + range;
                int yM = blockPos.getY() + range;
                int zM = blockPos.getZ() + range;
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                for (int x = blockPos.getX() - range / 2; x < xM; x++) {
                    for (int y = blockPos.getY() - range / 2; y < yM; y++) {
                        for (int z = blockPos.getZ() - range / 2; z < zM; z++) {
                            mutableBlockPos.set(x,y,z);
                            BlockState blockState = level.getBlockState(mutableBlockPos);
                            if (blockState == null || !des.accept(blockState))
                                continue;

                            Block block = blockState.getBlock();
                            BlockEntity blockEntity = serverLevel.getBlockEntity(mutableBlockPos);

//                            boolean removed = blockState.onDestroyedByPlayer(level, pos, player, false, level.getFluidState(pos));
//                            block.playerWillDestroy(level, mutableBlockPos, blockState, player);
                            boolean removed = setBlock(level, mutableBlockPos, level.getFluidState(mutableBlockPos).createLegacyBlock());
                            if (removed)
                                block.destroy(level, mutableBlockPos, blockState);

                            player.awardStat(Stats.BLOCK_MINED.get(block));
                            player.causeFoodExhaustion(0.005F);
                            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));

                            List<ItemStack> drops = Block.getDrops(blockState, serverLevel, mutableBlockPos, blockEntity, player, stack);
                            if (drops.isEmpty()) popResource(serverLevel, mutableBlockPos, new ItemStack(block));
                            else drops.forEach((p_49859_) -> popResource(serverLevel, mutableBlockPos, p_49859_));
                            int exp = blockState.getExpDrop(serverLevel, serverLevel.random, mutableBlockPos, stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE), stack.getEnchantmentLevel(Enchantments.SILK_TOUCH));
                            block.popExperience(serverLevel, mutableBlockPos, exp);

//                            if (removed) serverLevel.blockUpdated(mutableBlockPos, block);
                        }
                    }
                }

                serverLevel.getPlayers(p -> {
                    Networking.INSTANCE.send(
                            PacketDistributor.PLAYER.with(() -> p),
                            new MessageDestroy(blockPos,range));
                    return false;
                });
            }/* else if (level instanceof ClientLevel clientLevel){
                int xM = blockPos.getX() + range;
                int yM = blockPos.getY() + range;
                int zM = blockPos.getZ() + range;
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                for (int x = blockPos.getX() - range / 2; x < xM; x++) {
                    for (int y = blockPos.getY() - range / 2; y < yM; y++) {
                        for (int z = blockPos.getZ() - range / 2; z < zM; z++) {
                            mutableBlockPos.set(x,y,z);
                            BlockState blockState = level.getBlockState(mutableBlockPos);
                            if (blockState == null || !des.accept(blockState)) continue;
//                            Networking.INSTANCE.sendToServer(new MessageDes(pos));
//                            mc.getTutorial().onDestroyBlock(clientLevel, pos, blockState, 1.0F);
                            SoundType soundtype = blockState.getSoundType(clientLevel, mutableBlockPos, player);
                            mc.getSoundManager().play(new SimpleSoundInstance(soundtype.getHitSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 8.0F, soundtype.getPitch() * 0.5F, SoundInstance.createUnseededRandom(), mutableBlockPos));
//                clientLevel.destroyBlockProgress(player.getId(), pos, 9);
//                boolean removed = blockState.onDestroyedByPlayer(level, pos, player, false, level.getFluidState(pos));
//                if (removed) block.destroy(level, pos, blockState);
                        }
                    }
                }
            }*/
        }
    }

    public static boolean setBlock(Level level,BlockPos p_46605_, BlockState p_46606_) {
        int p_46607_ = level.isClientSide ? 8 : 1;
        if (level.isOutsideBuildHeight(p_46605_)) {
            return false;
        } else if (!level.isClientSide && level.isDebug()) {
            return false;
        } else {
            LevelChunk levelchunk = level.getChunkAt(p_46605_);

            p_46605_ = p_46605_.immutable(); // Forge - prevent mutable BlockPos leaks
            net.minecraftforge.common.util.BlockSnapshot blockSnapshot = null;
            if (level.captureBlockSnapshots && !level.isClientSide) {
                blockSnapshot = net.minecraftforge.common.util.BlockSnapshot.create(level.dimension(), level, p_46605_, p_46607_);
                level.capturedBlockSnapshots.add(blockSnapshot);
            }

            BlockState blockstate = levelchunk.setBlockState(p_46605_, p_46606_, false);
            if (blockstate == null) {
                if (blockSnapshot != null) level.capturedBlockSnapshots.remove(blockSnapshot);
                return false;
            } else {
                if (blockSnapshot == null) { // Don't notify clients or update physics while capturing blockstates
                    level.markAndNotifyBlock(p_46605_, levelchunk, blockstate, p_46606_, p_46607_, 512);
                }

                return true;
            }
        }
    }

    public static void desBlock(ItemStack stack,Level level,BlockPos pos,Player player) {
        if (stack.getItem() instanceof IManaitaPlusDestroy des) {
            BlockState blockState = level.getBlockState(pos);
            if (blockState == null || !des.accept(blockState))
                return;

            Block block = blockState.getBlock();
            if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
                BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
                if (serverLevel.getBlockEntity(pos) == null) {
                    serverPlayer.connection.send(new ClientboundBlockUpdatePacket(pos, serverLevel.getFluidState(pos).createLegacyBlock()));
                }

                boolean removed = blockState.onDestroyedByPlayer(level, pos, player, false, level.getFluidState(pos));
                if (removed)
                    block.destroy(level, pos, blockState);

                player.awardStat(Stats.BLOCK_MINED.get(block));
                player.causeFoodExhaustion(0.005F);
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));

                List<ItemStack> drops = Block.getDrops(blockState, serverLevel, pos, blockEntity, player, stack);
                if (drops.isEmpty())
                    drops = List.of(new ItemStack(block));
                drops.forEach((p_49859_) -> popResource(serverLevel, pos, p_49859_));
                int exp = blockState.getExpDrop(serverLevel, serverLevel.random, pos, stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE), stack.getEnchantmentLevel(Enchantments.SILK_TOUCH));
                block.popExperience(serverLevel, pos, exp);
            } else if (level instanceof ClientLevel clientLevel) {

//                            Networking.INSTANCE.sendToServer(new MessageDes(pos));
//                mc.getTutorial().onDestroyBlock(clientLevel, pos, blockState, 1.0F);
                SoundType soundtype = blockState.getSoundType(clientLevel, pos, player);
                mc.getSoundManager().play(new SimpleSoundInstance(soundtype.getHitSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 8.0F, soundtype.getPitch() * 0.5F, SoundInstance.createUnseededRandom(), pos));
//                clientLevel.destroyBlockProgress(player.getId(), pos, 9);
//                boolean removed = blockState.onDestroyedByPlayer(level, pos, player, false, level.getFluidState(pos));
//                if (removed) block.destroy(level, pos, blockState);
            }
        }
    }

    public static String getTypes(int i) {
        if (i == 1)
            return "wooden.";
        if (i == 2)
            return "stone.";
        if (i == 3)
            return "iron.";
        if (i == 4)
            return "gold.";
        if (i == 5)
            return "diamond.";
        if (i == 6)
            return "emerald.";
        if (i == 7)
            return "redstone.";
        if (i == 8)
            return "netherite.";
        return "";
    }

    public static String getTypes1(int i) {
        if (i == 2)
            return "stone";
        if (i == 3)
            return "iron";
        if (i == 4)
            return "gold";
        if (i == 5)
            return "diamond";
        if (i == 6)
            return "emerald";
        if (i == 7)
            return "redstone";
        if (i == 8)
            return "netherite";
        return "wooden";
    }

    public static void chat(Component p_93786_) {
        Minecraft.getInstance().gui.getChat().addMessage(p_93786_);
    }
}
