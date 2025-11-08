package org.aussiebox.wingcrafter.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.client.screen.ScrollScreen;
import org.aussiebox.wingcrafter.config.ClientConfig;
import org.aussiebox.wingcrafter.datagen.AdvancementProvider;
import org.aussiebox.wingcrafter.datagen.ModelProvider;
import org.aussiebox.wingcrafter.init.ScreenHandlerTypeInit;
import org.lwjgl.glfw.GLFW;

public class WingcrafterClient implements ClientModInitializer, DataGeneratorEntrypoint {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlerTypeInit.SCROLL, ScrollScreen::new);
        ClientConfig.HANDLER.load();

        KeyBinding spell1Keybind;
        KeyBinding spell2Keybind;
        KeyBinding spell3Keybind;
        final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of(Wingcrafter.MOD_ID, "keybinds"));
        spell1Keybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.wingcrafter.spell1",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                CATEGORY
        ));
        spell2Keybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.wingcrafter.spell2",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                CATEGORY
        ));
        spell3Keybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.wingcrafter.spell3",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (spell1Keybind.wasPressed()) {
                client.player.sendMessage(Text.literal("Spell 1 was cast!"), false);
            }
            while (spell2Keybind.wasPressed()) {
                client.player.sendMessage(Text.literal("Spell 2 was cast!"), false);
            }
            while (spell3Keybind.wasPressed()) {
                client.player.sendMessage(Text.literal("Spell 3 was cast!"), false);
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            ClientConfig.HANDLER.save();
        });
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelProvider::new);
        pack.addProvider(AdvancementProvider::new);
    }
}
