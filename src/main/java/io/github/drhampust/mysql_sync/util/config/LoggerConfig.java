package io.github.drhampust.mysql_sync.util.config;

import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.data.Config;
import me.lortseam.completeconfig.data.ConfigOptions;

import java.util.List;

import static org.spongepowered.include.com.google.common.collect.ImmutableList.of;

public class LoggerConfig extends Config {

    @ConfigEntry(requiresRestart = true, comment = "\nEnables (true)/Disables (false) warning messages in console (Default: true)")
    public boolean warning = true;

    @ConfigEntry(requiresRestart = true, comment = "\nEnables (true)/Disables (false) information messages in console (Default: true)")
    public boolean info = true;

    @ConfigEntry(requiresRestart = true, comment = "\nEnables (true)/Disables (false) debugging messages in console (Default: false)")
    public boolean debug = false;

    @ConfigEntry(requiresRestart = true, comment = "\nEnables (true)/Disables (false) trace messages in console (Default: false)")
    public boolean trace = false;

    public LoggerConfig() {
        super(ConfigOptions.mod("mysql_sync").branch(new String[]{"", "Logging"}).fileHeader("Main Configuration file for Logging messages output to console Options"));
    }
}
