package org.aussiebox.wingcrafter.entity;

import net.minecraft.block.Blocks;
import net.minecraft.block.LightBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MoonGlobeEntity extends Entity implements GeoAnimatable {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    public static final TrackedData<UUID> FOLLOWING = DataTracker.registerData(MoonGlobeEntity.class, ModEntities.UUID);
    public static final TrackedData<List<BlockPos>> LIGHT_POSITIONS = DataTracker.registerData(MoonGlobeEntity.class, ModEntities.BLOCKPOS_LIST);

    public MoonGlobeEntity(EntityType<? extends Entity> entityType, World world) {
        super(entityType, world);
        noClip = true;
    }

    public void setFollowing(PlayerEntity player) {
        if (player == null) dataTracker.set(FOLLOWING, UUID.fromString("00000000-0000-0000-0000-000000000000"));
        else dataTracker.set(FOLLOWING, player.getUuid());
    }

    public PlayerEntity getFollowing() {
        MinecraftServer server = this.getEntityWorld().getServer();
        if (server == null || dataTracker.get(FOLLOWING) == UUID.fromString("00000000-0000-0000-0000-000000000000")) return null;
        return server.getPlayerManager().getPlayer(dataTracker.get(FOLLOWING));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getEntityWorld().isClient()) {
            this.move(MovementType.SELF, this.getVelocity());
            return;
        }

        PlayerEntity player = getFollowing();
        if (player != null) {
            float yawOffsetDegrees = player.getBodyYaw() + 90.0F;
            double yawRadians = Math.toRadians(yawOffsetDegrees);

            double offsetX = -Math.sin(yawRadians) * 2.5;
            double offsetZ = Math.cos(yawRadians) * 2.5;

            Vec3d targetPos = player.getEyePos().add(offsetX, -0.5, offsetZ);
            Vec3d currentPos = this.getEntityPos();

            Vec3d direction = targetPos.subtract(currentPos);
            double distance = direction.length();

            if (distance > 0.05) {
                double speed = Math.min(0.5, distance * 0.35);
                Vec3d velocity = direction.normalize().multiply(speed);
                this.setVelocity(velocity);
            } else {
                this.setVelocity(Vec3d.ZERO);
            }

        } else this.setVelocity(this.getVelocity().multiply(0.91F));
        this.velocityDirty = true;

        this.move(MovementType.SELF, this.getVelocity());

        BlockPos pos = this.getBlockPos();
        List<BlockPos> positions = this.dataTracker.get(LIGHT_POSITIONS);
        if (this.getEntityWorld().getBlockState(pos).isAir()) {
            this.getEntityWorld().setBlockState(pos, Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, 8));
            positions.add(pos);
        }
        List<BlockPos> positionsAgain = new ArrayList<>(positions);
        for (BlockPos blockPos : positionsAgain) {
            if (!pos.equals(blockPos)) {
                if (this.getEntityWorld().getBlockState(blockPos).isOf(Blocks.LIGHT)) this.getEntityWorld().setBlockState(blockPos, Blocks.AIR.getDefaultState());
                positions.remove(blockPos);
            }
        }

        this.dataTracker.set(LIGHT_POSITIONS, positions);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canHit() {
        return !super.isRemoved();
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(FOLLOWING, UUID.fromString("00000000-0000-0000-0000-000000000000"));
        builder.add(LIGHT_POSITIONS, new ArrayList<>());
    }

    @Override
    protected void readCustomData(ReadView tag) {

    }

    @Override
    protected void writeCustomData(WriteView tag) {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(DefaultAnimations.genericIdleController());
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public double getTick(@Nullable Object object) {
        return 0;
    }
}
