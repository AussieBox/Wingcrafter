package org.aussiebox.fzzy;

import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import org.aussiebox.fzzy.config.ClientConfig;
import org.aussiebox.fzzy.config.ServerConfig;

public class ModConfig {
    public static ClientConfig clientConfig = ConfigApiJava.registerAndLoadConfig(ClientConfig::new, RegisterType.CLIENT);
    public static ServerConfig serverConfig = ConfigApiJava.registerAndLoadConfig(ServerConfig::new, RegisterType.BOTH);

    public static void init() {}
}
