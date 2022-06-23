package racing.boathub.race;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Racer extends BPlayer{
    Long startTime;


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









}
