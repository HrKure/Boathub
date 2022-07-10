package racing.boathub.race;

import co.aikar.idb.*;
import com.google.common.util.concurrent.AtomicDouble;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Boat;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.sql.Array;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class Main extends JavaPlugin {
    public static String table;
    public FileConfiguration config = getConfig();
    public static WorldManager wManager;
    public HashMap<UUID, TimeTrial> timeTrials = new HashMap<>();
    public HashMap<UUID, Race> races = new HashMap<>();
    public HashMap<String, Track> tracks = new HashMap<>();
    public HashMap<UUID, Racer> players = new HashMap<>();
    public HashMap<BPlayer, Editor> editors = new HashMap<>();
    public Location spawn;
    public List<Racer> timer = new ArrayList<>();
    public Long ctime = null;
    private static Main instance;
    @Override
    public void onEnable() {
        //some epic stuff
        Main.instance = this;
        Main.wManager = new WorldManager();
        
        //config stuff
        config.options().copyDefaults(true);
        saveConfig();

        //Database connection stuff
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


        //Database table setup
        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS Tracks " +
                    "(ID VARCHAR(100) NOT NULL UNIQUE," +
                    " LABEL VARCHAR(100) NOT NULL," +
                    " START VARCHAR(100) NOT NULL," +
                    " END VARCHAR(100) NOT NULL," +
                    " PITSTOP VARCHAR(100) NOT NULL," +
                    " CHECKPOINTS VARCHAR(2000) NOT NULL," +
                    " CREATORS VARCHAR(2000) NOT NULL," +
                    " SPAWNS VARCHAR(2000) NOT NULL," +
                    " RESPAWN VARCHAR(100) NOT NULL," +
                    " YAW SMALLINT NOT NULL);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS Times " +
                    "(ID VARCHAR(100) NOT NULL UNIQUE," +
                    " RACER VARCHAR(100) NOT NULL," +
                    " TRACKID VARCHAR(100) NOT NULL," +
                    " START VARCHAR(100) NOT NULL," +
                    " TIME VARCHAR(100) NOT NULL," +
                    " END VARCHAR(100) NOT NULL);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS CPTimes " +
                    "(TIMEID VARCHAR(100) NOT NULL," +
                    " RACER VARCHAR(100) NOT NULL," +
                    " NUMBER INT NOT NULL," +
                    " START VARCHAR(100) NOT NULL," +
                    " TIME VARCHAR(100) NOT NULL," +
                    " END VARCHAR(100) NOT NULL);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS Tries " +
                    "(ID VARCHAR(100) NOT NULL UNIQUE," +
                    " RACER VARCHAR(100) NOT NULL," +
                    " TRACKID VARCHAR(100) NOT NULL," +
                    " START VARCHAR(100) NOT NULL," +
                    " TIME VARCHAR(100) NOT NULL," +
                    " END VARCHAR(100) NOT NULL," +
                    " CHECKPOINTS LONGTEXT NOT NULL);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            DB.executeUpdate("CREATE TABLE IF NOT EXISTS Session " +
                    "(ID VARCHAR(100) NOT NULL UNIQUE," +
                    " RACER VARCHAR(100) NOT NULL," +
                    " START VARCHAR(100) NOT NULL," +
                    " END VARCHAR(100) NOT NULL," +
                    " TIMES LONGTEXT NOT NULL," +
                    " TRIES LONGTEXT NOT NULL);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Load data from db :)
        loadTracks();
        //Register Events
        getServer().getPluginManager().registerEvents(new EditorListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new tickListener(), this);
        //Register Commands
        Objects.requireNonNull(getServer().getPluginCommand("editor")).setExecutor(new editorCmd());
        Objects.requireNonNull(getServer().getPluginCommand("edebug")).setExecutor(new debugCmd());
        //start Timer
        startTimer();
        spawn = new Location(Bukkit.getWorld("world"), 24, 97, -78);
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        DB.close();
    }
    public static Main getInstance() {
        return Main.instance;
    }

    public static WorldManager getWmanager() {
        return Main.wManager;
    }

    public void addNewTrack(String id, String label, Region start, Region end, Region pitstop, List<Region> checkpoints, String creators, List<Vector> spawns, int yaw, Vector respawn) {
        this.tracks.put(id, new Track(id, label, checkpoints, start, end, pitstop, creators, yaw, spawns, respawn));
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                DB.executeUpdate("INSERT INTO Tracks (ID, LABEL, START, END, PITSTOP, CHECKPOINTS, CREATORS, SPAWNS, YAW, RESPAWN) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", id, label, start.getMinMax(), end.getMinMax(), pitstop.getMinMax(), cpsToString(checkpoints), creators, spawnsToString(spawns), yaw, vectorToString(respawn));
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
        Vector min = stringToVector(mins);
        Vector max = stringToVector(maxs);
        return new Region(min, max, rt);
    }
    public void loadTracks() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                List<DbRow> dbRows = DB.getResults("SELECT * FROM Tracks;");
                for( DbRow s : dbRows) {
                    String id = s.get("ID");
                    String start = s.get("START");
                    String end = s.get("END");
                    String respawn = s.get("RESPAWN");
                    String pitstop = s.get("PITSTOP");
                    String label = s.getString("LABEL");
                    String checkpoints = s.getString("CHECKPOINTS");
                    int yaw = s.getInt("YAW");
                    String spawns = s.getString("SPAWNS");
                    String creators = s.getString("CREATORS");
                    tracks.put(id, new Track(id, label, stringToCpsRegions(checkpoints), minMaxToRegion(start, RegionType.START), minMaxToRegion(end, RegionType.FINISH), minMaxToRegion(pitstop, RegionType.PITSTOP), creators, yaw, stringToSpawns(spawns), stringToVector(respawn)));

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        });

    }
    public String spawnsToString(List<Vector> spawns) {
        StringBuilder spawnsS = new StringBuilder();
        for (Vector spawn : spawns) {
            spawnsS.append(vectorToString(spawn)).append(";");
        }
        spawnsS.deleteCharAt(spawnsS.length() - 1);
        return spawnsS.toString();
    }
    public String vectorToString(Vector vec) {
        return vec.getBlockX() + "," + vec.getBlockY() + "," + vec.getBlockZ();
    }
    public List<Vector> stringToSpawns(String s) {
        List<Vector> vs = new ArrayList<>();
        for(String a : s.split(";")) {
            vs.add(stringToVector(a));
        }
        return vs;
    }
    public Vector stringToVector(String s) {
                String[] xyz = s.split(",");
                return new Vector(Integer.parseInt(xyz[0]), Integer.parseInt(xyz[1]), Integer.parseInt(xyz[2]));
    }
    public Region selectionToRegion(Vector a, Vector b, RegionType rt) {
        return new Region(Vector.getMinimum(a, b), Vector.getMaximum(a, b), rt);
    }
    public void backupPlayer(Racer player) {

    }
    public Long getCurrentMillis() {
        return this.ctime;
    }
    public void startTimer() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for(Racer racer : timer) {
                if(racer.getTrackTime() != null) {
                    if(racer.getGamemode() == Gamemodes.TIMETRIAL) {
                        ChatColor color = ChatColor.GREEN;
                        Long time = getCurrentMillis() - racer.getTrackTime().getStart();
                        if(!racer.isBetterTime(racer.track, time)) {color = ChatColor.RED;}
                        racer.p.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(color + getNiceTime(time)));
                    }
                }
            }




        }, 2, 2);
    }
    public String getNiceTime(Long time) {
        String min = String.valueOf((time / 1000) / 60);
        String sec = String.valueOf((time / 1000) % 60);
        String mil = String.valueOf(time - Integer.parseInt(min) * 60000L - Integer.parseInt(sec) * 1000L);
        StringBuilder timer = new StringBuilder();
        if (min.length() == 1) {
            min = "0" + min;
        }
        if (sec.length() == 1) {
            sec = "0" + sec;
        }
        if (mil.length() == 1) {
            mil = "00" + mil;
        }
        else if (mil.length() == 2) {
            mil = "0" + mil;
        }
        timer.append(min).append(":").append(sec).append(".").append(mil);
        return String.valueOf(timer);
    }
    public void setTime(Long time) {
        this.ctime = time;
    }
    public void goFly(Boat boat, int time) {
        AtomicInteger counter = new AtomicInteger();
        Bukkit.getScheduler().runTaskTimer(this, task -> {
            if(counter.addAndGet(1) > time) {
                task.cancel();
            }
            else {
                boat.setVelocity(boat.getVelocity().add(new Vector(0, 5, 0)));
            }
        }, 1, 1);
    }
    public void saveSession(Session s) {}
    public void saveRun(TrackTime trackTime, List<CPTime> cpTimes) {}
    public void saveCPTime(Long start, Long end, Racer r, UUID runID, int number) {}

}
