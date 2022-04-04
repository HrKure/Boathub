package racing.boathub.trails;

import co.aikar.idb.DB;
import org.bukkit.Color;

import java.sql.SQLException;

public class Editor {
    String type = null;
    Color color = null;
    String name;
    Trails plugin = Trails.getInstance();
    public Editor(String name) {
        this.name = name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setRed(int red) {
        this.color = this.color.setRed(red);
    }
    public void setGreen(int green) {
        this.color = this.color.setGreen(green);
    }
    public void setBlue(int blue) {
        this.color = this.color.setBlue(blue);
    }
    public void setType(String type) {
        this.type = type;
    }
    public void finish() {
        try {
            String color2 = color.getRed() + ":" + color.getGreen() + ":" + color.getBlue();
            DB.executeUpdate("INSERT INTO Trails (NAME, TYPE, COLOR ) VALUES (?, ?, ?);", name, type, color2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        plugin.trails.put(name, new Trail(name, type, color));


    }
}
