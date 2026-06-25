package org.aussiebox.wingcrafter.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.aussiebox.wingcrafter.init.ScreenHandlerTypeInit;
import org.aussiebox.wingcrafter.network.SpellcasterDataPayload;

import java.util.List;

public class SpellcasterSpellSelectScreenHandler extends ScreenHandler {
    public List<String> spells;
    public ItemStack itemStack;

    public SpellcasterSpellSelectScreenHandler(int syncId, PlayerInventory playerInventory, SpellcasterDataPayload payload) {
        this(syncId, playerInventory, payload.itemStack());
        this.spells = payload.spellList();
        this.itemStack = payload.itemStack();
    }

    public SpellcasterSpellSelectScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack itemStack) {
        super(ScreenHandlerTypeInit.SPELL_SELECT, syncId);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}