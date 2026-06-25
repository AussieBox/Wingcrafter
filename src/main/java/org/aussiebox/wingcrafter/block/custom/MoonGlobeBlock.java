package org.aussiebox.wingcrafter.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.aussiebox.wingcrafter.block.blockentities.MoonGlobeBlockEntity;
import org.aussiebox.wingcrafter.entity.ModEntities;
import org.aussiebox.wingcrafter.entity.MoonGlobeEntity;
import org.aussiebox.wingcrafter.util.WingcrafterUtil;
import org.jspecify.annotations.Nullable;

public class MoonGlobeBlock extends BlockWithEntity {
    public static final MapCodec<FireglobeBlock> CODEC = createCodec(FireglobeBlock::new);
    private static final VoxelShape OUTLINE_SHAPE = Block.createCuboidShape(3, 3, 3, 13, 13, 13);
    public static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(3, 3, 3, 13, 13, 13);

    public MoonGlobeBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    public static int getLuminance(BlockState state) {
        return 8;
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.up()).isIn(BlockTags.LEAVES);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        world.setBlockState(pos, Blocks.AIR.getDefaultState());

        Vec3d spawn = pos.toCenterPos();
        MoonGlobeEntity entity = new MoonGlobeEntity(ModEntities.MOON_GLOBE, world);
        entity.setPos(spawn.x, spawn.y-0.35, spawn.z);
        entity.lastX = entity.getX();
        entity.lastY = entity.getY();
        entity.lastZ = entity.getZ();

        world.spawnEntity(entity);
        WingcrafterUtil.grantAdvancement((ServerPlayerEntity) player, "obtain_moon_globe");
        return ActionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MoonGlobeBlockEntity(pos, state);
    }
}
