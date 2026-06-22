package org.aussiebox.wingcrafter.entity;

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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class MoonGlobeEntity extends Entity implements GeoAnimatable {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    public static final TrackedData<UUID> FOLLOWING = DataTracker.registerData(MoonGlobeEntity.class, ModEntities.UUID);

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
                double speed = Math.min(0.3, distance * 0.2);
                Vec3d velocity = direction.normalize().multiply(speed);
                this.setVelocity(velocity);
            } else {
                this.setVelocity(Vec3d.ZERO);
            }

        } else this.setVelocity(this.getVelocity().multiply(0.91F));
        this.velocityDirty = true;

        this.move(MovementType.SELF, this.getVelocity());
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
