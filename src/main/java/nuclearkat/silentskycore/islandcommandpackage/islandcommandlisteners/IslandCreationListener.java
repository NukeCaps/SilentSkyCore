package nuclearkat.silentskycore.islandcommandpackage.islandcommandlisteners;

import nuclearkat.silentskycore.islandcommandpackage.IslandCommand;
import nuclearkat.silentskycore.islandlogic.IslandManager;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.File;

public class IslandCreationListener implements Listener {

    private final IslandManager islandManager;
    private final File schematicFile;

    public IslandCreationListener(IslandManager islandManager, File schematicFile){
        this.islandManager = islandManager;
        this.schematicFile = schematicFile;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals(ChatColor.AQUA + "Island Creation Menu") && e.getCurrentItem() != null) {
            Player player = (Player) e.getWhoClicked();
            if (e.getClick().isKeyboardClick() || e.getClick().isShiftClick() || e.getClick().isLeftClick() || e.getClick().isLeftClick()){
                e.setCancelled(true);
                switch (e.getRawSlot()) {
                    case 12:
                        IslandCommand.islandBeingCreated.add(player.getUniqueId());
                        World islandWorld = islandManager.createIsland(player.getUniqueId(), schematicFile);

                        player.sendMessage(ChatColor.RED + "Please wait a moment while your island is created!");
                        if (islandWorld != null) {
                            islandManager.teleportPlayerToIsland(player, islandWorld);
                        } else {
                            player.sendMessage(ChatColor.DARK_RED + "Island creation failed!");
                        }

                        player.sendMessage(ChatColor.WHITE + "Please enter your island name! It must be " + ChatColor.RED + "8 " + ChatColor.WHITE + "or less characters.");
                        player.closeInventory();
                        break;
                    case 14:
                        player.closeInventory();
                        break;
                    default:

                        break;
                }
            }
        }
    }

}
