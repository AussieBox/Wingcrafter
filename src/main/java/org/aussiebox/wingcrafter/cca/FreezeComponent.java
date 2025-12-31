package org.aussiebox.wingcrafter.cca;

import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;

public class FreezeComponent implements AutoSyncedComponent, ClientTickingComponent {
    public static final ComponentKey<FreezeComponent> KEY =
            ComponentRegistry.getOrCreate(Identifier.of(Wingcrafter.MOD_ID, "freeze"), FreezeComponent.class);

    @Getter
    private int freezeTicks;

    private final PlayerEntity player;

    public FreezeComponent(PlayerEntity player) {
        this.player = player;
    }

    public void setFreezeTicks(int ticks) {
        this.freezeTicks = ticks;
        sync();
    }

    public void reset() {
        this.freezeTicks = 0;
        sync();
    }

    public void sync() {
        KEY.sync(this.player);
    }

    @Override
    public void readData(ReadView tag) {
        this.freezeTicks = tag.contains("freeze_ticks") ? tag.getInt("freeze_ticks", 0) : 0;
    }

    @Override
    public void writeData(WriteView tag) {
        tag.putInt("freeze_ticks", this.freezeTicks);
    }

    @Override
    public void clientTick() {
        if (this.freezeTicks > 0) {
            --this.freezeTicks;
        }
        this.sync();
    }
}
