package nuclearkat.silentskycore.islandcommandpackage;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminGuiCommand implements CommandExecutor {
    private IslandCommand islandCommand;
    public AdminGuiCommand(IslandCommand islandCommand){
        this.islandCommand = islandCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player) sender;
        if (sender instanceof Player){
            if (p.hasPermission("ssc.island.adminmenu")){
                p.openInventory(islandCommand.adminMenu());
            }
        }
        return false;
    }
}
