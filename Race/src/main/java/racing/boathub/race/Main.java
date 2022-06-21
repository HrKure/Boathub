package racing.boathub.race;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class Main extends JavaPlugin {
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
