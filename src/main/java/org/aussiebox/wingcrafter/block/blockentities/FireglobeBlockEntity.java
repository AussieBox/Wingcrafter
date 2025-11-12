package org.aussiebox.wingcrafter.block.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.block.ModBlockEntities;
import org.aussiebox.wingcrafter.component.FireglobeGlass;

import java.util.Optional;

public class FireglobeBlockEntity extends BlockEntity {
    private String front;
    private String left;
    private String back;
    private String right;

    public FireglobeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FIREGLOBE_BLOCK_ENTITY, pos, state);
    }

    public FireglobeGlass getGlass() {
        Wingcrafter.LOGGER.info(front);
        return new FireglobeGlass(front, left, back, right);
    }

    public void setGlass(String frontGlass, String leftGlass, String backGlass, String rightGlass) {
        front = frontGlass;
        left = leftGlass;
        back = backGlass;
        right = rightGlass;
        markDirty();
    }

    @Override
    protected void writeData(WriteView writeView) {
        writeView.put("glass", FireglobeGlass.CODEC, new FireglobeGlass(front, left, back, right));
        super.writeData(writeView);
    }

    @Override
    protected void readData(ReadView readView) {
        super.readData(readView);
        Optional<FireglobeGlass> readGlass = readView.read("glass", FireglobeGlass.CODEC);
        if (readGlass.isPresent()) {
            FireglobeGlass glass = readGlass.get();
            front = glass.front();
            left = glass.left();
            back = glass.back();
            right = glass.right();
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
