package org.aussiebox.wingcrafter.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FireglobeGlass(String front, String left, String back, String right) {
    public static final Codec<FireglobeGlass> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.STRING.fieldOf("front").forGetter(FireglobeGlass::front),
                Codec.STRING.fieldOf("left").forGetter(FireglobeGlass::left),
                Codec.STRING.fieldOf("back").forGetter(FireglobeGlass::back),
                Codec.STRING.fieldOf("right").forGetter(FireglobeGlass::right)
        ).apply(builder, FireglobeGlass::new);
    });
}
