package org.aussiebox.wingcrafter.client.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.aussiebox.wingcrafter.block.blockentities.ScrollBlockEntity;
import org.aussiebox.wingcrafter.network.ScrollTextPayload;
import org.aussiebox.wingcrafter.screenhandler.ScrollBlockScreenHandler;

import java.util.Objects;

public class ScrollScreen extends HandledScreen<ScrollBlockScreenHandler> {
    int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
    int screenHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();

    public ScrollScreen(ScrollBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
    final ScrollBlockEntity blockEntity = this.handler.getBlockEntity();

        EditBoxWidget text = new EditBoxWidget.Builder()
                .x(screenWidth / 10)
                .y(screenHeight / 10 - this.textRenderer.fontHeight + 10)
                .placeholder(Text.literal("Welcome to the scroll editor!\n\nWith scrolls, you can share stories, or take notes!\nThey might even support Markdown in future!\n\nScrolls have no length limit. Write as much as you want!"))
                .build(this.textRenderer, screenWidth / 10 * 8, (int) ((double) screenHeight / 10 * 7.5), Text.literal("Immortalise your story with the power of parchment."));
        if (!Objects.equals(this.handler.getText(), "")) {
            text.setText(this.handler.getText());
        }

        ButtonWidget finishWriting = ButtonWidget.builder(Text.of("Finish Writing"), (btn) -> {
            ScrollTextPayload payload = new ScrollTextPayload(blockEntity.getPos(), text.getText());
            ClientPlayNetworking.send(payload);
        }).dimensions(screenWidth/2-60, screenHeight/10*9, 120, 20).build();

        this.addDrawableChild(text);
        this.addDrawableChild(finishWriting);

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

        String text = "Editing Scroll...";
        context.drawText(this.textRenderer, text, screenWidth/2-(textRenderer.getWidth(text)/2), screenHeight/10 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {

    }
}
