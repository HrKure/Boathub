package racing.boathub.race;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

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
            else {
                System.out.println("stuff very broken");
            }
        }
        else{sender.sendMessage("ee something no worky");}

        return false;
    }
}
