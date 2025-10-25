package sen.manaita_plus_legacy.common.recipe.ingredient;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import sen.manaita_plus_legacy.common.recipe.ManaitaPlusLegacyNBTCraftingRecipe;
import sen.manaita_plus_legacy.common.util.ManaitaPlusLegacyNBTData;

import java.util.stream.Stream;

public class ManaitaPlusLegacyNBTIngredient extends Ingredient {

    public ManaitaPlusLegacyNBTIngredient(Stream<? extends Value> p_43907_) {
        super(p_43907_);
    }

    @Override
    public boolean test(@org.jetbrains.annotations.Nullable ItemStack p_43914_) {
        if (p_43914_ == null) {
            return false;
        } else if (this.isEmpty()) {
            return p_43914_.isEmpty();
        } else {
            for(ItemStack itemstack : this.getItems()) {
                if (itemstack.hasTag() && p_43914_.hasTag()) {
                    assert itemstack.getTag() != null;
                    assert p_43914_.getTag() != null;
                    int manaitaType = itemstack.getTag().getInt(ManaitaPlusLegacyNBTData.ItemType);
                    if (manaitaType != 0 && (!p_43914_.hasTag() || manaitaType != p_43914_.getTag().getInt(ManaitaPlusLegacyNBTData.ItemType))) continue;
                }
                if (itemstack.is(p_43914_.getItem())) {
                    return true;
                }
            }

            return false;
        }
    }


    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer()
    {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements IIngredientSerializer<ManaitaPlusLegacyNBTIngredient>
    {
        public static final Serializer INSTANCE = new Serializer();

        public ManaitaPlusLegacyNBTIngredient parse(FriendlyByteBuf buffer)
        {
            return ManaitaPlusLegacyNBTCraftingRecipe.Serializer.fromValues(Stream.generate(() -> new Ingredient.ItemValue(buffer.readItem())).limit(buffer.readVarInt()));
        }

        public ManaitaPlusLegacyNBTIngredient parse(JsonObject json)
        {
            return ManaitaPlusLegacyNBTCraftingRecipe.Serializer.fromValues(Stream.of(Ingredient.valueFromJson(json)));
        }

        public void write(FriendlyByteBuf buffer, ManaitaPlusLegacyNBTIngredient ingredient)
        {
            ItemStack[] items = ingredient.getItems();
            buffer.writeVarInt(items.length);

            for (ItemStack stack : items)
                buffer.writeItem(stack);
        }
    }
}