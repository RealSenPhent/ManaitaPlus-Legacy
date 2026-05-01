package sen.manaita_plus_legacy.client.network.implement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;

import java.util.UUID;
import java.util.function.Supplier;

public class FarAttackEntityPacket {
    private final int targetId;
    private final UUID targetUuid;


    public FarAttackEntityPacket(FriendlyByteBuf buffer) {
        targetId = buffer.readInt();
        targetUuid = buffer.readUUID();
    }


    public FarAttackEntityPacket(Entity target) {
        this.targetId = target.getId();
        this.targetUuid = target.getUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(targetId);
        buf.writeUUID(targetUuid);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) return;
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> {
                ServerPlayer sender = ctx.get().getSender();
                ItemStack itemInHand = sender.getItemInHand(InteractionHand.MAIN_HAND);
                if (itemInHand.getItem() instanceof IManaitaPlusLegacyDoubling) {
                    Entity entity = sender.level().getEntity(targetId);
                    if (entity == null) {
                        if (sender.level() instanceof ServerLevel serverLevel) {
                            Entity entity1 = serverLevel.entityManager.visibleEntityStorage.byUuid.get(targetUuid);
                            itemInHand.getItem().onLeftClickEntity(itemInHand,sender,entity1);
                        }
                    } else {
                        itemInHand.getItem().onLeftClickEntity(itemInHand,sender,entity);
                    }
                }
                return null;
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
