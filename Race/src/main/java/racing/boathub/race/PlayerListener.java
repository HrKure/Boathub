package racing.boathub.race;

import org.bukkit.Location;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerListener implements Listener {
    private static final Main plugin = Main.getInstance();
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        plugin.players.put(e.getPlayer().getUniqueId(), new Racer(e.getPlayer().getUniqueId(), new HashMap<>(), e.getPlayer()));
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        plugin.backupPlayer(plugin.players.get(e.getPlayer().getUniqueId()));
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        //Ignore yaw and pitch change
        Location from = e.getFrom();
        Location to = e.getTo();
        if(from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {


            //ignore players not in a boat
            Player p = e.getPlayer();
            if (p.isInsideVehicle() && p.getVehicle() instanceof Boat) {
                Racer racer = plugin.players.get(p.getUniqueId());


                //Timetrial
                if(racer.getGamemode() == Gamemodes.TIMETRIAL) {
                    TimeTrial timeTrial = racer.getTimeTrial();
                    Track track = timeTrial.track;
                    if(racer.getStartTime() == null) {
                        if (track.start.isInside(p)) {
                            racer.setStartTime(plugin.getCurrentMillis());
                            racer.start();
                        }
                    }
                    else if(racer.getCPProgress() != track.cps.size()) {
                        if(track.cps.get(racer.checkpoints).isInside(p)) {
                            racer.passedCP();
                        }
                    }
                    else if(racer.getCPProgress() == track.cps.size()) {
                        if(track.end.isInside(p)) {
                            plugin.saveLapTime(racer, track, plugin.getCurrentMillis() - racer.getStartTime());
                            racer.resetProgress();
                            if(track.noEnd) {
                                racer.start();
                                racer.setStartTime(plugin.getCurrentMillis());
                            }
                        }
                    }
                }


                //race
                else if(racer.getGamemode() == Gamemodes.RACE) {
                    TimeTrial timeTrial = racer.getTimeTrial();
                    Track track = timeTrial.track;
                    if(racer.getStartTime() == null) {
                        if (track.start.isInside(p)) {
                            racer.setStartTime(plugin.getCurrentMillis());
                            racer.start();
                        }
                    }
                    else if(racer.getCPProgress() != track.cps.size()) {
                        if(track.cps.get(racer.checkpoints).isInside(p)) {
                            racer.passedCP();
                        }
                    }
                    else if(racer.getCPProgress() == track.cps.size()) {
                        if(track.end.isInside(p)) {
                            racer.passedLap();
                        }
                        if(track.pitstop.isInside(p)) {
                            racer.passedPit();
                        }
                    }
                }
            }

        }
    }
}
