package org.aussiebox.wingcrafter.attach;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record SoulAttachedData(int soul) {
    public static Codec<SoulAttachedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("soul").forGetter(SoulAttachedData::soul)
    ).apply(instance, SoulAttachedData::new));
    public static PacketCodec<ByteBuf, SoulAttachedData> PACKET_CODEC = PacketCodecs.codec(CODEC);

    public static SoulAttachedData DEFAULT = new SoulAttachedData(1000);

    public SoulAttachedData setSoul(int set) {
            return new SoulAttachedData(Math.clamp(set, 0, 1000));
    }

    public SoulAttachedData addSoul(SoulAttachedData data, int add) {
        return new SoulAttachedData(Math.clamp(data.soul() + add, 0, 1000));
    }

    public SoulAttachedData removeSoul(SoulAttachedData data, int remove) {
        return new SoulAttachedData(Math.clamp(data.soul() - remove, 0, 1000));
    }
}
