package nuclearkat.silentskycore.islandcommandpackage;

import nuclearkat.silentskycore.SilentSkyCore;
import nuclearkat.silentskycore.islandcommandpackage.islandMinions.MinionCategory;
import nuclearkat.silentskycore.islandcommandpackage.islandMinions.MinionEgg;
import nuclearkat.silentskycore.islandcommandpackage.islandcommandlisteners.IslandNamingListener;
import nuclearkat.silentskycore.islandlogic.Island;
import nuclearkat.silentskycore.islandlogic.IslandManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IslandCommand implements CommandExecutor {

    public static ArrayList<UUID> islandDeletion = new ArrayList<>();
    public static ArrayList<UUID> islandIsCreated = new ArrayList<>();
    public static ArrayList<UUID> islandBeingCreated = new ArrayList<>();
    public static ArrayList<UUID> invitedToIsland = new ArrayList<>();

    private final IslandManager islandManager;
    public IslandCommand(IslandManager islandManager){
        this.islandManager = islandManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if (player.hasPermission("ssc.islandmenu.use")) {
            handleUserCommand(player, args);
        } else {
            player.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
        }

        return false;
    }

    private void handleUserCommand(Player player, String[] args) {
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "invite":
                    handleInviteCommand(player, args);
                    break;
                case "accept":
                    handleAcceptCommand(player, args);
                    break;
                case "deny":
                    handleDenyCommand(player, args);
                    break;
                case "delete":
                    handleDeleteCommand(player, args);
                default:
                    break;
            }
        } else {
            handleNoArguments(player);
        }
    }

    private void handleInviteCommand(Player player, String[] args) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                if (player.getUniqueId() != target.getUniqueId()) {
                    target.sendMessage(ChatColor.WHITE + "You have just been invited to " + ChatColor.DARK_RED + player.getDisplayName() + ChatColor.WHITE + "'s island! " +
                            "Please use " + ChatColor.DARK_RED + "/island accept <name>" + ChatColor.WHITE + " or " + ChatColor.DARK_RED + "/island deny <name>");
                    invitedToIsland.add(target.getUniqueId());
                } else {
                    player.sendMessage(ChatColor.RED + "You cannot invite yourself!");
                }
            } else {
                player.sendMessage(ChatColor.RED + "Player not found!");
            }
        }
    }

    private void handleAcceptCommand(Player player, String[] args) {
        if (args.length == 2) {
            if (invitedToIsland.contains(player.getUniqueId())) {
                Player inviter = Bukkit.getPlayer(args[1]);
                if (inviter != null) {
                    Island inviterIsland = islandManager.getIsland(inviter.getUniqueId());
                    if (inviterIsland != null) {
                        inviterIsland.addMember(player.getUniqueId());
                        player.sendMessage(ChatColor.WHITE + "You have just joined " + ChatColor.DARK_RED + inviter.getDisplayName() + ChatColor.WHITE + "'s island!");
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&cNo island available to join!"));
                    }
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&cInviter not found!"));
                }
                invitedToIsland.remove(player.getUniqueId());
            }
        }
    }

    private void handleDenyCommand(Player player, String[] args) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (!invitedToIsland.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&cYou have not been invited to an island!"));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&lYou have just denied the island invite!"));
                if (target != null) {
                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&c" + player.getDisplayName() + " &l&fHas not accepted your island invite!"));
                }
            }
        }
    }

    private void handleDeleteCommand(Player player, String[] args) {
        if (islandManager.getIsland(player.getUniqueId()) != null) {
            if (islandDeletion.contains(player.getUniqueId())) {
                islandManager.deleteIsland(player.getUniqueId());
                islandDeletion.remove(player.getUniqueId());
                islandIsCreated.remove(player.getUniqueId());
                IslandNamingListener.islandName.remove(player.getUniqueId());
                player.sendMessage(ChatColor.DARK_RED + "You have just deleted your island!");
            } else if (!islandDeletion.contains(player.getUniqueId())) {
                islandDeletion.add(player.getUniqueId());
                player.sendMessage(ChatColor.DARK_RED + "You are about to delete your island! To confirm, please repeat the command!");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You do not have an island!");
        }
    }

    private void handleNoArguments(Player player) {
        if (!islandIsCreated.contains(player.getUniqueId())) {
            player.openInventory(creationMenu());
        } else {
            player.openInventory(islandMenu());
        }
    }

    public static Inventory creationMenu() {
        Inventory creationMenu = Bukkit.createInventory(null, 27, ChatColor.AQUA + "Island Creation Menu");

        ItemStack islandCreate = new ItemStack(Material.GREEN_WOOL);
        ItemStack cancelButton = new ItemStack(Material.RED_WOOL);

        ItemMeta islandCreateMeta = islandCreate.getItemMeta();
        ItemMeta cancelButtonMeta = cancelButton.getItemMeta();

        if (cancelButtonMeta != null){
            cancelButtonMeta.setDisplayName(ChatColor.WHITE + "Click to " + ChatColor.RED + "cancel");
            List<String> cancelButtonLore = new ArrayList<>();
            cancelButtonLore.add(ChatColor.GRAY + "Close this menu");
            cancelButtonMeta.setLore(cancelButtonLore);
            cancelButton.setItemMeta(cancelButtonMeta);
        }
        PersistentDataContainer cancelButtonContainer = cancelButton.getItemMeta().getPersistentDataContainer();
        NamespacedKey cancelButtonKey = new NamespacedKey(SilentSkyCore.getPlugin(SilentSkyCore.class), "cancel_button");

        if (islandCreateMeta != null){
            islandCreateMeta.setDisplayName(ChatColor.WHITE + "Click to " + ChatColor.GREEN + "create " + ChatColor.WHITE + "an island!");
            List<String> islandCreateLore = new ArrayList<>();
            islandCreateLore.add(ChatColor.GRAY + "Create a new island!");
            islandCreateMeta.setLore(islandCreateLore);
            islandCreate.setItemMeta(islandCreateMeta);
        }
        PersistentDataContainer islandCreateContainer = islandCreate.getItemMeta().getPersistentDataContainer();
        NamespacedKey islandCreateKey = new NamespacedKey(SilentSkyCore.getPlugin(SilentSkyCore.class), "create_island");

        for (int i = 0; i < creationMenu.getSize(); i++){

            switch (i) {
                case 12:
                    creationMenu.setItem(12, islandCreate);
                    break;
                case 14:
                    creationMenu.setItem(14, cancelButton);
                    break;
                default:
                    creationMenu.setItem(i, border());
                    break;

            }

        }
        return creationMenu;
    }
    public static Inventory islandMenu() {
        Inventory islandMenu = Bukkit.createInventory(null, 36, ChatColor.AQUA + "Island Menu");

        ItemStack islandSettings = new ItemStack(Material.PAPER);
        ItemStack islandMembers = new ItemStack(Material.IRON_INGOT);
        ItemStack islandMinions = new ItemStack(Material.STICK);

        ItemStack placeholder = new ItemStack(Material.DIAMOND);

        ItemMeta islandSettingsMeta = islandSettings.getItemMeta();
        ItemMeta islandMembersMeta = islandMembers.getItemMeta();
        ItemMeta islandMinionsMeta = islandMinions.getItemMeta();

        if (islandMinionsMeta != null){
            islandMinionsMeta.setDisplayName(ChatColor.AQUA + "Island Minions");
            List<String> islandMinionLore = new ArrayList<>();
            islandMinionLore.add(ChatColor.GRAY + "Click to edit / view island minions");
            islandMinionsMeta.setLore(islandMinionLore);
            islandMinions.setItemMeta(islandMinionsMeta);
        }
        PersistentDataContainer islandMinionContainer = islandMinions.getItemMeta().getPersistentDataContainer();
        NamespacedKey islandMinionKey = new NamespacedKey(SilentSkyCore.getPlugin(SilentSkyCore.class), "island_minion_key");

        if (islandMembersMeta != null){
            islandMembersMeta.setDisplayName(ChatColor.AQUA + "Island Members");
            List<String> islandMembersLore = new ArrayList<>();
            islandMembersLore.add(ChatColor.GRAY + "Click to edit / view island members");
            islandMembersMeta.setLore(islandMembersLore);
            islandMembers.setItemMeta(islandMembersMeta);
        }
        PersistentDataContainer islandMemeberContainer = islandMembers.getItemMeta().getPersistentDataContainer();
        NamespacedKey islandMemberKey = new NamespacedKey(SilentSkyCore.getPlugin(SilentSkyCore.class), "island_member_key");

        if (islandSettingsMeta != null){
            islandSettingsMeta.setDisplayName(ChatColor.AQUA + "Island Settings");
            List<String> islandSettingsLore = new ArrayList<>();
            islandSettingsLore.add(ChatColor.GRAY + "Click to edit island settings!");
            islandSettingsMeta.setLore(islandSettingsLore);
            islandSettings.setItemMeta(islandSettingsMeta);
        }
        PersistentDataContainer islandSettingsContainer = islandSettings.getItemMeta().getPersistentDataContainer();
        NamespacedKey islandSettingsKey = new NamespacedKey(SilentSkyCore.getPlugin(SilentSkyCore.class), "island_settings_key");

        for (int i = 0; i < islandMenu.getSize(); i++){
            switch (i){

                case 10:
                    islandMenu.setItem(10, islandSettings);
                    break;
                case 11:
                    islandMenu.setItem(11, islandMembers);
                    break;
                case 12:
                    islandMenu.setItem(12, islandMinions);
                    break;
                case 13:
                    islandMenu.setItem(13, placeholder);
                    break;
                case 14:
                    islandMenu.setItem(14, placeholder);
                    break;
                case 15:
                    islandMenu.setItem(15, placeholder);
                    break;
                case 16:
                    islandMenu.setItem(16, placeholder);
                    break;
                case 19:
                    islandMenu.setItem(19, placeholder);
                    break;
                case 20:
                    islandMenu.setItem(20, placeholder);
                    break;
                case 21:
                    islandMenu.setItem(21, placeholder);
                    break;
                case 22:
                    islandMenu.setItem(22, closeButton());
                    break;
                case 23:
                    islandMenu.setItem(23, placeholder);
                    break;
                case 24:
                    islandMenu.setItem(24, placeholder);
                    break;
                case 25:
                    islandMenu.setItem(25, placeholder);
                    break;
                default:
                    islandMenu.setItem(i, border());
                    break;
            }
        }
        return islandMenu;
    }
    public Inventory adminMenu() {
        Inventory adminMenu = Bukkit.createInventory(null, 36, ChatColor.AQUA + "Admin Menu");

        MinionEgg minerEgg = new MinionEgg(MinionCategory.MINER, 1, 1, 1);
        MinionEgg farmerEgg = new MinionEgg(MinionCategory.FARMER, 1, 1, 1);
        MinionEgg grinderEgg = new MinionEgg(MinionCategory.GRINDER, 1, 1, 1);

        adminMenu.setItem(0, minerEgg);
        adminMenu.setItem(1, farmerEgg);
        adminMenu.setItem(2, grinderEgg);

        return adminMenu;
        }

        public static ItemStack closeButton(){
        ItemStack closeButton = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta closeButtonMeta = closeButton.getItemMeta();
        if (closeButtonMeta != null){
            closeButtonMeta.setDisplayName(ChatColor.WHITE + "Click to " + ChatColor.RED + "close " + ChatColor.WHITE + "this menu!");
            closeButton.setItemMeta(closeButtonMeta);
        }
        PersistentDataContainer closeButtonContainer = closeButton.getItemMeta().getPersistentDataContainer();
        NamespacedKey closeButtonKey = new NamespacedKey(SilentSkyCore.getPlugin(SilentSkyCore.class), "close_button_key");

        return closeButton;
        }
        public static ItemStack border(){
        ItemStack border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta borderMeta = border.getItemMeta();

        if (borderMeta != null){
            borderMeta.setDisplayName(" ");
            border.setItemMeta(borderMeta);
        }
        PersistentDataContainer borderContainer = border.getItemMeta().getPersistentDataContainer();
        NamespacedKey borderKey = new NamespacedKey(SilentSkyCore.getPlugin(SilentSkyCore.class), "boarder_key");
        return border;
    }
    public static ItemStack backButton(){
        ItemStack backButton = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta backButtonMeta = backButton.getItemMeta();
        if (backButtonMeta != null){
            backButtonMeta.setDisplayName(ChatColor.WHITE + "Click to " + ChatColor.RED + "return " + ChatColor.WHITE + "to the previous menu");
            backButton.setItemMeta(backButtonMeta);
        }
        PersistentDataContainer backButtonContainer = backButton.getItemMeta().getPersistentDataContainer();
        NamespacedKey backButtonKey = new NamespacedKey(SilentSkyCore.getPlugin(SilentSkyCore.class), "back_button_key");


        return backButton;
    }
}