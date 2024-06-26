package racing.boathub.trails;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EditorCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if(args.length >= 1) {
            Trails plugin = Trails.getInstance();
            if(args[0].equalsIgnoreCase("create")) {
                if(args.length == 2 && !plugin.editsessions.containsKey(p)) {
                    plugin.editsessions.put(p, new Editor(args[1]));
                    p.sendMessage("Editor created");
                }
            }
            if(args[0].equalsIgnoreCase("colorset")) {
                if(args.length == 4 && plugin.editsessions.containsKey(p)) {
                    Editor editor = plugin.editsessions.get(p);
                    editor.setRed(Integer.parseInt(args[1]));
                    editor.setGreen(Integer.parseInt(args[2]));
                    editor.setBlue(Integer.parseInt(args[3]));
                    p.sendMessage("Color set");
                }
            }
            if(args[0].equalsIgnoreCase("typeset")) {
                if(args.length == 2 && plugin.editsessions.containsKey(p)) {
                    Editor editor = plugin.editsessions.get(p);
                    editor.setType(args[1].toUpperCase());
                    p.sendMessage("Type set");
                }
            }
            if(args[0].equalsIgnoreCase("set")) {
                if(args.length == 2 && plugin.trails.containsKey(args[1])) {
                    Trail trail = plugin.trails.get(args[1]);
                    plugin.selectedtrail.put(p.getUniqueId(), args[1]);
                    p.sendMessage("Trail changed");
                }
            }
            if(args[0].equalsIgnoreCase("publish")) {
                if(args.length == 1 && plugin.editsessions.containsKey(p)) {
                    Editor editor = plugin.editsessions.get(p);
                    editor.finish();
                    p.sendMessage("Trail saved");
                }
            }
            if(args[0].equalsIgnoreCase("cancel")) {
                if(args.length == 1 && plugin.editsessions.containsKey(p)) {
                    plugin.editsessions.remove(p);
                    p.sendMessage("Editor closed");
                }
            }
        }
        return false;
    }
}
