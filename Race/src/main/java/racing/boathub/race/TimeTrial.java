package racing.boathub.race;

import java.util.List;

public class TimeTrial extends Gamemode{
    List<Player> players;
    Track track;
    public TimeTrial(Track track, List<Player> players) {
        super(Gamemodes.TIMETRIAL, "Epic BoatGang");
        this.players = players;
        this.track = track;
    }
}
