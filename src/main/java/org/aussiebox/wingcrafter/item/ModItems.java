package org.aussiebox.wingcrafter.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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

import java.util.function.Function;

public class ModItems {

    public static final Item SOUL_SCROLL = registerItem("soul_scroll", Item::new, new Item.Settings()
            .rarity(Rarity.EPIC)
            .fireproof()
            .maxCount(1)
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

    public static void registerModItems() {
        Wingcrafter.LOGGER.info("Registering mod items for " + Wingcrafter.MOD_ID);

        Registry.register(Registries.ITEM_GROUP, WINGCRAFTER_KEY, WINGCRAFTER);
        ItemGroupEvents.modifyEntriesEvent(WINGCRAFTER_KEY).register(itemGroup -> {
            itemGroup.add(SOUL_SCROLL);
        });
    }
}