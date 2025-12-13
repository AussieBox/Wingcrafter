package org.aussiebox.wingcrafter.attach;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

public class ModAttachmentTypes {
    public static final AttachmentType<SoulAttachedData> SOUL_ATTACH = AttachmentRegistry.create(
                    Identifier.of(Wingcrafter.MOD_ID,"soul"),
                    builder->builder
                            .initializer(()-> SoulAttachedData.DEFAULT)
                            .persistent(SoulAttachedData.CODEC)
            .syncWith(
                    SoulAttachedData.PACKET_CODEC,
                    AttachmentSyncPredicate.all()
            )
    );

    public static final AttachmentType<DragonflameCactusFuseAttachedData> DRAGONFLAME_CACTUS_FUSE_ATTACH = AttachmentRegistry.create(
            Identifier.of(Wingcrafter.MOD_ID,"dragonflame_cactus_fuse"),
            builder->builder
                    .initializer(()-> DragonflameCactusFuseAttachedData.DEFAULT)
                    .persistent(DragonflameCactusFuseAttachedData.CODEC)
                    .syncWith(
                            DragonflameCactusFuseAttachedData.PACKET_CODEC,
                            AttachmentSyncPredicate.all()
                    )
    );

    public static void init() {

    }
}