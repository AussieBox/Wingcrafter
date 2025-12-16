package org.aussiebox.wingcrafter.client.screen.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import org.aussiebox.wingcrafter.client.screen.SoulScrollSpellSelectScreen;
import org.aussiebox.wingcrafter.client.screen.widget.entry.SoulScrollSpellButtonListEntry;

public class SoulScrollSpellButtonListWidget extends ElementListWidget<SoulScrollSpellButtonListEntry> {
    private final SoulScrollSpellSelectScreen parent;

    public SoulScrollSpellButtonListWidget(SoulScrollSpellSelectScreen parent, MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
        this.parent = parent;
    }
}
