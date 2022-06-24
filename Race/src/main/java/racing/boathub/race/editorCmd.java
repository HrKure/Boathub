package racing.boathub.race;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class editorCmd implements CommandExecutor {
    private static final Main plugin = Main.getInstance();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //Creating a new edit session
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = ((Player) sender).getPlayer();
        assert p != null;
        BPlayer player = plugin.players.get(p.getUniqueId());
        if (Objects.equals(player.getState(), States.IDLE)) {
            if (args[0].equalsIgnoreCase("create")) {
                if (args.length == 3 && (!(plugin.editors.containsKey(player)))) {
                    player.setState(States.EDIT);
                    plugin.editors.put(player, new Editor(p, true, 1));
                    p.sendMessage("Created a new editing session");
                    return true;
                }
            }
        }
        else if (!Objects.equals(player.getState(), States.PLAYING)) {p.sendMessage("You can't enter the editor while playing!");}
        if (Objects.equals(player.getState(), States.EDIT)) {
            //Load up a unpublished/published track for editing in the future
            if (args[0].equalsIgnoreCase("edit")) {

            }
            //Add a checkpoint
            else if (args[0].equalsIgnoreCase("addcheckpoint")) {
                plugin.editors.get(player).addCheckpoint();
                p.sendMessage("Added a new checkpoint");
                return true;
            }
            //
            else if (args[0].equalsIgnoreCase("setend")) {
                plugin.editors.get(player).setEnd();
                p.sendMessage("Succesfuly set the endpoint to the current selection");
                return true;
            }
            else if (args[0].equalsIgnoreCase("setrespawn")) {
                plugin.editors.get(player).setRespawn(p.getLocation().toVector());
                p.sendMessage("Succesfuly set the Respawn point");
                return true;
            }
            else if (args[0].equalsIgnoreCase("setpit")) {
                plugin.editors.get(player).setPitstop();
                p.sendMessage("Succesfuly set the Pitstop to the current selection");
                return true;
            }
            else if (args[0].equalsIgnoreCase("setspawn")) {
                Location loc = p.getLocation();
                Vector vec = new Vector(loc.getX(), loc.getY(), loc.getZ());
                plugin.editors.get(player).addSpawn(vec);
                plugin.editors.get(player).setYaw((int) loc.getYaw());
                p.sendMessage("Succesfuly added a spawnpoint");
                return true;
            }
            //
            else if (args[0].equalsIgnoreCase("publish")) {
                plugin.editors.get(player).publish();
                plugin.editors.remove(player);
                player.setState(States.IDLE);
                p.sendMessage("Succesfully published the track");
                return true;
            }
            //
            else if (args[0].equalsIgnoreCase("end")) {
                plugin.editors.remove(player);
                player.setState(States.IDLE);
                p.sendMessage("Ended current editing session");
                return true;
            }
        }
        else {p.sendMessage("First create/load a editing session");}
        return false;
    }
}
