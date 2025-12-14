package org.aussiebox.wingcrafter.entity;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.attach.DragonflameCactusFuseAttachedData;
import org.aussiebox.wingcrafter.attach.ModAttachmentTypes;
import org.aussiebox.wingcrafter.item.ModItems;
import org.aussiebox.wingcrafter.util.WingcrafterUtil;

public class DragonflameCactusEntity extends PersistentProjectileEntity {

    public DragonflameCactusEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public DragonflameCactusEntity(World world, PlayerEntity player) {
        super(ModEntities.DragonflameCactusEntityType, player, world, ModItems.DRAGONFLAME_CACTUS.getDefaultStack(), null);
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        World world = entity.getEntityWorld();
        if (!world.isClient()) {
            if (entity instanceof LivingEntity livingEntity) {
                Vec3d pos = entityHitResult.getPos();
                world.createExplosion(this, pos.x, pos.y, pos.z, 2, true, World.ExplosionSourceType.NONE);
                if (this.getOwner() instanceof ServerPlayerEntity player) {
                    if (player.distanceTo(entity) >= 25) {
                        WingcrafterUtil.grantAdvancement(player, "hit_dragonflame_cactus");
                    }
                }
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        if (!this.getEntityWorld().isClient()) {
            this.setOnGround(true);
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
                if (data.fuse() == 10) {
                    if (this.getEntityWorld().getServer() != null) {
                        for (ServerPlayerEntity player : PlayerLookup.all(this.getEntityWorld().getServer())) {
                            if (player.distanceTo(this) <= 5) {
                                WingcrafterUtil.grantAdvancement(player, "witness_dragonflame_cactus");
                            }
                        }
                    }
                }
            }
            if (this.isOnFire()) {
                world.createExplosion(this, this.lastX, this.lastY, this.lastZ, 2, true, World.ExplosionSourceType.NONE);
                this.kill((ServerWorld) world);
            }
        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return ModItems.DRAGONFLAME_CACTUS.getDefaultStack();
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ENCHANT_THORNS_HIT;
    }
}
