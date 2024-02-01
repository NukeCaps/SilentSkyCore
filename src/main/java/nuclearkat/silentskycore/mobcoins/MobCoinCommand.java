package nuclearkat.silentskycore.mobcoins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MobCoinCommand implements CommandExecutor {

    final Map<UUID, Integer> playerCoins = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        //regular player permission check
        if (p.hasPermission("mobcoins.gui.use")) {
            //check for the args length
            if (args.length == 1) {
                //check for what args[0] is equal to
                if (args[0].equalsIgnoreCase("shop")) {
                    p.openInventory(mobCoinInventory(p));
                    //checks for whether the command is /mb shop or /mb balance
                } else if (args[0].equalsIgnoreCase("balance")) {
                    int playerCoins = getPlayerCoins(p.getUniqueId());
                    p.sendMessage(ChatColor.WHITE + "You currently have " + ChatColor.DARK_RED + playerCoins + ChatColor.WHITE + "mobcoins!");
                }
            } else if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Use /mb shop and /mb balance!");
            }
        }
        //admin usage / permission check

        if (p.hasPermission("mobcoins.gui.*")) {
            //check for the args length
            if (args.length == 4) {
                //check for what args[0,1] are equal to
                if (args[0].equalsIgnoreCase("balance") && args[1].equalsIgnoreCase("set")) {
                    String targetPlayerName = args[2];
                    Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                    if (targetPlayer == null || !targetPlayer.isOnline()){
                        p.sendMessage(ChatColor.DARK_RED + "Player not found / isn't online!");
                    }

                    UUID targetUUID = targetPlayer.getUniqueId();
                    //functionality of /mb balance set (int)
                    try {
                        int coinsToSet = Integer.parseInt(args[3]);
                        if (coinsToSet < 0){
                            p.sendMessage(ChatColor.DARK_RED + "You have to enter a positive integer value!");
                        }

                        p.sendMessage(ChatColor.WHITE + "You have just set " + ChatColor.DARK_RED + targetPlayer.getDisplayName() + "'s " + ChatColor.WHITE + "balance to " + ChatColor.DARK_RED + coinsToSet);
                        setCoinsToPlayer(targetUUID, coinsToSet);
                    } catch (NumberFormatException e) {
                        p.sendMessage(ChatColor.DARK_RED + "You need to enter an integer value!");
                        e.printStackTrace();
                    }

                    //check for what args[0,1] are equal to
                } else if (args[0].equalsIgnoreCase("balance") && args[1].equalsIgnoreCase("add")) {
                    //functionality of /mb balance add (int)
                    String targetPlayerName = args[2];
                    Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                    if (targetPlayer == null || !targetPlayer.isOnline()){
                        p.sendMessage(ChatColor.DARK_RED + "Player is not found / isn't online!");
                    }
                    UUID targetUUID = targetPlayer.getUniqueId();
                    try {
                        int coinsToAdd = Integer.parseInt(args[3]);
                        p.sendMessage(ChatColor.WHITE + "You have just added " + ChatColor.DARK_RED + coinsToAdd + ChatColor.WHITE + " to " + ChatColor.WHITE + targetPlayer.getDisplayName() + "'s balance!");
                        addCoinsToPlayer(targetUUID, coinsToAdd);
                    } catch (NumberFormatException e) {
                        p.sendMessage(ChatColor.DARK_RED + "You need to enter an integer value!");
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    //Inventory method used for the creation of mobcoin shop
    public Inventory mobCoinInventory(Player p) {

        int playerCoins = getPlayerCoins(p.getUniqueId());

        Inventory mobCoinGui = Bukkit.createInventory(null, 36, ChatColor.DARK_AQUA + "MobCoin Shop");

        ItemStack coinBalance = new ItemStack(Material.GHAST_TEAR);
        ItemMeta coinBalanceMeta = coinBalance.getItemMeta();
        coinBalanceMeta.setDisplayName(ChatColor.WHITE + "Your coin balance: " + ChatColor.DARK_RED + playerCoins);
        coinBalance.setItemMeta(coinBalanceMeta);



        mobCoinGui.setItem(10, testItem());
        mobCoinGui.setItem(35, coinBalance);

        return mobCoinGui;
    }

    public static ItemStack testItem(){
        ItemStack testItem = new ItemStack(Material.DIAMOND);
        ItemMeta testItemMeta = testItem.getItemMeta();
        testItemMeta.setDisplayName(ChatColor.DARK_RED + "TEST ITEM");
        testItem.setItemMeta(testItemMeta);
        return testItem;
    }

    //get the player's coins method
    public int getPlayerCoins(UUID playerUUID) {
        return playerCoins.get(playerUUID);
    }

    //set the player's coins method
    public void setCoinsToPlayer(UUID playerUUID, int amount){
        playerCoins.put(playerUUID, amount);

    }
    //add coins to the player's account method
    public void addCoinsToPlayer(UUID playerUUID, int amount) {
        int currentCoins = getPlayerCoins(playerUUID);
        playerCoins.put(playerUUID, currentCoins + amount);

    }

    //remove coins from the player's account method
    public void removeCoinsFromPlayer(UUID playerUUID, int amount) {
        Player p = Bukkit.getPlayer(playerUUID);
        int currentCoins = getPlayerCoins(playerUUID);
        if (currentCoins >= amount) {
            playerCoins.put(playerUUID, currentCoins - amount);
        } else if (currentCoins < amount){
            p.sendMessage(ChatColor.DARK_RED + "You do not have enough mob coins to purchase this! " + ChatColor.WHITE + "Your current balance is " + ChatColor.DARK_RED + getPlayerCoins(p.getUniqueId()));
        }
    }
    //method to set the default amount of coins
    public void setDefaultCoinBalance(UUID playerUUID, int defaultBalance) {
        if (!playerCoins.containsKey(playerUUID)) {
            playerCoins.put(playerUUID, defaultBalance);
        }
    }
}
