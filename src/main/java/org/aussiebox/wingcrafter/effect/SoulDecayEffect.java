package org.aussiebox.wingcrafter.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.aussiebox.wingcrafter.cca.SoulComponent;

public class SoulDecayEffect extends StatusEffect {
    int ticksSinceDecay = 0;
    public SoulDecayEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        ticksSinceDecay++;
        if (ticksSinceDecay > 50-(amplifier*2.5)) {
            ticksSinceDecay = 0;
            if (entity instanceof ServerPlayerEntity player) {
                SoulComponent.KEY.get(player).changeSoul(-(amplifier/2));
            }
        }
        return super.applyUpdateEffect(world, entity, amplifier);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        ticksSinceDecay = 0;
        super.onApplied(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int pDuration, int pAmplifier) {
        return true;
    }
}
