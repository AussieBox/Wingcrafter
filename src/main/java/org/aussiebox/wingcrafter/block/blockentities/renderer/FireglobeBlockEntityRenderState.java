package org.aussiebox.wingcrafter.block.blockentities.renderer;

import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.math.Direction;
import org.aussiebox.wingcrafter.component.FireglobeGlass;

public class FireglobeBlockEntityRenderState extends BlockEntityRenderState {
    public SpriteIdentifier frontTexture;
    public SpriteIdentifier backTexture;
    public SpriteIdentifier leftTexture;
    public SpriteIdentifier rightTexture;
    public FireglobeGlass glass;
    public Direction facing;
}
