package org.aussiebox.wingcrafter.client.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.input.KeyInput;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import org.aussiebox.wingcrafter.block.blockentities.ScrollBlockEntity;
import org.aussiebox.wingcrafter.block.custom.ScrollBlock;
import org.aussiebox.wingcrafter.network.ScrollTextPayload;
import org.aussiebox.wingcrafter.screenhandler.ScrollBlockScreenHandler;

import java.util.Objects;

public class ScrollScreen extends HandledScreen<ScrollBlockScreenHandler> {
    int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
    int screenHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
    boolean editMode = true;

    String titleText = "Custom Screen Title";

    int editorWidth = screenWidth / 10 * 8;
    int editorHeight = (int) ((double) screenHeight / 10 * 6.9);
    int bottomButtonX = screenWidth / 2;
    int bottomButtonY = (int) ((double) screenHeight / 10 * 9.05);

    public ScrollScreen(ScrollBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        playerInventoryTitleX = -100;
    }

    @Override
    protected void init() {
    final ScrollBlockEntity blockEntity = this.handler.getBlockEntity();

        TextFieldWidget title = new TextFieldWidget(this.textRenderer, screenWidth / 10, screenHeight / 10 - this.textRenderer.fontHeight + 10, editorWidth, this.textRenderer.fontHeight + 10, Text.of("Write a good title here..."));
        title.setPlaceholder(Text.of("Give your scroll a title here..."));
        title.setMaxLength(75);
        if (!Objects.equals(this.handler.getTitle(), "")) {
            title.setText(this.handler.getTitle());
        }

        TextWidget viewTitle = new TextWidget(screenWidth / 10, screenHeight / 10 - this.textRenderer.fontHeight + 10, editorWidth, this.textRenderer.fontHeight + 10, Text.of("Read a title here..."), this.textRenderer);

        EditBoxWidget text = new EditBoxWidget.Builder()
                .x(screenWidth / 10)
                .y(screenHeight / 10 - this.textRenderer.fontHeight + 35)
                .placeholder(Text.literal("Welcome to the scroll editor!\n\nWith scrolls, you can share stories, or take notes!\nThey might even support Markdown in future!\n\nScrolls have no length limit. Write as much as you want!"))
                .build(this.textRenderer, editorWidth, editorHeight, Text.literal("Immortalise your story with the power of parchment."));
        if (!Objects.equals(this.handler.getText(), "")) {
            text.setText(this.handler.getText());
        }

        ScrollableTextWidget viewText = new ScrollableTextWidget(screenWidth / 10, screenHeight / 10 - this.textRenderer.fontHeight + 35, editorWidth, editorHeight, Text.literal("Knowledge is a flame in the darkness."), this.textRenderer);
        if (!Objects.equals(this.handler.getText(), "")) {
            viewText.setMessage(Text.of(this.handler.getText()));
        }

        ButtonWidget finishWriting = ButtonWidget.builder(Text.of("Finish Writing"), (btn) -> {
            ScrollTextPayload payload = new ScrollTextPayload(blockEntity.getPos(), text.getText(), title.getText());
            ClientPlayNetworking.send(payload);
        }).dimensions(bottomButtonX + 5, bottomButtonY, 120, 20).build();

        ButtonWidget switchModes = ButtonWidget.builder(Text.of("Switch Modes"), (btn) -> {
            editMode = !editMode;
            if (editMode) {
                text.visible = true;
                title.visible = true;
                viewText.visible = false;
                viewTitle.visible = false;
                titleText = "Editing Scroll...";
            } else {
                text.visible = false;
                title.visible = false;
                viewText.visible = true;
                viewText.setMessage(Text.of(text.getText()));
                viewTitle.visible = true;
                viewTitle.setMessage(Text.of(title.getText()));
                titleText = "Reading Scroll...";
            }
        }).dimensions(bottomButtonX - 125, bottomButtonY, 120, 20).build();

        this.addDrawableChild(title);
        this.addDrawableChild(text);
        this.addDrawableChild(viewTitle);
        this.addDrawableChild(viewText);
        this.addDrawableChild(finishWriting);
        this.addDrawableChild(switchModes);

        BlockState blockState = Objects.requireNonNull(blockEntity.getWorld()).getBlockState(blockEntity.getPos());
        if (blockState.get(ScrollBlock.SEALED)) {
            editMode = false;
            switchModes.visible = false;
            finishWriting.visible = false;
        }

        if (editMode) {
            text.visible = true;
            title.visible = true;
            viewText.visible = false;
            viewTitle.visible = false;
            titleText = "Editing Scroll...";
        } else {
            text.visible = false;
            title.visible = false;
            viewText.visible = true;
            viewText.setMessage(Text.of(text.getText()));
            viewTitle.visible = true;
            viewTitle.setMessage(Text.of(title.getText()));
            titleText = "Reading Scroll...";
        }
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

        context.drawText(this.textRenderer, titleText, screenWidth/2-(textRenderer.getWidth(titleText)/2), screenHeight/10 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {

    }
}
