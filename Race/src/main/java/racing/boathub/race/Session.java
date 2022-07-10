package racing.boathub.race;

import java.util.List;

public class Session {
    Racer r;
    Long start;
    Long end;
    List<TrackTime> trackTimes;
    Boolean paused = false;
    Main plugin = Main.getInstance();
    public Session(Racer racer) {
        start = racer.getTrackTime().getStart();
        this.r = racer;
    }

}
