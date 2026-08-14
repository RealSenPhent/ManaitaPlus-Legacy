package sen.manaita_plus_legacy_core.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import sen.manaita_plus_legacy.common.curios.CuriosUtil;
import sen.manaita_plus_legacy.common.item.ManaitaPlusLegacyGodSwordItem;
import sen.manaita_plus_legacy.common.item.ManaitaPlusLegacySourceItem;
import sen.manaita_plus_legacy.common.item.curios.CuriosSourceItem;
import sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase;
import sen.manaita_plus_legacy.common.network.Networking;
import sen.manaita_plus_legacy.client.network.implement.PreventDropPacket;
import sen.manaita_plus_legacy.common.proxy.CommomProxy;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntities;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy.common.util.item.ManaitaPlusItemData;
import sen.manaita_plus_legacy.common.util.item.ManaitaPlusLegacyItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class EventUtil {
    public static final Vec3 remove = new Vec3(Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE);
    public static double getAttributeValue(LivingEntity living, Attribute p_21134_) {
        double value = living.getAttributes().getValue(p_21134_);
        if (p_21134_ == Attributes.MAX_HEALTH && value < 20.0D) {
            living.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
            return 20.0D;
        } else if (p_21134_ == Attributes.MOVEMENT_SPEED && value < 0.1D) {
            living.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.15D);
            return 0.15D;
        } else if (p_21134_ == Attributes.FLYING_SPEED && value < 0.05D) {
            living.getAttribute(Attributes.FLYING_SPEED).setBaseValue(0.075D);
            return 0.075D;
        }
        return value;
    }

    public static boolean isManaita(LivingEntity living) {
        return /*living instanceof Player player && ManaitaPlusUtils.isManaita(player) || */ManaitaPlusLegacyEntityData.manaita.accept(living);
    }


    public static boolean isManaita(Entity entity) {
        return /*entity instanceof Player player && ManaitaPlusUtils.isManaita(player) ||*/ ManaitaPlusLegacyEntityData.manaita.accept(entity);
    }

    public static boolean isDead(LivingEntity living) {
        return ManaitaPlusLegacyEntityData.death.accept(living);
    }

    public static boolean isDead(Entity entity) {
        return ManaitaPlusLegacyEntityData.death.accept(entity);
    }


    public static boolean isDead(Object o) {
        return o instanceof Entity entity && ManaitaPlusLegacyEntityData.death.accept(entity);
    }

    public static boolean isRemove(Object o) {
        return o instanceof Entity entity && ManaitaPlusLegacyEntityData.remove.accept(entity);
    }

    public static boolean isRemove(EntityAccess o) {
        return o instanceof Entity entity && ManaitaPlusLegacyEntityData.remove.accept(entity);
    }

    public static boolean isRemove(LivingEntity localPlayer) {
        return ManaitaPlusLegacyEntityData.remove.accept(localPlayer);
    }

    public static boolean isRemove(Entity entity) {
        return ManaitaPlusLegacyEntityData.remove.accept(entity);
    }

    public static boolean check = false;
    public static boolean isDown(Object o) {
        check = true;
        boolean b = o instanceof Entity entity && ManaitaPlusLegacyEntityData.down.accept(entity);
        check = false;
        return b;
    }

    public static boolean isDown(EntityAccess o) {
        check = true;
        boolean b = o instanceof Entity entity && ManaitaPlusLegacyEntityData.down.accept(entity);
        check = false;
        return b;
    }

    public static boolean isDown(LivingEntity localPlayer) {
        check = true;
        boolean accept = ManaitaPlusLegacyEntityData.down.accept(localPlayer);
        check = false;
        return accept;
    }

    public static boolean isDown(Entity entity) {
        return ManaitaPlusLegacyEntityData.down.accept(entity);
    }

   public static boolean isFall(Object o) {
        return ManaitaPlusLegacyEntities.fall.accept(o.getClass().getName());
    }

    public static float getMaxHealth(LivingEntity living) {
        float attributeValue = (float) living.getAttributeValue(Attributes.MAX_HEALTH);
        if (attributeValue < 20.0F) {
            living.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
            return 20.0F;
        }

        return attributeValue;
    }

    public static boolean canHurt(ServerPlayer player) {
        if (player != null) {
            Inventory inventory = player.getInventory();
            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack itemstack = inventory.getItem(i);
                if (itemstack != null && itemstack.getItem() instanceof ManaitaPlusLegacySourceItem) {
                    return false;
                }
            }
            if (CommomProxy.curios) {
                return !CuriosSourceItem.inCurios(player);
            }
        }
        return true;
    }




}
