package org.aussiebox.wingcrafter.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.aussiebox.wingcrafter.screenhandler.SoulScrollSpellSelectScreenHandler;

public class SoulScrollSpellSelectScreen extends HandledScreen<SoulScrollSpellSelectScreenHandler> {
    int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
    int screenHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();

    public SoulScrollSpellSelectScreen(SoulScrollSpellSelectScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        playerInventoryTitleX = -100;
    }

    @Override
    protected void init() {

    }

    @Override
    public boolean keyPressed(KeyInput input) {
        assert client != null;
        if (client.options.inventoryKey.matchesKey(input)) {
            return false;
        } else {
            return super.keyPressed(input);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {

    }
}
