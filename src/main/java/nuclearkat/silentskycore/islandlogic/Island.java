package nuclearkat.silentskycore.islandlogic;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Island {

    private Map<UUID, IslandPlayer> members;

    public Island(World islandWorld) {
        this.members = new HashMap<>();

    }

    public void addMember(UUID uuid) {
        // Logic to add a new member with default role (e.g., MEMBER)
        IslandPlayer player = new IslandPlayer(uuid, IslandRanks.GUEST);
        members.put(uuid, player);
    }

    public void promoteIslandMember(UUID uuid) {
        // Logic to promote a member to Co-Leader
        IslandPlayer player = members.get(uuid);
        Player p = (Player) player;
        if (player != null) {
            if (player.getRole() == IslandRanks.GUEST){
                player.setRole(IslandRanks.MEMBER);
            } else if (player.getRole() == IslandRanks.MEMBER){
                player.setRole(IslandRanks.MODERATOR);
            } else if (player.getRole() == IslandRanks.MODERATOR){
                player.setRole(IslandRanks.CO_LEADER);
            } else if (player.getRole() == IslandRanks.CO_LEADER) {
                p.sendMessage(ChatColor.DARK_RED + "You must transfer ownership through the /island command!");
            }
            player.setRole(IslandRanks.CO_LEADER);
        }
    }

    public void demoteIslandMember(UUID uuid) {
        // Logic to demote a Member
        IslandPlayer player = members.get(uuid);
        if (player != null) {
            if (player.getRole() == IslandRanks.CO_LEADER){
                player.setRole(IslandRanks.MODERATOR);
            } else if (player.getRole() == IslandRanks.MODERATOR) {
                player.setRole(IslandRanks.MEMBER);
            } else if (player.getRole() == IslandRanks.MEMBER) {
                player.setRole(IslandRanks.GUEST);
            }
        }
    }

}
