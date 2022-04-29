package racing.boathub.race;

import java.util.List;

public class TimeTrial extends Gamemode{
    List<Player> players;
    Track track;
    public TimeTrial(String type, String label, Track track, List<Player> players) {
        super(type, label);
        this.players = players;
        this.track = track;
    }
}
