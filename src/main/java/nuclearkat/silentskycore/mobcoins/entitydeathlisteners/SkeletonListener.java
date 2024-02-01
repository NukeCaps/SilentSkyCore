package nuclearkat.silentskycore.mobcoins.entitydeathlisteners;

import nuclearkat.silentskycore.mobcoins.MobCoinCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Random;

public class SkeletonListener implements Listener {
    private MobCoinCommand mobCoinCommand;
    private double DROP_CHANCE = 0.05;
    private int COIN_DROP_AMOUNT = 1;

    public SkeletonListener(MobCoinCommand mobCoinCommand){
        this.mobCoinCommand = mobCoinCommand;
    }

    @EventHandler
    public void onSkeletonDeath(EntityDeathEvent e){

        if (e.getEntityType() == EntityType.SKELETON){
            if (new Random().nextDouble() <= DROP_CHANCE){
                Skeleton skeleton = (Skeleton) e.getEntity();
                Player killer = skeleton.getKiller();

                killer.sendMessage(ChatColor.WHITE + "You have just gained " + ChatColor.DARK_RED + COIN_DROP_AMOUNT + ChatColor.WHITE + " mobcoin!");
                mobCoinCommand.addCoinsToPlayer(killer.getUniqueId(), COIN_DROP_AMOUNT);
            }

        }


    }

}
