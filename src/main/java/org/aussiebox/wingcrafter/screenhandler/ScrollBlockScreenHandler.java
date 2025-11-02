package org.aussiebox.wingcrafter.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import org.aussiebox.wingcrafter.block.ModBlocks;
import org.aussiebox.wingcrafter.block.blockentities.ScrollBlockEntity;
import org.aussiebox.wingcrafter.init.ScreenHandlerTypeInit;
import org.aussiebox.wingcrafter.network.BlockPosPayload;

public class ScrollBlockScreenHandler extends ScreenHandler {
    private final ScrollBlockEntity blockEntity;
    private final ScreenHandlerContext context;
    private String text;
    private String title;

    public ScrollBlockScreenHandler(int syncId, PlayerInventory playerInventory, BlockPosPayload payload) {
        this(syncId, playerInventory, (ScrollBlockEntity) playerInventory.player.getEntityWorld().getBlockEntity(payload.pos()));
        this.text = payload.text();
        this.title = payload.titleText();
    }

    public ScrollBlockScreenHandler(int syncId, PlayerInventory playerInventory, ScrollBlockEntity block) {
        super(ScreenHandlerTypeInit.SCROLL, syncId);

        this.blockEntity = block;
        this.context = ScreenHandlerContext.create(this.blockEntity.getWorld(), this.blockEntity.getPos());

    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ModBlocks.SCROLL);
    }

    public ScrollBlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    public String getText() {
        return this.text;
    }

    public String getTitle() {
        return this.title;
    }
}