package sen.manaita_plus_legacy.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import sen.manaita_plus_legacy.ManaitaPlus;
import sen.manaita_plus_legacy.common.network.data.MessageKey;
import sen.manaita_plus_legacy.common.network.data.MessageDestroy;

public class Networking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ++ID;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(ManaitaPlus.MODID, "manaita_networking"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );
        INSTANCE.messageBuilder(MessageKey.class, nextID())
                .encoder(MessageKey::toBytes)
                .decoder(MessageKey::new)
                .consumerNetworkThread(MessageKey::handler)
                .add();
        INSTANCE.messageBuilder(MessageDestroy.class, nextID())
                .encoder(MessageDestroy::toBytes)
                .decoder(MessageDestroy::new)
                .consumerNetworkThread(MessageDestroy::handler)
                .add();

    }
}
