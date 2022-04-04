package racing.boathub.trails;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class BoatList implements Listener {
    Trails plugin = Trails.getInstance();
    @EventHandler
    public void BoatEnter(VehicleEnterEvent e) {
        if(e.getEntered() instanceof Player) {
            plugin.boaters.add((Player) e.getEntered());
        }
    }
    @EventHandler
    public void BoatExit(VehicleExitEvent e) {
        if(e.getExited() instanceof Player) {
            plugin.boaters.remove((Player) e.getExited());
        }
    }
//wow this is soo clever my dude noowayyy
}
