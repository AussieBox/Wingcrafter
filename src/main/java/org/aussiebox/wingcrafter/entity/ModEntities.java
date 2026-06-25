package org.aussiebox.wingcrafter.entity;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import org.aussiebox.wingcrafter.Wingcrafter;

import java.util.List;
import java.util.UUID;

public class ModEntities {
    public static final TrackedDataHandler<UUID> UUID = TrackedDataHandler.create(Uuids.PACKET_CODEC);
    public static final TrackedDataHandler<List<BlockPos>> BLOCKPOS_LIST = TrackedDataHandler.create(PacketCodecs.codec(Codec.list(BlockPos.CODEC)));

    public static final EntityType<DragonflameCactusEntity> DRAGONFLAME_CACTUS = Registry.register(Registries.ENTITY_TYPE,
            Wingcrafter.id("dragonflame_cactus"),
            EntityType.Builder.<DragonflameCactusEntity>create(DragonflameCactusEntity::new, SpawnGroup.MISC)
                    .dimensions(0.4F, 0.4F).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Wingcrafter.id("dragonflame_cactus")))
    );
    public static final EntityType<MoonGlobeEntity> MOON_GLOBE = Registry.register(Registries.ENTITY_TYPE,
            Wingcrafter.id("moon_globe"),
            EntityType.Builder.create(MoonGlobeEntity::new, SpawnGroup.MISC)
                    .dimensions(0.65F, 0.65F).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Wingcrafter.id("moon_globe")))
    );

    public static void init() {
        FabricTrackedDataRegistry.register(Wingcrafter.id("uuid"), UUID);
        FabricTrackedDataRegistry.register(Wingcrafter.id("blockpos_list"), BLOCKPOS_LIST);
    }
}
