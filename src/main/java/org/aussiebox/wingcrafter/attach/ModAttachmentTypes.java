package org.aussiebox.wingcrafter.attach;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

public class ModAttachmentTypes {
    public static final AttachmentType<ModCustomAttachedData> SOUL_ATTACH = AttachmentRegistry.create(
                    Identifier.of(Wingcrafter.MOD_ID,"soul"),
                    builder->builder
                            .initializer(()->ModCustomAttachedData.DEFAULT)
                            .persistent(ModCustomAttachedData.CODEC)
            .syncWith(
                    ModCustomAttachedData.PACKET_CODEC,
                    AttachmentSyncPredicate.all()
            )
    );

    public static void init() {

    }
}