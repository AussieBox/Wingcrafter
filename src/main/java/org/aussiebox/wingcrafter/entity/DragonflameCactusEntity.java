package org.aussiebox.wingcrafter.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.attach.DragonflameCactusFuseAttachedData;
import org.aussiebox.wingcrafter.attach.ModAttachmentTypes;
import org.aussiebox.wingcrafter.item.ModItems;

public class DragonflameCactusEntity extends ThrownItemEntity {
    double x;
    double y;
    double z;

    public DragonflameCactusEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public DragonflameCactusEntity(double x, double y, double z, World world, ItemStack stack) {
        super(ModEntities.DragonflameCactusEntityType, x, y, z, world, stack);
    }

    public DragonflameCactusEntity(LivingEntity owner, World world, ItemStack stack) {
        super(ModEntities.DragonflameCactusEntityType, owner, world, stack);
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        World world = entity.getEntityWorld();
        if (entity instanceof LivingEntity livingEntity) {
            Vec3d pos = entityHitResult.getPos();
            world.createExplosion(this, pos.x, pos.y, pos.z, 2, true, World.ExplosionSourceType.NONE);
        }
        this.kill((ServerWorld) world);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.getEntityWorld().isClient()) {
            this.setOnGround(true);
            this.x = this.lastX;
            this.y = this.lastY;
            this.z = this.lastZ;
        }
    }

    @Override
    public void tick() {
        super.tick();
        DragonflameCactusFuseAttachedData data = this.getAttached(ModAttachmentTypes.DRAGONFLAME_CACTUS_FUSE_ATTACH);
        World world = this.getEntityWorld();
        if (!world.isClient()) {
            if (data != null) {
                this.setAttached(ModAttachmentTypes.DRAGONFLAME_CACTUS_FUSE_ATTACH, new DragonflameCactusFuseAttachedData(data.fuse()-1));
                if (data.fuse() <= 0) {
                    world.createExplosion(this, this.lastX, this.lastY, this.lastZ, 2, true, World.ExplosionSourceType.NONE);
                    this.kill((ServerWorld) world);
                }
            }
            if (this.isOnGround()) {
                this.requestTeleport(this.x, this.y, this.z);
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.DRAGONFLAME_CACTUS;
    }
}
