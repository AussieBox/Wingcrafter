package org.aussiebox.wingcrafter.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.attach.DragonflameCactusFuseAttachedData;
import org.aussiebox.wingcrafter.attach.ModAttachmentTypes;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;
import org.aussiebox.wingcrafter.entity.DragonflameCactusEntity;
import org.aussiebox.wingcrafter.item.ModItems;

public class DragonflameCactusItem extends Item {
    public DragonflameCactusItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack itemStack = super.getDefaultStack();
        itemStack.set(ModDataComponentTypes.DRAGONFLAME_CACTUS_FUSE, 20);
        return itemStack;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient()) {
            DragonflameCactusEntity dragonflameCactusEntity = new DragonflameCactusEntity(user, world, itemStack);
            int fuse = itemStack.get(ModDataComponentTypes.DRAGONFLAME_CACTUS_FUSE);
            dragonflameCactusEntity.setOwner(user);
            dragonflameCactusEntity.setAttached(ModAttachmentTypes.DRAGONFLAME_CACTUS_FUSE_ATTACH, new DragonflameCactusFuseAttachedData(fuse));
            dragonflameCactusEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 0F);
            world.spawnEntity(dragonflameCactusEntity);
        }
        itemStack.decrementUnlessCreative(1, user);
        return ActionResult.SUCCESS;
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, PlayerEntity player) {
        super.onCraftByPlayer(stack, player);
        ItemStack itemStack = this.getDefaultStack();
        for (int i = 1; i <= 9; i++) {
            if (player.currentScreenHandler.getSlot(i).getStack().isOf(ModItems.DRAGONFLAME_CACTUS)) {
                itemStack = player.currentScreenHandler.getSlot(i).getStack();
            }
        }
        stack.set(ModDataComponentTypes.DRAGONFLAME_CACTUS_FUSE, itemStack.get(ModDataComponentTypes.DRAGONFLAME_CACTUS_FUSE) + 10);
        player.getInventory().markDirty();
    }
}
