package org.aussiebox.wingcrafter.spells;

import lombok.Getter;
import net.minecraft.util.Identifier;

public final class Spell {
    @Getter private final String spellName;
    @Getter private final int soulPenalty;
    @Getter private final Identifier buttonTexture;

    public Spell(String spellName, int soulPenalty, Identifier buttonTexture) {
        this.spellName = spellName;
        this.soulPenalty = soulPenalty;
        this.buttonTexture = buttonTexture;
    }
}
