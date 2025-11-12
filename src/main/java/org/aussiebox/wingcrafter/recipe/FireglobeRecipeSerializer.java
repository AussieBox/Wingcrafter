package org.aussiebox.wingcrafter.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

public class FireglobeRecipeSerializer implements RecipeSerializer<SpecialCraftingRecipe> {
    private FireglobeRecipeSerializer() {
    }

    public static final FireglobeRecipeSerializer INSTANCE = new FireglobeRecipeSerializer();
    public static final Identifier ID = Identifier.of(Wingcrafter.MOD_ID, "crafting_fireglobe");

    public static RecipeSerializer<FireglobeRecipe> CRAFTING_FIREGLOBE = register(
            "crafting_fireglobe", new SpecialCraftingRecipe.SpecialRecipeSerializer<>(FireglobeRecipe::new)
    );

    static <S extends RecipeSerializer<T>, T extends SpecialCraftingRecipe> S register(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, id, serializer);
    }

    @Override
    public MapCodec<SpecialCraftingRecipe> codec() {
        return null;
    }

    @Override
    public PacketCodec<RegistryByteBuf, SpecialCraftingRecipe> packetCodec() {
        return null;
    }
}
