package org.aussiebox.wingcrafter;

import net.minecraft.entity.player.PlayerEntity;
import org.aussiebox.wingcrafter.cca.FreezeComponent;
import org.aussiebox.wingcrafter.cca.SoulComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class WingcrafterComponents implements EntityComponentInitializer {

    public WingcrafterComponents() {}

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, FreezeComponent.KEY)
                .respawnStrategy(RespawnCopyStrategy.NEVER_COPY)
                .end(FreezeComponent::new);
        registry.beginRegistration(PlayerEntity.class, SoulComponent.KEY)
                .respawnStrategy(RespawnCopyStrategy.NEVER_COPY)
                .end(SoulComponent::new);
    }
}
