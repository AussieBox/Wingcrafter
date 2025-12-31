package org.aussiebox.wingcrafter.mixin;

import net.minecraft.block.Oxidizable;
import net.minecraft.entity.passive.CopperGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.cca.SoulComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CopperGolemEntity.class)
public class CopperGolemMixin {

    @Inject(
            method = "interactMob",
            at = @At(value = "HEAD")
    )
    private void giveSoulOnScrape(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        CopperGolemEntity entity = (CopperGolemEntity) (Object) this;
        World world = entity.getEntityWorld();

        if (!world.isClient()) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                if (serverPlayer.getStackInHand(hand).isIn(ItemTags.AXES)) {
                    Oxidizable.OxidationLevel oxidationLevel = entity.getOxidationLevel();
                    if (oxidationLevel != Oxidizable.OxidationLevel.UNAFFECTED) {
                        SoulComponent.KEY.get(serverPlayer).changeSoul(10);
                        Wingcrafter.LOGGER.info(entity.getUuidAsString());
                    }
                }
            }
        }
    }

}
