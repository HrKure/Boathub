package racing.boathub.race;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class tickListener implements Listener {
    Main plugin = Main.getInstance();
    @EventHandler
    public void onTick(ServerTickStartEvent e) {
        plugin.ctime = System.currentTimeMillis();
    }
}
