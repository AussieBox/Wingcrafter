package org.aussiebox.wingcrafter;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.block.ModBlockEntities;
import org.aussiebox.wingcrafter.block.ModBlocks;
import org.aussiebox.wingcrafter.block.blockentities.ScrollBlockEntity;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;
import org.aussiebox.wingcrafter.init.ScreenHandlerTypeInit;
import org.aussiebox.wingcrafter.item.ModItems;
import org.aussiebox.wingcrafter.network.ScrollTextPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static org.aussiebox.wingcrafter.block.custom.ScrollBlock.TITLED;
import static org.aussiebox.wingcrafter.block.custom.ScrollBlock.WRITTEN;

public class Wingcrafter implements ModInitializer {
    public static final String MOD_ID = "wingcrafter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(ScrollTextPayload.ID, ScrollTextPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ScrollTextPayload.ID, (payload, context) -> {
            ScrollBlockEntity blockEntity = (ScrollBlockEntity) context.player().getEntityWorld().getBlockEntity(payload.pos());
            if (blockEntity instanceof ScrollBlockEntity scrollBlockEntity) {
                scrollBlockEntity.setTitle(payload.titleText());
                scrollBlockEntity.setText(payload.text());
                if (!Objects.equals(payload.text(), "")) {
                    Objects.requireNonNull(blockEntity.getWorld()).setBlockState(blockEntity.getPos(), blockEntity.getWorld().getBlockState(blockEntity.getPos()).with(WRITTEN, true));
                } else {
                    Objects.requireNonNull(blockEntity.getWorld()).setBlockState(blockEntity.getPos(), blockEntity.getWorld().getBlockState(blockEntity.getPos()).with(WRITTEN, false));
                }
                if (!Objects.equals(payload.titleText(), "")) {
                    Objects.requireNonNull(blockEntity.getWorld()).setBlockState(blockEntity.getPos(), blockEntity.getWorld().getBlockState(blockEntity.getPos()).with(TITLED, true));
                } else {
                    Objects.requireNonNull(blockEntity.getWorld()).setBlockState(blockEntity.getPos(), blockEntity.getWorld().getBlockState(blockEntity.getPos()).with(TITLED, false));
                }
                Objects.requireNonNull(scrollBlockEntity.getWorld()).updateListeners(payload.pos(), scrollBlockEntity.getWorld().getBlockState(payload.pos()), scrollBlockEntity.getWorld().getBlockState(payload.pos()), Block.NOTIFY_LISTENERS);
            }
        });

        ScreenHandlerTypeInit.init();
        ModItems.registerModItems();
        ModBlockEntities.registerModBlockEntities();
        ModDataComponentTypes.registerDataComponentTypes();
        ModBlocks.registerModBlocks();
    }
}
