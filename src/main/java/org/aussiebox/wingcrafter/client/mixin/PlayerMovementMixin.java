package org.aussiebox.wingcrafter.client.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.cca.FreezeComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerMovementMixin extends LivingEntity {
    protected PlayerMovementMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "canMoveVoluntarily",
            at = @At("HEAD"),
            cancellable = true
    )
    private void canMove(CallbackInfoReturnable<Boolean> cir) {
        if (FreezeComponent.KEY.get(this).getFreezeTicks() > 0) {
            cir.setReturnValue(false);
        }
    }
}