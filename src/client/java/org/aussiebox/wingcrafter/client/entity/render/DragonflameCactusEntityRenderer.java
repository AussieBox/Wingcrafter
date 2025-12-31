package org.aussiebox.wingcrafter.client.entity.render;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.client.entity.model.DragonflameCactusEntityModel;
import org.aussiebox.wingcrafter.entity.DragonflameCactusEntity;

public class DragonflameCactusEntityRenderer extends EntityRenderer<DragonflameCactusEntity, EntityRenderState> {
    protected DragonflameCactusEntityModel model;

    public DragonflameCactusEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new DragonflameCactusEntityModel(context.getPart(DragonflameCactusEntityModel.CACTUS));
    }

    @Override
    public void render(EntityRenderState renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(3.5F));
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(25.25F));
        matrices.translate(0.3, 0.34, 0.525);
        matrices.scale(0.65F, 0.65F, 0.65F);
        queue.submitModel(
                this.model,
                renderState,
                matrices,
                RenderLayer.getEntityTranslucent(Identifier.of(Wingcrafter.MOD_ID, "textures/entity/dragonflame_cactus_entity.png"), false),
                renderState.light,
                OverlayTexture.DEFAULT_UV,
                0x00000000,
                null
        );
        matrices.pop();
        super.render(renderState, matrices, queue, cameraState);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
