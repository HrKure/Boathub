package racing.boathub.race;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Racer extends BPlayer{
    Long startTime = null;
    Boat boat;
    TimeTrial timeTrial;
    Track track = null;
    HashMap<Track, Long> bestTimes = new HashMap<>();
    int lap = 0;
    int pitstops = 0;
    int checkpoints = 0;

    UUID race;
    public Racer(UUID id, HashMap<String, String> data, Player p) {
        super(id, data, p);

        this.race = null;
    }
    //Set the start time, mainly used for time trial
    public void setStartTime(Long start) {
        this.startTime = start;
    }
    //get the last set start time in millis.
    public Long getStartTime() {
        return this.startTime;
    }
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
        checkpoints += 1;
        //get split time or smth
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
    }
    public void resetProgress() {
        lap = 0;
        pitstops = 0;
        checkpoints = 0;
        startTime = null;
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
        return bestTimes.get(track) > time;

    }






}
