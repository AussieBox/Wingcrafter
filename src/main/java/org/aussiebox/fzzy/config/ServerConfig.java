package org.aussiebox.fzzy.config;

import me.fzzyhmstrs.fzzy_config.api.SaveType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.jspecify.annotations.NonNull;

public class ServerConfig extends Config {
    public ServerConfig() {
        super(Wingcrafter.id("server"));
    }

    public boolean serverTest = true;

    @Override
    public int defaultPermLevel() {
        return 4;
    }

    @Override
    public @NonNull SaveType saveType() {
        return SaveType.SEPARATE;
    }
}
