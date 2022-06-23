package racing.boathub.race;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BPlayer {
    UUID UUID;
    HashMap<String, String> data;
    States state;
    Gamemodes gamemode;
    org.bukkit.entity.Player p;


    public BPlayer(UUID id, HashMap<String, String> data, Player p) {
        this.UUID = id;
        this.p = p;
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
