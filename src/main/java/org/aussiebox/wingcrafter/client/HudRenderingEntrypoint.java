package org.aussiebox.wingcrafter.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.attach.ModAttachmentTypes;
import org.aussiebox.wingcrafter.attach.ModCustomAttachedData;
import org.aussiebox.wingcrafter.item.ModItems;

public class HudRenderingEntrypoint implements ClientModInitializer {
    public static final Identifier BACKGROUND = Identifier.of(Wingcrafter.MOD_ID, "textures/soul/background.png");
    public static final Identifier PROGRESS = Identifier.of(Wingcrafter.MOD_ID, "textures/soul/progress.png");

    @Override
    public void onInitializeClient() {
        // Attach our rendering code to before the chat hud layer. Our layer will render right before the chat. The API will take care of z spacing.
        HudElementRegistry.attachElementBefore(VanillaHudElements.FOOD_BAR, Identifier.of(Wingcrafter.MOD_ID, "before_chat"), HudRenderingEntrypoint::render);
    }

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player.getInventory().contains(new ItemStack(ModItems.SOUL_SCROLL))) {
            ModCustomAttachedData data = player.getAttachedOrSet(ModAttachmentTypes.SOUL_ATTACH, ModCustomAttachedData.DEFAULT);
            int soul = data.soul();
            int progressEquation = 71-(soul*71/1000);

            int width = context.getScaledWindowWidth();
            int height = context.getScaledWindowHeight();

            context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    BACKGROUND,
                    (int) ((double) width/2+12.5),
                    (int) ((double) height-55),
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
                    (int) ((double) width/2+14.5),
                    (int) ((double) height-53),
                    (float) 71,
                    (float) 0,
                    progressEquation,
                    8,
                    progressEquation,
                    8,
                    71,
                    8
            );
        }
    }
}
