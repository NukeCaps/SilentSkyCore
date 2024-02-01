package nuclearkat.silentskycore.mobcoins.listeners;

import nuclearkat.silentskycore.mobcoins.MobCoinCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MobCoinShopListener implements Listener {

    private MobCoinCommand mobCoinCommand;

    public MobCoinShopListener(MobCoinCommand mobCoinCommand){
        this.mobCoinCommand = mobCoinCommand;

    }


    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(ChatColor.DARK_AQUA + "MobCoin Shop")){
            e.setCancelled(true);
            int getClickedSlot = e.getRawSlot();
            switch (getClickedSlot){
                case 10:
                    if (mobCoinCommand.getPlayerCoins(p.getUniqueId()) >= 50){
                        int coinsToRemove = 50;

                        p.closeInventory();
                        p.openInventory(mobCoinCommand.mobCoinInventory(p));
                        p.getInventory().addItem(mobCoinCommand.testItem());
                        mobCoinCommand.removeCoinsFromPlayer(p.getUniqueId(), coinsToRemove);
                    }
                    break;
                case 35:
                    p.closeInventory();
                    break;
            }

        }

    }
}
