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
import java.util.List;
import java.util.Objects;

public class WorldManager {
    SlimePlugin Splugin;
    public WorldManager() {
        Splugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

    }
    public void loadTemplate(String templateName, int number, Track track) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            SlimeWorld slimeWorld = null;
            try {
                slimeWorld = Splugin.loadWorld(Splugin.getLoader("mysql"), templateName, true, Splugin.getWorld(templateName).getPropertyMap()).clone(templateName + number);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException e) {
                e.printStackTrace();
            }
            if(slimeWorld != null) {
                SlimeWorld finalSlimeWorld = slimeWorld;
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    Splugin.generateWorld(finalSlimeWorld);
                    track.addWorld(new SWorld(finalSlimeWorld));


                });
            }
        });
    }
    public void createCopy(String worldName, String newName, Track track) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try {
                Splugin.getWorld(worldName).clone(newName, Splugin.getLoader("mysql"));
            } catch (IOException | WorldAlreadyExistsException e) {
                e.printStackTrace();
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
        properties.setValue(SlimeProperties.DEFAULT_BIOME, "plains");
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            SlimeWorld slimeWorld = null;
            try {
                slimeWorld = Splugin.loadWorld(Splugin.getLoader("mysql"), track.getId(), true, properties);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException e) {
                e.printStackTrace();
            }
            if(slimeWorld != null) {
                SlimeWorld finalSlimeWorld = slimeWorld;
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    Splugin.generateWorld(finalSlimeWorld);
                    track.addWorld(new SWorld(finalSlimeWorld));


                });
            }
        });
    }
    public void loadWorld(String name) {
        System.out.println(name + " funky");
        SlimePropertyMap properties = new SlimePropertyMap();
        properties.setValue(SlimeProperties.DIFFICULTY, "normal");
        properties.setValue(SlimeProperties.SPAWN_X, 0);
        properties.setValue(SlimeProperties.SPAWN_Y, 100);
        properties.setValue(SlimeProperties.SPAWN_Z, 0);
        properties.setValue(SlimeProperties.ALLOW_ANIMALS, false);
        properties.setValue(SlimeProperties.ALLOW_MONSTERS, false);
        properties.setValue(SlimeProperties.PVP, false);
        properties.setValue(SlimeProperties.DEFAULT_BIOME, "plains");
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            SlimeWorld slimeWorld = null;
            try {
                slimeWorld = Splugin.loadWorld(Splugin.getLoader("mysql"), name, true, properties);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException e) {
                e.printStackTrace();
            }
            System.out.println(name + " funky x2 ");
            if(slimeWorld != null) {
                System.out.println(name + " funky x3");
                SlimeWorld finalSlimeWorld = slimeWorld;
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    Splugin.generateWorld(finalSlimeWorld);
                    System.out.println(name + " funky x4");
                });
            }
        });
    }
    public void createWorld(String wName, Player p, Editor editor) {
        SlimePropertyMap properties = new SlimePropertyMap();
        properties.setValue(SlimeProperties.DIFFICULTY, "normal");
        properties.setValue(SlimeProperties.SPAWN_X, 0);
        properties.setValue(SlimeProperties.SPAWN_Y, 100);
        properties.setValue(SlimeProperties.SPAWN_Z, 0);
        properties.setValue(SlimeProperties.ALLOW_ANIMALS, false);
        properties.setValue(SlimeProperties.ALLOW_MONSTERS, false);
        properties.setValue(SlimeProperties.PVP, false);
        properties.setValue(SlimeProperties.DEFAULT_BIOME, "plains");
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            SlimeWorld slimeWorld = null;
            try {
                slimeWorld = Splugin.createEmptyWorld(Splugin.getLoader("mysql"), wName, false, properties);
            } catch (IOException | WorldAlreadyExistsException e) {
                e.printStackTrace();
            }
            if(slimeWorld != null) {
                SlimeWorld finalSlimeWorld = slimeWorld;
                editor.setWorld(new SWorld(finalSlimeWorld));
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
    public void loadSave(Integer slot, Player p, Editor editor) {
        SlimePropertyMap properties = new SlimePropertyMap();
        properties.setValue(SlimeProperties.DIFFICULTY, "normal");
        properties.setValue(SlimeProperties.SPAWN_X, 0);
        properties.setValue(SlimeProperties.SPAWN_Y, 100);
        properties.setValue(SlimeProperties.SPAWN_Z, 0);
        properties.setValue(SlimeProperties.ALLOW_ANIMALS, false);
        properties.setValue(SlimeProperties.ALLOW_MONSTERS, false);
        properties.setValue(SlimeProperties.PVP, false);
        properties.setValue(SlimeProperties.DEFAULT_BIOME, "plains");
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            SlimeWorld slimeWorld = null;
            try {
                slimeWorld = Splugin.loadWorld(Splugin.getLoader("mysql"), p.getName() + slot, false, properties);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException | WorldInUseException e) {
                e.printStackTrace();
            }
            if(slimeWorld != null) {
                SlimeWorld finalSlimeWorld = slimeWorld;
                editor.setWorld(new SWorld(finalSlimeWorld));
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
    public void unloadWorld(String name) { // make sure to keep the template world if more then 1 instance of track
        Bukkit.unloadWorld(name, true);
        SlimeLoader loader = Splugin.getLoader("mysql");
        unlockWorldFinally(Objects.requireNonNull(Bukkit.getWorld(name)), loader);
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
