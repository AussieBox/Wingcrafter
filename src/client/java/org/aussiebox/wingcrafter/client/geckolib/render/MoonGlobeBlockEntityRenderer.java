package org.aussiebox.wingcrafter.client.geckolib.render;

import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.block.blockentities.MoonGlobeBlockEntity;
import org.aussiebox.wingcrafter.client.geckolib.render.state.MoonGlobeBlockEntityRenderState;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class MoonGlobeBlockEntityRenderer extends GeoBlockRenderer<MoonGlobeBlockEntity, MoonGlobeBlockEntityRenderState> {
    public MoonGlobeBlockEntityRenderer() {
        super(new DefaultedBlockGeoModel<>(Wingcrafter.id("hanging_moon_globe")));
        this.renderLayers.addLayer(new AutoGlowingGeoLayer<>(this));
    }
}
