package org.aussiebox.wingcrafter;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public interface WingcrafterConstants {

    /// FIREGLOBE GLASS - ITEM SPRITE IDS
    // Each itemSpriteIDs entry links an item to a specific texture for Fireglobe glass.

    Map<Item, String> itemSpriteIDs = new HashMap<>();
    static Map<Item, String> getFireglobeGlassItemSpriteIDs() {
        itemSpriteIDs.putIfAbsent(Items.GLASS_PANE, "clear");
        itemSpriteIDs.putIfAbsent(Items.WHITE_STAINED_GLASS_PANE, "white");
        itemSpriteIDs.putIfAbsent(Items.LIGHT_GRAY_STAINED_GLASS_PANE, "light_gray");
        itemSpriteIDs.putIfAbsent(Items.GRAY_STAINED_GLASS_PANE, "gray");
        itemSpriteIDs.putIfAbsent(Items.BLACK_STAINED_GLASS_PANE, "black");
        itemSpriteIDs.putIfAbsent(Items.BROWN_STAINED_GLASS_PANE, "brown");
        itemSpriteIDs.putIfAbsent(Items.RED_STAINED_GLASS_PANE, "red");
        itemSpriteIDs.putIfAbsent(Items.ORANGE_STAINED_GLASS_PANE, "orange");
        itemSpriteIDs.putIfAbsent(Items.YELLOW_STAINED_GLASS_PANE, "yellow");
        itemSpriteIDs.putIfAbsent(Items.LIME_STAINED_GLASS_PANE, "lime");
        itemSpriteIDs.putIfAbsent(Items.GREEN_STAINED_GLASS_PANE, "green");
        itemSpriteIDs.putIfAbsent(Items.CYAN_STAINED_GLASS_PANE, "cyan");
        itemSpriteIDs.putIfAbsent(Items.LIGHT_BLUE_STAINED_GLASS_PANE, "light_blue");
        itemSpriteIDs.putIfAbsent(Items.BLUE_STAINED_GLASS_PANE, "blue");
        itemSpriteIDs.putIfAbsent(Items.PURPLE_STAINED_GLASS_PANE, "purple");
        itemSpriteIDs.putIfAbsent(Items.MAGENTA_STAINED_GLASS_PANE, "magenta");
        itemSpriteIDs.putIfAbsent(Items.PINK_STAINED_GLASS_PANE, "pink");
        return itemSpriteIDs;
    }

}
