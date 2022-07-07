package io.github.drhampust.mysql_sync;

import com.oroarmor.config.Config;
import com.oroarmor.config.ConfigItemGroup;
import com.oroarmor.config.ConfigItem;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.util.List;

import static org.spongepowered.include.com.google.common.collect.ImmutableList.of;

public class SQLConfig extends Config {
    public static final ConfigItemGroup mainGroup = new ConfigGroupLevel1();
    public static final List<ConfigItemGroup> configs = of(mainGroup);

    public SQLConfig() {
        super(configs, new File(FabricLoader.getInstance().getConfigDir().toFile(), "MySQL_Sync.json"), "MySQL_Sync");
    }

    public static class ConfigGroupLevel1 extends ConfigItemGroup {


        public ConfigGroupLevel1() {
            super(of(new SQLGroup(), new LoggingGroup()), "Configuration");
        }

        public static class SQLGroup extends ConfigItemGroup {
            public static final ConfigItem<String> host = new ConfigItem<String>("Host", "127.0.0.1", "Specifies what host to try and connect to (Default: 127.0.0.1 (same as localhost))");
            public static final ConfigItem<String> port = new ConfigItem<String>("Port", "3306", "Specifies what port the SQL database is running on (Default: 3306)");
            public static final ConfigItem<String> db = new ConfigItem<String>("Database", "Minecraft", "Sets the database in which it will create tables and store information");
            public static final ConfigItem<String> username = new ConfigItem<String>("Username", "", "Enter username for SQL accesses");
            public static final ConfigItem<String> password = new ConfigItem<String>("Password", "", "Enter password for SQL accesses");

            public SQLGroup() {
                super(List.of(host, port, db, username, password), "SQL");
            }
        }

        public static class LoggingGroup extends ConfigItemGroup {
            public static final ConfigItem<String> logLevel = new ConfigItem<String>("Logging Level", "info", "Get what level of logging should be shown in console. (Default: INFO)"
                    + "\n\"DEBUG\" shows all information regarding what the plugin is doing at all times to troubleshoot errors."
                    + "\n\"INFO\" shows information regarding what the plugin is doing and any warnings and errors."
                    + "\n\"WARNING\" Only shows warnings or a errors, Note that a warning can mean that something was wrong but the program is able to continue this can lead to unintened behaviour."
                    + "\n\"ERROR\" Only shows when something has gone critically wrong. This means that the program is not functioning as intended.");

            public LoggingGroup() {
                super(of(logLevel), "Logging");
            }
        }
    }
}
