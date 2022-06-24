package racing.boathub.race;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Objects;

public class EditorListener implements Listener {
    private static final Main plugin = Main.getInstance();
    @EventHandler
    public void rClickBlock(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (Objects.requireNonNull(e.getHand()).toString().equals("OFF_HAND")) {
            return;
        }
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        if(Objects.equals(plugin.players.get(e.getPlayer().getUniqueId()).getState(), States.EDIT) && Objects.requireNonNull(e.getPlayer().getInventory().getItem(EquipmentSlot.HAND)).getType() == Material.STICK){

            Player p = e.getPlayer();
            plugin.editors.get(plugin.players.get(p.getUniqueId())).setSelection1(block.getLocation().toVector());
            p.sendMessage("Set position 2");
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void lClickBlock(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        if(Objects.equals(plugin.players.get(e.getPlayer().getUniqueId()).getState(), States.EDIT) && Objects.requireNonNull(e.getPlayer().getInventory().getItem(EquipmentSlot.HAND)).getType() == Material.STICK){

            Player p = e.getPlayer();
            plugin.editors.get(plugin.players.get(p.getUniqueId())).setSelection2(block.getLocation().toVector());
            p.sendMessage("Set position 1");
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void lClickBlock(BlockBreakEvent e) {
        if(Objects.equals(plugin.players.get(e.getPlayer().getUniqueId()).getState(), States.EDIT) && Objects.requireNonNull(e.getPlayer().getInventory().getItem(EquipmentSlot.HAND)).getType() == Material.STICK){
            e.setCancelled(true);
        }
    }
}
