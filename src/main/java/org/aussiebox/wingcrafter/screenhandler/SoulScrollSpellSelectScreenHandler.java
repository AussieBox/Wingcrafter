package org.aussiebox.wingcrafter.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.aussiebox.wingcrafter.init.ScreenHandlerTypeInit;
import org.aussiebox.wingcrafter.network.SoulScrollDataPayload;

public class SoulScrollSpellSelectScreenHandler extends ScreenHandler {
    private String spell1;
    private String spell2;
    private String spell3;

    public SoulScrollSpellSelectScreenHandler(int syncId, PlayerInventory playerInventory, SoulScrollDataPayload payload) {
        super(ScreenHandlerTypeInit.SOUL_SCROLL_SPELL_SELECT, syncId);
        this.spell1 = payload.spell1();
        this.spell2 = payload.spell2();
        this.spell3 = payload.spell3();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }
}