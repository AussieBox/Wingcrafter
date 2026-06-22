package org.aussiebox.wingcrafter.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.block.blockentities.FireglobeBlockEntity;
import org.aussiebox.wingcrafter.block.blockentities.ScrollBlockEntity;

public class ModBlockEntities {

    public static final BlockEntityType<ScrollBlockEntity> SCROLL_BLOCK_ENTITY =
            register("scroll", ScrollBlockEntity::new, ModBlocks.SCROLL);

    public static final BlockEntityType<FireglobeBlockEntity> FIREGLOBE_BLOCK_ENTITY =
            register("fireglobe", FireglobeBlockEntity::new, ModBlocks.FIREGLOBE);

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.of(Wingcrafter.MOD_ID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

    public static void init() {

    }
}
