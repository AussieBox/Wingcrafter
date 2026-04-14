package org.aussiebox.wingcrafter.client.hud;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.aussiebox.wingcrafter.cca.SpellDataComponent;
import org.aussiebox.wingcrafter.client.screen.SpellcasterSpellSelectScreen;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;
import org.aussiebox.wingcrafter.item.ModItems;
import org.aussiebox.wingcrafter.spells.util.Spell;
import org.aussiebox.wingcrafter.spells.util.SpellRegistry;

import java.util.List;
import java.util.Objects;

public class SpellcasterSpellHud {

    public static void render(DrawContext context, RenderTickCounter tickCounter) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (player == null) return;

        ItemStack stack;

        if (!player.getMainHandStack().isOf(ModItems.SOUL_SCROLL)) {
            if (!player.getOffHandStack().isOf(ModItems.SOUL_SCROLL)) return;
            else stack = player.getOffHandStack();
        } else {
            stack = player.getMainHandStack();
        }

        List<String> spells = stack.get(ModDataComponentTypes.SPELLCASTER_SPELLS);
        if (spells == null) return;

        int height = context.getScaledWindowHeight();

        int centerX = 100;
        int posY = height-50;

        List<List<String>> spellGrid = Lists.partition(spells, 5);

        Object2ObjectMap<Integer, Integer[]> xPositions = new Object2ObjectLinkedOpenHashMap<>();
        xPositions.put(1, new Integer[]{centerX});
        xPositions.put(2, new Integer[]{centerX-20, centerX+20});
        xPositions.put(3, new Integer[]{centerX-20, centerX, centerX+20});
        xPositions.put(4, new Integer[]{centerX-40, centerX-20, centerX+20, centerX+40});
        xPositions.put(5, new Integer[]{centerX-40, centerX-20, centerX, centerX+20, centerX+40});

        int i;
        boolean selected;
        for (List<String> spellList : spellGrid) {
            i = 0;
            for (String spellID : spellList) {
                selected = false;
                if (Objects.equals(spellID, "none")) continue;
                if (spells.indexOf(spellID) == stack.getOrDefault(ModDataComponentTypes.SPELLCASTER_SELECTED_SLOT, 0)) selected = true;

                Spell spell;
                try {
                    spell = SpellRegistry.getSpell(spellID);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (spell == null) continue;

                int posX = xPositions.get(spellList.size())[i];
                context.drawTexture(
                        RenderPipelines.GUI_TEXTURED,
                        spell.getButtonTexture(),
                        posX,
                        posY,
                        0,
                        0,
                        16,
                        16,
                        16,
                        16,
                        16,
                        16
                );

                int cooldown = SpellDataComponent.KEY.get(player).getSpellCooldown(spellID);
                float delta = (float) cooldown / spell.getCastCooldown();
                if (cooldown > 0) {
                    int totalSeconds = cooldown / 20;
                    int minutes = totalSeconds / 60;
                    int seconds = totalSeconds % 60;
                    Text timer = Text.literal(minutes + ":" + (seconds < 10 ? "0" : "") + seconds);
                    context.fill(posX, MathHelper.lerp(delta, posY+16, posY), posX+16, posY+16, 0x69FFFFFF);

                    // Text rendering is still in-dev (TODO: finish lol)
                    int textX = posX+8-(textRenderer.getWidth(timer)/4);
                    int textY = posY+8-(textRenderer.fontHeight/4);

                    context.getMatrices().pushMatrix();
                    context.getMatrices().scale(0.5F);
                    context.getMatrices().translate((float) (textX*2), (float) (textY*2));
                    context.drawText(
                            MinecraftClient.getInstance().textRenderer,
                            timer,
                            0,
                            0,
                            0xFFFFFFFF,
                            true
                    );
                    context.getMatrices().popMatrix();
                }

                context.drawTexture(
                        RenderPipelines.GUI_TEXTURED,
                        selected ? SpellcasterSpellSelectScreen.SELECTED : SpellcasterSpellSelectScreen.UNSELECTED,
                        posX-2,
                        posY-2,
                        0,
                        0,
                        20,
                        20,
                        44,
                        44,
                        44,
                        44
                );
                i++;
            }
            posY += 20;
        }
    }
}
