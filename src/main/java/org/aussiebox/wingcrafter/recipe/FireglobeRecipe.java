package org.aussiebox.wingcrafter.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.block.blockentities.renderer.FireglobeBlockEntityRenderer;
import org.aussiebox.wingcrafter.component.FireglobeGlass;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;
import org.aussiebox.wingcrafter.item.ModItems;

import java.util.Map;

public class FireglobeRecipe extends SpecialCraftingRecipe {
    public FireglobeRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    private static ItemStack getBack(CraftingRecipeInput input) {
        return input.getStackInSlot(1, 0);
    }

    private static ItemStack getLeft(CraftingRecipeInput input) {
        return input.getStackInSlot(0, 1);
    }

    private static ItemStack getRight(CraftingRecipeInput input) {
        return input.getStackInSlot(2, 1);
    }

    private static ItemStack getFront(CraftingRecipeInput input) {
        return input.getStackInSlot(1, 2);
    }

    private static ItemStack getCenter(CraftingRecipeInput input) {
        return input.getStackInSlot(2, 2);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        return input.getWidth() == 3 && input.getHeight() == 3 && input.getStackCount() == 4
                && getBack(input).isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of(Wingcrafter.MOD_ID, "stained_glass_panes")))
                && getLeft(input).isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of(Wingcrafter.MOD_ID, "stained_glass_panes")))
                && getRight(input).isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of(Wingcrafter.MOD_ID, "stained_glass_panes")))
                && getFront(input).isIn(TagKey.of(RegistryKeys.ITEM, Identifier.of(Wingcrafter.MOD_ID, "stained_glass_panes")))
                && getCenter(input).isIn(ItemTags.CANDLES);
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        Map<Item, String> itemSpriteIDs = FireglobeBlockEntityRenderer.getItemSpriteIDs();
        ItemStack itemStack = new ItemStack(ModItems.FIREGLOBE);
        itemStack.set(ModDataComponentTypes.FIREGLOBE_GLASS, new FireglobeGlass(
            itemSpriteIDs.get(getFront(input).getItem()),
            itemSpriteIDs.get(getLeft(input).getItem()),
            itemSpriteIDs.get(getBack(input).getItem()),
            itemSpriteIDs.get(getRight(input).getItem())
        ));
        return itemStack;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return FireglobeRecipeSerializer.CRAFTING_FIREGLOBE;
    }
}
