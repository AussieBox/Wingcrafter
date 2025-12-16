package org.aussiebox.wingcrafter.client.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.network.UpdateSoulScrollDataPayload;
import org.aussiebox.wingcrafter.screenhandler.SoulScrollSpellSelectScreenHandler;

import java.util.Arrays;
import java.util.Objects;

public class SoulScrollSpellSelectScreen extends HandledScreen<SoulScrollSpellSelectScreenHandler> {
    int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
    int screenHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
    public int selectedSlot = 1;
    public String[] selectedSpells = {this.handler.spell1, this.handler.spell2, this.handler.spell3};

    public static final Identifier SELECTED = Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/soul_scroll/spell_select/selection.png");
    public static final Identifier SLOT = Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/soul_scroll/spell_select/slot.png");

    Text titleText = Text.of("Spell Selection");

    public SoulScrollSpellSelectScreen(SoulScrollSpellSelectScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        playerInventoryTitleX = -100;
    }

    ButtonTextures spellSlotTextures = new ButtonTextures(Identifier.of("wingcrafter:soul_scroll/spell_select/slot"));
    TexturedButtonWidget spellSlot1 = new TexturedButtonWidget(screenWidth/2-22-50, screenHeight/10, 44, 44, spellSlotTextures, (btn) -> {
        if (selectedSlot == 1) {
            selectedSpells[selectedSlot-1] = "none";
        }
        selectedSlot = 1;
    });
    TexturedButtonWidget spellSlot2 = new TexturedButtonWidget(screenWidth/2-22, screenHeight/10, 44, 44, spellSlotTextures, (btn) -> {
        if (selectedSlot == 2) {
            selectedSpells[selectedSlot-1] = "none";
        }
        selectedSlot = 2;
    });
    TexturedButtonWidget spellSlot3 = new TexturedButtonWidget(screenWidth/2-22+50, screenHeight/10, 44, 44, spellSlotTextures, (btn) -> {
        if (selectedSlot == 3) {
            selectedSpells[selectedSlot-1] = "none";
        }
        selectedSlot = 3;
    });

    // -[ SPELL BUTTONS]- //

    ButtonTextures frostbeamSpellTexture = new ButtonTextures(Identifier.of("wingcrafter:soul_scroll/spells/frostbeam"));
    TexturedButtonWidget frostbeamSpell = new TexturedButtonWidget(screenWidth/10*5-36, screenHeight/10+60, 32, 32, frostbeamSpellTexture, (btn) -> {
        selectedSpells[selectedSlot-1] = "frostbeam";
    });

    ButtonTextures flamethrowerSpellTexture = new ButtonTextures(Identifier.of("wingcrafter:soul_scroll/spells/frostbeam"));
    TexturedButtonWidget flamethrowerSpell = new TexturedButtonWidget(screenWidth/10*5+4, screenHeight/10+60, 32, 32, flamethrowerSpellTexture, (btn) -> {
        selectedSpells[selectedSlot-1] = "flamethrower";
    });

    // -[SPELL INFORMATION]- //

    TexturedButtonWidget[] spells = {
            frostbeamSpell,
            flamethrowerSpell
    };
    String[] spellIDs = {
            "frostbeam",
            "flamethrower"
    };
    Identifier[] spellTextures = {
            Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/soul_scroll/spells/frostbeam.png"),
            Identifier.of(Wingcrafter.MOD_ID, "textures/gui/sprites/soul_scroll/spells/frostbeam.png")
    };

    @Override
    protected void init() {
        this.addDrawableChild(spellSlot1);
        this.addDrawableChild(spellSlot2);
        this.addDrawableChild(spellSlot3);

        for (TexturedButtonWidget widget : spells) {
            this.addDrawableChild(widget);
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
    public void close() {
        UpdateSoulScrollDataPayload payload = new UpdateSoulScrollDataPayload(this.handler.itemStack, selectedSpells[0], selectedSpells[1], selectedSpells[2]);
        ClientPlayNetworking.send(payload);
        super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawText(this.textRenderer, titleText, screenWidth/2-(textRenderer.getWidth(titleText)/2), screenHeight/10 - this.textRenderer.fontHeight - 10, 0xFFFFFFFF, true);
        if (selectedSlot == 1) {
            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    SELECTED,
                    spellSlot1.getX(),
                    spellSlot1.getY(),
                    0,
                    0,
                    44,
                    44,
                    44,
                    44
            );
        } else if (selectedSlot == 2) {
            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    SELECTED,
                    spellSlot2.getX(),
                    spellSlot2.getY(),
                    0,
                    0,
                    44,
                    44,
                    44,
                    44
            );
        } else if (selectedSlot == 3) {
            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    SELECTED,
                    spellSlot3.getX(),
                    spellSlot3.getY(),
                    0,
                    0,
                    44,
                    44,
                    44,
                    44
            );
        }

            int i = 0;
            for (TexturedButtonWidget widget : spells) {
                String id = spellIDs[i];
                Identifier texture = spellTextures[i];

                if (Arrays.stream(selectedSpells).toList().contains(id)) {
                    widget.visible = false;
                    if (Objects.equals(selectedSpells[0], id)) {
                        context.drawTexture(
                                RenderPipelines.GUI_TEXTURED,
                                texture,
                                spellSlot1.getX()+6,
                                spellSlot1.getY()+6,
                                0,
                                0,
                                32,
                                32,
                                32,
                                32
                        );
                    } else if (Objects.equals(selectedSpells[1], id)) {
                        context.drawTexture(
                                RenderPipelines.GUI_TEXTURED,
                                texture,
                                spellSlot2.getX()+6,
                                spellSlot2.getY()+6,
                                0,
                                0,
                                32,
                                32,
                                32,
                                32
                        );
                    } else if (Objects.equals(selectedSpells[2], id)) {
                        context.drawTexture(
                                RenderPipelines.GUI_TEXTURED,
                                texture,
                                spellSlot3.getX()+6,
                                spellSlot3.getY()+6,
                                0,
                                0,
                                32,
                                32,
                                32,
                                32
                        );
                    }
                } else {
                    widget.visible = true;
                }
                i++;
            }
    }

    @Override
    protected void drawBackground(DrawContext context, float deltaTicks, int mouseX, int mouseY) {

    }
}
