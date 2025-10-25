package sen.manaita_plus_legacy.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.common.network.client.KeyPressPacket;
import sen.manaita_plus_legacy.common.network.server.DestroyBlockPacket;
import sen.manaita_plus_legacy.common.network.server.ChangeEntityDataPacket;

public class Networking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ++ID;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(ManaitaPlusLegacy.MODID, "manaita_networking"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        INSTANCE.messageBuilder(KeyPressPacket.class, nextID())
                .encoder(KeyPressPacket::toBytes)
                .decoder(KeyPressPacket::new)
                .consumerNetworkThread(KeyPressPacket::handler)
                .add();
        INSTANCE.messageBuilder(DestroyBlockPacket.class, nextID())
                .encoder(DestroyBlockPacket::toBytes)
                .decoder(DestroyBlockPacket::new)
                .consumerNetworkThread(DestroyBlockPacket::handler)
                .add();
        INSTANCE.messageBuilder(ChangeEntityDataPacket.class, nextID())
                .encoder(ChangeEntityDataPacket::toBytes)
                .decoder(ChangeEntityDataPacket::new)
                .consumerNetworkThread(ChangeEntityDataPacket::handler)
                .add();
    }

    public static void sendToSameLevelPlayers(Level level,Object packet) {
        if (level instanceof ServerLevel serverLevel)
            serverLevel.getPlayers(p -> {
            Networking.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> p),
                    packet
            );
            return false;
        });
    }

    public static void sendToNearByPlayers(Level level,Object packet,int range) {
        if (level instanceof ServerLevel serverLevel) {
            int finalRange = range * range;
            serverLevel.getPlayers(p -> {
                if (p.distanceToSqr(p) <= finalRange)
                    Networking.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> p),
                        packet
                );
                return false;
            });
        }
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

    public static void sendToTrackBySeen(Level level, Player player, Object packet) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().broadcast(player,INSTANCE.toVanillaPacket(packet, NetworkDirection.PLAY_TO_CLIENT));
        }
    }
}
