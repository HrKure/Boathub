package racing.boathub.race;

import java.util.List;

public class TimeTrial extends Gamemode{
    List<Racer> players;
    Track track;
    public TimeTrial(Track track, List<Racer> players) {
        super(Gamemodes.TIMETRIAL, "Epic BoatGang");
        this.players = players;
        this.track = track;
    }
}
