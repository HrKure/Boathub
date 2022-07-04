package racing.boathub.race;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TimeTrial extends Gamemode{
    List<Racer> players = new ArrayList<>();
    Track track;
    SWorld world;
    Main plugin;
    UUID id;
    public TimeTrial(Track track, List<Racer> players, UUID id) {
        super(Gamemodes.TIMETRIAL, "Epic BoatGang");
        this.plugin = Main.getInstance();
        for (Racer p : players) {
            addPlayer(p);
        }
        this.track = track;
        this.id = id;
        this.world = track.getWorld();
        if(world == null) {
            Bukkit.getScheduler().runTaskTimer(plugin, task -> {
                this.world = track.getWorld();
                if(this.world != null) {
                    task.cancel();
                }
            }, 20L, 20L);
        }
    }
    public void addPlayer(Racer racer) {
        players.add(racer);
        if(world != null) {
            racer.setGamemode(Gamemodes.TIMETRIAL);
            racer.setTimeTrial(this);
            racer.p.teleport(track.respawn.toLocation(world.bWorld, track.yaw, 0));
            racer.setBoat(racer.spawnBoat(track.getRespawn().toLocation(world.bWorld, track.yaw, 0), world.bWorld));
            racer.getBoat().addPassenger(racer.p);
            racer.p.setGameMode(GameMode.ADVENTURE);
            racer.setState(States.PLAYING);
            racer.setTrack(track);
            plugin.timer.add(racer);
        }
    }
    public void removePlayer(Racer racer) {
        players.remove(racer);
        racer.setGamemode(Gamemodes.TIMETRIAL);
        racer.setState(States.IDLE);
        racer.setTimeTrial(null);
        racer.getBoat().remove();
        racer.setBoat(null);
        racer.p.teleport(plugin.spawn);
        racer.p.setGameMode(GameMode.ADVENTURE);
        racer.setTrack(null);
        plugin.timer.remove(racer);
        if(players.isEmpty()) {
            startEmptyTime();
        }
    }
    public SWorld getWorld() {return world;}
    public Track getTrack() {return track;}
    public List<Racer> getPlayers() {return players;}
    public void deleteInstance() {
        unloadWorld();
        plugin.timeTrials.remove(id);
    }
    public void unloadWorld() {
        kickAll();
        world.unloadWorld();
        track.removeWorld(world);
        world = null;
    }
    public void kickAll() {
        for(Racer r : players) {
            removePlayer(r);
        }
    }
    public void startEmptyTime() {
        Bukkit.getScheduler().runTaskLater(plugin, task -> {
            if(getPlayers().isEmpty()) {
                   deleteInstance();
            }
        }, 20L*60L*5L);
    }
}
