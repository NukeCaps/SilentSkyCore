package nuclearkat.silentskycore.mobcoins.entitydeathlisteners;

import nuclearkat.silentskycore.mobcoins.MobCoinCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Random;

public class BlazeListener implements Listener {

    private MobCoinCommand mobCoinCommand;
    private double DROP_CHANCE = 0.1;
    private int COIN_DROP_AMOUNT = 1;

    public BlazeListener(MobCoinCommand mobCoinCommand){
        this.mobCoinCommand = mobCoinCommand;
    }

    @EventHandler
    public void onBlazeDeath(EntityDeathEvent e){

        if (e.getEntityType() == EntityType.BLAZE){
            if (new Random().nextDouble() <= DROP_CHANCE){
                Blaze blaze = (Blaze) e.getEntity();
                Player killer = blaze.getKiller();

                killer.sendMessage(ChatColor.WHITE + "You have just gained " + ChatColor.DARK_RED + COIN_DROP_AMOUNT + ChatColor.WHITE + " mobcoin!");
                mobCoinCommand.addCoinsToPlayer(killer.getUniqueId(), COIN_DROP_AMOUNT);
            }

        }


    }

}
