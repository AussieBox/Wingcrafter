package org.aussiebox.fzzy.config;

import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.api.SaveType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.jspecify.annotations.NonNull;

public class ClientConfig extends Config {
    public ClientConfig() {
        super(Wingcrafter.id("client"));
    }

    public RenderingSection rendering = new RenderingSection();
    public static class RenderingSection extends ConfigSection {
        public ConfigGroup soul = new ConfigGroup("soul");

        public ConfigGroup changes = new ConfigGroup("soul_changes");
        @Comment("Displays a counter above the Soul Meter when your soul is updated.")
        public boolean displaySoulChanges = true;
        @ConfigGroup.Pop
        @Comment("The number of pixels to shift the Soul Meter counter by vertically.")
        public int soulChangeYModifier = 0;

        public ConfigGroup dialogue = new ConfigGroup("soul_dialogue");
        @Comment("Displays randomised dialogue lines when your soul drops below certain thresholds.")
        public boolean displaySoulDialogue = true;
        @ConfigGroup.Pop
        @ConfigGroup.Pop
        @Comment("The number of pixels to shift soul-related dialogue lines by vertically.")
        public int soulDialogueYModifier = 0;

        @Comment("Controls whether displaying scroll information requires sneaking to be rendered.")
        public boolean displayScrollInfoRequiresSneak = true;
    }

    @Override
    public @NonNull SaveType saveType() {
        return SaveType.SEPARATE;
    }
}
