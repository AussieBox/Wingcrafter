package org.aussiebox.wingcrafter.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.SpriteMapper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.block.ModBlockEntities;
import org.aussiebox.wingcrafter.block.ModBlocks;
import org.aussiebox.wingcrafter.client.block.entity.render.FireglobeBlockEntityRenderer;
import org.aussiebox.wingcrafter.client.entity.model.DragonflameCactusEntityModel;
import org.aussiebox.wingcrafter.client.entity.model.MoonGlobeEntityModel;
import org.aussiebox.wingcrafter.client.entity.render.DragonflameCactusEntityRenderer;
import org.aussiebox.wingcrafter.client.entity.render.MoonGlobeEntityRenderer;
import org.aussiebox.wingcrafter.client.screen.ScrollScreen;
import org.aussiebox.wingcrafter.client.screen.SpellcasterSpellSelectScreen;
import org.aussiebox.wingcrafter.component.FireglobeGlass;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;
import org.aussiebox.wingcrafter.entity.ModEntities;
import org.aussiebox.wingcrafter.init.ScreenHandlerTypeInit;
import org.aussiebox.wingcrafter.item.ModItems;
import org.aussiebox.wingcrafter.network.CastSpellPayload;
import org.aussiebox.wingcrafter.spells.util.Spell;
import org.aussiebox.wingcrafter.spells.util.SpellRegistry;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class WingcrafterClient implements ClientModInitializer {

    public static final Identifier FIREGLOBE_GLASS_ATLAS_PATH = Identifier.of(Wingcrafter.MOD_ID, "textures/atlas/fireglobe_glass.png");
    public static final Identifier FIREGLOBE_GLASS_ATLAS_DEFINITION = Identifier.of(Wingcrafter.MOD_ID, "fireglobe_glass");
    public static final SpriteMapper FIREGLOBE_GLASS = new SpriteMapper(FIREGLOBE_GLASS_ATLAS_PATH, FIREGLOBE_GLASS_ATLAS_DEFINITION.getPath());

    public static KeyBinding castKeybind;
    public static KeyBinding mouseScrollModifierKeybind;

    @Override
    public void onInitializeClient() {

        HandledScreens.register(ScreenHandlerTypeInit.SCROLL, ScrollScreen::new);
        HandledScreens.register(ScreenHandlerTypeInit.SOUL_SCROLL_SPELL_SELECT, SpellcasterSpellSelectScreen::new);

        BlockRenderLayerMap.putBlock(ModBlocks.SCROLL, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(ModBlocks.FIREGLOBE, BlockRenderLayer.TRANSLUCENT);
            BlockEntityRendererFactories.register(ModBlockEntities.FIREGLOBE_BLOCK_ENTITY, FireglobeBlockEntityRenderer::new);
        BlockRenderLayerMap.putBlock(ModBlocks.DRAGONFLAME_CACTUS_PLANT, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(ModBlocks.DRAGONFLAME_CACTUS_BLOCK, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(ModBlocks.FROST_WILLOW_LOG, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(ModBlocks.FROST_WILLOW_LEAVES, BlockRenderLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(ModBlocks.MOON_GLOBE, BlockRenderLayer.TRANSLUCENT);

        EntityModelLayerRegistry.registerModelLayer(FireglobeBlockEntityRenderer.FIREGLOBE_SIDES, FireglobeBlockEntityRenderer::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(DragonflameCactusEntityModel.CACTUS, DragonflameCactusEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MoonGlobeEntityModel.GLOBE, MoonGlobeEntityModel::getTexturedModelData);

        EntityRendererFactories.register(ModEntities.DragonflameCactusEntityType, DragonflameCactusEntityRenderer::new);
        EntityRendererFactories.register(ModEntities.MoonGlobeEntityType, MoonGlobeEntityRenderer::new);

        registerKeybinds();
        registerItemTooltips();
    }

    public void registerKeybinds() {
        final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of(Wingcrafter.MOD_ID, "keybinds"));
        castKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.wingcrafter.cast",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                CATEGORY
        ));
        mouseScrollModifierKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.wingcrafter.mouse_scroll_modifier",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ItemStack stack = ItemStack.EMPTY;
            if (client.player != null) {
                if (client.player.getOffHandStack().isIn(TagKey.of(RegistryKeys.ITEM, Wingcrafter.id("spellcasters")))) {
                    stack = client.player.getOffHandStack();
                } else if (client.player.getMainHandStack().isIn(TagKey.of(RegistryKeys.ITEM, Wingcrafter.id("spellcasters")))) {
                    stack = client.player.getMainHandStack();
                }
                while (castKeybind.wasPressed()) {
                    if (stack.isIn(TagKey.of(RegistryKeys.ITEM, Wingcrafter.id("spellcasters")))) {
                        if (stack.contains(ModDataComponentTypes.SPELLCASTER_SPELLS)) {
                            List<String> spells = stack.get(ModDataComponentTypes.SPELLCASTER_SPELLS);
                            int selectedSpell = stack.getOrDefault(ModDataComponentTypes.SPELLCASTER_SELECTED_SLOT, 0);
                            if (spells == null) return;
                            String spell = spells.get(selectedSpell);
                            if (spell != null && !spell.equals("none")) {
                                CastSpellPayload payload = new CastSpellPayload(spell);
                                ClientPlayNetworking.send(payload);
                            }
                        }
                    }
                }
            }
        });

    }

    public void registerItemTooltips() {

        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
            if (itemStack.isOf(ModItems.QUILL)) {
                list.add(1, Text.translatable("item.wingcrafter.quill.tooltip.1").withColor(0xFFAAAAAA));
                list.add(2, Text.translatable("item.wingcrafter.quill.tooltip.2").withColor(0xFFAAAAAA));
                list.add(3, Text.empty());
                list.add(4, Text.translatable("item.wingcrafter.quill.tooltip.3").withColor(0xFFAAAAAA));
            }
            if (itemStack.isOf(ModItems.SOUL_SCROLL)) {
                list.add(1, Text.translatable("item.wingcrafter.soul_scroll.tooltip.1").withColor(0xFFAAAAAA));
                list.add(2, Text.translatable("item.wingcrafter.soul_scroll.tooltip.2").withColor(0xFFAAAAAA));
                list.add(3, Text.empty());
                list.add(4, Text.translatable("item.wingcrafter.soul_scroll.tooltip.3").withColor(0xFFAAAAAA));
                if (itemStack.contains(ModDataComponentTypes.SPELLCASTER_OWNER)) {
                    String ownerName = itemStack.get(ModDataComponentTypes.SPELLCASTER_OWNER_NAME);
                    if (ownerName != null) {
                        list.add(1, Text.translatable("item.wingcrafter.soul_scroll.tooltip.owner").withColor(0xFFAAAAAA).append(Text.literal(ownerName).withColor(0xFFFFFFFF)));
                        list.add(2, Text.empty());
                    }
                }
                if (itemStack.contains(ModDataComponentTypes.SPELLCASTER_SPELLS)) {
                    List<String> spells = itemStack.get(ModDataComponentTypes.SPELLCASTER_SPELLS);
                    if (spells == null) return;
                    int i = 3;
                    int slot = 1;
                    for (String spellID : spells) {
                        Spell spell;

                        if (Objects.equals(spellID, "none") || spellID == null) continue;
                        try {
                            spell = SpellRegistry.getSpell(spellID);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        if (spell == null) continue;

                        list.add(i, Text.translatable("item.wingcrafter.tooltip.spellcaster.spell", slot).withColor(0xFFFFFFFF)
                                .append(Text.translatable("spell.wingcrafter." + spell.getSpellID()).withColor(spell.getColor())));
                        slot++;
                        i++;
                    }
                }
                if (MinecraftClient.getInstance().options.advancedItemTooltips) {
                    list.add(list.size()-2, Text.translatable("item.wingcrafter.tooltip.spellcaster").withColor(0xFF555555));
                }
            }
            if (itemStack.isOf(ModItems.FIREGLOBE)) {
                FireglobeGlass glass = itemStack.get(ModDataComponentTypes.FIREGLOBE_GLASS);
                if (glass != null) {
                    list.add(1, Text.empty());
                    list.add(2, Text.translatable("glass.wingcrafter." + glass.front()).withColor(0xFFAAAAAA));
                    list.add(3, Text.translatable("glass.wingcrafter." + glass.left()).withColor(0xFFAAAAAA));
                    list.add(4, Text.translatable("glass.wingcrafter." + glass.back()).withColor(0xFFAAAAAA));
                    list.add(5, Text.translatable("glass.wingcrafter." + glass.right()).withColor(0xFFAAAAAA));
                }
            }
            if (itemStack.isOf(ModItems.DRAGONFLAME_CACTUS)) {
                if (itemStack.contains(ModDataComponentTypes.DRAGONFLAME_CACTUS_FUSE)) {
                    int fuseTicks = itemStack.getOrDefault(ModDataComponentTypes.DRAGONFLAME_CACTUS_FUSE, 20);
                    String fuseSeconds = new DecimalFormat("0.00").format((double) fuseTicks/20);
                    list.add(1, Text.translatable("item.wingcrafter.dragonflame_cactus.tooltip.fuse.1").withColor(0xAAAAAA)
                            .append(Text.literal(String.valueOf(fuseTicks)).withColor(0xFFAAAAAA))
                            .append(Text.translatable("item.wingcrafter.dragonflame_cactus.tooltip.fuse.2").withColor(0xFFAAAAAA))
                            .append(Text.literal(fuseSeconds).withColor(0xFFAAAAAA))
                            .append(Text.translatable("item.wingcrafter.dragonflame_cactus.tooltip.fuse.3").withColor(0xFFAAAAAA)));
                }
            }
        });

    }
}
