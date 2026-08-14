package sen.manaita_plus_legacy.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyItemCore;
import sen.manaita_plus_legacy.common.entity.ManaitaPlusLegacyLightningBolt;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.common.item.tier.ManaitaPlusLegacyToolTier;
import sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy.common.util.item.ManaitaPlusItemData;
import sen.manaita_plus_legacy.common.util.tag.ManaitaPlusLegacyTagData;
import sen.manaita_plus_legacy.common.util.text.ManaitaPlusText;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static sen.manaita_plus_legacy.common.core.ManaitaPlusLegacyEntityCore.ManaitaLightningBolt;

public class ManaitaPlusLegacyGodSwordItem extends SwordItem implements IManaitaPlusLegacyKey, IManaitaPlusLegacyDoubling {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    public ManaitaPlusLegacyGodSwordItem() {
        super(ManaitaPlusLegacyToolTier.INSTANCE, 0, 0, new Item.Properties().fireResistant());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", Double.POSITIVE_INFINITY, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", Double.POSITIVE_INFINITY, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        CompoundTag display = p_41404_.getTagElement("display");
        if (display != null && display.contains("Name", 8)) {
            try {
                Component component = Component.Serializer.fromJson(display.getString("Name"));
                if (component != null) {
                    if (component.getString().contains("寂灭")) {
                        Component newComponent = Component.literal("陨灭").setStyle(component.getStyle());
                        display.putString("Name", Component.Serializer.toJson(newComponent));
                        p_41404_.getOrCreateTag().put("display", display);
                    }
                }

            } catch (Exception exception) {}
        }
        if (p_41406_ instanceof Player player) {
            player.getCooldowns().removeCooldown(ManaitaPlusLegacyItemCore.ManaitaSwordGod.get());
            player.getAbilities().mayfly = true;
            player.setHealth(player.getMaxHealth());
            if (p_41404_.hasTag()) {
                if (isAntiDisarming(ManaitaPlusLegacyToolBase.getType(p_41404_))) {
//                    if (!ManaitaPlusItemData.stackList.contains(p_41404_)) ManaitaPlusItemData.stackList.add(p_41404_);
                    ManaitaPlusLegacyEntityData.anti.add(player);
                }
            }
        }
        ManaitaPlusLegacyEntityData.manaita.add(p_41406_);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && player.isShiftKeyDown()) {
            ManaitaPlusUtils.godKill(stack,player);
        }
        return false;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        ManaitaPlusUtils.attack(entity, player, getMode(stack));
        return true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level p_41432_, Player player, InteractionHand p_41434_) {
        ItemStack itemstack = player.getItemInHand(p_41434_);
        player.startUsingItem(p_41434_);
        if (!p_41432_.isClientSide) {
            Random random = new Random();
            Vec3 position = player.position();
            double minDist = 3.0;
            double maxDist = 65.0;
            minDist *= minDist * minDist;
            maxDist = maxDist * maxDist - minDist;
            for (int i = 0; i < 125; i++) {
                ManaitaPlusLegacyLightningBolt bolt = ManaitaLightningBolt.get().create(p_41432_);
                if (bolt != null) {
                    float angle = random.nextFloat() * 62.831852F;
                    double distance = Math.sqrt(minDist + random.nextDouble() * maxDist);

                    double x = Mth.sin(angle) * distance + position.x;
                    double z = Mth.cos(angle) * distance + position.z;

                    int y;
                    if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000) {
                        if (p_41432_.hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z))) {
                            LevelChunk chunk = p_41432_.getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z));
//                            if (!chunk.hasPrimedHeightmap(Heightmap.Types.WORLD_SURFACE_WG)) continue;
                            y = chunk
                                    .getHeight(Heightmap.Types.WORLD_SURFACE_WG, ((int)x) & 15, ((int)z) & 15)
                                    + 1;
                        } else {
                            y = p_41432_.getMinBuildHeight();
                        }
                    } else {
                        y = p_41432_.getSeaLevel() + 1;
                    }
                    bolt.setPos(x, y, z);
                    p_41432_.addFreshEntity(bolt);
                }
            }
        }
        ManaitaPlusUtils.godKill(itemstack,player);
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_43274_) {
        return p_43274_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43274_);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        int type = ManaitaPlusLegacyToolBase.getType(p_41421_);
        StringBuilder sb = new StringBuilder(I18n.get("mode.pick.name") + ": ");
        if (ManaitaPlusLegacyToolBase.canPick(type,true)) {
            sb.append(I18n.get("info.pick_items.name"));
            if (ManaitaPlusLegacyToolBase.canPick(type,false)) sb.append("&").append(I18n.get("info.pick_experience.name"));
        } else if (ManaitaPlusLegacyToolBase.canPick(type,false)) {
            sb.append(I18n.get("info.pick_experience.name"));
        } else {
            sb.append("None");
        }
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.doubling.name") + ":" + (isDoubling(type) ? I18n.get("info.on") : I18n.get("info.off")))));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.base.name") + ":" + getModeName(getMode(p_41421_)))));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.anti_disarming.name") + ":" + (isAntiDisarming(type) ? I18n.get("info.on") : I18n.get("info.off")))));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(sb.toString())));
        p_41423_.add(Component.empty());
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_enchantment.formatting(I18n.get("info.item.manaita_sword_god.1"))));
    }

    @Override
    public @NotNull Object getRenderPropertiesInternal() {
        return new IClientItemExtensions() {
            @Nullable
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (entityLiving.getUsedItemHand() == hand && entityLiving.getUseItemRemainingTicks() > 0)
                    return HumanoidModel.ArmPose.BLOCK;
                return null;
            }

            @Override
            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == (arm == HumanoidArm.LEFT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND)) {
                        int side = arm == HumanoidArm.RIGHT ? 1 : -1;
                        double f = Mth.sin(swingProcess * swingProcess * Mth.PI);
                        double f1 = Mth.sin(Mth.sqrt(swingProcess) * Mth.PI);
                        poseStack.translate(side * 0.56, -0.52 + equipProcess * -0.6, -0.72);
                        poseStack.translate(side * -0.1414214, 0.08, 0.1414214);
                        poseStack.mulPose(Axis.XP.rotationDegrees((float) (-102.25F - f1 * 80.0F)));
                        poseStack.mulPose(Axis.YP.rotationDegrees((float) (side * 13.365F - f * 20.0F)));
                        poseStack.mulPose(Axis.ZP.rotationDegrees((float) (side * 78.050003F - f1 * 20.0F)));
                        return true;
                }
                return false;
            }
        };
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        String input = I18n.get("item.manaita_sword_god.name");
        if (input.contains("寂灭")) {
            input = "陨灭";
        }
        return Component.literal(ManaitaPlusText.manaita_infinity.formatting(input));
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.CUSTOM;
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return true;
    }

    public void onManaitaKeyPress(ItemStack itemStack, Player player,int i) {
        if (i == 0) {
            int type = ManaitaPlusLegacyToolBase.getType(itemStack);
            if (player.isShiftKeyDown()) {
                changeMode(itemStack);
            } else {
                setDoubling(itemStack, type);
            }
        } else if (i == 2) {
            int type = ManaitaPlusLegacyToolBase.getType(itemStack);
            ManaitaPlusLegacyToolBase.setPickMode(itemStack, player.isShiftKeyDown(), type);
        } else if (i == 3) {
            boolean flag = ManaitaPlusLegacyTagData.toggleMode(itemStack,ManaitaPlusLegacyTagData.antiDisarming);
            if (flag) ManaitaPlusLegacyEntityData.anti.add(player);
            else ManaitaPlusLegacyEntityData.anti.remove(player);
        }
    }

    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player,int i) {
        if (i == 0) {
            if (player.isShiftKeyDown()) {
                ManaitaPlusUtils.Client.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(itemStack.getDisplayName().getString() + " " + I18n.get("mode.base.name") + ":" + getModeName(changeMode(itemStack)))));
            } else {
                ManaitaPlusUtils.Client.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(String.format("[%s] %s: %s", I18n.get("item.manaita_sword_god.name"), I18n.get("mode.doubling.name"), (setDoubling(itemStack, ManaitaPlusLegacyToolBase.getType(itemStack)) ? I18n.get("info.on") : I18n.get("info.off"))))));
            }
            ManaitaPlusItemData.current = itemStack;
        } else if (i == 2) {
            ManaitaPlusLegacyToolBase.setPickMode(itemStack, player.isShiftKeyDown(), ManaitaPlusLegacyToolBase.getType(itemStack));
            int type = ManaitaPlusLegacyToolBase.getType(itemStack);
            StringBuilder sb = new StringBuilder(I18n.get("mode.pick.name") + ": ");
            if (ManaitaPlusLegacyToolBase.canPick(type,true)) {
                sb.append(I18n.get("info.pick_items.name"));
                if (ManaitaPlusLegacyToolBase.canPick(type,false)) sb.append("&").append(I18n.get("info.pick_experience.name"));
            } else if (ManaitaPlusLegacyToolBase.canPick(type,false)) {
                sb.append(I18n.get("info.pick_experience.name"));
            } else {
                sb.append("None");
            }
            ManaitaPlusUtils.Client.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(itemStack.getDisplayName().getString() + " " + sb)));
            ManaitaPlusItemData.current = itemStack;
        } else if (i == 3) {
            boolean flag = ManaitaPlusLegacyTagData.toggleMode(itemStack,ManaitaPlusLegacyTagData.antiDisarming);
            if (flag) ManaitaPlusLegacyEntityData.anti.add(player);
            else ManaitaPlusLegacyEntityData.anti.remove(player);
//            if (!ManaitaPlusItemData.stackList.contains(itemStack)) ManaitaPlusItemData.stackList.add(itemStack);
            ManaitaPlusUtils.Client.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(itemStack.getDisplayName().getString() + " " + I18n.get("mode.anti_disarming.name") + ": " + (flag ? I18n.get("info.on") : I18n.get("info.off")))));
            ManaitaPlusItemData.current = itemStack;
        }
    }

    public static String getModeName(int mode) {
        if (mode == 0) {
            return I18n.get("mode.normal.name");
        } else if (mode == 1) {
            return I18n.get("mode.remove.name");
        }  else if (mode == 2) {
            return I18n.get("mode.down.name");
        }  else if (mode == 3) {
            return I18n.get("mode.fall.name");
        } else {
            return I18n.get("mode.normal.name");
        }
    }

    public static boolean isAntiDisarming(int type) {
        return (type & ManaitaPlusLegacyTagData.antiDisarming) != 0;
    }

    public static int getMode(ItemStack itemStack) {
        return itemStack.getTag() == null ? 0 : itemStack.getTag().getInt(ManaitaPlusLegacyTagData.Mode);
    }

    public static int changeMode(ItemStack itemStack) {
        CompoundTag orCreateTag = itemStack.getOrCreateTag();
        int p128407 = ((orCreateTag.getInt(ManaitaPlusLegacyTagData.Mode) + 1) % 4);
        orCreateTag.putInt(ManaitaPlusLegacyTagData.Mode, p128407);
        return p128407;
    }
}
