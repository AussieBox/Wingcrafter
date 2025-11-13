package org.aussiebox.wingcrafter.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.block.custom.FireglobeBlock;
import org.aussiebox.wingcrafter.block.custom.ScrollBlock;
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
