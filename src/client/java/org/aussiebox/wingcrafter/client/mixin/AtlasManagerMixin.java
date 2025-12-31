package org.aussiebox.wingcrafter.client.mixin;

import net.minecraft.client.texture.AtlasManager;
import org.aussiebox.wingcrafter.client.WingcrafterClient;
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
        ATLAS_METADATA.add(new AtlasManager.Metadata(WingcrafterClient.FIREGLOBE_GLASS_ATLAS_PATH, WingcrafterClient.FIREGLOBE_GLASS_ATLAS_DEFINITION, true));
        ATLAS_METADATA = List.copyOf(ATLAS_METADATA);
    }
}
