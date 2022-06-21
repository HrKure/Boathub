package racing.boathub.race;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class Editor extends Gamemode {
    WorldManager wManager = null;
    Main plugin = null;
    public Editor(Player p, Boolean isNew, @Nullable Integer saveSlot) {
        super(Gamemodes.EDITOR, "Epic BoatGang");
        wManager = Main.getWmanager();
        plugin = Main.getInstance();
        if(isNew) {
            wManager.createWorld(p.getName() + saveSlot, p);
        }
        else {
            wManager.loadETrack(plugin.tracks.get(p.getName() + saveSlot), p);
        }
    }
}
