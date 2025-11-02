package org.aussiebox.wingcrafter.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.aussiebox.wingcrafter.Wingcrafter;

public record ScrollTextPayload(BlockPos pos, String text, String titleText) implements CustomPayload {
    public static final Identifier SCROLL_TEXT_ID = Identifier.of(Wingcrafter.MOD_ID, "scroll_text");
    public static final CustomPayload.Id<ScrollTextPayload> ID = new CustomPayload.Id<>(SCROLL_TEXT_ID);
    public static final PacketCodec<RegistryByteBuf, ScrollTextPayload> CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, ScrollTextPayload::pos, PacketCodecs.STRING, ScrollTextPayload::text, PacketCodecs.STRING, ScrollTextPayload::titleText, ScrollTextPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}