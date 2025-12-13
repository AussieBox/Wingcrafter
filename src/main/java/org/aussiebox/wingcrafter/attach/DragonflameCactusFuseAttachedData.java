package org.aussiebox.wingcrafter.attach;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record DragonflameCactusFuseAttachedData(int fuse) {
    public static Codec<DragonflameCactusFuseAttachedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("fuse").forGetter(DragonflameCactusFuseAttachedData::fuse)
    ).apply(instance, DragonflameCactusFuseAttachedData::new));
    public static PacketCodec<ByteBuf, DragonflameCactusFuseAttachedData> PACKET_CODEC = PacketCodecs.codec(CODEC);

    public static DragonflameCactusFuseAttachedData DEFAULT = new DragonflameCactusFuseAttachedData(20);
}
