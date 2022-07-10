package racing.boathub.race;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Objects;

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
        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {


            //ignore players not in a boat
            Player p = e.getPlayer();
            if (p.isInsideVehicle() && p.getVehicle() instanceof Boat) {
                Racer racer = plugin.players.get(p.getUniqueId());

                Boat boat = (Boat) p.getVehicle();
                Location blockLoc = p.getLocation();
                blockLoc.setY(blockLoc.getY() - 1);

                if(p.getLocation().getWorld().getBlockAt(blockLoc).getType() == Material.LIGHT_BLUE_SHULKER_BOX) {
                    Boat b = (Boat) p.getVehicle();
                    plugin.goFly(b, 20);
                }

                //Timetrial
                if (racer.getGamemode() == Gamemodes.TIMETRIAL) {
                    TimeTrial timeTrial = racer.getTimeTrial();
                    Track track = timeTrial.getTrack();
                    if (racer.getTrackTime() == null) {
                        if (track.start.isInside(p)) {
                            racer.start();


                        }
                    } else if (racer.getCPProgress() != track.cps.size()) {
                        if (track.cps.get(racer.checkpoints).isInside(p)) {
                            racer.passedCP();
                        }
                    } else if (racer.getCPProgress() == track.cps.size()) {
                        if (track.end.isInside(p)) {
                            racer.resetProgress();
                            if (track.noEnd) {
                                racer.start();
                               //replace with new TrackTime
                            }
                        }
                    }
                }


                //race redo timing to be handeled on the race side instead of racer obj
                else if (racer.getGamemode() == Gamemodes.RACE) {
                    TimeTrial timeTrial = racer.getTimeTrial();
                    Track track = timeTrial.getTrack();
                    if (racer.getTrackTime().getStart() == null) {
                        if (track.start.isInside(p)) {
                            //racer.getTrackTime().setStartTime(plugin.getCurrentMillis());
                            racer.start();
                        }
                    } else if (racer.getCPProgress() != track.cps.size()) {
                        if (track.cps.get(racer.checkpoints).isInside(p)) {
                            racer.passedCP();
                        }
                    } else if (racer.getCPProgress() == track.cps.size()) {
                        if (track.end.isInside(p)) {
                            racer.passedLap();
                        }
                        if (track.pitstop.isInside(p)) {
                            racer.passedPit();
                        }
                    }
                }
            }

        }
    }

    @EventHandler
    public void onBoatLeave(VehicleExitEvent e) {
        if (e.getExited() instanceof Player) {
            Player p = (Player) e.getExited();
            Racer r = plugin.players.get(p.getUniqueId());
            if(e.getVehicle() == r.getBoat()) {
                if (r.getState() == States.PLAYING && r.getGamemode() == Gamemodes.TIMETRIAL) {
                    TimeTrial tt = r.getTimeTrial();
                    r.resetRun();
                }
            }
        }
    }

    @EventHandler
    public void rClickLeave(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        if (Objects.requireNonNull(e.getHand()).toString().equals("OFF_HAND")) {
            return;
        }
        Player p = e.getPlayer();
        Racer r = plugin.players.get(p.getUniqueId());
        if(Objects.equals(r.getState(), States.PLAYING) && Objects.requireNonNull(p.getInventory().getItem(EquipmentSlot.HAND)).getType() == Material.BARRIER) {
            if(r.getGamemode() == Gamemodes.TIMETRIAL) {
                r.getTimeTrial().removePlayer(r);
            }
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Racer r = plugin.players.get(p.getUniqueId());
        if(!p.hasPermission("i.am.the.god") && r.getState() != States.EDIT) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        Racer r = plugin.players.get(p.getUniqueId());
        if(!p.hasPermission("i.am.the.god") && r.getState() != States.EDIT) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if(!e.getPlayer().hasPermission("i.am.the.god")) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onItemMove(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Racer r = plugin.players.get(p.getUniqueId());
        if(!p.hasPermission("i.am.the.god") && r.getState() != States.EDIT) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent e) {
        if (e.getEntered() instanceof Player) {
            Player p = (Player) e.getEntered();
            if(e.getVehicle() instanceof Boat) {
                Boat boat = (Boat) e.getVehicle();
                boat.setMaxSpeed(100);
                boat.setWorkOnLand(true);
            }
        }
    }
    @EventHandler
    public void playerGoBounce(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {


            //ignore players not in a boat
            Player p = e.getPlayer();
            if (p.isInsideVehicle() && p.getVehicle() instanceof Boat) {

            }
            }
    }
}
