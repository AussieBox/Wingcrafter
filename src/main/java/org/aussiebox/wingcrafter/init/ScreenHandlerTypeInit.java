package org.aussiebox.wingcrafter.init;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.network.BlockPosPayload;
import org.aussiebox.wingcrafter.network.SoulScrollDataPayload;
import org.aussiebox.wingcrafter.screenhandler.ScrollBlockScreenHandler;
import org.aussiebox.wingcrafter.screenhandler.SoulScrollSpellSelectScreenHandler;

public class ScreenHandlerTypeInit {
    public static final ScreenHandlerType<ScrollBlockScreenHandler> SCROLL =
            register("scroll", ScrollBlockScreenHandler::new, BlockPosPayload.PACKET_CODEC);

    public static final ScreenHandlerType<SoulScrollSpellSelectScreenHandler> SOUL_SCROLL_SPELL_SELECT =
            register("soul_scroll_spell_select", SoulScrollSpellSelectScreenHandler::new, SoulScrollDataPayload.PACKET_CODEC);

    public static <T extends ScreenHandler, D extends CustomPayload> ExtendedScreenHandlerType<T, D>
        register(String name,
                 ExtendedScreenHandlerType.ExtendedFactory<T, D> factory,
                 PacketCodec<? super RegistryByteBuf, D> codec) {
        return Registry.register(Registries.SCREEN_HANDLER, Wingcrafter.id(name), new ExtendedScreenHandlerType<>(factory, codec));
    }

    public static void init() {

    }
}
