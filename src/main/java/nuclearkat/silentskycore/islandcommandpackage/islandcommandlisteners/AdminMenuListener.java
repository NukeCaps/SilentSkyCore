package nuclearkat.silentskycore.islandcommandpackage.islandcommandlisteners;

import nuclearkat.silentskycore.islandcommandpackage.islandMinions.MinionCategory;
import nuclearkat.silentskycore.islandcommandpackage.islandMinions.MinionEgg;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AdminMenuListener implements Listener {

    @EventHandler
    public void Click(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.AQUA + "Admin Menu") && e.getCurrentItem() != null) {
            Player player = (Player) e.getWhoClicked();
            e.setCancelled(true);

            MinionEgg minerEgg = new MinionEgg(MinionCategory.MINER, 1, 1, 1);
            MinionEgg farmerEgg = new MinionEgg(MinionCategory.FARMER, 1, 1, 1);
            MinionEgg grinderEgg = new MinionEgg(MinionCategory.GRINDER, 1, 1, 1);

            switch (e.getRawSlot()) {
                case 0:
                    player.getInventory().addItem(minerEgg);
                    break;
                case 1:
                    player.getInventory().addItem(farmerEgg);
                    break;
                case 2:
                    player.getInventory().addItem(grinderEgg);
                    break;
                default:
                    player.closeInventory();
                    break;
            }
        }
    }

}
