package org.aussiebox.wingcrafter.mixin;

import net.minecraft.client.texture.AtlasManager;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(AtlasManager.class)
public class AtlasManagerMixin {
    @Shadow @Final private static List<AtlasManager.Metadata> ATLAS_METADATA;

    static {
        ATLAS_METADATA = new ArrayList<>(ATLAS_METADATA);
        ATLAS_METADATA.add(new AtlasManager.Metadata(Identifier.of(Wingcrafter.MOD_ID, "textures/atlas/fireglobe_glass.png"), Identifier.of(Wingcrafter.MOD_ID, "fireglobe_glass"), false));
        ATLAS_METADATA = List.copyOf(ATLAS_METADATA);
    }
}
