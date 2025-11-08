package org.aussiebox.wingcrafter.item;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.MinecraftClient;
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
    public static final Item SEAL = registerItem("seal", Item::new, new Item.Settings()
            .maxCount(32)
    );
    public static final Item QUILL = registerItem("quill", Item::new, new Item.Settings()
            .maxCount(1)
            .maxDamage(64)
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
            itemGroup.add(ModBlocks.SCROLL.asItem());
            itemGroup.add(SOUL_SCROLL);
            itemGroup.add(SEAL);
            itemGroup.add(QUILL);
        });

        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
            if (itemStack.isOf(QUILL)) {
                list.add(1, Text.translatable("item.wingcrafter.quill.tooltip.1").withColor(0xFFAAAAAA));
                list.add(2, Text.translatable("item.wingcrafter.quill.tooltip.2").withColor(0xFFAAAAAA));
                list.add(3, Text.empty());
                list.add(4, Text.translatable("item.wingcrafter.quill.tooltip.3").withColor(0xFFAAAAAA));
            }
            if (itemStack.isOf(SOUL_SCROLL)) {
                list.add(1, Text.translatable("item.wingcrafter.soul_scroll.tooltip.1").withColor(0xFFAAAAAA));
                list.add(2, Text.translatable("item.wingcrafter.soul_scroll.tooltip.2").withColor(0xFFAAAAAA));
                list.add(3, Text.empty());
                list.add(4, Text.translatable("item.wingcrafter.soul_scroll.tooltip.3").withColor(0xFFAAAAAA));
                if (MinecraftClient.getInstance().options.advancedItemTooltips) {
                    list.add(list.size()-2, Text.translatable("item.wingcrafter.tooltip.spell_caster").withColor(0xFF555555));
                }
                if (itemStack.contains(ModDataComponentTypes.SOUL_SCROLL_OWNER)) {
                    String ownerName = itemStack.get(ModDataComponentTypes.SOUL_SCROLL_OWNER_NAME);
                    if (ownerName != null) {
                        list.add(1, Text.translatable("item.wingcrafter.soul_scroll.tooltip.owner").withColor(0xFFAAAAAA).append(Text.literal(ownerName).withColor(0xFFFFFFFF)));
                        list.add(2, Text.empty());
                    }
                }
            }
        });
    }
}