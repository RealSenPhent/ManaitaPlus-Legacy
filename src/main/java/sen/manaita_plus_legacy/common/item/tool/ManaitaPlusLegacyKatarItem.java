package sen.manaita_plus_legacy.common.item.tool;

import com.google.common.collect.Multimap;
import moze_intel.projecte.api.capabilities.item.IExtraFunction;
import moze_intel.projecte.capability.ExtraFunctionItemCapabilityWrapper;
import moze_intel.projecte.capability.ModeChangerItemCapabilityWrapper;
import moze_intel.projecte.config.ProjectEConfig;
import moze_intel.projecte.gameObjs.EnumMatterType;
import moze_intel.projecte.gameObjs.PETags;
import moze_intel.projecte.gameObjs.items.IItemMode;
import moze_intel.projecte.gameObjs.items.ItemPE;
import moze_intel.projecte.gameObjs.items.tools.PETool;
import moze_intel.projecte.gameObjs.registries.PEDamageTypes;
import moze_intel.projecte.gameObjs.registries.PESoundEvents;
import moze_intel.projecte.utils.ItemHelper;
import moze_intel.projecte.utils.PlayerHelper;
import moze_intel.projecte.utils.ToolHelper;
import moze_intel.projecte.utils.text.ILangEntry;
import moze_intel.projecte.utils.text.PELang;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.common.item.tool.base.ManaitaPlusLegacyToolBase;
import sen.manaita_plus_legacy.common.network.Networking;
import sen.manaita_plus_legacy.common.network.implement.ChangeDeathDataPacket;
import sen.manaita_plus_legacy.common.util.EnumMatterTypeAdder;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;
import sen.manaita_plus_legacy.common.util.entity.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy.common.util.text.ManaitaPlusText;

import java.util.*;
import java.util.function.Predicate;

public class ManaitaPlusLegacyKatarItem extends PETool implements IItemMode, IExtraFunction, IManaitaPlusLegacyKey {
    private final ToolHelper.ChargeAttributeCache attributeCache = new ToolHelper.ChargeAttributeCache();
    private final ILangEntry[] modeDesc;

    public ManaitaPlusLegacyKatarItem(int numCharges, Item.Properties props) {
        super(EnumMatterTypeAdder.addMatterType("manaita","manaita_plus_legacy",Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY,10000F,10000,null,EnumMatterType.RED_MATTER,null, MapColor.COLOR_BLACK), PETags.Blocks.MINEABLE_WITH_PE_KATAR, 1000000, 1000000, numCharges, props);
        modeDesc = new ILangEntry[]{PELang.MODE_KATAR_1, PELang.MODE_KATAR_2};
        addItemCapability(ModeChangerItemCapabilityWrapper::new);
        addItemCapability(ExtraFunctionItemCapabilityWrapper::new);
    }

    @Override
    public ILangEntry[] getModeLangEntries() {
        return modeDesc;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltips, @NotNull TooltipFlag flags) {
        super.appendHoverText(stack, level, tooltips, flags);
        tooltips.add(getToolTip(stack));
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_SHEARS_ACTIONS.contains(toolAction) ||
                ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_HOE_ACTIONS.contains(toolAction) ||
                ToolHelper.DEFAULT_PE_KATAR_ACTIONS.contains(toolAction);
    }

