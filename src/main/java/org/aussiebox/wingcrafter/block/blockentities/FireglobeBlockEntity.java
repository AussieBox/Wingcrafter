package org.aussiebox.wingcrafter.block.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.block.ModBlockEntities;
import org.aussiebox.wingcrafter.block.custom.FireglobeBlock;
import org.aussiebox.wingcrafter.component.FireglobeGlass;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

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

    public Direction getFacing() {
        return this.getCachedState().get(Properties.FACING);
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
            if (this.front == null) {
                this.front = "clear";
            }
            if (this.left == null) {
                this.left = "clear";
            }
            if (this.back == null) {
                this.back = "clear";
            }
            if (this.right == null) {
                this.right = "clear";
            }
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

    public static void tick(World world, BlockPos pos, BlockState state, FireglobeBlockEntity entity) {
        if (state.get(FireglobeBlock.LIT)) {
            if (world instanceof ServerWorld serverWorld) {
                if (ThreadLocalRandom.current().nextInt(0, 10) == 0) {
                    if (state.get(FireglobeBlock.HANGING)) {
                        serverWorld.spawnParticles(ParticleTypes.SMALL_FLAME, pos.getX()+0.5, pos.getY()+0.61, pos.getZ()+0.5, 1, 0, 0, 0, 0);
                    } else if (state.get(FireglobeBlock.MOUNTED)) {
                        if (state.get(FireglobeBlock.FACING) == Direction.EAST) {
                            serverWorld.spawnParticles(ParticleTypes.SMALL_FLAME, pos.getX()+0.3, pos.getY()+0.61, pos.getZ()+0.5, 1, 0, 0, 0, 0);
                        } else if (state.get(FireglobeBlock.FACING) == Direction.NORTH) {
                            serverWorld.spawnParticles(ParticleTypes.SMALL_FLAME, pos.getX()+0.5, pos.getY()+0.61, pos.getZ()+0.7, 1, 0, 0, 0, 0);
                        } else if (state.get(FireglobeBlock.FACING) == Direction.SOUTH) {
                            serverWorld.spawnParticles(ParticleTypes.SMALL_FLAME, pos.getX()+0.5, pos.getY()+0.61, pos.getZ()+0.3, 1, 0, 0, 0, 0);
                        } else if (state.get(FireglobeBlock.FACING) == Direction.WEST) {
                            serverWorld.spawnParticles(ParticleTypes.SMALL_FLAME, pos.getX()+0.7, pos.getY()+0.61, pos.getZ()+0.5, 1, 0, 0, 0, 0);
                        }
                    } else {
                        serverWorld.spawnParticles(ParticleTypes.SMALL_FLAME, pos.getX()+0.5, pos.getY()+0.51, pos.getZ()+0.5, 1, 0, 0, 0, 0);
                    }
                }
                if (ThreadLocalRandom.current().nextInt(0, 6) == 0) {
                    if (state.get(FireglobeBlock.HANGING)) {
                        serverWorld.spawnParticles(ParticleTypes.SMOKE, pos.getX()+0.5, pos.getY()+0.61, pos.getZ()+0.5, 1, 0, 0, 0, 0);
                    } else if (state.get(FireglobeBlock.MOUNTED)) {
                        if (state.get(FireglobeBlock.FACING) == Direction.EAST) {
                            serverWorld.spawnParticles(ParticleTypes.SMOKE, pos.getX()+0.3, pos.getY()+0.61, pos.getZ()+0.5, 1, 0, 0, 0, 0);
                        } else if (state.get(FireglobeBlock.FACING) == Direction.NORTH) {
                            serverWorld.spawnParticles(ParticleTypes.SMOKE, pos.getX()+0.5, pos.getY()+0.61, pos.getZ()+0.7, 1, 0, 0, 0, 0);
                        } else if (state.get(FireglobeBlock.FACING) == Direction.SOUTH) {
                            serverWorld.spawnParticles(ParticleTypes.SMOKE, pos.getX()+0.5, pos.getY()+0.61, pos.getZ()+0.3, 1, 0, 0, 0, 0);
                        } else if (state.get(FireglobeBlock.FACING) == Direction.WEST) {
                            serverWorld.spawnParticles(ParticleTypes.SMOKE, pos.getX()+0.7, pos.getY()+0.61, pos.getZ()+0.5, 1, 0, 0, 0, 0);
                        }
                    } else {
                        serverWorld.spawnParticles(ParticleTypes.SMOKE, pos.getX()+0.5, pos.getY()+0.51, pos.getZ()+0.5, 1, 0, 0, 0, 0);
                    }
                }
                if (ThreadLocalRandom.current().nextInt(0, 100) == 0) {
                    serverWorld.playSound(null, pos, SoundEvents.BLOCK_CANDLE_AMBIENT, SoundCategory.BLOCKS, 2.0F, 0.0F);
                }
            }
        }
    }
}
