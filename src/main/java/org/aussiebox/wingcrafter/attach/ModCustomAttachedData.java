package org.aussiebox.wingcrafter.attach;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record ModCustomAttachedData(int soul) {
    public static Codec<ModCustomAttachedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("soul").forGetter(ModCustomAttachedData::soul)
    ).apply(instance, ModCustomAttachedData::new));
    public static PacketCodec<ByteBuf, ModCustomAttachedData> PACKET_CODEC = PacketCodecs.codec(CODEC);

    public static ModCustomAttachedData DEFAULT = new ModCustomAttachedData(1000);

    public ModCustomAttachedData setSoul(int set) {
            return new ModCustomAttachedData(set);
    }

    public ModCustomAttachedData addSoul(ModCustomAttachedData data, int add) {
        if (data.soul() + add >= 1000) {
            return new ModCustomAttachedData(1000);
        } else {
            return new ModCustomAttachedData(data.soul() + add);
        }
    }

    public ModCustomAttachedData removeSoul(ModCustomAttachedData data, int remove) {
        if (data.soul() - remove <= 0) {
            return new ModCustomAttachedData(0);
        } else {
            return new ModCustomAttachedData(data.soul() - remove);
        }
    }
}
