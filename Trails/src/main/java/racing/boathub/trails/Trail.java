package racing.boathub.trails;

import org.bukkit.Color;
import org.bukkit.Particle;

public class Trail {
    String type = null;
    Color color = null;
    String name = null;

    public Trail(String name, String type, Color color) {
        this.type = type;
        this.color = color;
        this.name = name;
    }
    public Particle getParticle() {
        return Particle.valueOf(type);
    }
    public String getType() {
        return type;
    }
    public Color getColor() {
        return color;
    }
}
