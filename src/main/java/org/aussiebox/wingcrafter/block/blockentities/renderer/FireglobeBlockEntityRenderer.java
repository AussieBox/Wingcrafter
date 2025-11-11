package org.aussiebox.wingcrafter.block.blockentities.renderer;

import net.minecraft.client.model.*;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.block.blockentities.FireglobeBlockEntity;

import java.util.EnumSet;

public class FireglobeBlockEntityRenderer implements BlockEntityRenderer<FireglobeBlockEntity, FireglobeBlockEntityRenderState> {
    private final ModelPart front;
    private final ModelPart back;
    private final ModelPart left;
    private final ModelPart right;
    private final SpriteHolder materials;

    public FireglobeBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.front = getTexturedModelData().createModel().getChild("front");
        this.back = getTexturedModelData().createModel().getChild("back");
        this.left = getTexturedModelData().createModel().getChild("left");
        this.right = getTexturedModelData().createModel().getChild("right");
        this.materials = context.spriteHolder();
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0, 0).cuboid(4.0F, 2.0F, 4.0F, 8.0F, 8.0F, 8.0F, EnumSet.of(Direction.NORTH));
        modelPartData.addChild("back", modelPartBuilder, ModelTransform.of(8.0F, 12.0F, 1.0F, 0.0F, 0.0F, (float) Math.PI));
        modelPartData.addChild("left", modelPartBuilder, ModelTransform.of(0.0F, 12.0F, 1.0F, 0.0F, (float) (-Math.PI / 2), (float) Math.PI));
        modelPartData.addChild("right", modelPartBuilder, ModelTransform.of(8.0F, 12.0F, 15.0F, 0.0F, (float) (Math.PI / 2), (float) Math.PI));
        modelPartData.addChild("front", modelPartBuilder, ModelTransform.of(0.0F, 12.0F, 16.0F, (float) Math.PI, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 16, 16);
    }

    @Override
    public FireglobeBlockEntityRenderState createRenderState() {
        return new FireglobeBlockEntityRenderState();
    }

    @Override
    public void render(FireglobeBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();
        queue.submitModelPart(
                this.front,
                matrices,
                RenderLayers.getEntityBlockLayer(state.blockState),
                state.lightmapCoordinates,
                OverlayTexture.DEFAULT_UV,
                this.materials.getSprite(new SpriteIdentifier(Identifier.of(Wingcrafter.MOD_ID, "textures/atlas/fireglobe_glass.png"), Identifier.of(Wingcrafter.MOD_ID, "fireglobe_glass/blue")))
        );
        matrices.pop();
    }

    private int getLightLevel(World world, BlockPos pos) {
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }
}