package racing.boathub.race;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Racer extends BPlayer{
    Boat boat;
    Main plugin = Main.getInstance();
    TimeTrial timeTrial;
    Track track = null;
    HashMap<Track, Long> bestTimes = new HashMap<>();
    HashMap<Integer, CPTime> cpTimes = new HashMap<>();
    Session session = null;
    TrackTime trackTime = null;
    Long lastCP = null;
    int lap = 0;
    int pitstops = 0;
    int checkpoints = 0;

    UUID race;
    public Racer(UUID id, HashMap<String, String> data, Player p) {
        super(id, data, p);

        this.race = null;
    }
    //get the last set start time in millis.
    //sets the racers race to the race id pog
    public void setRace(UUID id) {
        this.race = id;
    }
    //get race id
    public UUID getRace() {
        return this.race;
    }
    public void setBoat(Boat boat) {this.boat = boat;}
    public Boat getBoat() {return this.boat;}
    public Boat spawnBoat(Location l, World w) {
        return (Boat) w.spawnEntity(l, EntityType.BOAT);
    }
    public TimeTrial getTimeTrial() {return  timeTrial;}
    public void setTimeTrial(TimeTrial timeTrial) {this.timeTrial = timeTrial;}
    public int getCPProgress() {return checkpoints;}
    public void passedCP() {

        Long cpStart;
        if(lastCP == null) {
            cpStart = trackTime.getStartTime();
        }
        else {
            cpStart = lastCP;
        }
        Long currentTime = plugin.getCurrentMillis();
        cpTimes.put(checkpoints, new CPTime(cpStart, currentTime));
        lastCP = currentTime;
        checkpoints += 1;
    }
    public void passedLap() {
        lap += 1;
    }
    public void passedPit() {
        lap += 1;
        pitstops += 1;
    }
    public void start() {
        lap = 1;
        if(getSession() == null) {
            setSession(new Session(this));
        }
        if(trackTime == null) {
            setTrackTime(new TrackTime(plugin.getCurrentMillis(), this, track));
        }
    }
    public void resetProgress() {
        lap = 0;
        pitstops = 0;
        checkpoints = 0;
    }
    public void completedTTLap() {
    }
    public void setTrack(Track track) {
        this.track = track;
    }
    public Track getTrack() {
        return track;
    }
    public Boolean isBetterTime(Track track, Long time) {
        if(bestTimes.containsKey(track)) {
            return bestTimes.get(track) > time;
        }
        else {return true;}
    }
    public void resetRun() {
        boat.remove();
        setBoat(null);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            setBoat(spawnBoat(track.getRespawn().toLocation(timeTrial.getWorld().bWorld, track.getYaw(), 0), timeTrial.getWorld().bWorld));
            p.teleport(track.getRespawn().toLocation(timeTrial.getWorld().bWorld, track.getYaw(), 0));
            boat.addPassenger(p);
            resetProgress();
        }, 1);
    }
    public Session getSession() {return session;}
    public TrackTime getTrackTime() {return trackTime;}
    public void setSession(Session s) {session = s;}
    public void setTrackTime(TrackTime tt) {trackTime = tt;}



}
