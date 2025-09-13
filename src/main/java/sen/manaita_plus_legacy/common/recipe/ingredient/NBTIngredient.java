package sen.manaita_plus_legacy.common.recipe.ingredient;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import sen.manaita_plus_legacy.common.recipe.ManaitaPlusNBTCraftingRecipe;

import java.util.stream.Stream;

public class NBTIngredient extends Ingredient {

    public NBTIngredient(Stream<? extends Value> p_43907_) {
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
                if (itemstack.hasTag()) {
                    int manaitaType = itemstack.getTag().getInt("ManaitaType");
                    if (manaitaType != 0 && (!p_43914_.hasTag() || manaitaType != p_43914_.getTag().getInt("ManaitaType"))) continue;
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

    public static class Serializer implements IIngredientSerializer<NBTIngredient>
    {
        public static final Serializer INSTANCE = new Serializer();

        public NBTIngredient parse(FriendlyByteBuf buffer)
        {
            return ManaitaPlusNBTCraftingRecipe.Serializer.fromValues(Stream.generate(() -> new Ingredient.ItemValue(buffer.readItem())).limit(buffer.readVarInt()));
        }

        public NBTIngredient parse(JsonObject json)
        {
            return ManaitaPlusNBTCraftingRecipe.Serializer.fromValues(Stream.of(Ingredient.valueFromJson(json)));
        }

        public void write(FriendlyByteBuf buffer, NBTIngredient ingredient)
        {
            ItemStack[] items = ingredient.getItems();
            buffer.writeVarInt(items.length);

            for (ItemStack stack : items)
                buffer.writeItem(stack);
        }
    }
}