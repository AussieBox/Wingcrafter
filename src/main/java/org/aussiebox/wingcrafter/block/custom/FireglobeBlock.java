package org.aussiebox.wingcrafter.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.aussiebox.wingcrafter.block.blockentities.FireglobeBlockEntity;
import org.aussiebox.wingcrafter.component.FireglobeGlass;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;
import org.jetbrains.annotations.Nullable;

public class FireglobeBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    public static final MapCodec<FireglobeBlock> CODEC = createCodec(FireglobeBlock::new);
    private static final VoxelShape SHAPE = Block.createCuboidShape(4, 2, 4, 12, 10, 12);

    public static final BooleanProperty LIT = BooleanProperty.of("lit");

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public FireglobeBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(LIT, false));
    }

    public static int getLuminance(BlockState state) {
        boolean lit = state.get(LIT);
        return lit ? 15 : 0;
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.getAbilities().allowModifyWorld) {
            return ActionResult.PASS;
        } else {
            if (!(world.getBlockEntity(pos) instanceof FireglobeBlockEntity fireglobeBlockEntity)) {
                return super.onUse(state, world, pos, player, hit);
            }
            boolean lit = state.get(LIT);
            if (lit) {
                world.setBlockState(pos, state.with(LIT, false));
                world.playSound(null, pos, SoundEvents.BLOCK_CANDLE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
                }
            }
        }

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof FireglobeBlockEntity fireglobeBlockEntity) {
            if (!world.isClient()) {
                ItemStack itemStack = new ItemStack(this);
                itemStack.applyComponentsFrom(blockEntity.createComponentMap());
                itemStack.set(ModDataComponentTypes.FIREGLOBE_GLASS, fireglobeBlockEntity.getGlass());

                ItemEntity itemEntity = new ItemEntity((World) world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            }
        }

        super.onBroken(world, pos, state);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FireglobeBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(LIT);
    }
}
