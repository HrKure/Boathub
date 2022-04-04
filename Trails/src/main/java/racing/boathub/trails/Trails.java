package racing.boathub.trails;

import co.aikar.idb.*;
import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public final class Trails extends JavaPlugin {
    private static Trails instance;
    HashMap<Player, Editor> editsessions = new HashMap<>();
    HashMap<String, Trail> trails = new HashMap<>();
    HashMap<UUID, String> selectedtrail = new HashMap<>();
    List<Player> boaters = new ArrayList<>();
    public FileConfiguration config = getConfig();
    @Override
    public void onEnable() {

        config.options().copyDefaults(true);
        saveConfig();
        String username = config.getString("username");
        String password = config.getString("password");
        String database = config.getString("database");
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
        options.setMaxConnections(5);
        Database db = new HikariPooledDatabase(options);
        DB.setGlobalDatabase(db);
        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS PlayerTrails ( UUID VARCHAR(100) NOT NULL, NAME VARCHAR(100) NOT NULL);");
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS Trails ( NAME VARCHAR(100) NOT NULL, TYPE VARCHAR(100) NOT NULL, COLOR VARCHAR(100) NOT NULL);");
            CompletableFuture<List<DbRow>> result = DB.getResultsAsync("SELECT * FROM Trails;");
            for (DbRow s : result.get()) {

                String name = s.getString("NAME");
                String type = s.getString("TYPE");
                String[] colorstring = s.getString("COLOR").split(":");
                Color color = Color.fromRGB(Integer.parseInt(colorstring[0]), Integer.parseInt(colorstring[1]), Integer.parseInt(colorstring[2]));
                trails.put(name, new Trail(name, type, color));

            }
            CompletableFuture<List<DbRow>> result2 = DB.getResultsAsync("SELECT * FROM PlayerTrails;");
            for (DbRow s : result2.get()) {
                UUID id = UUID.fromString(s.getString("UUID"));
                String trail = s.getString("TRAIL");
                selectedtrail.put(id, trail);


            }
        } catch (SQLException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Trails.instance = this;
        getServer().getPluginManager().registerEvents(new BoatList(), this);
        startSpawner();
        Objects.requireNonNull(this.getCommand("trail")).setExecutor(new EditorCMD());
    }

    @Override
    public void onDisable() {
        for(UUID id : selectedtrail.keySet()) {
//            Trail trail = trails.get(s);
//            String type = trail.getType();
//            Boolean colored = trail.getColored();
//            String colors = trail.getRed() + ":" + trail.getGreen() + ":" + trail.getBlue();
            try {
                DB.executeUpdate("INSERT INTO PlayerTrails (UUID, NAME) VALUES (?, ?);", id, selectedtrail.get(id));
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    public static Trails getInstance() {
        return Trails.instance;
    }
    public void startSpawner() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimerAsynchronously(this, () -> {
            for(Player p : boaters) {
                UUID id = p.getUniqueId();
                Trail trail = trails.get(selectedtrail.get(id));
                if(trail.type.equalsIgnoreCase("REDSTONE")) {
                    new ParticleBuilder(trail.getParticle()).color(trail.getColor()).source(p).spawn();
                    System.out.println("Redstone trail cool");
                }
                else {
                    new ParticleBuilder(trail.getParticle()).source(p).spawn();
                    System.out.println("Normal trail cool");
                }
            }
        }, 1L /*<-- the initial delay */, 1L /*<-- the interval */);
    }




}
