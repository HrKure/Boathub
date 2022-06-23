package racing.boathub.race;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Spectator extends BPlayer{
    public Spectator(UUID id, HashMap<String, String> data, Player p) {
        super(id, data, p);
    }
}
