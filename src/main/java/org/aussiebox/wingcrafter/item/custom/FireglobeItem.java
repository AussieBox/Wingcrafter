package org.aussiebox.wingcrafter.item.custom;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import org.aussiebox.wingcrafter.block.ModBlocks;
import org.aussiebox.wingcrafter.component.FireglobeGlass;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;

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
}
