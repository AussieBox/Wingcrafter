package org.aussiebox.wingcrafter.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.aussiebox.wingcrafter.block.blockentities.FireglobeBlockEntity;
import org.aussiebox.wingcrafter.component.FireglobeGlass;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;
import org.aussiebox.wingcrafter.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class FireglobeBlock extends FacingBlock implements BlockEntityProvider, Waterloggable {
    public static final MapCodec<FireglobeBlock> CODEC = createCodec(FireglobeBlock::new);
    private static final VoxelShape HANGING_SHAPE = Block.createCuboidShape(4, 2, 4, 12, 10, 12);
    private static final VoxelShape STANDING_SHAPE = Block.createCuboidShape(4, 0, 4, 12, 8, 12);

    public static final BooleanProperty LIT = BooleanProperty.of("lit");
    public static final BooleanProperty HANGING = Properties.HANGING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;


    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(HANGING) ? HANGING_SHAPE : STANDING_SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(HANGING) ? HANGING_SHAPE : STANDING_SHAPE;
    }

    public FireglobeBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(LIT, false));
        setDefaultState(getDefaultState().with(HANGING, false));
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    public static int getLuminance(BlockState state) {
        boolean lit = state.get(LIT);
        return lit ? 15 : 0;
    }

    @Override
    protected MapCodec<? extends FacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.getAbilities().allowModifyWorld) {
            return ActionResult.PASS;
        } else {
            if (!(world.getBlockEntity(pos) instanceof FireglobeBlockEntity)) {
                return super.onUse(state, world, pos, player, hit);
            }
            boolean lit = state.get(LIT);
            if (lit) {
                world.setBlockState(pos, state.with(LIT, false));
                world.playSound(null, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 2.0F, 1.0F);
                return ActionResult.SUCCESS;
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.getAbilities().allowModifyWorld) {
            return ActionResult.PASS;
        } else {
            if (!(world.getBlockEntity(pos) instanceof FireglobeBlockEntity fireglobeBlockEntity)) {
                return super.onUse(state, world, pos, player, hit);
            }
            if (stack.isOf(Items.FLINT_AND_STEEL)) {
                boolean lit = state.get(LIT);
                if (!lit) {
                    world.setBlockState(pos, state.with(LIT, true));
                    world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    if (!player.isInCreativeMode()) {
                        if (stack.getDamage() + 1 >= stack.getMaxDamage()) {
                            player.sendEquipmentBreakStatus(stack.getItem(), EquipmentSlot.MAINHAND);
                            player.getInventory().removeStack(player.getInventory().getSlotWithStack(stack), 1);
                        } else {
                            stack.damage(1, player);
                        }
                    }
                    player.getInventory().markDirty();
                    return ActionResult.SUCCESS;
                }
            }
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FireglobeBlockEntity fireglobeBlockEntity) {
            if (!world.isClient()) {
                FireglobeGlass glass = itemStack.get(ModDataComponentTypes.FIREGLOBE_GLASS);
                if (glass != null) {
                    fireglobeBlockEntity.setGlass(glass.front(), glass.left(), glass.back(), glass.right());
                    world.updateListeners(pos, state, state, 0);
                }
            }
        }

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = new ItemStack(ModItems.FIREGLOBE);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FireglobeBlockEntity fireglobeBlockEntity) {
            itemStack.set(ModDataComponentTypes.FIREGLOBE_GLASS, fireglobeBlockEntity.getGlass());
            return itemStack;
        }
        return super.getPickStack(world, pos, state, includeData);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FireglobeBlockEntity fireglobeBlockEntity) {
            if (!world.isClient()) {
                ItemStack itemStack = new ItemStack(ModItems.FIREGLOBE);
                itemStack.applyComponentsFrom(blockEntity.createComponentMap());
                itemStack.set(ModDataComponentTypes.FIREGLOBE_GLASS, fireglobeBlockEntity.getGlass());

                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        for (Direction direction : ctx.getPlacementDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                BlockState blockState = this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(HANGING, direction == Direction.UP);
                if (blockState.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) {
                    return blockState.with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
            }
        }
        return null;
    }

    protected static Direction attachedDirection(BlockState state) {
        return state.get(HANGING) ? Direction.DOWN : Direction.UP;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state,
            WorldView world,
            ScheduledTickView tickView,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            Random random
    ) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return attachedDirection(state).getOpposite() == direction && !state.canPlaceAt(world, pos)
                ? Blocks.AIR.getDefaultState()
                : super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = attachedDirection(state).getOpposite();
        return Block.sideCoversSmallSquare(world, pos.offset(direction), direction.getOpposite());
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FireglobeBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(HANGING);
        builder.add(WATERLOGGED);
        builder.add(LIT);
    }
}
