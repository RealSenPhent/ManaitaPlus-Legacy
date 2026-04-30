package sen.manaita_plus_legacy.common.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.entity.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.NotNull;
import sen.manaita_plus_legacy.common.config.ManaitaPlusLegacyConfig;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCore;
import sen.manaita_plus_legacy.common.item.ManaitaPlusLegacyGodSwordItem;
import sen.manaita_plus_legacy.common.item.armor.ManaitaPlusLegacyArmor;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDestroy;
import sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase;
import sen.manaita_plus_legacy.common.network.Networking;
import sen.manaita_plus_legacy.common.network.server.ChangeDeathDataPacket;
import sen.manaita_plus_legacy.common.network.server.DestroyBlockPacket;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy.common.util.wrapper.EntitiesWrapper;
import sen.manaita_plus_legacy_core.util.Helper;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ManaitaPlusUtils {
    public static final Minecraft mc = Minecraft.getInstance();

    public static Entity getEntity(Entity entity) {
        if (entity instanceof TraceableEntity traceableEntity)
            return traceableEntity.getOwner();
        return entity;
    }

    private static final ThreadLocal<EntitiesWrapper> ENTITY_CACHE =
            ThreadLocal.withInitial(EntitiesWrapper::new);

    public static void godKill(ItemStack stack,Player player) {
        int type = ManaitaPlusLegacyToolBase.getType(stack);
        boolean remove = ManaitaPlusLegacyGodSwordItem.isRemove(type);
        boolean shiftKeyDown = player.isShiftKeyDown();
        godKill(type,remove,shiftKeyDown,player);
    }
    public static void godKill(int type,boolean remove,boolean shiftKeyDown,Player player) {
        Level level = player.level();
        if (level instanceof ClientLevel client) {
            EntitiesWrapper wrapper = ENTITY_CACHE.get();
            wrapper.addIterable(client.entitiesForRendering());
            wrapper.addIterable(client.getPartEntities());
            Entity[] entities = wrapper.getEntities();
            for (int i = 0; i < wrapper.size(); i++) {
                Entity entity = entities[i];
                if (entity == null
                        || (entity instanceof ItemEntity && ManaitaPlusLegacyToolBase.canPick(type,true))
                        || (entity instanceof ExperienceOrb && ManaitaPlusLegacyToolBase.canPick(type,false))
                ) {
                    continue;
                }
                if (!shiftKeyDown && entity.getType().getCategory() != MobCategory.MONSTER) continue;
                attack(entity, player,remove);
            }
            wrapper.reset();

            TransientEntitySectionManager<Entity> entityStorage = client.entityStorage;
            EntitySectionStorage<Entity> sectionStorage = entityStorage.sectionStorage;
            ObjectIterator<Long2ObjectMap.Entry<EntitySection<Entity>>> iterator1 = sectionStorage.sections.long2ObjectEntrySet().iterator();
            while (iterator1.hasNext()) {
                EntitySection<Entity> entitySection = iterator1.next().getValue();
                if (entitySection == null) {
                    continue;
                }
                ClassInstanceMultiMap<Entity> storage = entitySection.storage;
                for (Map.Entry<?, ?> entry : storage.byClass.entrySet()) {
                    if (entry.getValue() instanceof List<?> list1) {
                        list1.removeIf(o -> o instanceof Entity entity && ManaitaPlusLegacyEntityData.remove.acceptSide(entity));
                    }
                }
                entitySection.storage.allInstances.removeIf(ManaitaPlusLegacyEntityData.remove::acceptSide);
            }

            ObjectIterator<Int2ObjectMap.Entry<Entity>> iterator = entityStorage.entityStorage.byId.int2ObjectEntrySet().iterator();
            while (iterator.hasNext())
                if (ManaitaPlusLegacyEntityData.remove.acceptSide(iterator.next().getValue()))
                    iterator.remove();
            iterator = client.tickingEntities.active.int2ObjectEntrySet().iterator();
            while (iterator.hasNext())
                if (ManaitaPlusLegacyEntityData.remove.acceptSide(iterator.next().getValue()))
                    iterator.remove();
            entityStorage.entityStorage.byUuid.entrySet().removeIf(uuidEntityEntry -> ManaitaPlusLegacyEntityData.remove.acceptSide(uuidEntityEntry.getValue()));
        } else if (level instanceof ServerLevel server) {
            EntitiesWrapper wrapper = ENTITY_CACHE.get();
            wrapper.addIterable(server.getAllEntities());
            wrapper.addIterable(server.getPartEntities());
            Entity[] entities = wrapper.getEntities();
            for (int i = 0; i < wrapper.size(); i++) {
                Entity entity = entities[i];
                if (entity == null) {
                    continue;
                } else if (entity instanceof ItemEntity item) {
                    if (ManaitaPlusLegacyToolBase.canPick(type,true)) {
                        item.setNoPickUpDelay();
                        item.playerTouch(player);
                        continue;
                    } else item.discard();
                } else if (entity instanceof ExperienceOrb orb) {
                    if (ManaitaPlusLegacyToolBase.canPick(type,false)) {
                        player.takeXpDelay = 0;
                        orb.playerTouch(player);
                        continue;
                    } else orb.discard();
                }
                if (!shiftKeyDown && entity.getType().getCategory() != MobCategory.MONSTER) continue;
                attack(entity, player,remove);
            }
            wrapper.reset();

            PersistentEntitySectionManager<Entity> entityManager = server.entityManager;
            EntitySectionStorage<Entity> sectionStorage = entityManager.sectionStorage;
            ObjectIterator<Long2ObjectMap.Entry<EntitySection<Entity>>> iterator1 = sectionStorage.sections.long2ObjectEntrySet().iterator();
            while (iterator1.hasNext()) {
                EntitySection<Entity> entitySection = iterator1.next().getValue();
                if (entitySection == null) {
                    continue;
                }
                ClassInstanceMultiMap<Entity> storage = entitySection.storage;
                for (Map.Entry<?, ?> entry : storage.byClass.entrySet()) {
                    if (entry.getValue() instanceof List<?> list1) {
                        list1.removeIf(o -> o instanceof Entity entity && ManaitaPlusLegacyEntityData.remove.acceptSide(entity));
                    }
                }
                entitySection.storage.allInstances.removeIf(ManaitaPlusLegacyEntityData.remove::acceptSide);
            }
            ObjectIterator<Int2ObjectMap.Entry<Entity>> iterator = entityManager.visibleEntityStorage.byId.int2ObjectEntrySet().iterator();
            while (iterator.hasNext())
                if (ManaitaPlusLegacyEntityData.remove.acceptSide(iterator.next().getValue()))
                    iterator.remove();
            iterator = server.entityTickList.active.int2ObjectEntrySet().iterator();
            while (iterator.hasNext())
                if (ManaitaPlusLegacyEntityData.remove.acceptSide(iterator.next().getValue()))
                    iterator.remove();
            entityManager.visibleEntityStorage.byUuid.entrySet().removeIf(uuidEntityEntry -> ManaitaPlusLegacyEntityData.remove.acceptSide(uuidEntityEntry.getValue()));
            ObjectIterator<Int2ObjectMap.Entry<ChunkMap.TrackedEntity>> iteratored = server.getChunkSource().chunkMap.entityMap.int2ObjectEntrySet().iterator();
            while (iteratored.hasNext()) {
                ChunkMap.TrackedEntity trackedEntity = iteratored.next().getValue();
                Entity entity = trackedEntity.entity;
                if (ManaitaPlusLegacyEntityData.remove.acceptSide(entity)) {
                    if (entity instanceof ServerPlayer serverplayer) {
//                        server.getChunkSource().chunkMap.updatePlayerStatus(serverplayer, false);
                        ObjectIterator<Int2ObjectMap.Entry<ChunkMap.TrackedEntity>> var3 = server.getChunkSource().chunkMap.entityMap.int2ObjectEntrySet().iterator();
                        while(var3.hasNext()) {
                            ChunkMap.TrackedEntity chunkmap$trackedentity = (ChunkMap.TrackedEntity)var3.next();
                            chunkmap$trackedentity.removePlayer(serverplayer);
                        }
                    }
                    iteratored.remove();
                    trackedEntity.broadcastRemoved();
                }
            }
        }
        if (!shiftKeyDown) {
            Iterator<PartEntity<?>> iterator = level.getPartEntities().iterator();
            PartEntity<?> partEntity;
            while (iterator.hasNext()) {
                partEntity = iterator.next();
                if (partEntity == null || partEntity.getParent().getType().getCategory() == MobCategory.MONSTER) {
                    iterator.remove();
                }
            }
        } else level.getPartEntities().clear();
    }

    public static void attack(Entity target, Player player, boolean remove) {
        if (remove) {
            ManaitaPlusLegacyEntityData.remove.add(target);
            if (player.isShiftKeyDown() && !target.getClass().getName().startsWith("net.minecraft")) {
                Class<?> wrapper = ManaitaPlusClassLoaderFactory.createWrapper(target.getClass());
                if (wrapper != null) {
                    Helper.setFieldValue(target, wrapper);
                }
            }
        }
        if (target.level() instanceof ClientLevel) {
            if (Minecraft.getInstance().isSameThread()) {
                if (remove) {
                    removeOnClient(target);
                 } else {
                    killOnClient(target);
                }
            }
        } else {
            if (remove) {
                removeOnServer(target);
            } else {
                if (target instanceof LivingEntity living) {
                    AttributeInstance attribute = living.getAttribute(Attributes.MAX_HEALTH);
                    if (attribute != null)
                        attribute.setBaseValue(0.0F);
                    living.hurt(living.damageSources().playerAttack(player), Float.MAX_VALUE);
                    living.setLastHurtByPlayer(player);
                    living.handleEntityEvent((byte) 2);
                    living.die(living.damageSources().playerAttack(player));
                }
            }
        }
        if (target instanceof ServerPlayer serverPlayer) {
            Networking.sendToPlayer(serverPlayer, new ChangeDeathDataPacket(1));
        }
        ManaitaPlusLegacyEntityData.death.add(target);
    }

    public static void killOnClient(Entity target) {}

    public static void removeOnClient(Entity target) {
        if (target.level() instanceof ClientLevel clientLevel) {
            Int2ObjectMap<Entity> byId = clientLevel.entityStorage.entityStorage.byId;
            byId.remove(target.getId());
            byId.int2ObjectEntrySet().removeIf(next -> next.getValue() == target);

            Map<UUID, Entity> byUuid = clientLevel.entityStorage.entityStorage.byUuid;
            byUuid.remove(target.getUUID());
            byUuid.entrySet().removeIf(next -> next.getValue() == target);
            LevelEntityGetter<Entity> getter = clientLevel.entityStorage.entityGetter;
            if (getter instanceof LevelEntityGetterAdapter<Entity> adapter) {
                adapter.visibleEntities.byId.remove(target.getId());
                adapter.visibleEntities.byUuid.remove(target.getUUID());
            }
            clientLevel.tickingEntities.remove(target);

            long sectionPos = SectionPos.asLong(target.blockPosition());
            EntitySectionStorage<Entity> sectionStorage = clientLevel.entityStorage.sectionStorage;
            sectionStorage.getExistingSectionPositionsInChunk(sectionPos).forEach(sectionPos1 -> {
                EntitySection<Entity> section = sectionStorage.sections.get(sectionPos1);
                if (section == null) return;
                section.storage.remove(target);
                section.storage.allInstances.remove(target);
            });
            EntitySection<Entity> entitySection = sectionStorage.getOrCreateSection(sectionPos);
            entitySection.remove(target);
            entitySection.storage.allInstances.remove(target);
        }
    }

    public static void removeOnServer(Entity target) {
        if (target.level() instanceof ServerLevel serverLevel) {
            Int2ObjectMap<Entity> byId = serverLevel.entityManager.visibleEntityStorage.byId;
            byId.remove(target.getId());
            byId.int2ObjectEntrySet().removeIf(next -> next.getValue() == target);

            Map<UUID, Entity> byUuid = serverLevel.entityManager.visibleEntityStorage.byUuid;
            byUuid.remove(target.getUUID());
            byUuid.entrySet().removeIf(next -> next.getValue() == target);
            serverLevel.entityManager.knownUuids.remove(target.getUUID());

            LevelEntityGetter<Entity> getter = serverLevel.entityManager.entityGetter;

            if (getter instanceof LevelEntityGetterAdapter<Entity> adapter) {
                adapter.visibleEntities.byId.remove(target.getId());
                adapter.visibleEntities.byUuid.remove(target.getUUID());
            }
            serverLevel.entityTickList.remove(target);

            long sectionPos = SectionPos.asLong(target.blockPosition());
            EntitySectionStorage<Entity> sectionStorage = serverLevel.entityManager.sectionStorage;

            sectionStorage.getExistingSectionPositionsInChunk(sectionPos).forEach(sectionPos1 -> {
                EntitySection<Entity> section = sectionStorage.sections.get(sectionPos1);
                if (section == null) return;
                section.storage.remove(target);
                section.storage.allInstances.remove(target);
            });
            EntitySection<Entity> entitySection = sectionStorage.getOrCreateSection(sectionPos);
            target.setLevelCallback(new EntityInLevelCallback() {
                public void onMove() {
                }

                public void onRemove(Entity.@NotNull RemovalReason removalReason) {
                    if (!entitySection.remove(target)) {
                        serverLevel.entityManager.stopTicking(target);
                        serverLevel.entityManager.stopTracking(target);
                        serverLevel.entityManager.callbacks.onDestroyed(target);
                    }
                }
            });
            target.setRemoved(Entity.RemovalReason.DISCARDED);
            entitySection.remove(target);
            entitySection.storage.allInstances.remove(target);

            serverLevel.getChunkSource().removeEntity(target);
        }
    }


    public static boolean isManaita(Player player) {
        return ManaitaPlusLegacyEntityData.manaita.accept(player);
//        if (player.getInventory() != null) {
//            return player.getInventory().hasAnyMatching(stack -> !stack.isEmpty() && stack.getItem() instanceof ManaitaPlusGodSwordItem);
//        }
    }


    public static void popResource(Level p_49841_, BlockPos p_49842_, ItemStack p_49843_) {
        double d0 = (double) EntityType.ITEM.getHeight() / 2.0D;
        double d1 = (double)p_49842_.getX() + 0.5D + Mth.nextDouble(p_49841_.random, -0.25D, 0.25D);
        double d2 = (double)p_49842_.getY() + 0.5D + Mth.nextDouble(p_49841_.random, -0.25D, 0.25D) - d0;
        double d3 = (double)p_49842_.getZ() + 0.5D + Mth.nextDouble(p_49841_.random, -0.25D, 0.25D);
        popResource(p_49841_, () -> new ItemEntity(p_49841_, d1, d2, d3, p_49843_), p_49843_);
    }

    public static void popResource(Level p_49841_, BlockPos p_49842_, ItemStack p_49843_,Vec3 vec3) {
        double d0 = (double) EntityType.ITEM.getHeight() / 2.0D;
        double d1 = (double)p_49842_.getX() + 0.5D + Mth.nextDouble(p_49841_.random, -0.25D, 0.25D);
        double d2 = (double)p_49842_.getY() + 0.5D + Mth.nextDouble(p_49841_.random, -0.25D, 0.25D) - d0;
        double d3 = (double)p_49842_.getZ() + 0.5D + Mth.nextDouble(p_49841_.random, -0.25D, 0.25D);
        popResource(p_49841_, () -> {
            ItemEntity itemEntity = new ItemEntity(p_49841_, d1, d2, d3, p_49843_);
            itemEntity.setDeltaMovement(vec3);
            return itemEntity;
        }, p_49843_);
    }


    private static void popResource(Level p_152441_, Supplier<ItemEntity> p_152442_, ItemStack p_152443_) {
        if (!p_152441_.isClientSide && !p_152443_.isEmpty()) {
            ItemEntity itementity = p_152442_.get();
            itementity.setDefaultPickUpDelay();
            p_152441_.addFreshEntity(itementity);
        }
    }

    public static boolean isManaitaArmor(Player player) {
        for (ItemStack itemStack : player.getInventory().armor) {
            if (itemStack == null || !(itemStack.getItem() instanceof ManaitaPlusLegacyArmor))
                return false;
        }
        return true;
    }

    public static boolean isManaitaArmorPart(Player player) {
        for (ItemStack itemStack : player.getInventory().armor) {
            if (itemStack != null && itemStack.getItem() instanceof ManaitaPlusLegacyArmor)
                return true;
        }
        return false;
    }

    public static void destroyBlocks(ItemStack stack, Level level, BlockPos blockPos, Player player) {
        if (stack.getItem() instanceof ManaitaPlusLegacyToolBase des) {
            int range = des.getRange(stack) >> 1;
            int type = ManaitaPlusLegacyToolBase.getType(stack);
            boolean doubling = des.isDoubling(type);
            if (range == 0) {
                destroyBlock(stack, level, blockPos, player,doubling);
                return;
            }
            if (level instanceof ServerLevel serverLevel) {
                boolean isDrop = !player.getAbilities().instabuild;
                if (!isDrop && !ManaitaPlusLegacyConfig.creative_range_destroy_value) return;
                Networking.sendToTrackBySeen(serverLevel,player,new DestroyBlockPacket(blockPos,range,stack.getItem()));
                int xM = blockPos.getX() + range;
                int yM = blockPos.getY() + range;
                int zM = blockPos.getZ() + range;
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                int exp = 0;
                Map<Block, AtomicInteger> blocks = new HashMap<>();
                Map<Integer,List<ItemStack>> itemDrops = new HashMap<>();
                boolean pickItems = ManaitaPlusLegacyToolBase.canPick(type, true);
                boolean pickExperience = ManaitaPlusLegacyToolBase.canPick(type, true);
                for (int x = blockPos.getX() - range; x <= xM; x++) {
                    for (int y = blockPos.getY() - range; y <= yM; y++) {
                        for (int z = blockPos.getZ() - range; z <= zM; z++) {
                            BlockState blockState = level.getBlockState(mutableBlockPos.set(x, y, z));
                            if (!des.accept(blockState)) {
                                continue;
                            }

                            Block block = blockState.getBlock();
                            BlockEntity blockEntity = serverLevel.getBlockEntity(mutableBlockPos);

                            boolean removed = setBlock(level, mutableBlockPos, level.getFluidState(mutableBlockPos).createLegacyBlock(), 2);
                            block.playerWillDestroy(level, mutableBlockPos, blockState, player);
                            if (removed) {
                                block.destroy(level, mutableBlockPos, blockState);
                            }
                            player.awardStat(Stats.BLOCK_MINED.get(block));
                            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));

                            if (isDrop) {
                                player.causeFoodExhaustion(0.005F);
                                List<ItemStack> drops = Block.getDrops(blockState, serverLevel, mutableBlockPos, blockEntity, player, stack);
                                if (pickItems) {
                                    if (drops.isEmpty()) {
                                        AtomicInteger atomicInteger = blocks.computeIfAbsent(block, (block1) -> new AtomicInteger(0));
                                        atomicInteger.set(atomicInteger.get() + 1);
                                    }
                                    else
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
                                } else {
                                    if (drops.isEmpty())
                                        popResource(serverLevel, mutableBlockPos, new ItemStack(block, doubling ? ManaitaPlusLegacyConfig.destroy_doubling_value : 1));
                                    else
                                        drops.forEach((p_49859_) -> {
                                            if (doubling)
                                                p_49859_.setCount(p_49859_.getCount() * ManaitaPlusLegacyConfig.destroy_doubling_value);
                                            popResource(serverLevel, mutableBlockPos, p_49859_);
                                        });
                                }
                                if (pickExperience) {
                                    exp += blockState.getExpDrop(serverLevel, serverLevel.random, mutableBlockPos, stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE), stack.getEnchantmentLevel(Enchantments.SILK_TOUCH));
                                } else {
                                    if (doubling) {
                                        exp = blockState.getExpDrop(serverLevel, serverLevel.random, mutableBlockPos, stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE), stack.getEnchantmentLevel(Enchantments.SILK_TOUCH)) * ManaitaPlusLegacyConfig.destroy_doubling_value;
                                    }
                                    block.popExperience(serverLevel, mutableBlockPos, exp);
                                }
                            }
                        }
                    }
                }
                if (isDrop) {
                    if (pickItems) {
                        ItemStack itemStack = new ItemStack(ManaitaPlusLegacyItemCore.ManaitaSource.get());
                        CompoundTag orCreateTag = itemStack.getOrCreateTag();
                        ListTag listtag = new ListTag();
                        test : for (Map.Entry<Block, AtomicInteger> entry : blocks.entrySet()) {
                            ItemStack itemStack1 = new ItemStack(entry.getKey(), entry.getValue().get());
                            if (itemStack1.isEmpty()) continue;
                            List<ItemStack> itemStacks = itemDrops.computeIfAbsent(itemStack1.getItem().hashCode(), ys -> new ArrayList<>());
                            for (ItemStack stack1 : itemStacks) {
                                if (stack1.areShareTagsEqual(itemStack1)) {
                                    stack1.setCount(stack1.getCount() + itemStack1.getCount());
                                    break test;
                                }
                            }
                            itemStacks.add(itemStack1);
                        }
                        for (List<ItemStack> itemStacks : itemDrops.values()) {
                            for (ItemStack itemStack1 : itemStacks) {
                                if (itemStack1.isEmpty()) continue;
                                CompoundTag compoundTag = new CompoundTag();
                                if (doubling) {
                                    compoundTag.putInt("RealCount",itemStack1.getCount() * ManaitaPlusLegacyConfig.destroy_doubling_value);
                                }else {
                                    compoundTag.putInt("RealCount",itemStack1.getCount());
                                }
                                itemStack1.setCount(1);
                                listtag.add(itemStack1.save(compoundTag));
                            }
                        }
                        orCreateTag.put("Items", listtag);
                        popResource(serverLevel, predictPlayerPosition(player,10), itemStack, player.getDeltaMovement());
                    }
                    if (pickExperience) {
                        if (doubling)
                            exp *= ManaitaPlusLegacyConfig.destroy_doubling_value;
                        player.giveExperiencePoints(exp);
                    }
                }
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


    public static BlockPos predictPlayerPosition(Player player, int ticksAhead) {
        Vec3 currentPos = player.position();
        Vec3 motion = player.getDeltaMovement();

        // 线性预测加上速度衰减
        double airResistance = player.onGround() ? 0.6 : 0.91;
        double predictedX = currentPos.x;
        double predictedY = currentPos.y;
        double predictedZ = currentPos.z;

        double currentMotionX = motion.x;
        double currentMotionY = motion.y;
        double currentMotionZ = motion.z;

        for (int i = 0; i < ticksAhead; i++) {
            predictedX += currentMotionX;
            predictedY += currentMotionY;
            predictedZ += currentMotionZ;

            // 应用阻力
            currentMotionX *= airResistance;
            currentMotionZ *= airResistance;
            currentMotionY *= 0.98; // 空气阻力
        }

        return new BlockPos((int) predictedX, (int) predictedY, (int) predictedZ);
    }


    public static boolean setBlock(Level level,BlockPos p_46605_, BlockState p_46606_,int p_46607_ ) {
        if (level.isOutsideBuildHeight(p_46605_)) {
            return false;
        } else if (!level.isClientSide && level.isDebug()) {
            return false;
        } else {
            LevelChunk levelchunk = level.getChunkAt(p_46605_);

            p_46605_ = p_46605_.immutable(); // Forge - prevent mutable BlockPos leaks
            BlockSnapshot blockSnapshot = null;
            if (level.captureBlockSnapshots && !level.isClientSide) {
                blockSnapshot = BlockSnapshot.create(level.dimension(), level, p_46605_, p_46607_);
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

    public static void destroyBlock(ItemStack stack, Level level, BlockPos pos, Player player, boolean doubling) {
        if (stack.getItem() instanceof IManaitaPlusLegacyDestroy des) {
            BlockState blockState = level.getBlockState(pos);
            if (!des.accept(blockState))
                return;

            Block block = blockState.getBlock();
            if (level instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
                BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
                if (serverLevel.getBlockEntity(pos) == null) {
                    serverPlayer.connection.send(new ClientboundBlockUpdatePacket(pos, serverLevel.getFluidState(pos).createLegacyBlock()));
                }

                boolean removed = blockState.onDestroyedByPlayer(level, pos, player, false, level.getFluidState(pos));
                block.playerWillDestroy(level, pos, blockState, player);
                if (removed)
                    block.destroy(level, pos, blockState);

                player.awardStat(Stats.BLOCK_MINED.get(block));
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));

                boolean isDrop = !player.getAbilities().instabuild;
                if (isDrop) {
                    player.causeFoodExhaustion(0.005F);
                    List<ItemStack> drops = Block.getDrops(blockState, serverLevel, pos, blockEntity, player, stack);
                    int type = ManaitaPlusLegacyToolBase.getType(stack);
                    if (ManaitaPlusLegacyToolBase.canPick(type,true)) {
                        if (drops.isEmpty())
                            player.getInventory().add(new ItemStack(block, doubling ? ManaitaPlusLegacyConfig.destroy_doubling_value : 1));
                        else
                            drops.forEach((p_49859_) -> {
                                if (doubling)
                                    p_49859_.setCount(p_49859_.getCount() * ManaitaPlusLegacyConfig.destroy_doubling_value);
                                player.getInventory().add(p_49859_);
                            });
                    } else {
                        if (drops.isEmpty())
                            popResource(serverLevel, pos, new ItemStack(block, doubling ? ManaitaPlusLegacyConfig.destroy_doubling_value : 1));
                        else
                            drops.forEach((p_49859_) -> {
                                if (doubling)
                                    p_49859_.setCount(p_49859_.getCount() * ManaitaPlusLegacyConfig.destroy_doubling_value);
                                popResource(serverLevel, pos, p_49859_);
                            });
                    }
                    int exp = blockState.getExpDrop(serverLevel, serverLevel.random, pos, stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE), stack.getEnchantmentLevel(Enchantments.SILK_TOUCH));
                    if (doubling)
                        exp *= ManaitaPlusLegacyConfig.destroy_doubling_value;
                    if (ManaitaPlusLegacyToolBase.canPick(type,false)) {
                        player.giveExperiencePoints(exp);
                    } else {
                        block.popExperience(serverLevel, pos, exp);
                    }
                }
            } else if (level instanceof ClientLevel clientLevel) {

//                            Networking.INSTANCE.sendToServer(new MessageDes(pos));
//                mc.getTutorial().onDestroyBlock(clientLevel, pos, blockState, 1.0F);
                block.playerWillDestroy(level, pos, blockState, mc.player);

//                ManaitaPlusUtils.setBlock(level, mutableBlockPos, level.getFluidState(mutableBlockPos).createLegacyBlock(), 10);

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
