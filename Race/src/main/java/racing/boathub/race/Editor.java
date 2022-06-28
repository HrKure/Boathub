package racing.boathub.race;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Editor extends Gamemode {
    WorldManager wManager;
    Vector selection1 = null;
    Vector selection2 = null;
    List<BPlayer> builders = new ArrayList<>();
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
        this.builders.add(plugin.players.get(p.getUniqueId()));
        if(isNew) {
            wManager.createWorld(p.getName() + saveSlot, p, this);
        }
        else {
            wManager.loadSave(saveSlot, p, this);
        }
    }
    public void publish() {
        if(start != null && end != null && checkpoints.size() >= 1 && !spawns.isEmpty() && respawn != null && pitstop != null) {
            plugin.addNewTrack(id, label, start, end, pitstop, checkpoints, getCreators(), spawns, yaw, respawn);
            wManager.createCopy(editWorld.world.getName(), id, plugin.tracks.get(id));
            for(BPlayer player : builders) {
                player.p.teleport(plugin.spawn);
                player.p.setGameMode(GameMode.ADVENTURE);
            }
            Bukkit.getScheduler().runTaskLater(plugin, () -> {wManager.unloadWorld(editWorld.world.getName());}, 100);

        }
    }
    public void setWorld(SWorld world) {
        this.editWorld = world;
    }
    public void addCheckpoint() {
        this.checkpoints.add(plugin.selectionToRegion(selection1, selection2, RegionType.CP));
    }
    public void removeCheckpoint() {
        this.checkpoints.remove(this.checkpoints.get(this.checkpoints.size() - 1));
    }
    public void setStart() {
        this.start = plugin.selectionToRegion(selection1, selection2, RegionType.START);
    }
    public void setEnd() {
        this.end = plugin.selectionToRegion(selection1, selection2, RegionType.FINISH);
    }
    public void setPitstop() {
        this.pitstop = plugin.selectionToRegion(selection1, selection2, RegionType.PITSTOP);
    }
    public void setSelection1(Vector v) {
        selection1 = v;
    }
    public void setSelection2(Vector v) {
        selection2 = v;
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
