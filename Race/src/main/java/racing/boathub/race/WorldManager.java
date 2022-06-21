package racing.boathub.race;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.IOException;

public class WorldManager {
    SlimePlugin Splugin;
    public WorldManager() {
        Splugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

    }
    public void loadTemplate(String templateName, int number) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            SlimeWorld slimeWorld = null;
            try {
                slimeWorld = Splugin.loadWorld(Splugin.getLoader("mysql"), templateName, true, Splugin.getWorld(templateName).getPropertyMap()).clone(templateName + number);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException e) {
                e.printStackTrace();
            }
            if(slimeWorld != null) {
                SlimeWorld finalSlimeWorld = slimeWorld;
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> Splugin.generateWorld(finalSlimeWorld));
            }
        });
    }
    public void loadTrack(Track track) {
        SlimePropertyMap properties = new SlimePropertyMap();
        properties.setValue(SlimeProperties.DIFFICULTY, "normal");
        properties.setValue(SlimeProperties.SPAWN_X, 0);
        properties.setValue(SlimeProperties.SPAWN_Y, 100);
        properties.setValue(SlimeProperties.SPAWN_Z, 0);
        properties.setValue(SlimeProperties.ALLOW_ANIMALS, false);
        properties.setValue(SlimeProperties.ALLOW_MONSTERS, false);
        properties.setValue(SlimeProperties.PVP, false);
        properties.setValue(SlimeProperties.DEFAULT_BIOME, "PLAINS");
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            SlimeWorld slimeWorld = null;
            try {
                slimeWorld = Splugin.loadWorld(Splugin.getLoader("mysql"), track.getId(), true, properties);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException e) {
                e.printStackTrace();
            }
            if(slimeWorld != null) {
                SlimeWorld finalSlimeWorld = slimeWorld;
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> Splugin.generateWorld(finalSlimeWorld));
            }
        });
    }
    public void unloadWorld(String wName) {
        World bWorld = Bukkit.getWorld(wName);
        Bukkit.unloadWorld(wName, true);
        SlimeWorld world = Splugin.getWorld(wName);
        SlimeLoader loader = world.getLoader();
        assert bWorld != null;
        unlockWorldFinally(bWorld, loader);
    }
    public void createWorld(String wName, Player p) {
        SlimePropertyMap properties = new SlimePropertyMap();
        properties.setValue(SlimeProperties.DIFFICULTY, "normal");
        properties.setValue(SlimeProperties.SPAWN_X, 0);
        properties.setValue(SlimeProperties.SPAWN_Y, 100);
        properties.setValue(SlimeProperties.SPAWN_Z, 0);
        properties.setValue(SlimeProperties.ALLOW_ANIMALS, false);
        properties.setValue(SlimeProperties.ALLOW_MONSTERS, false);
        properties.setValue(SlimeProperties.PVP, false);
        properties.setValue(SlimeProperties.DEFAULT_BIOME, "PLAINS");
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            SlimeWorld slimeWorld = null;
            try {
                slimeWorld = Splugin.createEmptyWorld(Splugin.getLoader("mysql"), wName, false, properties);
            } catch (IOException | WorldAlreadyExistsException e) {
                e.printStackTrace();
            }
            if(slimeWorld != null) {
                SlimeWorld finalSlimeWorld = slimeWorld;
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    Splugin.generateWorld(finalSlimeWorld);
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        World world = Bukkit.getWorld(wName);
                        assert world != null;
                        world.getBlockAt(new Location(world, 0, 99, 0)).setType(Material.STONE);
                        p.teleport(new Location(world, 0, 101, 0));
                        p.setGameMode(GameMode.CREATIVE);
                    }, 100);
                });
            }
        });

    }
    public void loadETrack(Track track, Player p) {
        SlimePropertyMap properties = new SlimePropertyMap();
        properties.setValue(SlimeProperties.DIFFICULTY, "normal");
        properties.setValue(SlimeProperties.SPAWN_X, 0);
        properties.setValue(SlimeProperties.SPAWN_Y, 100);
        properties.setValue(SlimeProperties.SPAWN_Z, 0);
        properties.setValue(SlimeProperties.ALLOW_ANIMALS, false);
        properties.setValue(SlimeProperties.ALLOW_MONSTERS, false);
        properties.setValue(SlimeProperties.PVP, false);
        properties.setValue(SlimeProperties.DEFAULT_BIOME, "PLAINS");
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            SlimeWorld slimeWorld = null;
            try {
                slimeWorld = Splugin.loadWorld(Splugin.getLoader("mysql"), track.getId(), false, properties);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException e) {
                e.printStackTrace();
            }
            if(slimeWorld != null) {
                SlimeWorld finalSlimeWorld = slimeWorld;
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {Splugin.generateWorld(finalSlimeWorld);
                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        World world = Bukkit.getWorld(finalSlimeWorld.getName());
                        assert world != null;
                        p.teleport(new Location(world, 0, 101, 0));
                        p.setGameMode(GameMode.CREATIVE);
                    }, 100);
                });
            }
        });
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



}
