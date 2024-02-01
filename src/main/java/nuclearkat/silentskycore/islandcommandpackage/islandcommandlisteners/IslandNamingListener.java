package nuclearkat.silentskycore.islandcommandpackage.islandcommandlisteners;

import nuclearkat.silentskycore.islandcommandpackage.IslandCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.UUID;

public class IslandNamingListener implements Listener {

    // Map for getting specified players island name
    public static HashMap<UUID, String> islandName = new HashMap<>();

    // Listener for the chat prompt when creating an island name
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        /* Check for islandBeingCreated list containing players uuid (The reason we do this is to give the player access to
         the correct gui based on whether they have created an island or not */
        if (IslandCommand.islandBeingCreated.contains(p.getUniqueId())) {
            String message = e.getMessage();
            e.setCancelled(true);
            // Check for the message length in individual characters to mark a "max" character limit in the name of your island
            if (message.length() <= 8) {
                // The method for setting a player as the owner of an island + giving that island a name
                islandName.put(p.getUniqueId(), message);
                p.sendMessage(ChatColor.WHITE + "You have just named your island: " + ChatColor.GREEN + message);

                // The method for giving player access to the correct gui
                IslandCommand.islandBeingCreated.remove(p.getUniqueId());
                IslandCommand.islandIsCreated.add(p.getUniqueId());
            } else {
                p.sendMessage(ChatColor.DARK_RED + "You may only enter a name with 8 or less characters!");
            }
        }
    }
}