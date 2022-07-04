package racing.boathub.race;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class debugCmd implements CommandExecutor {
    private static final Main plugin = Main.getInstance();
    private static final WorldManager wManager = Main.getWmanager();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 2) {
            if(args[0].equals("load")) {
                wManager.loadWorld(args[1]);
                System.out.println("stuff funky");
                return true;
            }
            else if(args[0].equals("unload")) {
                wManager.unloadWorld(args[1]);
                System.out.println("stuff unfunky");
                return true;
            }
            else if(args[0].equals("timetrial")) {
                UUID id = UUID.randomUUID();
//                plugin.tracks.get(args[1]).loadTrack();
//                Bukkit.getScheduler().runTaskLater(plugin, () -> {
//                    plugin.timeTrials.put(id, new TimeTrial(plugin.tracks.get(args[1]), new ArrayList<>()));
//                    System.out.println("Timetrial has been created with id: " + id);
//                }, 100);
                if(plugin.tracks.containsKey(args[1])) {
                    plugin.timeTrials.put(id, new TimeTrial(plugin.tracks.get(args[1]), new ArrayList<>(), id));
                    Bukkit.getScheduler().runTaskLater(plugin, () -> System.out.println("Timetrial has been created with id: " + id), 20);
                }
                else {sender.sendMessage("track with this name doesn't exist");}
                return true;
            }
            else if(args[0].equals("joinTT") && sender instanceof Player p) {
                TimeTrial tt = plugin.timeTrials.get(UUID.fromString(args[1]));
                tt.addPlayer(plugin.players.get(p.getUniqueId()));
                System.out.println("Player joined the TimeTrial");
                return true;
            }
            else {
                System.out.println("stuff very broken");
            }
        }
        else{sender.sendMessage("ee something no worky");}

        return false;
    }
}
