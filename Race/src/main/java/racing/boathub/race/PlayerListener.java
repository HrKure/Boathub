package racing.boathub.race;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerListener implements Listener {
    private static final Main plugin = Main.getInstance();
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        plugin.players.put(e.getPlayer().getUniqueId(), new BPlayer(e.getPlayer().getUniqueId(), new HashMap<>(), e.getPlayer()));
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        plugin.backupPlayer(plugin.players.get(e.getPlayer().getUniqueId()));
    }
}
