package org.aussiebox.wingcrafter.client.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.aussiebox.wingcrafter.Wingcrafter;

public class ClientConfig {
    public static ConfigClassHandler<ClientConfig> HANDLER = ConfigClassHandler.createBuilder(ClientConfig.class)
            .id(Identifier.of(Wingcrafter.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("wingcrafter/client.json5"))
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry(comment = "Displays a counter above the Soul Meter when your soul is updated.")
    public static boolean displaySoulChanges = true;

    @SerialEntry
    public static int soulChangeYModifier = 0;

    @SerialEntry(comment = "Displays randomised dialogue lines when your soul drops below certain thresholds.")
    public static boolean displaySoulDialogue = true;

    @SerialEntry
    public static int soulDialogueYModifier = 0;

    @SerialEntry(comment = "Controls whether displaying scroll information requires sneaking to be rendered.")
    public static boolean displayScrollInfoRequiresSneak = true;

    public static YetAnotherConfigLib getLibConfig() {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("Wingcrafter Client Configuration."))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("wingcrafter.config.client.tab.rendering"))
                        .tooltip(Text.translatable("wingcrafter.config.client.tab.rendering.tooltip"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("wingcrafter.config.client.rendering.group.soul_change"))
                                .description(OptionDescription.of(Text.translatable("wingcrafter.config.client.rendering.group.soul_change.tooltip")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("wingcrafter.config.client.rendering.soul_change.render"))
                                        .description(OptionDescription.of(Text.translatable("wingcrafter.config.client.rendering.soul_change.render.tooltip")))
                                        .binding(true, () -> displaySoulChanges, newVal -> displaySoulChanges = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("wingcrafter.config.client.rendering.soul_change.y_modifier"))
                                        .description(OptionDescription.of(Text.translatable("wingcrafter.config.client.rendering.soul_change.y_modifier.tooltip")))
                                        .binding(0, () -> soulChangeYModifier, newVal -> soulChangeYModifier = newVal)
                                        .controller(integerOption -> IntegerFieldControllerBuilder.create(integerOption).range(-1000, 1000))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("wingcrafter.config.client.rendering.group.soul_dialogue"))
                                .description(OptionDescription.of(Text.translatable("wingcrafter.config.client.rendering.group.soul_dialogue.tooltip")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("wingcrafter.config.client.rendering.soul_dialogue.render"))
                                        .description(OptionDescription.of(Text.translatable("wingcrafter.config.client.rendering.soul_dialogue.render.tooltip")))
                                        .binding(true, () -> displaySoulDialogue, newVal -> displaySoulDialogue = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("wingcrafter.config.client.rendering.soul_dialogue.y_modifier"))
                                        .description(OptionDescription.of(Text.translatable("wingcrafter.config.client.rendering.soul_dialogue.y_modifier.tooltip")))
                                        .binding(0, () -> soulDialogueYModifier, newVal -> soulDialogueYModifier = newVal)
                                        .controller(integerOption -> IntegerFieldControllerBuilder.create(integerOption).range(-1000, 1000))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("wingcrafter.config.client.rendering.group.scrolls"))
                                .description(OptionDescription.of(Text.translatable("wingcrafter.config.client.rendering.group.scrolls.tooltip")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("wingcrafter.config.client.rendering.scrolls.require_sneak"))
                                        .description(OptionDescription.of(Text.translatable("wingcrafter.config.client.rendering.scrolls.require_sneak.tooltip")))
                                        .binding(true, () -> displayScrollInfoRequiresSneak, newVal -> displayScrollInfoRequiresSneak = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .build();
    }

}
