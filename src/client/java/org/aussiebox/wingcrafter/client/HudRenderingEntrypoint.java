package org.aussiebox.wingcrafter.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.block.ModBlocks;
import org.aussiebox.wingcrafter.block.blockentities.ScrollBlockEntity;
import org.aussiebox.wingcrafter.cca.SoulComponent;
import org.aussiebox.wingcrafter.client.config.ClientConfig;
import org.aussiebox.wingcrafter.item.ModItems;
import org.aussiebox.wingcrafter.network.SoulKillPayload;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static org.aussiebox.wingcrafter.block.custom.ScrollBlock.TITLED;
import static org.aussiebox.wingcrafter.block.custom.ScrollBlock.WRITTEN;

public class HudRenderingEntrypoint implements ClientModInitializer {
    static int warningColorT;
    static int warningColorO;
    static Text scrollTitle = Text.of("Untitled Scroll");
    static Text scrollLineCountText = Text.of("Line Count: N/A");
    static Text changeText = Text.of("idk bro im not a calculator 🥀");
    static int scrollLineCount = 0;
    static int change = 0;
    static int changeTime = 0;
    static int changeFadeIn;
    static int changeFadeOut;
    static Text warningText = Text.of("Warning! This is a warning!");
    static int warningTime = 0;
    static int warningFadeIn;
    static int warningFadeOut;
    static int lastSoul;
    static int soulDeathTimer = 170;
    static boolean lastVisibility = false;

    static String[] soulWarning750 = {"I feel cold.", "My talons are shaking.", "I don't feel very good."};
    static String[] soulWarning500 = {"Something feels different.", "What's happening to me?", "Am I alright?"};
    static String[] soulWarning250 = {"Something just slithered over my grave.", "Is there any turning back?", "Will I survive this eternal pain?", "Reality is shifting in and out of focus."};
    static String[] soulWarning100 = {"My magic reclaims me."};
    static String[] soulWarning0 = {"My soul will rise to the sky, never to return.", "I am to be remembered forever.", "Whatever rises must eventually fall."};

    public static final Identifier BACKGROUND = Identifier.of(Wingcrafter.MOD_ID, "textures/soul/background.png");
    public static final Identifier PROGRESS = Identifier.of(Wingcrafter.MOD_ID, "textures/soul/progress.png");

