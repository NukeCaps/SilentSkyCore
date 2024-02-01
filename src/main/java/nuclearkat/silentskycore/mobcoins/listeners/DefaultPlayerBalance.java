package nuclearkat.silentskycore.mobcoins.listeners;

import nuclearkat.silentskycore.mobcoins.MobCoinCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class DefaultPlayerBalance implements Listener {

    private MobCoinCommand mobCoinCommand;

    public DefaultPlayerBalance(MobCoinCommand mobCoinCommand){
        this.mobCoinCommand = mobCoinCommand;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if (!e.getPlayer().hasPlayedBefore()){
            mobCoinCommand.setDefaultCoinBalance(p.getUniqueId(), 0);

        }

    }
}
