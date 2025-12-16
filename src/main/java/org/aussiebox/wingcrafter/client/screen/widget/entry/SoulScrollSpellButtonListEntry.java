package org.aussiebox.wingcrafter.client.screen.widget.entry;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import org.aussiebox.wingcrafter.client.screen.SoulScrollSpellSelectScreen;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulScrollSpellButtonListEntry extends ElementListWidget.Entry<SoulScrollSpellButtonListEntry> {
    private final MinecraftClient client;
    @Nullable
    private final TexturedButtonWidget spellButton;

    public SoulScrollSpellButtonListEntry(MinecraftClient client, SoulScrollSpellSelectScreen parent, String spellID, ButtonTextures spellButtonTextures) {
        this.client = client;
        this.spellButton = new TexturedButtonWidget(
                0,
                0,
                32,
                32,
                spellButtonTextures,
                (btn) -> {
                    parent.selectedSpells[parent.selectedSlot-1] = spellID;
                }
        );
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of();
    }

    @Override
    public List<? extends Element> children() {
        return List.of();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {

    }
}
