package racing.boathub.race;

import java.util.HashMap;
import java.util.UUID;

public class Racer extends Player{
    Long startTime;


    UUID race;
    public Racer(UUID id, HashMap<String, String> data) {
        super(id, data);

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

    public void setRace(UUID id) {
        this.race = id;
    }
    public UUID getRace() {
        return this.race;
    }









}
