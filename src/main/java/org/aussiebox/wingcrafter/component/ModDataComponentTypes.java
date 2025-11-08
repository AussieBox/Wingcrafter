package org.aussiebox.wingcrafter.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final ComponentType<String> SCROLL_TEXT =
            register("scroll_text", builder -> builder.codec(Codec.STRING));

    public static final ComponentType<String> SCROLL_TITLE =
            register("scroll_title", builder -> builder.codec(Codec.STRING));

    public static final ComponentType<String> SOUL_SCROLL_OWNER =
            register("soul_scroll_owner", builder -> builder.codec(Codec.STRING));

    public static final ComponentType<String> SOUL_SCROLL_OWNER_NAME =
            register("soul_scroll_owner_name", builder -> builder.codec(Codec.STRING));

    public static final ComponentType<SoulScrollSpells> SOUL_SCROLL_SPELLS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Wingcrafter.MOD_ID, "soul_scroll_spells"),
            ComponentType.<SoulScrollSpells>builder().codec(SoulScrollSpells.CODEC).build()
    );

    private static <T>ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Wingcrafter.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponentTypes() {
        Wingcrafter.LOGGER.info("Registering ComponentTypes for mod " + Wingcrafter.MOD_ID);
    }
}
