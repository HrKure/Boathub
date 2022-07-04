package racing.boathub.race;

import java.util.List;
import java.util.UUID;

public class Instance {
    Race race;
    TimeTrial timeTrial;
    Gamemodes gamemode;
    List<Racer> players;
    List<Track> mappool;
    BPlayer owner;
    Long validUntil;
    String label;
    UUID id;
    SWorld world;

    public Instance() {
    }
}
