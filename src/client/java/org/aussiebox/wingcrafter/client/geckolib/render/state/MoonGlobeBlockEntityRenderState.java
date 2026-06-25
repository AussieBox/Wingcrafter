package org.aussiebox.wingcrafter.client.geckolib.render.state;

import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import org.jspecify.annotations.Nullable;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.Map;

public class MoonGlobeBlockEntityRenderState extends BlockEntityRenderState implements GeoRenderState {
    public MoonGlobeBlockEntityRenderState() {
        
    }

    @Override
    public <D> void addGeckolibData(DataTicket<D> dataTicket, @Nullable D data) {

    }

    @Override
    public boolean hasGeckolibData(DataTicket<?> dataTicket) {
        return false;
    }

    @Override
    public @Nullable <D> D getGeckolibData(DataTicket<D> dataTicket) {
        return null;
    }

    @Override
    public Map<DataTicket<?>, Object> getDataMap() {
        return Map.of();
    }
}
