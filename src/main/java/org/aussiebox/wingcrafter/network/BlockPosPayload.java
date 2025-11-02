package org.aussiebox.wingcrafter.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import org.aussiebox.wingcrafter.Wingcrafter;

public record BlockPosPayload(BlockPos pos, String text, String titleText) implements CustomPayload {
    public static final Id<BlockPosPayload> ID = new Id<>(Wingcrafter.id("block_pos"));
    public static final PacketCodec<RegistryByteBuf, BlockPosPayload> PACKET_CODEC =
            PacketCodec.tuple(BlockPos.PACKET_CODEC, BlockPosPayload::pos, PacketCodecs.STRING, BlockPosPayload::text, PacketCodecs.STRING, BlockPosPayload::titleText, BlockPosPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}