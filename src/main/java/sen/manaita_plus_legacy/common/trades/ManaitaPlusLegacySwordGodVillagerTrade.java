package sen.manaita_plus_legacy.common.trades;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

public  class ManaitaPlusLegacySwordGodVillagerTrade implements VillagerTrades.ItemListing {
    private final ItemStack input;
    private final ItemStack input1 = new ItemStack(Items.NETHER_STAR);
    private final ItemStack output;
    private final int maxUses;
    private final int xp;
    private final float priceMultiplier;

    public ManaitaPlusLegacySwordGodVillagerTrade(ItemStack input, ItemStack output, int maxUses, int xp, float priceMultiplier) {
        this.input = input;
        this.output = output;
        this.maxUses = maxUses;
        this.xp = xp;
        this.priceMultiplier = priceMultiplier;
    }

    @Override
    public MerchantOffer getOffer(Entity trader, RandomSource rand) {
        return new MerchantOffer(
                input.copy(),
                input1.copy(),
                output.copy(),
                maxUses,
                xp,
                priceMultiplier
        );
    }
}
