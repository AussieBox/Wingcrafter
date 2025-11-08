package org.aussiebox.wingcrafter.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

public record SoulScrollDataPayload(ItemStack itemStack, String spell1, String spell2, String spell3) implements CustomPayload {
    public static final Identifier SOUL_SCROLL_DATA_ID = Identifier.of(Wingcrafter.MOD_ID, "soul_scroll_data");
    public static final Id<SoulScrollDataPayload> ID = new Id<>(SOUL_SCROLL_DATA_ID);
    public static final PacketCodec<RegistryByteBuf, SoulScrollDataPayload> PACKET_CODEC = PacketCodec.tuple(ItemStack.PACKET_CODEC, SoulScrollDataPayload::itemStack, PacketCodecs.STRING, SoulScrollDataPayload::spell1, PacketCodecs.STRING, SoulScrollDataPayload::spell2, PacketCodecs.STRING, SoulScrollDataPayload::spell3, SoulScrollDataPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}