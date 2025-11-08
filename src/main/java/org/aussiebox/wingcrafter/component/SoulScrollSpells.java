package org.aussiebox.wingcrafter.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record SoulScrollSpells(String spell1, String spell2, String spell3) {
    public static final Codec<SoulScrollSpells> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.STRING.optionalFieldOf("spell1", null).forGetter(SoulScrollSpells::spell1),
                Codec.STRING.optionalFieldOf("spell2", null).forGetter(SoulScrollSpells::spell2),
                Codec.STRING.optionalFieldOf("spell3", null).forGetter(SoulScrollSpells::spell3)
        ).apply(builder, SoulScrollSpells::new);
    });
}
