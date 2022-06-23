package racing.boathub.race;

import co.aikar.idb.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.sql.SQLException;
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
    public void addNewTrack(String id, String label, Region start, Region end, Region pitstop, List<Region> checkpoints, String creators) {
        List<Region> fregions = new ArrayList<>();
        fregions.add(start);
        fregions.addAll(checkpoints);
        fregions.add(end);
        this.tracks.put(id, new Track(id, label, fregions));
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                DB.executeUpdate("INSERT INTO Tracks (ID, LABEL, START, END, PITSTOP, CHECKPOINTS, CREATORS) VALUES (?, ?, ?, ?, ?, ?, ?);", id, label, start.getMinMax(), end.getMinMax(), pitstop.getMinMax(), cpsToString(checkpoints), creators);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    public String cpsToString(List<Region> regions) {
        StringBuilder cps = new StringBuilder();
        for(Region r : regions) {
            cps.append(r.getMinMax());
            cps.append(";");
        }
        cps.deleteCharAt(cps.length() - 1);
        return cps.toString();
    }
    public List<Region> stringToCpsRegions(String cps) {
        String[] cps2 = cps.split(";");
        List<Region> regions = new ArrayList<>();
        for(String checkpoint : cps2) {
            regions.add(minMaxToRegion(checkpoint, RegionType.CP));
        }
        return regions;
    }
    public Region minMaxToRegion(String minmax1, RegionType rt) {
        String[] minmax = minmax1.split(":");
        String mins = minmax[0];
        String maxs = minmax[1];
        String[] minp = mins.split(",");
        String[] maxp = maxs.split(",");
        Vector min = new Vector(Integer.parseInt(minp[0]), Integer.parseInt(minp[1]), Integer.parseInt(minp[2]));
        Vector max = new Vector(Integer.parseInt(maxp[0]), Integer.parseInt(maxp[1]), Integer.parseInt(maxp[2]));
        return new Region(min, max, rt);
    }
    public void loadTracks() {

    }
}
