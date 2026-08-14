package sen.manaita_plus_legacy.client.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.OnlyIns;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDestroy;
import sen.manaita_plus_legacy.common.network.implement.*;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy_core.util.ClientEventUtil;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ClientPacketHandlers {

    public static void handler(ChangeDeathDataPacket packet) {
        Entity entity = Minecraft.getInstance().player;
        ClientEventUtil.htaedTime = 20;
        if (entity == null) return;
        if (packet.flag == 1) {
            ManaitaPlusLegacyEntityData.death.add(entity);
        } else {
            ManaitaPlusLegacyEntityData.death.remove(entity);
        }
    }

    public static void handler(DestroyFTBBlockPacket packet) {
        Entity entity = Minecraft.getInstance().player;
        if (entity == null) return;
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null || mc.player == null) return;
        if (packet.item instanceof IManaitaPlusLegacyDestroy des) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            List<Integer> blockPos = packet.blockPos;
            for (int i = 0; i < blockPos.size(); i += 3) {
                BlockState blockState = level.getBlockState(mutableBlockPos.set(blockPos.get(i), blockPos.get(i + 1), blockPos.get(i + 2)));
                if (blockState == null || !des.accept(blockState))
                    continue;
                Block block = blockState.getBlock();
//                block.playerWillDestroy(level, mutableBlockPos, blockState, mc.player);

                ManaitaPlusUtils.setBlock(level, mutableBlockPos, level.getFluidState(mutableBlockPos).createLegacyBlock(), 10);

                SoundType soundtype = blockState.getSoundType(level, mutableBlockPos, mc.player);
                mc.getSoundManager().play(new SimpleSoundInstance(soundtype.getHitSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 8.0F, soundtype.getPitch() * 0.5F, SoundInstance.createUnseededRandom(), mutableBlockPos));
//                            blockState.onDestroyedByPlayer(level, pos, mc.player, false, level.getFluidState(pos));
            }
        }
    }

    public static void handler(ChangeEntitiesIDDataPacket packet) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            for (ManaitaPlusLegacyEntityData value : ManaitaPlusLegacyEntityData.values()) {
                if (value.getFlag() == packet.flag) {
                    for (int id : packet.ids) {
                        value.getIdBooleanMap().put(id, Boolean.TRUE);
                    }
                }
            }
            return;
        }
        for (ManaitaPlusLegacyEntityData value : ManaitaPlusLegacyEntityData.values()) {
            if (value.getFlag() == packet.flag) {
                for (int id : packet.ids) {
                    Entity entity = level.getEntity(id);
                    if (entity == null) {
                        value.getIdBooleanMap().put(id, Boolean.TRUE);
                        continue;
                    }
                    value.add(entity);
                }
                break;
            }
        }
    }

    public static void handler(ChangeEntitiesUUIDDataPacket packet) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            for (ManaitaPlusLegacyEntityData value : ManaitaPlusLegacyEntityData.values()) {
                if (value.getFlag() == packet.flag) {
                    for (UUID uuid : packet.uuids) {
                        value.getUuidBooleanMap().put(uuid, Boolean.TRUE);
                    }
                    break;
                }
            }
            return;
        }
        for (ManaitaPlusLegacyEntityData value : ManaitaPlusLegacyEntityData.values()) {
            if (value.getFlag() == packet.flag) {
                for (UUID uuid : packet.uuids) {
                    Entity entity = level.entityStorage.entityGetter.get(uuid);
                    if (entity == null) {
                        value.getUuidBooleanMap().put(uuid, Boolean.TRUE);
                        continue;
                    }
                    value.add(entity);
                }
                break;
            }
        }
    }

    public static void handler(ChangeEntityDataPacket packet) {
        if (packet.id == null && packet.flag == 0) {
            for (ManaitaPlusLegacyEntityData value : ManaitaPlusLegacyEntityData.values()) {
                value.clear();
            }
        }
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || packet.id == null) return;
        Entity entity = level.entityStorage.entityGetter.get(packet.id);
        if (entity == null) return;
        boolean remove = packet.flag < 0;
        for (ManaitaPlusLegacyEntityData entityList : ManaitaPlusLegacyEntityData.values()) {
            if ((entityList.getFlag() & packet.flag) != 0) {
                if (remove) {
                    entityList.remove(entity);
                } else {
                    entityList.add(entity);
                }
            }
        }
    }

    public static void handler(DestroyBlockPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null || mc.player == null) return;
        if (packet.item instanceof IManaitaPlusLegacyDestroy des) {
            int xM = packet.blockPos.getX() + packet.range;
            int yM = packet.blockPos.getY() + packet.range;
            int zM = packet.blockPos.getZ() + packet.range;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (int x = packet.blockPos.getX() - packet.range; x <= xM; x++) {
                for (int y = packet.blockPos.getY() - packet.range; y <= yM; y++) {
                    for (int z = packet.blockPos.getZ() - packet.range; z <= zM; z++) {
                        BlockState blockState = level.getBlockState(mutableBlockPos.set(x, y, z));
                        if (blockState == null || !des.accept(blockState))
                            continue;
                        Block block = blockState.getBlock();
                        block.playerWillDestroy(level, mutableBlockPos, blockState, mc.player);

//                        ManaitaPlusUtils.setBlock(level, mutableBlockPos, level.getFluidState(mutableBlockPos).createLegacyBlock(), 10);

                        SoundType soundtype = blockState.getSoundType(level, mutableBlockPos, mc.player);
                        mc.getSoundManager().play(new SimpleSoundInstance(soundtype.getHitSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 8.0F, soundtype.getPitch() * 0.5F, SoundInstance.createUnseededRandom(), mutableBlockPos));
//                            blockState.onDestroyedByPlayer(level, pos, mc.player, false, level.getFluidState(pos));
                    }
                }
            }
        }
    }
}
