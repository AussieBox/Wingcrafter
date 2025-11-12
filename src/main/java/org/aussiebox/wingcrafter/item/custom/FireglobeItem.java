package org.aussiebox.wingcrafter.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.aussiebox.wingcrafter.block.ModBlocks;
import org.aussiebox.wingcrafter.block.blockentities.renderer.FireglobeBlockEntityRenderer;
import org.aussiebox.wingcrafter.component.FireglobeGlass;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;

import java.util.Map;

public class FireglobeItem extends BlockItem {
    public FireglobeItem(Settings settings) {
        super(ModBlocks.FIREGLOBE, settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack itemStack = super.getDefaultStack();
        itemStack.set(ModDataComponentTypes.FIREGLOBE_GLASS, new FireglobeGlass("clear", "clear", "clear", "clear"));
        return itemStack;
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, PlayerEntity player) {
        super.onCraftByPlayer(stack, player);
        Item backItem = player.currentScreenHandler.getSlot(2).getStack().getItem();
        Item leftItem = player.currentScreenHandler.getSlot(4).getStack().getItem();
        Item rightItem = player.currentScreenHandler.getSlot(6).getStack().getItem();
        Item frontItem = player.currentScreenHandler.getSlot(8).getStack().getItem();
        Map<Item, String> itemSpriteIDs = FireglobeBlockEntityRenderer.getItemSpriteIDs();
        stack.set(ModDataComponentTypes.FIREGLOBE_GLASS, new FireglobeGlass(
                itemSpriteIDs.get(frontItem),
                itemSpriteIDs.get(leftItem),
                itemSpriteIDs.get(backItem),
                itemSpriteIDs.get(rightItem)
        ));
        player.getInventory().markDirty();
    }
}