    @NotNull
    @Override
    public AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
        int charge = getCharge(stack);
        return target.getBoundingBox().inflate(charge, charge / 4D, charge);
    }

    @Override
    protected float getShortCutDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        float destroySpeed = super.getShortCutDestroySpeed(stack, state);
        if (destroySpeed == 1) {
            //Special handling for swords which still have hardcoded material checks
            // Note: we don't bother with the cobweb check because that will get caught by the tag for the blocks we can mine,
            // but we do need to include the material based checks that vanilla's sword still has
            if (state.is(BlockTags.SWORD_EFFICIENT)) {
                return 1.5F;
            }
        }
        return destroySpeed;
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        //Order that it attempts to use the item:
        // Strip logs, hoe ground, carve pumpkin, shear beehive, AOE remove logs, AOE remove leaves
        return ToolHelper.performActions(ToolHelper.stripLogsAOE(context, state, 0),
                () -> ToolHelper.scrapeAOE(context, state, 0),
                () -> ToolHelper.waxOffAOE(context, state, 0),
                () -> ToolHelper.tillAOE(context, state, 0),
                () -> {
                    if (state.is(BlockTags.LOGS)) {
                        //Mass clear (acting as an axe)
                        //Note: We already tried to strip the log in an earlier action
                        return ToolHelper.clearTagAOE(level, player, context.getHand(), context.getItemInHand(), 0, BlockTags.LOGS);
                    }
                    return InteractionResult.PASS;
                }, () -> {
                    if (state.is(BlockTags.LEAVES)) {
                        //Mass clear (acting as shears)
                        return ToolHelper.clearTagAOE(level, player, context.getHand(), context.getItemInHand(), 0, BlockTags.LEAVES);
                    }
                    return InteractionResult.PASS;
                });
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity damaged, @NotNull LivingEntity damager) {
        ToolHelper.attackWithCharge(stack, damaged, damager, 1.0F);
        ManaitaPlusLegacyEntityData.death.add(damaged);
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
       return ToolHelper.shearBlock(stack, pos, player).consumesAction();
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return ItemHelper.actionResultFromType(ToolHelper.shearEntityAOE(player, hand, 0), player.getItemInHand(hand));
    }

    @Override
    public boolean doExtraFunction(@NotNull ItemStack stack, @NotNull Player player, InteractionHand hand) {
        attackAOE(stack, player, getMode(stack) == 1, Float.POSITIVE_INFINITY, 0, hand);
        return true;
    }

    private static final Predicate<Entity> SLAY_MOB = entity -> !entity.isSpectator() && entity instanceof Enemy;
    private static final Predicate<Entity> SLAY_ALL = entity -> !entity.isSpectator() && (entity instanceof Enemy || entity instanceof LivingEntity);


    public void attackAOE(ItemStack stack, Player player, boolean slayAll, float damage, long emcCost, InteractionHand hand) {
        Level level = player.level();
            int charge = getCharge(stack);
            List<Entity> toAttack = level.getEntities(player, player.getBoundingBox().inflate(2.5F * (float) charge), slayAll ? SLAY_ALL : SLAY_MOB);
            DamageSource src = PEDamageTypes.BYPASS_ARMOR_PLAYER_ATTACK.source(player);
            boolean hasAction = false;

            for (Entity entity : toAttack) {
                if (!ItemPE.consumeFuel(player, stack, emcCost, true)) {
                    break;
                }
                if (!level.isClientSide) {
                    if (entity instanceof LivingEntity living) {
                        living.setLastHurtByPlayer(player);
                        entity.hurt(src, damage);
                        if (living.captureDrops() != null) living.die(living.damageSources().playerAttack(player));
                        for (SynchedEntityData.DataItem value : living.entityData.itemsById.values()) {
                            if (value.getValue().getClass() == Float.class) {
                                living.entityData.set(value.getAccessor(),0.0F);
                                value.setValue((0.0F));
                            }
                        }
                    }
                    if (entity instanceof ServerPlayer serverPlayer) {
                        Networking.sendToPlayer(serverPlayer, new ChangeDeathDataPacket(1));
                    }
                }
                ManaitaPlusLegacyEntityData.death.add(entity);
                hasAction = true;
            }
            if (hasAction) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(), PESoundEvents.CHARGE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                PlayerHelper.swingItem(player, hand);
            }

    }

    @NotNull
    @Override
    public UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 72_000;
    }

    @NotNull
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@NotNull EquipmentSlot slot, ItemStack stack) {
        return attributeCache.addChargeAttributeModifier(super.getAttributeModifiers(slot, stack), slot, stack);
    }

    /**
     * Copy of {@link net.minecraft.world.item.ShearsItem#interactLivingEntity(ItemStack, Player, LivingEntity, InteractionHand)}
     */
    @NotNull
    @Override
    public InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        if (entity instanceof IForgeShearable target) {
            BlockPos pos = entity.blockPosition();
            if (target.isShearable(stack, entity.level(), pos)) {
                if (!entity.level().isClientSide) {
                    List<ItemStack> drops = target.onSheared(player, stack, entity.level(), pos, stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE));
                    Random rand = new Random();
                    drops.forEach(d -> {
                        ItemEntity ent = entity.spawnAtLocation(d, 1.0F);
                        if (ent != null) {
                            ent.setDeltaMovement(ent.getDeltaMovement().add((rand.nextFloat() - rand.nextFloat()) * 0.1F, rand.nextFloat() * 0.05F,
                                    (rand.nextFloat() - rand.nextFloat()) * 0.1F));
                        }
                    });
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onManaitaKeyPress(ItemStack paramItemStack, Player paramEntityPlayer, int i) {

    }

    @Override
    public void onManaitaKeyPressOnClient(ItemStack paramItemStack, Player paramEntityPlayer, int i) {

    }
}