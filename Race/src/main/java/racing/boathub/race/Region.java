package racing.boathub.race;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Region {
    Vector minp;
    Vector maxp;
    RegionType regionType;
    public Region(Vector min, Vector max, RegionType rt) {
        maxp = max;
        minp = min;
        regionType = rt;
    }
    public boolean isInside(Player p) {
        Vector pLoc = p.getLocation().toVector();
        return pLoc.isInAABB(minp, maxp);
    }
}
