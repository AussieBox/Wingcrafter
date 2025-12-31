package org.aussiebox.wingcrafter.cca;

import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class SoulComponent implements AutoSyncedComponent {
    public static final ComponentKey<SoulComponent> KEY =
            ComponentRegistry.getOrCreate(Identifier.of(Wingcrafter.MOD_ID, "soul"), SoulComponent.class);

    @Getter
    private int soul = 1000;

    private final PlayerEntity player;

    public SoulComponent(PlayerEntity player) {
        this.player = player;
    }

    public void setSoul(int soul) {
        this.soul = Math.clamp(soul, 0, 1000);
        sync();
    }

    public void changeSoul(int change) {
        this.soul = Math.clamp(this.soul + change, 0, 1000);
        sync();
    }

    public void sync() {
        KEY.sync(this.player);
    }

    @Override
    public void readData(@NotNull ReadView tag) {
        this.soul = tag.contains("soul") ? tag.getInt("soul", 1000) : 1000;
    }

    @Override
    public void writeData(@NotNull WriteView tag) {
        tag.putInt("soul", this.soul);
    }
}
