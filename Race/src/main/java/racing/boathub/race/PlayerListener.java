package racing.boathub.race;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class PlayerListener implements Listener {
    private static final Main plugin = Main.getInstance();
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        plugin.players.put(e.getPlayer().getUniqueId(), new BPlayer(e.getPlayer().getUniqueId(), new HashMap<>(), e.getPlayer()));
    }
}
