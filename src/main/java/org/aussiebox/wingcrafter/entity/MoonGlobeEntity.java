package org.aussiebox.wingcrafter.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.util.WingcrafterUtil;
import software.bernie.geckolib.animatable.GeoAnimatable;

import java.util.UUID;

public class MoonGlobeEntity extends Entity implements GeoAnimatable {
    public UUID following = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public MoonGlobeEntity(EntityType<? extends Entity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        MinecraftServer server = this.getEntityWorld().getServer();
        if (server == null) return;

        PlayerEntity player = server.getPlayerManager().getPlayer(following);
        if (player == null) return;

        Vec3d pos = WingcrafterUtil.shiftVecTowardsVec(this.getEntityPos(), player.getEyePos(), 1);
        Vec3d velocity = pos.subtract(this.getEntityPos());
        this.setVelocity(velocity);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        following = player.getUuid();
        return ActionResult.SUCCESS;
    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    protected void readCustomData(ReadView tag) {
        following = UUID.fromString(tag.getString("following", "00000000-0000-0000-0000-000000000000"));
    }

    @Override
    protected void writeCustomData(WriteView tag) {
        tag.putString("following", following.toString());
    }
}
