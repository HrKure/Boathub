package racing.boathub.race;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Editor extends Gamemode {
    WorldManager wManager;
    Vector selection1 = null;
    Vector selection2 = null;
    List<BPlayer> builders;
    Main plugin;
    SWorld editWorld;
    Vector respawn = null;
    List<Region> checkpoints = new ArrayList<>();
    List<Vector> spawns = new ArrayList<>();
    Region start = null;
    Region end = null;
    Region pitstop = null;
    int yaw = 0;
    String id;
    String label;
    public Editor(Player p, Boolean isNew, Integer saveSlot) {
        super(Gamemodes.EDITOR, "Epic BoatGang");
        wManager = Main.getWmanager();
        plugin = Main.getInstance();
        if(isNew) {
            wManager.createWorld(p.getName() + saveSlot, p, this);
        }
        else {
            wManager.loadSave(saveSlot, p, this);
        }
    }
    public void publish() {
        if(start != null && end != null && checkpoints.size() >= 1 && !spawns.isEmpty() && respawn != null) {
            plugin.addNewTrack(id, label, start, end, pitstop, checkpoints, getCreators(), spawns, yaw, respawn);
            wManager.createCopy(editWorld.world.getName(), id, plugin.tracks.get(id));

        }
    }
    public void setWorld(SWorld world) {
        this.editWorld = world;
    }
    public void addCheckpoint(Region region) {
        this.checkpoints.add(region);
    }
    public void removeCheckpoint(Region region) {
        this.checkpoints.remove(region);
    }
    public void setStart(Region region) {
        this.start = region;
    }
    public void setEnd(Region region) {
        this.end = region;
    }
    public void setPitstop(Region region) {
        this.pitstop = region;
    }
    public void setRespawn(Vector v) {
        this.respawn = v;
    }
    public void setLabel(String label) {
        this.label = label;
        this.id = label.toLowerCase().replaceAll(" ", "");
    }
    public void setYaw(int yaw) {
        this.yaw = yaw;
    }
    public void addSpawn(Vector v) {
        spawns.add(v);
    }
    public String getCreators() {
        StringBuilder creators = new StringBuilder();
        for(BPlayer p : builders) {
            creators.append(p);
            creators.append(", ");
        }
        creators.deleteCharAt(creators.length() - 1);
        creators.deleteCharAt(creators.length() - 1);
        return creators.toString();
    }


}
