package org.aussiebox.wingcrafter.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

public record SoulKillPayload(String uuid) implements CustomPayload {
    public static final Identifier SOUL_KILL_ID = Identifier.of(Wingcrafter.MOD_ID, "soul_kill");
    public static final Id<SoulKillPayload> ID = new Id<>(SOUL_KILL_ID);
    public static final PacketCodec<RegistryByteBuf, SoulKillPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, SoulKillPayload::uuid, SoulKillPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}