package org.aussiebox.wingcrafter.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.block.ModBlocks;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;
import org.aussiebox.wingcrafter.item.custom.DragonflameCactusItem;
import org.aussiebox.wingcrafter.item.custom.FireglobeItem;
import org.aussiebox.wingcrafter.item.custom.SoulScrollItem;

import java.util.function.Function;

public class ModItems {

    public static final Item SOUL_SCROLL = registerItem("soul_scroll", SoulScrollItem::new, new Item.Settings()
            .rarity(Rarity.EPIC)
            .fireproof()
            .maxCount(1)
            .component(ModDataComponentTypes.SOUL_SCROLL_OWNER, null)
            .component(ModDataComponentTypes.SOUL_SCROLL_OWNER_NAME, null)
            .component(ModDataComponentTypes.SOUL_SCROLL_SPELLS, null)
    );
    public static final BlockItem FIREGLOBE = registerBlockItem("fireglobe", FireglobeItem::new, new Item.Settings()
            .useBlockPrefixedTranslationKey()
    );
    public static final Item SEAL = registerItem("seal", Item::new, new Item.Settings()
            .maxCount(32)
    );
    public static final Item QUILL = registerItem("quill", Item::new, new Item.Settings()
            .maxCount(1)
            .maxDamage(64)
    );
    public static final Item DRAGONFLAME_CACTUS = registerItem("dragonflame_cactus", DragonflameCactusItem::new, new Item.Settings()

    );

    public static final RegistryKey<ItemGroup> WINGCRAFTER_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(Wingcrafter.MOD_ID, "wingcrafter"));
    public static final ItemGroup WINGCRAFTER = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModBlocks.SCROLL.asItem()))
            .displayName(Text.translatable("itemGroup.wingcrafter.wingcrafter"))
            .build();

    public static Item registerItem(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Wingcrafter.MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }

    public static BlockItem registerBlockItem(String name, Function<Item.Settings, BlockItem> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Wingcrafter.MOD_ID, name));
        BlockItem item = itemFactory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }

    public static void registerModItems() {
        Wingcrafter.LOGGER.info("Registering mod items for " + Wingcrafter.MOD_ID);

        Registry.register(Registries.ITEM_GROUP, WINGCRAFTER_KEY, WINGCRAFTER);
        ItemGroupEvents.modifyEntriesEvent(WINGCRAFTER_KEY).register(itemGroup -> {
            itemGroup.add(ModBlocks.SCROLL.asItem());
            itemGroup.add(SOUL_SCROLL);
            itemGroup.add(SEAL);
            itemGroup.add(QUILL);
            itemGroup.add(FIREGLOBE.getDefaultStack());
            itemGroup.add(DRAGONFLAME_CACTUS.getDefaultStack());
            itemGroup.add(ModBlocks.DRAGONFLAME_CACTUS_BLOCK.asItem());
            itemGroup.add(ModBlocks.FROST_WILLOW_LOG.asItem());
            itemGroup.add(ModBlocks.FROST_WILLOW_LEAVES.asItem());
        });

    }
}