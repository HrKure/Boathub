package racing.boathub.race;

import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

import java.io.IOException;

public class SWorld {
    SlimeWorld world;
    World bWorld;
    Integer maxPlayers = 20;
    public SWorld(SlimeWorld world) {
        this.world = world;
        bWorld = Bukkit.getWorld(world.getName());
    }
    public void unloadWorld() { // make sure to keep the template world if more then 1 instance of track
        Bukkit.unloadWorld(world.getName(), true);
        SlimeLoader loader = world.getLoader();
        assert bWorld != null;
        unlockWorldFinally(bWorld, loader);
    }
    private void unlockWorldFinally(World world, SlimeLoader loader) {
        String worldName = world.getName();

        System.out.println("Attempting to unlock world.. " + worldName + ".");
        try {
            if (loader != null && loader.isWorldLocked(worldName)) {
                System.out.println("World.. " + worldName + " is locked.");
                loader.unlockWorld(worldName);
                System.out.println("Attempted to unlock world.. " + worldName + ".");
            } else {
                System.out.println(worldName + " was not unlocked. This could be because the world is either unlocked or not in the config. This is not an error");
            }
        } catch (UnknownWorldException | IOException e) {
            e.printStackTrace();
        }
        System.out.println(ChatColor.GREEN + "World " + ChatColor.YELLOW + worldName + ChatColor.GREEN + " unloaded correctly.");
    }
    public boolean isEmpty() {
        return (bWorld.getPlayers().isEmpty());
    }
    public boolean isFull() {
        return bWorld.getPlayers().size() >= maxPlayers;
    }
    public Integer getPCount() {
        return bWorld.getPlayers().size();
    }
}
