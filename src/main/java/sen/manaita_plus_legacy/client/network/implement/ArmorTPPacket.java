package sen.manaita_plus_legacy.client.network.implement;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.NetworkEvent;
import org.objectweb.asm.Opcodes;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.common.network.Networking;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.function.Supplier;

public class ArmorTPPacket {
    public ArmorTPPacket(FriendlyByteBuf buffer) {

    }


    public ArmorTPPacket() {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) return;
            ServerPlayer sender = ctx.get().getSender();
            if (ManaitaPlusUtils.isManaita(sender)) {
                Level level = sender.level();
//                level.getChunkAt(targetPos).

                double d0 = 256;
                HitResult hitResult = sender.pick(d0, 1.0F, false);

                Vec3 vec3 = sender.getEyePosition(1.0F);
                d0 *= d0;
                if (hitResult.getType() != HitResult.Type.MISS) {
                    d0 = hitResult.getLocation().distanceToSqr(vec3);
                }

                Vec3 vec31 = sender.getViewVector(1.0F);
                Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);

                AABB aabb = sender.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
                EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(sender, vec3, vec32, aabb, (p_234237_) -> true, d0);
                if (entityhitresult == null) {
                    if (hitResult instanceof BlockHitResult blockHitResult) {
                        BlockPos targetPos = blockHitResult.getBlockPos();
                        Direction direction = blockHitResult.getDirection();
                        BlockState blockState = level.getBlockState(targetPos);
                        if (blockState.getBlock() != Blocks.AIR) {
                            if (direction == Direction.UP) {
                                if (sender.isShiftKeyDown()) {
                                    blockState = level.getBlockState(targetPos.below());
                                    if (blockState.getBlock() == Blocks.AIR) {
                                        sender.teleportTo(targetPos.getX() + 0.5, targetPos.getY() - 1, targetPos.getZ() + 0.5);
                                    } else {
                                        sender.teleportTo(targetPos.getX() + 0.5, sender.getY() - 1, targetPos.getZ() + 0.5);
                                    }
                                } else {
                                    blockState = level.getBlockState(targetPos.above(2));
                                    if (blockState.getBlock() == Blocks.AIR) {
                                        sender.teleportTo(targetPos.getX() + 0.5, targetPos.getY() + 1, targetPos.getZ() + 0.5);
                                    }
                                }
                            } else if (direction == Direction.DOWN) {
                                blockState = level.getBlockState(targetPos.above(2));
                                if (blockState.getBlock() == Blocks.AIR) {
                                    sender.teleportTo(targetPos.getX() + 0.5, targetPos.getY() + 1, targetPos.getZ() + 0.5);
                                } else {
                                    sender.teleportTo(targetPos.getX() + 0.5, sender.getY()  + 1, targetPos.getZ() + 0.5);
                                }
                            } else {
                                if (level.getBlockState(targetPos.above(2)).getBlock() == Blocks.AIR) {
                                    sender.teleportTo(targetPos.getX() + 0.5, targetPos.getY() + 1, targetPos.getZ() + 0.5);
                                } else if (level.getBlockState(targetPos.above(1)).getBlock() == Blocks.AIR) {
                                    sender.teleportTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5);
                                } else if (level.getBlockState(BlockPos.containing(sender.position())).getBlock() != Blocks.AIR) {
                                    Direction direction1 = direction.getOpposite();
                                    int stepX = direction1.getStepX();
                                    int stepZ = direction1.getStepZ();
                                    float p_121949_ = 1F;

                                    sender.teleportTo(
                                            targetPos.getX() + (0.5) + (stepX * p_121949_),
                                            targetPos.getY() - 1,
                                            targetPos.getZ() + (0.5) + (stepZ * p_121949_)
                                    );
                                } else {
                                    Direction direction1 = sender.isShiftKeyDown() ? direction.getOpposite() : direction;
                                    int stepX = direction1.getStepX();
                                    int stepZ = direction1.getStepZ();
                                    float p_121949_ = 0.8F;

                                    sender.teleportTo(
                                            targetPos.getX() + (0.5) + (stepX * p_121949_),
                                            targetPos.getY(),
                                            targetPos.getZ() + (0.5) + (stepZ * p_121949_)
                                    );
                                }
                            }
                        }
                    }
                } else {
                    Entity entity = entityhitresult.getEntity();
                    Vec3 position = entity.getPosition(1.0F);
                    Vec3 position1 = sender.getPosition(1.0F);
                    Vec3 subtract = position.subtract(position1);
                    if (sender.isShiftKeyDown() || entity.getType().getCategory() == MobCategory.MONSTER) entity.hurt(sender.level().damageSources().playerAttack(sender), (float) position.distanceToSqr(position1) * 1000F);
                    entity.setDeltaMovement(subtract);
                    sender.teleportTo(position.x, position.y, position.z);
                    if (entity instanceof LivingEntity living) {
                        living.setTicksFrozen(0);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

//    public static void teleportTo(Player player,double x, double y, double z) {
////        player.getBoundingBox().expandTowards(3,3,3).move()
//        player.teleportTo(x, y, z);
//    }
}
