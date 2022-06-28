package racing.boathub.race;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.List;

public class TimeTrial extends Gamemode{
    List<Racer> players = new ArrayList<>();
    Track track;
    SWorld world;
    Main plugin;
    public TimeTrial(Track track, List<Racer> players) {
        super(Gamemodes.TIMETRIAL, "Epic BoatGang");
        this.plugin = Main.getInstance();
        for (Racer p : players) {
            addPlayer(p);
        }
        this.track = track;
        this.world = track.getWorld();
        if(world == null) {
            Bukkit.getScheduler().runTaskTimer(plugin, task -> {
                this.world = track.getWorld();
                if(this.world != null) {
                    task.cancel();
                }
            }, 100L, 100L);
        }
    }
    public void addPlayer(Racer racer) {
        players.add(racer);
        if(world != null) {
            racer.setGamemode(Gamemodes.TIMETRIAL);
            racer.setTimeTrial(this);
            racer.p.teleport(track.respawn.toLocation(world.bWorld, track.yaw, 0));
            racer.setBoat(racer.spawnBoat(track.respawn.toLocation(world.bWorld, track.yaw, 0), world.bWorld));
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
        racer.setTimeTrial(null);
        racer.getBoat().remove();
        racer.setBoat(null);
        racer.p.teleport(plugin.spawn);
        racer.p.setGameMode(GameMode.ADVENTURE);
        racer.setState(States.IDLE);
        racer.setTrack(null);
        plugin.timer.remove(racer);
    }
}
