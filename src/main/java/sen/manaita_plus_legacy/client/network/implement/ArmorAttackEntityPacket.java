package sen.manaita_plus_legacy.client.network.implement;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.UUID;
import java.util.function.Supplier;

public class ArmorAttackEntityPacket {
    private final int targetId;
    private final UUID targetUuid;


    public ArmorAttackEntityPacket(FriendlyByteBuf buffer) {
        targetId = buffer.readInt();
        targetUuid = buffer.readUUID();
    }


    public ArmorAttackEntityPacket(Entity target) {
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
            ServerPlayer sender = ctx.get().getSender();
            if (!ManaitaPlusUtils.isManaita(sender)) return;
            Entity entity = sender.level().getEntity(targetId);
            if (entity == null) {
                if (sender.level() instanceof ServerLevel serverLevel) {
                    entity = serverLevel.entityManager.visibleEntityStorage.byUuid.get(targetUuid);
                    if (entity == null) return;
                }
            }
            Vec3 position = entity.getPosition(1.0F);
            Vec3 subtract = position.subtract(sender.getPosition(1.0F));
            entity.setDeltaMovement(subtract);
            sender.teleportTo(position.x, position.y, position.z);
            entity.hurt(sender.level().damageSources().playerAttack(sender), 10000);
        });
        ctx.get().setPacketHandled(true);
    }
}
