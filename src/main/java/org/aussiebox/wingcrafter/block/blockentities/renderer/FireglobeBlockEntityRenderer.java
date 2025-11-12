package org.aussiebox.wingcrafter.block.blockentities.renderer;

import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.block.blockentities.FireglobeBlockEntity;
import org.aussiebox.wingcrafter.client.WingcrafterClient;
import org.aussiebox.wingcrafter.component.FireglobeGlass;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class FireglobeBlockEntityRenderer implements BlockEntityRenderer<FireglobeBlockEntity, FireglobeBlockEntityRenderState> {
    public static final EntityModelLayer FIREGLOBE_SIDES = new EntityModelLayer(Identifier.of(Wingcrafter.MOD_ID, "globe"), "main");

    public static final SpriteIdentifier CLEAR = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "template"));
    public static final SpriteIdentifier WHITE = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "white"));
    public static final SpriteIdentifier LIGHT_GRAY = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "light_gray"));
    public static final SpriteIdentifier GRAY = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "gray"));
    public static final SpriteIdentifier BLACK = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "black"));
    public static final SpriteIdentifier BROWN = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "brown"));
    public static final SpriteIdentifier RED = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "red"));
    public static final SpriteIdentifier ORANGE = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "orange"));
    public static final SpriteIdentifier YELLOW = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "yellow"));
    public static final SpriteIdentifier LIME = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "lime"));
    public static final SpriteIdentifier GREEN = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "green"));
    public static final SpriteIdentifier CYAN = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "cyan"));
    public static final SpriteIdentifier LIGHT_BLUE = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "light_blue"));
    public static final SpriteIdentifier BLUE = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "blue"));
    public static final SpriteIdentifier PURPLE = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "purple"));
    public static final SpriteIdentifier MAGENTA = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "magenta"));
    public static final SpriteIdentifier PINK = WingcrafterClient.FIREGLOBE_GLASS.map(Identifier.of(Wingcrafter.MOD_ID, "pink"));

    public static Map<String, SpriteIdentifier> spriteIDs = new HashMap<>();
    public static Map<String, SpriteIdentifier> getSpriteIDs() {
        spriteIDs.putIfAbsent("clear", CLEAR);
        spriteIDs.putIfAbsent("white", WHITE);
        spriteIDs.putIfAbsent("light_gray", LIGHT_GRAY);
        spriteIDs.putIfAbsent("gray", GRAY);
        spriteIDs.putIfAbsent("black", BLACK);
        spriteIDs.putIfAbsent("brown", BROWN);
        spriteIDs.putIfAbsent("red", RED);
        spriteIDs.putIfAbsent("orange", ORANGE);
        spriteIDs.putIfAbsent("yellow", YELLOW);
        spriteIDs.putIfAbsent("lime", LIME);
        spriteIDs.putIfAbsent("green", GREEN);
        spriteIDs.putIfAbsent("cyan", CYAN);
        spriteIDs.putIfAbsent("light_blue", LIGHT_BLUE);
        spriteIDs.putIfAbsent("blue", BLUE);
        spriteIDs.putIfAbsent("purple", PURPLE);
        spriteIDs.putIfAbsent("magenta", MAGENTA);
        spriteIDs.putIfAbsent("pink", PINK);
        return spriteIDs;
    }

    public static Map<Item, String> itemSpriteIDs = new HashMap<>();
    public static Map<Item, String> getItemSpriteIDs() {
        itemSpriteIDs.putIfAbsent(Items.GLASS_PANE, "clear");
        itemSpriteIDs.putIfAbsent(Items.WHITE_STAINED_GLASS_PANE, "white");
        itemSpriteIDs.putIfAbsent(Items.LIGHT_GRAY_STAINED_GLASS_PANE, "light_gray");
        itemSpriteIDs.putIfAbsent(Items.GRAY_STAINED_GLASS_PANE, "gray");
        itemSpriteIDs.putIfAbsent(Items.BLACK_STAINED_GLASS_PANE, "black");
        itemSpriteIDs.putIfAbsent(Items.BROWN_STAINED_GLASS_PANE, "brown");
        itemSpriteIDs.putIfAbsent(Items.RED_STAINED_GLASS_PANE, "red");
        itemSpriteIDs.putIfAbsent(Items.ORANGE_STAINED_GLASS_PANE, "orange");
        itemSpriteIDs.putIfAbsent(Items.YELLOW_STAINED_GLASS_PANE, "yellow");
        itemSpriteIDs.putIfAbsent(Items.LIME_STAINED_GLASS_PANE, "lime");
        itemSpriteIDs.putIfAbsent(Items.GREEN_STAINED_GLASS_PANE, "green");
        itemSpriteIDs.putIfAbsent(Items.CYAN_STAINED_GLASS_PANE, "cyan");
        itemSpriteIDs.putIfAbsent(Items.LIGHT_BLUE_STAINED_GLASS_PANE, "light_blue");
        itemSpriteIDs.putIfAbsent(Items.BLUE_STAINED_GLASS_PANE, "blue");
        itemSpriteIDs.putIfAbsent(Items.PURPLE_STAINED_GLASS_PANE, "purple");
        itemSpriteIDs.putIfAbsent(Items.MAGENTA_STAINED_GLASS_PANE, "magenta");
        itemSpriteIDs.putIfAbsent(Items.PINK_STAINED_GLASS_PANE, "pink");
        return itemSpriteIDs;
    }

    private final ModelPart front;
    private final ModelPart back;
    private final ModelPart left;
    private final ModelPart right;
    private final SpriteHolder materials;
    private SpriteIdentifier frontTexture;
    private SpriteIdentifier backTexture;
    private SpriteIdentifier leftTexture;
    private SpriteIdentifier rightTexture;

    public FireglobeBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        ModelPart modelPart = context.getLayerModelPart(FIREGLOBE_SIDES);
        this.front = modelPart.getChild("front");
        this.back = modelPart.getChild("back");
        this.left = modelPart.getChild("left");
        this.right = modelPart.getChild("right");
        this.materials = context.spriteHolder();
        this.frontTexture = CLEAR;
        this.backTexture = CLEAR;
        this.leftTexture = CLEAR;
        this.rightTexture = CLEAR;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0, 0).cuboid(4.0F, 2.0F, 4.0F, 8.0F, 8.0F, 0.0F, EnumSet.of(Direction.NORTH));
        modelPartData.addChild("back", modelPartBuilder, ModelTransform.of(16.0F, 12.0F, 0.0F, 0.0F, 0.0F, (float) Math.PI));
        modelPartData.addChild("left", modelPartBuilder, ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, (float) (-Math.PI / 2), (float) Math.PI));
        modelPartData.addChild("right", modelPartBuilder, ModelTransform.of(16.0F, 12.0F, 16.0F, 0.0F, (float) (Math.PI / 2), (float) Math.PI));
        modelPartData.addChild("front", modelPartBuilder, ModelTransform.of(0.0F, 12.0F, 16.0F, (float) Math.PI, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 8, 8);
    }

    @Override
    public FireglobeBlockEntityRenderState createRenderState() {
        return new FireglobeBlockEntityRenderState();
    }

    @Override
    public void updateRenderState(FireglobeBlockEntity blockEntity, FireglobeBlockEntityRenderState state, float tickProgress, Vec3d cameraPos, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
        getSpriteIDs();
        FireglobeGlass glass = blockEntity.getGlass();
        this.frontTexture = spriteIDs.get(glass.front());
        this.leftTexture = spriteIDs.get(glass.left());
        this.backTexture = spriteIDs.get(glass.back());
        this.rightTexture = spriteIDs.get(glass.right());
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
    }

    @Override
    public void render(FireglobeBlockEntityRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        matrices.push();
        if (frontTexture != null) {
            queue.submitModelPart(
                    this.front,
                    matrices,
                    RenderLayer.getEntityTranslucent(WingcrafterClient.FIREGLOBE_GLASS_ATLAS_PATH),
                    state.lightmapCoordinates,
                    OverlayTexture.DEFAULT_UV,
                    this.materials.getSprite(frontTexture)
            );
        }
        if (leftTexture != null) {
            queue.submitModelPart(
                    this.left,
                    matrices,
                    RenderLayer.getEntityTranslucent(WingcrafterClient.FIREGLOBE_GLASS_ATLAS_PATH),
                    state.lightmapCoordinates,
                    OverlayTexture.DEFAULT_UV,
                    this.materials.getSprite(leftTexture)
            );
        }
        if (backTexture != null) {
            queue.submitModelPart(
                    this.back,
                    matrices,
                    RenderLayer.getEntityTranslucent(WingcrafterClient.FIREGLOBE_GLASS_ATLAS_PATH),
                    state.lightmapCoordinates,
                    OverlayTexture.DEFAULT_UV,
                    this.materials.getSprite(backTexture)
            );
        }
        if (rightTexture != null) {
            queue.submitModelPart(
                    this.right,
                    matrices,
                    RenderLayer.getEntityTranslucent(WingcrafterClient.FIREGLOBE_GLASS_ATLAS_PATH),
                    state.lightmapCoordinates,
                    OverlayTexture.DEFAULT_UV,
                    this.materials.getSprite(rightTexture)
            );
        }
        matrices.pop();
    }
}