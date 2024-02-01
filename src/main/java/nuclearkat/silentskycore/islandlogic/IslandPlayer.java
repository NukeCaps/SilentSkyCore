package nuclearkat.silentskycore.islandlogic;

import java.util.UUID;

public class IslandPlayer {
    private UUID uuid;
    private IslandRanks role;

    public IslandPlayer(UUID uuid, IslandRanks role) {
        this.uuid = uuid;
        this.role = role;
    }
    public UUID getUuid() {
        return uuid;
    }

    public IslandRanks getRole() {
        return role;
    }

    public void setRole(IslandRanks role) {
        this.role = role;
    }
}
