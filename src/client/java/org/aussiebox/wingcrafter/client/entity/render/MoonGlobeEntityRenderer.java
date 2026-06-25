package org.aussiebox.wingcrafter.client.entity.render;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.client.entity.model.MoonGlobeEntityModel;
import org.aussiebox.wingcrafter.client.entity.render.state.MoonGlobeEntityRenderState;
import org.aussiebox.wingcrafter.entity.MoonGlobeEntity;

public class MoonGlobeEntityRenderer extends EntityRenderer<MoonGlobeEntity, MoonGlobeEntityRenderState> {
    protected MoonGlobeEntityModel model;

    public MoonGlobeEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new MoonGlobeEntityModel(context.getPart(MoonGlobeEntityModel.GLOBE));
    }

    @Override
    public void render(MoonGlobeEntityRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();
        matrices.translate(0, -0.7425, 0);
        queue.submitModel(
                this.model,
                renderState,
                matrices,
                RenderLayer.getEntityTranslucent(Wingcrafter.id("textures/entity/hanging_moon_globe.png"), false),
                renderState.light,
                OverlayTexture.DEFAULT_UV,
                0x00000000,
                null
        );
        matrices.pop();
        super.render(renderState, matrices, queue, cameraState);
    }

    @Override
    public MoonGlobeEntityRenderState createRenderState() {
        return new MoonGlobeEntityRenderState();
    }

    @Override
    public void updateRenderState(MoonGlobeEntity entity, MoonGlobeEntityRenderState renderState, float f) {
        super.updateRenderState(entity, renderState, f);
        renderState.idleAnimationState.startIfNotRunning(0);
    }
}
