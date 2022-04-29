package racing.boathub.race;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {
    HashMap<UUID, TimeTrial> timeTrials = new HashMap<>();
    HashMap<UUID, Race> Races = new HashMap<>();
    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
