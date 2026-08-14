package sen.manaita_plus_legacy.common.network.implement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.function.Supplier;

public class InformationPacket {
    public final String context;

    public InformationPacket(FriendlyByteBuf buffer) {
        context = buffer.readUtf();
    }


    public InformationPacket(String context) {
        this.context = context;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(context);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ManaitaPlusUtils.Client.chat(context)));
        ctx.get().setPacketHandled(true);
    }
}
