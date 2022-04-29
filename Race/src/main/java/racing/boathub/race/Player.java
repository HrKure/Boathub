package racing.boathub.race;

import java.util.HashMap;
import java.util.UUID;

public class Player {
    UUID UUID;
    HashMap<String, String> data;
    States state;
    Gamemodes gamemode;

    public Player(UUID id, HashMap<String, String> data) {
        this.UUID = id;
        this.data = data;
        this.state = States.IDLE;




        //Data backup task here, cancel when leave <3
    }

    public void setState(States state) {
        this.state = state;
    }
    public States getState() {
        return this.state;
    }

    public void setGamemode(Gamemodes gamemode) {
        this.gamemode = gamemode;
    }
    public Gamemodes getGamemode() {
        return this.gamemode;
    }

}
