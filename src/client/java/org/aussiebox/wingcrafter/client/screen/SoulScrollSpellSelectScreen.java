package org.aussiebox.wingcrafter.client.screen;

import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.network.UpdateSoulScrollDataPayload;
import org.aussiebox.wingcrafter.screenhandler.SoulScrollSpellSelectScreenHandler;
import org.aussiebox.wingcrafter.spells.Spells;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SoulScrollSpellSelectScreen extends BaseOwoHandledScreen<FlowLayout, SoulScrollSpellSelectScreenHandler> {
    public int selectedSlot = 1;
    public String[] selectedSpells = {this.handler.spell1, this.handler.spell2, this.handler.spell3};
    ButtonComponent spellButton1;
    ButtonComponent spellButton2;
    ButtonComponent spellButton3;

    public static final Identifier SELECTED = Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/soul_scroll/spell_select/selection.png");
    public static final Identifier UNSELECTED = Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/soul_scroll/spell_select/slot.png");

    Text titleText = Text.of("Spell Selection");

    public SoulScrollSpellSelectScreen(SoulScrollSpellSelectScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        playerInventoryTitleX = -10000;
    }

    public Map<String, Identifier> spellInfo = new HashMap<>();
    public Map<String, Identifier> fetchSpellInfo() {
        spellInfo.put("frostbeam", Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/soul_scroll/spells/frostbeam.png"));
        spellInfo.put("flamethrower", Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/soul_scroll/spells/frostbeam.png"));
        return spellInfo;
    }

    public Map<String, Text> spellTooltips = new HashMap<>();
    public Map<String, Text> fetchSpellTooltips() {
        Map<String, Integer> soulPenalties = Spells.fetchSpellSoulInfo();
        spellTooltips.put("frostbeam",
                Text.translatable("spell.wingcrafter.frostbeam")
                        .withColor(0xFF91F8FF)
                    .append("\n\nFreeze a 3x3x3 block area in front of you.\nAny entities hit by the beam will take\n2.5 hearts of damage.")
                        .withColor(0xFFAAAAAA)
                    .append("\n\nSoul Penalty: ")
                        .withColor(0xFF91C4FF)
                    .append(soulPenalties.get("frostbeam") + "+")
                        .withColor(0xFFFFFFFF)
        );
        spellTooltips.put("flamethrower",
                Text.translatable("spell.wingcrafter.flamethrower")
                        .withColor(0xFFFC8403)
                    .append("\n\nBreathe fire for a short period of time.")
                        .withColor(0xFFAAAAAA)
                    .append("\n\nSoul Penalty: ")
                        .withColor(0xFF91C4FF)
                    .append(String.valueOf(soulPenalties.get("flamethrower")))
                        .withColor(0xFFFFFFFF)
        );
        return spellTooltips;
    }

    private final GridLayout buttonGrid = (GridLayout) Containers.grid(Sizing.content(), Sizing.content(), 1000000, 9)
            .padding(Insets.of(10))
            .surface(Surface.DARK_PANEL)
            .verticalAlignment(VerticalAlignment.TOP)
            .horizontalAlignment(HorizontalAlignment.CENTER);
    private GridLayout currentButtonGrid;

    private final FlowLayout selectionFlow = (FlowLayout) Containers.horizontalFlow(Sizing.content(), Sizing.content())
            .padding(Insets.of(10))
            .surface(Surface.BLANK)
            .verticalAlignment(VerticalAlignment.TOP)
            .horizontalAlignment(HorizontalAlignment.CENTER);
    private FlowLayout currentSelectionFlow;

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        fetchSpellInfo();
        fetchSpellTooltips();

        setSelectionFlow(selectionFlow);
        setButtonGrid(buttonGrid);
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);
        rootComponent.child(Components.label(titleText))
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.TOP)
                .padding(Insets.of(15, 10, 10, 10));
        rootComponent.child(getSelectionFlow())
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.TOP);
        rootComponent.child(getButtonGrid())
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.TOP);
    }

    @Override
    public void close() {
        UpdateSoulScrollDataPayload payload = new UpdateSoulScrollDataPayload(this.handler.itemStack, selectedSpells[0], selectedSpells[1], selectedSpells[2]);
        ClientPlayNetworking.send(payload);
        super.close();
    }

    public GridLayout getButtonGrid() {
        return this.currentButtonGrid;
    }

    public void setButtonGrid(GridLayout grid) {
        this.currentButtonGrid = grid;
        int row = 1;
        int column = 0;
        for (String id : spellInfo.keySet()) {
            column++;
            if (column > 8) {
                row++;
                column = 1;
            }
            this.currentButtonGrid.child(Components.button(
                                    Text.empty(),
                                    button -> {
                                        if (Arrays.stream(selectedSpells).noneMatch(Predicate.isEqual(id))) {
                                            ButtonComponent oldSpell = this.currentButtonGrid.childById(ButtonComponent.class, "spell_" + selectedSpells[selectedSlot-1]);
                                            ButtonComponent newSpell = this.currentButtonGrid.childById(ButtonComponent.class, "spell_" + id);

                                            if (oldSpell != null) {
                                                oldSpell.renderer(ButtonComponent.Renderer.texture(
                                                        spellInfo.get(selectedSpells[selectedSlot-1]),
                                                        0,
                                                        0,
                                                        32,
                                                        32
                                                ));
                                            }
                                            selectedSpells[selectedSlot-1] = id;
                                            newSpell.renderer(ButtonComponent.Renderer.texture(
                                                    Arrays.stream(selectedSpells).anyMatch(Predicate.isEqual(id)) ? Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/soul_scroll/spell_selected.png") : spellInfo.get(id),
                                                    0,
                                                    0,
                                                    32,
                                                    32
                                            ));

                                            setSelectionFlow(selectionFlow);
                                        }
                                    }
                            )
                            .renderer(ButtonComponent.Renderer.texture(
                                    Arrays.stream(selectedSpells).anyMatch(Predicate.isEqual(id)) ? Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/soul_scroll/spell_selected.png") : spellInfo.get(id),
                                    0,
                                    0,
                                    32,
                                    32
                            ))
                            .tooltip(spellTooltips.get(id))
                            .sizing(Sizing.fixed(32))
                            .margins(Insets.of(5))
                            .id("spell_" + id),
                    row, column);
        }
    }

    public FlowLayout getSelectionFlow() {
        return this.currentSelectionFlow;
    }

    public void setSelectionFlow(FlowLayout flow) {
        this.currentSelectionFlow = flow;
        this.currentSelectionFlow.clearChildren();
        for (int i = 1; i <= 3; i++) {
            int index = i;
            this.currentSelectionFlow.child(Components.button(
                                    Text.empty(),
                                    button -> {
                                        if (selectedSlot == index) {
                                            ButtonComponent oldSpell = this.currentButtonGrid.childById(ButtonComponent.class, "spell_" + selectedSpells[selectedSlot-1]);
                                            if (oldSpell != null) {
                                                oldSpell.renderer(ButtonComponent.Renderer.texture(
                                                        spellInfo.get(selectedSpells[selectedSlot-1]),
                                                        0,
                                                        0,
                                                        32,
                                                        32
                                                ));
                                            }

                                            selectedSpells[selectedSlot-1] = "none";
                                        }
                                        selectedSlot = index;
                                        setSelectionFlow(selectionFlow);
                                    }
                            )
                            .renderer(ButtonComponent.Renderer.texture(
                                    selectedSlot == index ? SELECTED : UNSELECTED,
                                    0,
                                    0,
                                    44,
                                    44
                            ))
                            .sizing(Sizing.fixed(44))
                            .margins(Insets.of(4))
                            .id("spell_slot_" + index)
            );
        }
        spellButton1 = this.currentSelectionFlow.childById(ButtonComponent.class, "spell_slot_1");
        spellButton2 = this.currentSelectionFlow.childById(ButtonComponent.class, "spell_slot_2");
        spellButton3 = this.currentSelectionFlow.childById(ButtonComponent.class, "spell_slot_3");
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if (spellInfo.containsKey(selectedSpells[0])) {
            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    spellInfo.get(selectedSpells[0]),
                    spellButton1.getX() + 6,
                    spellButton1.getY() + 6,
                    0,
                    0,
                    spellButton1.getWidth() - 12,
                    spellButton1.getHeight() - 12,
                    spellButton1.getWidth() - 12,
                    spellButton1.getHeight() - 12
            );
        }
        if (spellInfo.containsKey(selectedSpells[1])) {
            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    spellInfo.get(selectedSpells[1]),
                    spellButton2.getX() + 6,
                    spellButton2.getY() + 6,
                    0,
                    0,
                    spellButton2.getWidth() - 12,
                    spellButton2.getHeight() - 12,
                    spellButton2.getWidth() - 12,
                    spellButton2.getHeight() - 12
            );
        }
        if (spellInfo.containsKey(selectedSpells[2])) {
            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    spellInfo.get(selectedSpells[2]),
                    spellButton3.getX() + 6,
                    spellButton3.getY() + 6,
                    0,
                    0,
                    spellButton3.getWidth() - 12,
                    spellButton3.getHeight() - 12,
                    spellButton3.getWidth() - 12,
                    spellButton3.getHeight() - 12
            );
        }
    }
}
