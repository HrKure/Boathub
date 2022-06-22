package racing.boathub.race;

import co.aikar.idb.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin {
    public static String table;
    public FileConfiguration config = getConfig();
    public static WorldManager wManager;
    public HashMap<UUID, TimeTrial> timeTrials = new HashMap<>();
    public HashMap<UUID, Race> Races = new HashMap<>();
    public HashMap<String, Track> tracks = new HashMap<>();
    private static Main instance;
    @Override
    public void onEnable() {
        // Plugin startup logic
        Main.instance = this;
        Main.wManager = new WorldManager();
        config.options().copyDefaults(true);
        saveConfig();
        String username = config.getString("username");
        String password = config.getString("password");
        String database = config.getString("database");
        table = config.getString("table");
        int port = config.getInt("port");
        String host = config.getString("ip");
        assert username != null;
        assert password != null;
        assert database != null;
        PooledDatabaseOptions options = BukkitDB.getRecommendedOptions(this, username, password, database, host + ":" + port);
        options.setDataSourceProperties(new HashMap<>() {{
            put("useSSL", false);
        }});
        options.setMinIdleConnections(5);
        options.setMaxConnections(10);
        Database db = new HikariPooledDatabase(options);
        DB.setGlobalDatabase(db);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static Main getInstance() {
        return Main.instance;
    }
    public static WorldManager getWmanager() {
        return Main.wManager;
    }
}
