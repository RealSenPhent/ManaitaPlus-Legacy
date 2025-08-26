package sen.manaita_plus.common.loottable;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import sen.manaita_plus.common.core.ManaitaPlusItemCore;
import sen.manaita_plus.common.item.ManaitaPlusBowItem;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ManaitaPlusLootTable implements LootTableSubProvider, DataProvider {
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> p_249643_) {
        p_249643_.accept(
                new ResourceLocation("minecraft", "chests/end_city_treasure"),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(ManaitaPlusItemCore.ManaitaBow.get())
                                        .setWeight(2)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                                .when(LootItemRandomChanceCondition.randomChance(0.0027F)))
        );
    }

    @Override
    public CompletableFuture<?> run(CachedOutput p_236071_) {
        return null;
    }

    @Override
    public String getName() {
        return "";
    }
}
