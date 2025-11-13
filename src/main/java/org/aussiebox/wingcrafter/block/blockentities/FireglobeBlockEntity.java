package org.aussiebox.wingcrafter.block.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import org.aussiebox.wingcrafter.block.ModBlockEntities;
import org.aussiebox.wingcrafter.component.FireglobeGlass;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;

import java.util.Optional;

public class FireglobeBlockEntity extends BlockEntity {
    public String front;
    public String left;
    public String back;
    public String right;

    public FireglobeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FIREGLOBE_BLOCK_ENTITY, pos, state);
    }

    public void setGlass(String frontGlass, String leftGlass, String backGlass, String rightGlass) {
        this.front = frontGlass;
        this.left = leftGlass;
        this.back = backGlass;
        this.right = rightGlass;
        markDirty();
    }

    public FireglobeGlass getGlass() {
        return new FireglobeGlass(this.front, this.left, this.back, this.right);
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        FireglobeGlass glass = components.get(ModDataComponentTypes.FIREGLOBE_GLASS);
        this.front = glass.front();
        this.left = glass.left();
        this.back = glass.back();
        this.right = glass.right();
    }

    @Override
    protected void addComponents(ComponentMap.Builder builder) {
        super.addComponents(builder);
        builder.add(ModDataComponentTypes.FIREGLOBE_GLASS, new FireglobeGlass(this.front, this.left, this.back, this.right));
    }

    @Override
    protected void writeData(WriteView writeView) {
        writeView.put("glass", FireglobeGlass.CODEC, getGlass());
        super.writeData(writeView);
    }

    @Override
    protected void readData(ReadView readView) {
        super.readData(readView);
        Optional<FireglobeGlass> readGlass = readView.read("glass", FireglobeGlass.CODEC);
        if (readGlass.isPresent()) {
            FireglobeGlass glass = readGlass.get();
            this.front = glass.front();
            this.left = glass.left();
            this.back = glass.back();
            this.right = glass.right();
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }
}
