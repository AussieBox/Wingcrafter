package org.aussiebox.wingcrafter.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

import java.util.List;
import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<String> SCROLL_TEXT =
            register("scroll_text", builder -> builder.codec(Codec.STRING));

    public static final ComponentType<String> SCROLL_TITLE =
            register("scroll_title", builder -> builder.codec(Codec.STRING));

    public static final ComponentType<String> SPELLCASTER_OWNER =
            register("spellcaster_owner", builder -> builder.codec(Codec.STRING));

    public static final ComponentType<String> SPELLCASTER_OWNER_NAME =
            register("spellcaster_owner_name", builder -> builder.codec(Codec.STRING));

    public static final ComponentType<Integer> SPELLCASTER_SLOT_MAXIMUM =
            register("spellcaster_slot_maximum", builder -> builder.codec(Codec.INT));

    public static final ComponentType<Integer> SPELLCASTER_SELECTED_SLOT =
            register("spellcaster_selected_slot", builder -> builder.codec(Codec.INT));

    public static final ComponentType<List<String>> SPELLCASTER_SPELLS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Wingcrafter.MOD_ID, "spellcaster_spells"),
            ComponentType.<List<String>>builder().codec(Codec.list(Codec.STRING)).build()
    );

    public static final ComponentType<FireglobeGlass> FIREGLOBE_GLASS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Wingcrafter.MOD_ID, "fireglobe_glass"),
            ComponentType.<FireglobeGlass>builder().codec(FireglobeGlass.CODEC).build()
    );

    public static final ComponentType<Integer> DRAGONFLAME_CACTUS_FUSE =
            register("dragonflame_cactus_fuse", builder -> builder.codec(Codec.INT));


    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Wingcrafter.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void init() {

    }
}
