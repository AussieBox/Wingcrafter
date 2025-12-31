package org.aussiebox.wingcrafter.client.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

public class DragonflameCactusEntityModel extends EntityModel<EntityRenderState> {
	public static final EntityModelLayer CACTUS = new EntityModelLayer(Identifier.of(Wingcrafter.MOD_ID, "dragonflame_cactus_entity"), "main");
	private final ModelPart bone;
	public DragonflameCactusEntityModel(ModelPart root) {
		super(root);
		this.bone = root.getChild("bone");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 0).cuboid(-12.0F, -12.0F, 4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 16).cuboid(-9.0F, -12.0F, 2.0F, 0.0F, 8.0F, 12.0F, new Dilation(0.0F))
		.uv(32, 0).cuboid(-14.0F, -12.0F, 5.0F, 12.0F, 8.0F, 0.0F, new Dilation(0.0F))
		.uv(48, 32).cuboid(-12.0F, -14.0F, 9.0F, 8.0F, 12.0F, 0.0F, new Dilation(0.0F))
		.uv(48, 44).cuboid(-12.0F, -14.0F, 7.0F, 8.0F, 12.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 56).cuboid(-12.0F, -14.0F, 11.0F, 8.0F, 12.0F, 0.0F, new Dilation(0.0F))
		.uv(56, 0).cuboid(-12.0F, -14.0F, 5.0F, 8.0F, 12.0F, 0.0F, new Dilation(0.0F))
		.uv(32, 8).cuboid(-14.0F, -12.0F, 11.0F, 12.0F, 8.0F, 0.0F, new Dilation(0.0F))
		.uv(48, 16).cuboid(-14.0F, -12.0F, 7.0F, 12.0F, 8.0F, 0.0F, new Dilation(0.0F))
		.uv(48, 24).cuboid(-14.0F, -12.0F, 9.0F, 12.0F, 8.0F, 0.0F, new Dilation(0.0F))
		.uv(24, 16).cuboid(-7.0F, -12.0F, 2.0F, 0.0F, 8.0F, 12.0F, new Dilation(0.0F))
		.uv(0, 36).cuboid(-11.0F, -12.0F, 2.0F, 0.0F, 8.0F, 12.0F, new Dilation(0.0F))
		.uv(24, 36).cuboid(-5.0F, -12.0F, 2.0F, 0.0F, 8.0F, 12.0F, new Dilation(0.0F)), ModelTransform.rotation(8.0F, 24.0F, -8.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void setAngles(EntityRenderState state) {
		super.setAngles(state);
	}

	/* @Override
	public void setAngles(DragonflameCactusEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	} */
}