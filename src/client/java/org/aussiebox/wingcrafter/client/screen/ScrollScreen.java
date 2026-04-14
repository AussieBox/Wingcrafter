package org.aussiebox.wingcrafter.client.screen;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.client.input.KeyInput;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import org.aussiebox.wingcrafter.network.ScrollTextPayload;
import org.aussiebox.wingcrafter.screenhandler.ScrollBlockScreenHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ScrollScreen extends BaseOwoHandledScreen<FlowLayout, ScrollBlockScreenHandler> {
    String titleText = "";
    String scrollText = "";
    boolean editMode = true;

    ScrollContainer<FlowLayout> titleContainer;
    ScrollContainer<FlowLayout> textContainer;

    public ScrollScreen(ScrollBlockScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        playerInventoryTitleX = -10000;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        setScrollTitleBox();
        setScrollTextBox();

        rootComponent.alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

        FlowLayout editor = Containers.verticalFlow(Sizing.content(), Sizing.content());
        editor.child(getScrollTitleBox());
        editor.child(getScrollTextBox());

        rootComponent.child(editor.id("editor"));
        rootComponent.child(getModeButton(rootComponent));
    }

    @Override
    public void close() {
        ScrollTextPayload payload = new ScrollTextPayload(this.handler.getBlockEntity().getPos(), scrollText, titleText);
        ClientPlayNetworking.send(payload);
        super.close();
    }

    public ScrollContainer<FlowLayout> getScrollTitleBox() {
        return this.titleContainer;
    }

    public void setScrollTitleBox() {
        if (Objects.equals(titleText, "")) titleText = this.handler.getTitle();
        if (titleText == null) titleText = "";

        FlowLayout flow = Containers.horizontalFlow(Sizing.fill(), Sizing.content());
        if (editMode) {
            TextBoxComponent textBox = Components.textBox(Sizing.fill());

            textBox.setMaxLength(1000000);
            textBox.setText(titleText);
            textBox.setPlaceholder(Text.translatable("scroll.tip.title"));
            textBox.onChanged().subscribe(newText -> {
                titleText = newText;
            });

            flow.child(textBox);
        } else {
            Component textComponent = MiniMessage.miniMessage().deserialize(titleText);
            String textJSON = GsonComponentSerializer.gson().serialize(textComponent);
            Text text = TextCodecs.CODEC
                    .decode(JsonOps.INSTANCE, new Gson().fromJson(textJSON, JsonElement.class))
                    .getOrThrow()
                    .getFirst();

            LabelComponent label = Components.label(text);

            flow.child(label);
        }

        ScrollContainer<FlowLayout> container = Containers.horizontalScroll(Sizing.fill(80), Sizing.content(), flow);
        container.margins(Insets.vertical(10));
        container.id("title");
        this.titleContainer = container;
    }

    public ScrollContainer<FlowLayout> getScrollTextBox() {
        return this.textContainer;
    }

    public void setScrollTextBox() {
        if (Objects.equals(scrollText, "")) scrollText = this.handler.getText();
        if (scrollText == null) scrollText = "";

        FlowLayout flow = Containers.verticalFlow(Sizing.fill(), Sizing.fill());
        if (editMode) {
            TextAreaComponent textBox = Components.textArea(Sizing.fill(), Sizing.fill());

            textBox.setText(scrollText);
            textBox.displayCharCount(true);
            textBox.onChanged().subscribe(newText -> {
                scrollText = newText;
            });

            flow.child(textBox);
        } else {
            Component textComponent = MiniMessage.miniMessage().deserialize(scrollText);
            String textJSON = GsonComponentSerializer.gson().serialize(textComponent);
            Text text = TextCodecs.CODEC
                    .decode(JsonOps.INSTANCE, new Gson().fromJson(textJSON, JsonElement.class))
                    .getOrThrow()
                    .getFirst();

            LabelComponent label = Components.label(text);
            label.horizontalSizing(Sizing.fill(100));
            label.verticalSizing(Sizing.content());
            ScrollContainer<LabelComponent> scroll = Containers.verticalScroll(Sizing.fill(), Sizing.fill(), label);

            flow.child(scroll);
        }

        ScrollContainer<FlowLayout> container = Containers.verticalScroll(Sizing.fill(80), Sizing.fill(60), flow);
        container.margins(Insets.vertical(10));
        container.id("text");
        this.textContainer = container;
    }

    public ButtonComponent getModeButton(FlowLayout rootComponent) {
        ButtonComponent button = Components.button(
                editMode ? Text.translatable("scroll.mode.edit") : Text.translatable("scroll.mode.view"),
                buttonComponent -> {
                    editMode = !editMode;
                    buttonComponent.setMessage(editMode ? Text.translatable("scroll.mode.edit") : Text.translatable("scroll.mode.view"));

                    setScrollTitleBox();
                    setScrollTextBox();

                    FlowLayout editor = rootComponent.childById(FlowLayout.class, "editor");
                    editor.clearChildren();
                    editor.child(getScrollTitleBox());
                    editor.child(getScrollTextBox());
                }
        );
        return button;
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
}
