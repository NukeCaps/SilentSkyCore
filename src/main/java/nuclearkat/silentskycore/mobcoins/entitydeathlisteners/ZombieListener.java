package nuclearkat.silentskycore.mobcoins.entitydeathlisteners;

import nuclearkat.silentskycore.mobcoins.MobCoinCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Random;

public class ZombieListener implements Listener {

    private MobCoinCommand mobCoinCommand;
    private double DROP_CHANCE = 0.05;
    private int COIN_DROP_AMOUNT = 1;

    public ZombieListener(MobCoinCommand mobCoinCommand){
        this.mobCoinCommand = mobCoinCommand;
    }

    @EventHandler
    public void onZombieDeath(EntityDeathEvent e){

        if (e.getEntityType() == EntityType.ZOMBIE){
            if (new Random().nextDouble() <= DROP_CHANCE){
                Zombie zombie = (Zombie) e.getEntity();
                Player killer = zombie.getKiller();

                killer.sendMessage(ChatColor.WHITE + "You have just gained " + ChatColor.DARK_RED + COIN_DROP_AMOUNT + ChatColor.WHITE + " mobcoin!");
                mobCoinCommand.addCoinsToPlayer(killer.getUniqueId(), COIN_DROP_AMOUNT);
            }

        }


    }
}
