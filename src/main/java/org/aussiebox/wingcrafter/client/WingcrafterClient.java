package org.aussiebox.wingcrafter.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.SpriteMapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.block.ModBlockEntities;
import org.aussiebox.wingcrafter.block.ModBlocks;
import org.aussiebox.wingcrafter.block.blockentities.renderer.FireglobeBlockEntityRenderer;
import org.aussiebox.wingcrafter.client.screen.ScrollScreen;
import org.aussiebox.wingcrafter.client.screen.SoulScrollSpellSelectScreen;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;
import org.aussiebox.wingcrafter.component.SoulScrollSpells;
import org.aussiebox.wingcrafter.config.ClientConfig;
import org.aussiebox.wingcrafter.datagen.AdvancementProvider;
import org.aussiebox.wingcrafter.datagen.ModelProvider;
import org.aussiebox.wingcrafter.entity.ModEntities;
import org.aussiebox.wingcrafter.entity.model.DragonflameCactusEntityModel;
import org.aussiebox.wingcrafter.entity.render.DragonflameCactusEntityRenderer;
import org.aussiebox.wingcrafter.init.ScreenHandlerTypeInit;
import org.aussiebox.wingcrafter.item.ModItems;
import org.aussiebox.wingcrafter.network.CastSpellPayload;
import org.lwjgl.glfw.GLFW;

public class WingcrafterClient implements ClientModInitializer, DataGeneratorEntrypoint {

    public static final Identifier FIREGLOBE_GLASS_ATLAS_PATH = Identifier.of(Wingcrafter.MOD_ID, "textures/atlas/fireglobe_glass.png");
    public static final Identifier FIREGLOBE_GLASS_ATLAS_DEFINITION = Identifier.of(Wingcrafter.MOD_ID, "fireglobe_glass");
    public static final SpriteMapper FIREGLOBE_GLASS = new SpriteMapper(FIREGLOBE_GLASS_ATLAS_PATH, FIREGLOBE_GLASS_ATLAS_DEFINITION.getPath());

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ScreenHandlerTypeInit.SCROLL, ScrollScreen::new);
        HandledScreens.register(ScreenHandlerTypeInit.SOUL_SCROLL_SPELL_SELECT, SoulScrollSpellSelectScreen::new);
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
            ItemStack soulScroll = ItemStack.EMPTY;
            if (client.player != null) {
                if (client.player.getMainHandStack().isOf(ModItems.SOUL_SCROLL)) {
                    soulScroll = client.player.getMainHandStack();
                } else if (client.player.getOffHandStack().isOf(ModItems.SOUL_SCROLL)) {
                    soulScroll = client.player.getOffHandStack();
                }
                while (spell1Keybind.wasPressed()) {
                    if (soulScroll.isOf(ModItems.SOUL_SCROLL)) {
                        SoulScrollSpells spells = soulScroll.get(ModDataComponentTypes.SOUL_SCROLL_SPELLS);
                        if (spells.spell1() != null && !spells.spell1().equals("none")) {
                            CastSpellPayload payload = new CastSpellPayload(spells.spell1());
                            ClientPlayNetworking.send(payload);
                        }
                    }
                }
                while (spell2Keybind.wasPressed()) {
                    if (soulScroll.isOf(ModItems.SOUL_SCROLL)) {
                        SoulScrollSpells spells = soulScroll.get(ModDataComponentTypes.SOUL_SCROLL_SPELLS);
                        if (spells.spell2() != null && !spells.spell2().equals("none")) {
                            CastSpellPayload payload = new CastSpellPayload(spells.spell2());
                            ClientPlayNetworking.send(payload);
                        }
                    }
                }
                while (spell3Keybind.wasPressed()) {
                    if (soulScroll.isOf(ModItems.SOUL_SCROLL)) {
                        SoulScrollSpells spells = soulScroll.get(ModDataComponentTypes.SOUL_SCROLL_SPELLS);
                        if (spells.spell3() != null && !spells.spell3().equals("none")) {
                            CastSpellPayload payload = new CastSpellPayload(spells.spell3());
                            ClientPlayNetworking.send(payload);
                        }
                    }
                }
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            ClientConfig.HANDLER.save();
        });

        BlockRenderLayerMap.putBlock(ModBlocks.SCROLL, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(ModBlocks.FIREGLOBE, BlockRenderLayer.TRANSLUCENT);
        BlockEntityRendererFactories.register(ModBlockEntities.FIREGLOBE_BLOCK_ENTITY, FireglobeBlockEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(FireglobeBlockEntityRenderer.FIREGLOBE_SIDES, FireglobeBlockEntityRenderer::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(DragonflameCactusEntityModel.CACTUS, DragonflameCactusEntityModel::getTexturedModelData);

        EntityRendererFactories.register(ModEntities.DragonflameCactusEntityType, DragonflameCactusEntityRenderer::new);
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelProvider::new);
        pack.addProvider(AdvancementProvider::new);
    }
}
