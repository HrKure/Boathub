package racing.boathub.race;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Editor extends Gamemode {
    WorldManager wManager;
    List<BPlayer> builders;
    Main plugin;
    SWorld editWorld;
    List<Region> checkpoints = new ArrayList<>();
    Region start = null;
    Region end = null;
    Region pitstop = null;
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
        if(start != null && end != null && checkpoints.size() >= 1) {
            plugin.addNewTrack(id, label, start, end, pitstop, checkpoints, getCreators());
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
    public void setLabel(String label) {
        this.label = label;
        this.id = label.toLowerCase().replaceAll(" ", "");
    }
    public String getCreators() {
        StringBuilder creators = new StringBuilder();
        for(BPlayer p : builders) {
            creators.append(p);
            creators.append(",");
        }
        creators.deleteCharAt(creators.length() - 1);
        return creators.toString();
    }


}
