package org.aussiebox.wingcrafter.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.TintedParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.block.custom.*;
import org.aussiebox.wingcrafter.item.ModItems;

import java.util.function.Function;

public class ModBlocks {

    public static final Block SCROLL = register(
            "scroll",
            ScrollBlock::new,
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.WOOD)
                    .nonOpaque(),
            true
    );

    public static final Block FIREGLOBE = register(
            "fireglobe",
            FireglobeBlock::new,
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.LANTERN)
                    .luminance(FireglobeBlock::getLuminance)
                    .strength(3.5F)
                    .solid()
                    .nonOpaque(),
            false
    );

    public static final Block DRAGONFLAME_CACTUS_PLANT = register(
            "dragonflame_cactus_plant",
            DragonflameCactusPlantBlock::new,
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.WOOD)
                    .ticksRandomly()
                    .nonOpaque(),
            false
    );

    public static final Block DRAGONFLAME_CACTUS_BLOCK = register(
            "dragonflame_cactus_block",
            DragonflameCactusBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .ticksRandomly()
                    .strength(0.4F)
                    .sounds(BlockSoundGroup.WOOL)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .nonOpaque(),
            true
    );

    public static final Block FROST_WILLOW_LOG = register(
            "frost_willow_log",
            TranslucentPillarBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_BLUE)
                    .hardness(2)
                    .resistance(2)
                    .slipperiness(0.98F)
                    .strength(2.0F)
                    .sounds(BlockSoundGroup.GLASS)
                    .instrument(NoteBlockInstrument.CHIME)
                    .nonOpaque(),
            true
    );

    public static final Block FROST_WILLOW_LEAVES = register(
            "frost_willow_leaves",
            (settings) -> new UntintedParticleLeavesBlock(0.01F, TintedParticleEffect.create(ParticleTypes.TINTED_LEAVES, 8573695), settings),
            Blocks.createLeavesSettings(BlockSoundGroup.GRASS)
                    .slipperiness(0.971F),
            true
    );

    private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean shouldRegisterItem) {
        RegistryKey<Block> blockKey = keyOfBlock(name);
        Block block = blockFactory.apply(settings.registryKey(blockKey));

        if (shouldRegisterItem) {
            RegistryKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey());
            Registry.register(Registries.ITEM, itemKey, blockItem);
        }

        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Wingcrafter.MOD_ID, name));
    }

    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Wingcrafter.MOD_ID, name));
    }

    public static void registerModBlocks() {
        Wingcrafter.LOGGER.info("Registering mod blocks for " + Wingcrafter.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ModItems.WINGCRAFTER_KEY).register((itemGroup) -> {
            // itemGroup.add(ModBlocks.SCROLL.asItem());
                // This is done in ModItems now.
        });
    }

}
