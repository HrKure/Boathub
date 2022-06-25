package racing.boathub.race;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;


public class Track extends Map{
    Main plugin;
    WorldManager wManager;
    List<Region> cps;
    List<SWorld> worlds = new ArrayList<>();
    List<Vector> spawns;
    Vector respawn;
    int yaw;
    Region start;
    Region end;
    Region pitstop;
    String creators;
    public Track(String id, String label, List<Region> cps, Region start, Region end, Region pitstop, String creators, int yaw, List<Vector> spawns, Vector respawn) {
        super(id, label);
        this.cps = cps;
        this.start = start;
        this.end = end;
        this.pitstop = pitstop;
        this.creators = creators;
        this.yaw = yaw;
        this.spawns = spawns;
        this.respawn = respawn;
        plugin = Main.getInstance();
        wManager = Main.getWmanager();
    }
    public SWorld getWorld() {
     if(worlds.isEmpty()) {
         loadTrack();
     }
//     else if(worlds.size() == 1 && worlds.get(0).isFull()) {
//         wManager.loadTemplate(worlds.get(0).world.getName(), Integer.parseInt(worlds.get(0).world.getName().replaceAll("[\\D]", "")) + 1);
//     }
     else {
         int pCount = 0;
         SWorld emptiest = null;
         for (SWorld world : worlds) {
             if(!world.isFull() && world.getPCount() < pCount) {
                 emptiest = world; pCount = world.getPCount();
             }
         }
         if(emptiest != null) {
             return emptiest;
         }
         else {
             loadTrack();
         }
     }
     return null;
    }
    private void loadTrack() {
        if(worlds.isEmpty()) {
            wManager.loadTrack(this);
        }
        else {
            SWorld notFullW = null;
            for (SWorld world : worlds) {
                if(!world.isFull()) {
                    notFullW = world;
                }
            }
            if(notFullW == null) {
                wManager.loadTemplate(id, Integer.parseInt(worlds.get(0).world.getName().replaceAll("[\\D]", "")) + 1, this);
            }
        }
    }
    public void addWorld(SWorld world) {
        this.worlds.add(world);
    }
}