    public static final Identifier CRACKS_1 = Identifier.of(Wingcrafter.MOD_ID, "textures/soul/glass/cracks_1.png");
    public static final Identifier CRACKS_2 = Identifier.of(Wingcrafter.MOD_ID, "textures/soul/glass/cracks_2.png");
    public static final Identifier CRACKS_3 = Identifier.of(Wingcrafter.MOD_ID, "textures/soul/glass/cracks_3.png");
    public static final Identifier CRACKS_4 = Identifier.of(Wingcrafter.MOD_ID, "textures/soul/glass/cracks_4.png");

    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementBefore(VanillaHudElements.FOOD_BAR, Identifier.of(Wingcrafter.MOD_ID, "soul_bar"), HudRenderingEntrypoint::renderSoul);
        HudElementRegistry.attachElementBefore(VanillaHudElements.CROSSHAIR, Identifier.of(Wingcrafter.MOD_ID, "soul_glass_overlay"), HudRenderingEntrypoint::renderSoulGlassOverlay);
        HudElementRegistry.attachElementBefore(VanillaHudElements.CROSSHAIR, Identifier.of(Wingcrafter.MOD_ID, "scroll_tooltip"), HudRenderingEntrypoint::renderScroll);
    }

    private static void renderSoul(DrawContext context, RenderTickCounter tickCounter) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        SoulComponent data = SoulComponent.KEY.get(player);
        int soul = data.getSoul();
        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        if (player.getInventory().count(ModItems.SOUL_SCROLL) >= 1) {
            int progressEquation = 53-(soul*53/1000);

            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    BACKGROUND,
                    (int) ((double) width/2+12.5),
                    (int) ((double) height-52),
                    (float) 0,
                    (float) 0,
                    75,
                    12,
                    75,
                    12
            );
            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    PROGRESS,
                    (int) ((double) width/2+23.5),
                    (int) ((double) height-52),
                    (float) 0,
                    (float) 0,
                    progressEquation,
                    12,
                    progressEquation,
                    12,
                    53,
                    12
            );

            if (ClientConfig.displaySoulChanges) {
                if (soul > lastSoul) {
                    if (changeFadeIn == 0 && changeTime == 0 && changeFadeOut == 0) {
                        change = soul - lastSoul;
                        changeFadeIn = 20;
                        changeTime = 40;
                        changeFadeOut = 20;
                    } else {
                        change -= lastSoul - soul;
                        changeTime = 40;
                        changeFadeOut = 20;
                    }
                } else if (soul < lastSoul) {
                    if (changeFadeIn == 0 && changeTime == 0 && changeFadeOut == 0) {
                        change = soul - lastSoul;
                        changeFadeIn = 20;
                        changeTime = 40;
                        changeFadeOut = 20;
                    } else {
                        change += soul - lastSoul;
                        changeTime = 40;
                        changeFadeOut = 20;
                    }
                }
            }

            if (ClientConfig.displaySoulDialogue) {
                if (soul <= 750 && lastSoul > 750) {
                    if (warningFadeIn == 0 && warningTime == 0 && warningFadeOut == 0) {
                        warningText = Text.literal((String) Array.get(soulWarning750, ThreadLocalRandom.current().nextInt(0, soulWarning750.length)));
                        warningColorT = 0x00FFFFFF;
                        warningColorO = 0xFFFFFFFF;
                        warningFadeIn = 20;
                        warningTime = 80;
                        warningFadeOut = 20;
                    }
                }
                if (soul <= 500 && lastSoul > 500) {
                    if (warningFadeIn == 0 && warningTime == 0 && warningFadeOut == 0) {
                        warningText = Text.literal((String) Array.get(soulWarning500, ThreadLocalRandom.current().nextInt(0, soulWarning500.length)));
                        warningColorT = 0x00FFFFFF;
                        warningColorO = 0xFFFFFFFF;
                        warningFadeIn = 20;
                        warningTime = 80;
                        warningFadeOut = 20;
                    }
                }
                if (soul <= 250 && lastSoul > 250) {
                    if (warningFadeIn == 0 && warningTime == 0 && warningFadeOut == 0) {
                        warningText = Text.literal((String) Array.get(soulWarning250, ThreadLocalRandom.current().nextInt(0, soulWarning250.length)));
                        warningColorT = 0x00FFFFFF;
                        warningColorO = 0xFFFFFFFF;
                        warningFadeIn = 20;
                        warningTime = 80;
                        warningFadeOut = 20;
                    }
                }
                if (soul <= 100 && lastSoul > 100) {
                    if (warningFadeIn == 0 && warningTime == 0 && warningFadeOut == 0) {
                        warningText = Text.literal((String) Array.get(soulWarning100, ThreadLocalRandom.current().nextInt(0, soulWarning100.length)));
                        warningColorT = 0x00FF8787;
                        warningColorO = 0xFFFF8787;
                        warningFadeIn = 20;
                        warningTime = 100;
                        warningFadeOut = 20;
                    }
                }
                if (soul <= 0 && lastSoul > 0) {
                    if (warningFadeIn == 0 && warningTime == 0 && warningFadeOut == 0) {
                        warningText = Text.literal((String) Array.get(soulWarning0, ThreadLocalRandom.current().nextInt(0, soulWarning0.length)));
                        warningColorT = 0x00FF5C5C;
                        warningColorO = 0xFFFF5C5C;
                        warningFadeIn = 20;
                        warningTime = 120;
                        warningFadeOut = 20;
                        soulDeathTimer = 170;
                    }
                }
            }

            int changeX = (int) ((int) ((double) width/2+12.5) - ((double) textRenderer.getWidth(changeText) / 2) + 37.5);
            int changeY = (int) ((double) height-55) - 10 + ClientConfig.soulChangeYModifier;

            int warningX = width/2 - (textRenderer.getWidth(warningText) / 2);
            int warningY = height/10*7 - (textRenderer.fontHeight / 2) + ClientConfig.soulDialogueYModifier;

            if (change >= 0) {
                changeText = Text.of("+" + change);
            } else {
                changeText = Text.of(String.valueOf(change));
            }

            if (change == 0) {
                changeText = Text.of("=0");
            }

            if (changeFadeIn > 0) {
                context.drawText(textRenderer, changeText, changeX - changeFadeIn, changeY, ColorHelper.lerp((float) (1.0-((double) changeFadeIn / 20)), 0x00FFFFFF, 0xFFFFFFFF), true);
                changeFadeIn--;
            } else if (changeTime > 0) {
                context.drawText(textRenderer, changeText, changeX, changeY, 0xFFFFFFFF, true);
                changeTime--;
            } else if (changeFadeOut > 0) {
                context.drawText(textRenderer, changeText, changeX + (-1*changeFadeOut) + 20, changeY, ColorHelper.lerp((float) (1.0-((double) changeFadeOut / 20)), 0xFFFFFFFF, 0x00FFFFFF), true);
                changeFadeOut--;
            }

            if (warningFadeIn > 0) {
                context.drawText(textRenderer, warningText, warningX, warningY + (warningFadeIn/2), ColorHelper.lerp((float) (1.0-((double) warningFadeIn / 20)), warningColorT, warningColorO), true);
                warningFadeIn--;
            } else if (warningTime > 0) {
                context.drawText(textRenderer, warningText, warningX, warningY, warningColorO, true);
                warningTime--;
            } else if (warningFadeOut > 0) {
                context.drawText(textRenderer, warningText, warningX, warningY + (warningFadeOut/2) - 10, ColorHelper.lerp((float) (1.0-((double) warningFadeOut / 20)), warningColorO, warningColorT), true);
                warningFadeOut--;
            }

            if (soul <= 0 && soulDeathTimer <= 0) {
                soulDeathTimer = 170;
                SoulKillPayload payload = new SoulKillPayload(player.getUuidAsString());
                ClientPlayNetworking.send(payload);
            } else if (soul <= 0) {
                soulDeathTimer--;
            }

            // Somehow, adding the "change" rendering system took me longer than the soul bar itself.
            // My life is full of pain, suffering, and rainbows.

            lastVisibility = true;
            lastSoul = data.getSoul();
        } else {
            lastVisibility = false;
            lastSoul = data.getSoul();
        }
    }

    private static void renderSoulGlassOverlay(DrawContext context, RenderTickCounter tickCounter) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        SoulComponent data = SoulComponent.KEY.get(player);
        int soul = data.getSoul();

        if (soul <= 400) {
            drawOverlay(context, CRACKS_1);
        }
        if (soul <= 300) {
            drawOverlay(context, CRACKS_2);
        }
        if (soul <= 200) {
            drawOverlay(context, CRACKS_3);
        }
        if (soul <= 100) {
            drawOverlay(context, CRACKS_4);
        }

        if (soul <= 400 && lastSoul > 400 || soul <= 300 && lastSoul > 300 || soul <= 200 && lastSoul > 200 || soul <= 100 && lastSoul > 100) {
            player.playSoundToPlayer(SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.UI, 1.0F, 1.2F);
        }
    }

    private static void drawOverlay(DrawContext context, Identifier texture) {
        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                texture,
                0,
                0,
                0.0F,
                0.0F,
                context.getScaledWindowWidth(),
                context.getScaledWindowHeight(),
                context.getScaledWindowWidth(),
                context.getScaledWindowHeight(),
                context.getScaledWindowWidth(),
                context.getScaledWindowHeight()
        );
    }

    private static void renderScroll(DrawContext context, RenderTickCounter tickCounter) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

        Text duplicateText = Text.literal("[RC] Duplicate Scroll");
        Text offhandScrollText = Text.literal("Place a scroll in your offhand.");
        Text unwrittenScrollText = Text.literal("Offhanded scroll must be empty.");

        int scrollTitleX = width/2 - (textRenderer.getWidth(scrollTitle)/2);
        int scrollTitleY = height/2 - (textRenderer.fontHeight/2) + 15;

        int scrollDuplicateX = width/2 - (textRenderer.getWidth(duplicateText)/2);
        int offhandScrollX = width/2 - (textRenderer.getWidth(offhandScrollText)/2);
        int unwrittenScrollX = width/2 - (textRenderer.getWidth(unwrittenScrollText)/2);

        int scrollLineCountX = width/2 - (textRenderer.getWidth(scrollLineCountText)/2);

            HitResult hitResult = MinecraftClient.getInstance().crosshairTarget;

            if (hitResult != null) {
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    World world = player.getEntityWorld();
                    if (world != null) {
                        BlockEntity blockEntity = world.getBlockEntity(BlockPos.ofFloored(hitResult.getPos()));
                        ItemStack offhand = player.getOffHandStack();
                        BlockStateComponent offhandData = offhand.get(DataComponentTypes.BLOCK_STATE);
                        if (blockEntity instanceof ScrollBlockEntity scrollBlockEntity) {
                            if (player.getMainHandStack().isOf(ModItems.QUILL)) {
                                if (player.getOffHandStack().isOf(ModBlocks.SCROLL.asItem())) {
                                    if (offhandData != null && Boolean.FALSE.equals(offhandData.getValue(WRITTEN)) && Boolean.FALSE.equals(offhandData.getValue(TITLED))) {
                                        context.drawText(textRenderer, duplicateText, scrollDuplicateX, scrollTitleY - 30, 0xFFFFFFFF, true);
                                    } else {
                                        context.drawText(textRenderer, unwrittenScrollText, unwrittenScrollX, scrollTitleY - 30, 0xFFAAAAAA, true);
                                        context.drawText(textRenderer, duplicateText, scrollDuplicateX, scrollTitleY - 41, 0xFF555555, true);
                                    }
                                } else {
                                        context.drawText(textRenderer, offhandScrollText, offhandScrollX, scrollTitleY - 30, 0xFFAAAAAA, true);
                                        context.drawText(textRenderer, duplicateText, scrollDuplicateX, scrollTitleY - 41, 0xFF555555, true);
                                }
                            }

                            scrollTitle = Text.of("\"" + scrollBlockEntity.getTitle() + "\"");
                            scrollLineCount = (int) scrollBlockEntity.getText().lines().count();
                            scrollLineCountText = Text.of("Line Count: " + scrollLineCount);

                            if (Objects.equals(scrollBlockEntity.getTitle(), "")) {
                                scrollTitle = Text.of("Unnamed Scroll");
                            }
                            if (Objects.equals(scrollBlockEntity.getTitle(), "") && Objects.equals(scrollBlockEntity.getText(), "")) {
                                scrollTitle = Text.of("Empty Scroll");
                                scrollLineCountText = Text.empty();
                            }

                            if (ClientConfig.displayScrollInfoRequiresSneak) {
                                if (player.isSneaking()) {
                                    context.drawText(textRenderer, scrollTitle, scrollTitleX, scrollTitleY, 0xFFFFFFFF, true);
                                    context.drawText(textRenderer, scrollLineCountText, scrollLineCountX, scrollTitleY + 11, 0xFFAAAAAA, true);
                                }
                            } else {
                                context.drawText(textRenderer, scrollTitle, scrollTitleX, scrollTitleY, 0xFFFFFFFF, true);
                                context.drawText(textRenderer, scrollLineCountText, scrollLineCountX, scrollTitleY + 11, 0xFFAAAAAA, true);
                            }
                        }
                    }
                }
            }
    }
}
