package org.aussiebox.wingcrafter.client.geckolib.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.client.geckolib.render.state.MoonGlobeEntityRenderState;
import org.aussiebox.wingcrafter.entity.MoonGlobeEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class MoonGlobeEntityRenderer extends GeoEntityRenderer<MoonGlobeEntity, MoonGlobeEntityRenderState> {
    public MoonGlobeEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DefaultedEntityGeoModel<>(Wingcrafter.id("moon_globe")));
        this.renderLayers.addLayer(new AutoGlowingGeoLayer<>(this));
    }
}
