package sen.manaita_plus_legacy.common.item.tool;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import org.jetbrains.annotations.Nullable;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusDestroy;
import sen.manaita_plus_legacy.common.item.data.IManaitaPlusKey;
import sen.manaita_plus_legacy.common.item.tier.ManaitaPlusItemTier;
import sen.manaita_plus_legacy.common.util.ManaitaPlusText;
import sen.manaita_plus_legacy.common.util.ManaitaPlusUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ManaitaPlusPaxelItem extends DiggerItem implements IManaitaPlusKey, IManaitaPlusDestroy {
    public static final TagKey<Block> MINEABLE = BlockTags.create(new ResourceLocation("mineable"));
    public ManaitaPlusPaxelItem() {
        super(ManaitaPlusItemTier.MAX, ManaitaPlusItemTier.MAX, new ManaitaPlusItemTier(), MINEABLE, new Properties().fireResistant());
    }

    @Override
    public float getDestroySpeed(ItemStack p_41004_, BlockState p_41005_) {
        return this.speed;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return true;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return true;
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
    }

    @Override
    public boolean mineBlock(ItemStack p_40998_, Level p_40999_, BlockState p_41000_, BlockPos p_41001_, LivingEntity p_41002_) {
        return true;
    }

    @Override
    public boolean canAttackBlock(BlockState p_41441_, Level p_41442_, BlockPos p_41443_, Player p_41444_) {
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        return false;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        entity.hurt(entity.damageSources().playerAttack(player), 10000);
        if (entity instanceof LivingEntity living) {
            living.die(living.damageSources().playerAttack(player));
            living.setHealth(0F);
            living.deathTime = 15;
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        String range = String.valueOf(getRange(p_41421_));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.manaita_tool") + ": " + range + "x" + range + "x" + range)));
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_mode.formatting(I18n.get("mode.doubling") + ":" + (getDoubling(p_41421_) ? I18n.get("info.on") : I18n.get("info.off")))));
        p_41423_.add(Component.empty());
        p_41423_.add(Component.literal(ManaitaPlusText.manaita_infinity.formatting(I18n.get("info.attack"))));
    }


    public void onManaitaKeyPress(ItemStack itemStack, Player player) {
        boolean doubling = !getDoubling(itemStack);
        setDoubling(itemStack, doubling);
    }

    public void onManaitaKeyPressOnClient(ItemStack itemStack, Player player) {
        boolean doubling = !getDoubling(itemStack);
        setDoubling(itemStack, doubling);
        ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting(String.format("[%s] %s: %s", I18n.get("item.manaita_plus_legacy.manaita_paxel"),I18n.get("mode.doubling"), (doubling ? I18n.get("info.on") : I18n.get("info.off"))))));
    }

    public static boolean getDoubling(ItemStack itemStack) {
        if (itemStack.getTag() == null)
            itemStack.setTag(new CompoundTag());
        return itemStack.getTag().getBoolean("Doubling");
    }

    public static void setDoubling(ItemStack itemStack, boolean invisibility) {
        if (itemStack.getTag() == null)
            itemStack.setTag(new CompoundTag());
        itemStack.getTag().putBoolean("Doubling", invisibility);
    }

    @Override
    public boolean accept(BlockState state) {
        return true;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        ItemStack itemInHand = p_41433_.getItemInHand(p_41434_);
        if (!p_41432_.isClientSide) {
            if (p_41433_.isShiftKeyDown()) {
                setRange(itemInHand,(getRange(itemInHand) + 2) % 21);
            } else {
                Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
                enchantmentMap.put(Enchantments.BLOCK_FORTUNE, 10);
                String s =  I18n.get("enchantments.fortune");
                if (!EnchantmentHelper.hasSilkTouch(itemInHand)) {
                    enchantmentMap.put(Enchantments.SILK_TOUCH, 1);
                    s = I18n.get("enchantments.silktouch");
                }
                EnchantmentHelper.setEnchantments(enchantmentMap, itemInHand);
                ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_enchantment.formatting(itemInHand.getDisplayName().getString() + I18n.get("info.enchantment") + ": " + s)));
            }
        }
        return InteractionResultHolder.pass(itemInHand);
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        p_41404_.setPopTime(0);
    }

    public int getRange(ItemStack itemStack) {
        if (itemStack.getTag() == null)
            itemStack.setTag(new CompoundTag());

        int range = itemStack.getTag().getInt("Range");
        if (range == 0) {
            itemStack.getTag().putInt("Range", 1);
            return 1;
        }
        return range;
    }

    public void setRange(ItemStack itemStack,int range) {
        if (range == 0) range = 1;
        itemStack.getTag().putInt("Range", range);
        ManaitaPlusUtils.chat(Component.literal(ManaitaPlusText.manaita_mode.formatting("[" + I18n.get("item.manaita_plus.manaita_paxel") + "] " +I18n.get("mode.range.name") + ": " + range + "x" + range + "x" + range)));
    }

    @Override
    public InteractionResult useOn(UseOnContext p_41427_) {
        Level level = p_41427_.getLevel();
        BlockPos blockpos = p_41427_.getClickedPos();
        Player player = p_41427_.getPlayer();
        int i = getRange(p_41427_.getItemInHand()) >> 1;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int xM = blockpos.getX() + i;
        int yM = blockpos.getY() + i;
        int zM = blockpos.getZ() + i;
        boolean flag = false;
        for (int x = blockpos.getX() -i; x <= xM; x++) {
            for (int y = blockpos.getY() - i; y <= yM; y++) {
                for (int z = blockpos.getZ() - i; z <= zM; z++) {
                    mutableBlockPos.set(x, y, z);
                    BlockState blockstate = level.getBlockState(mutableBlockPos);
                    Optional<BlockState> optional = Optional.ofNullable(blockstate.getToolModifiedState(p_41427_, net.minecraftforge.common.ToolActions.AXE_STRIP, false));
                    Optional<BlockState> optional1 = optional.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(p_41427_, net.minecraftforge.common.ToolActions.AXE_SCRAPE, false));
                    Optional<BlockState> optional2 = optional.isPresent() || optional1.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(p_41427_, net.minecraftforge.common.ToolActions.AXE_WAX_OFF, false));
                    ItemStack itemstack = p_41427_.getItemInHand();
                    Optional<BlockState> optional3 = Optional.empty();
                    if (optional.isPresent()) {
                        level.playSound(player, mutableBlockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
                        optional3 = optional;
                    } else if (optional1.isPresent()) {
                        level.playSound(player, mutableBlockPos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.levelEvent(player, 3005, mutableBlockPos, 0);
                        optional3 = optional1;
                    } else if (optional2.isPresent()) {
                        level.playSound(player, mutableBlockPos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.levelEvent(player, 3004, mutableBlockPos, 0);
                        optional3 = optional2;
                    }

                    if (optional3.isPresent()) {
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, mutableBlockPos, itemstack);
                        }

                        level.setBlock(mutableBlockPos, optional3.get(), 11);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, mutableBlockPos, GameEvent.Context.of(player, optional3.get()));
                        if (player != null) {
                            itemstack.hurtAndBreak(1, player, (p_150686_) -> {
                                p_150686_.broadcastBreakEvent(p_41427_.getHand());
                            });
                        }
                        flag = true;
                    }



                    if (blockstate.getBlock() instanceof GrowingPlantHeadBlock growingplantheadblock) {
                        if (!growingplantheadblock.isMaxAge(blockstate)) {
                            if (player instanceof ServerPlayer) {
                                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, mutableBlockPos, itemstack);
                            }

                            level.playSound(player, mutableBlockPos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);
                            BlockState blockstate1 = growingplantheadblock.getMaxAgeState(blockstate);
                            level.setBlockAndUpdate(mutableBlockPos, blockstate1);
                            level.gameEvent(GameEvent.BLOCK_CHANGE, mutableBlockPos, GameEvent.Context.of(p_41427_.getPlayer(), blockstate1));
                            if (player != null) {
                                itemstack.hurtAndBreak(1, player, (p_186374_) -> {
                                    p_186374_.broadcastBreakEvent(p_41427_.getHand());
                                });
                            }

                            flag = true;
                        }
                    }
                    if (p_41427_.getClickedFace() != Direction.DOWN)  {
                        BlockState blockstate1 = blockstate.getToolModifiedState(p_41427_, net.minecraftforge.common.ToolActions.SHOVEL_FLATTEN, false);
                        BlockState blockstate2 = null;
                        if (blockstate1 != null && level.isEmptyBlock(mutableBlockPos.above())) {
                            level.playSound(player, mutableBlockPos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                            blockstate2 = blockstate1;
                        } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                            if (!level.isClientSide()) {
                                level.levelEvent(null, 1009, mutableBlockPos, 0);
                            }

                            CampfireBlock.dowse(p_41427_.getPlayer(), level, mutableBlockPos, blockstate);
                            blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.FALSE);
                        }

                        if (blockstate2 != null) {
                            if (!level.isClientSide) {
                                level.setBlock(mutableBlockPos, blockstate2, 11);
                                level.gameEvent(GameEvent.BLOCK_CHANGE, mutableBlockPos, GameEvent.Context.of(player, blockstate2));
                                if (player != null) {
                                    p_41427_.getItemInHand().hurtAndBreak(1, player, (p_43122_) -> {
                                        p_43122_.broadcastBreakEvent(p_41427_.getHand());
                                    });
                                }
                            }

                            flag = true;
                        }
                    }
                }
            }
        }
        return flag ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
    }
}