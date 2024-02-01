package nuclearkat.silentskycore.islandcommandpackage.islandcommandlisteners.islandmenulisteners;

import nuclearkat.silentskycore.SilentSkyCore;
import nuclearkat.silentskycore.islandcommandpackage.IslandCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;

public class IslandMenuListener implements Listener {
    // Listener for the Island Menu
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(ChatColor.AQUA + "Island Menu")) {
            if (e.getClick().isKeyboardClick() || e.getClick().isShiftClick() || e.getClick().isLeftClick() || e.getClick().isLeftClick()){
                e.setCancelled(true);
                // Switch statement to implement clicking functionality of island menu buttons
                switch (e.getRawSlot()) {
                    case 10:
                        // CLICK LOGIC
                        p.openInventory(islandSettings());
                        break;
                    case 11:
                        p.openInventory(islandMembers());
                        break;
                    case 12:
                        p.openInventory(islandMinions());
                    default:
                        return;
                }
            }
            }
    }
    // The following sections will be inventories for the clicked buttons in island menu gui
    public static Inventory islandSettings(){
        Inventory islandSettings = Bukkit.createInventory(null, 27, ChatColor.AQUA + "Island Settings");

        ItemStack memberPermissions = new ItemStack(Material.PAPER);
        ItemStack visitPermissions = new ItemStack(Material.OAK_HANGING_SIGN);
        ItemStack keepInventory = new ItemStack(Material.BONE);
        ItemStack transferLeadership = new ItemStack(Material.CAULDRON);

        ItemMeta permissionsMeta = memberPermissions.getItemMeta();
        ItemMeta visitStatusMeta = visitPermissions.getItemMeta();
        ItemMeta keepInventoryMeta = keepInventory.getItemMeta();
        ItemMeta transferLeadershipMeta = transferLeadership.getItemMeta();

        if (permissionsMeta != null){
            permissionsMeta.setDisplayName(ChatColor.AQUA + "Permissions");
            List<String> permissionsLore = new ArrayList<>();
            permissionsLore.add(ChatColor.GRAY + "Click to edit rank permissions on your island");
            permissionsMeta.setLore(permissionsLore);
            memberPermissions.setItemMeta(permissionsMeta);
        }
        PersistentDataContainer permissionsContainer = memberPermissions.getItemMeta().getPersistentDataContainer();
        NamespacedKey permissionsKey = new NamespacedKey(SilentSkyCore.getPlugin(SilentSkyCore.class), "permissions_key");

        if (visitStatusMeta != null){
            visitStatusMeta.setDisplayName(ChatColor.AQUA + "Visitor Status");
            List<String> visitStatusLore = new ArrayList<>();
            visitStatusLore.add(ChatColor.GRAY + "Click to edit visitor permissions");
            visitStatusMeta.setLore(visitStatusLore);
            visitPermissions.setItemMeta(visitStatusMeta);
        }
        PersistentDataContainer visitStatusContainer = visitPermissions.getItemMeta().getPersistentDataContainer();
        NamespacedKey visitStatusKey = new NamespacedKey(SilentSkyCore.getPlugin(SilentSkyCore.class), "visit_status_key");

        if (keepInventoryMeta != null){
            keepInventoryMeta.setDisplayName(ChatColor.AQUA + "Keep Inventory");
            List<String> keepInventoryLore = new ArrayList<>();
            keepInventoryLore.add(ChatColor.GRAY + "Click to edit whether or not you keep inventory on your island");
            keepInventoryLore.add(ChatColor.RED + "(!) " + ChatColor.WHITE + ChatColor.BOLD + "Recommended to keep true!");
            keepInventoryMeta.setLore(keepInventoryLore);
            keepInventory.setItemMeta(keepInventoryMeta);
        }
        PersistentDataContainer keepInventoryContainer = keepInventory.getItemMeta().getPersistentDataContainer();
        NamespacedKey keepInventoryKey = new NamespacedKey(SilentSkyCore.getPlugin(SilentSkyCore.class), "keep_inventory_key");

        if (transferLeadershipMeta != null){
            transferLeadershipMeta.setDisplayName(ChatColor.AQUA + "Transfer Leadership");
            List<String> transferLeadershipLore = new ArrayList<>();
            transferLeadershipLore.add(ChatColor.GRAY + "Click to transfer leadership of the island!");
            transferLeadershipLore.add(ChatColor.RED + "Can only be done by the current leader!");
            transferLeadershipMeta.setLore(transferLeadershipLore);
            transferLeadership.setItemMeta(transferLeadershipMeta);
        }
        PersistentDataContainer transferLeadershipContainer = transferLeadershipMeta.getPersistentDataContainer();
        NamespacedKey transferLeadershipKey = new NamespacedKey(SilentSkyCore.getPlugin(SilentSkyCore.class), "transfer_leadership_key");

        for (int i = 0; i < islandSettings.getSize(); i++){
            switch (i) {
                case 10:
                    islandSettings.setItem(10, memberPermissions);
                    break;
                case 11:
                    islandSettings.setItem(11, visitPermissions);
                    break;
                case 12:
                    islandSettings.setItem(12, keepInventory);
                    break;
                case 13:
                    islandSettings.setItem(13, transferLeadership);
                    break;
                default:
                    islandSettings.setItem(i, IslandCommand.border());
                    break;
            }
        }
        return islandSettings;
    }
    public static Inventory islandMembers(){
        Inventory islandMembers = Bukkit.createInventory(null, 36, ChatColor.AQUA + "Island Members");



        return islandMembers;
    }
    public static Inventory islandMinions(){
        Inventory islandMinions = Bukkit.createInventory(null, 36, ChatColor.AQUA + "Island Minions");

        ItemStack viewPlacedMinions = new ItemStack(Material.DRAGON_HEAD);
        ItemStack viewAvailableMinions = new ItemStack(Material.CREEPER_HEAD);
        ItemStack movePlacedMinion = new ItemStack(Material.ZOMBIE_HEAD);


        return islandMinions;
    }
}
