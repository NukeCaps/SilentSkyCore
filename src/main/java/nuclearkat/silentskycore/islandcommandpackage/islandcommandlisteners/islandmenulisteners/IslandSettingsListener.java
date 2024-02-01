package nuclearkat.silentskycore.islandcommandpackage.islandcommandlisteners.islandmenulisteners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class IslandSettingsListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(ChatColor.AQUA + "Island Settings")){

        }
    }

}
