package nuclearkat.silentskycore.islandcommandpackage.islandMinions;

import nuclearkat.silentskycore.SilentSkyCore;
import nuclearkat.silentskycore.islandlogic.Island;
import nuclearkat.silentskycore.islandlogic.IslandManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class MinionPlacementListener implements Listener {

    private Minions minions;
    private final SilentSkyCore silentSkyCore;

    public MinionPlacementListener(SilentSkyCore silentSkyCore, Minions minions) {
        this.silentSkyCore = silentSkyCore;
        this.minions = minions;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block placedBlock = event.getBlockPlaced();
        ItemStack itemInHand = event.getItemInHand();

        if (itemInHand.getType() == Material.ARMOR_STAND && itemInHand.hasItemMeta()) {
            MinionEgg minionEgg = fromItemStack(itemInHand);

            if (minionEgg != null) {
                UUID ownerUUID = getPlayerUUID(event.getPlayer());
                IslandManager islandManager = getIslandManager();
                Island island = islandManager.getIsland(ownerUUID);

                if (island != null) {
                    if (isIslandWorld(placedBlock.getWorld(), event.getPlayer())) {
                        event.setCancelled(true);

                        ArmorStand armorStand = (ArmorStand) placedBlock.getWorld().spawnEntity(placedBlock.getLocation().add(0.5, 0, 0.5), EntityType.ARMOR_STAND);
                        armorStand.setSmall(true);
                        armorStand.setCustomNameVisible(false);
                        armorStand.setGravity(true);

                        armorStand.getEquipment().setHelmet(new ItemStack(Material.ZOMBIE_HEAD));
                        armorStand.getEquipment().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
                        armorStand.getEquipment().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
                        armorStand.getEquipment().setBoots(new ItemStack(Material.LEATHER_BOOTS));

                        Minions minion = new Minions(ownerUUID, minionEgg.getCategory(), armorStand.getLocation(), island);
                        minion.upgradeSpeed();
                        minion.upgradeQuantity();
                        minion.upgradeBackpack();
                        minion.startMinionTask();
                    } else {
                        event.setCancelled(true);
                        event.getPlayer().sendMessage(ChatColor.RED + "Minion placement failed. Make sure to place it within an island world!");
                    }
                } else {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "Minion placement failed. Make sure to place it within your island!");
                }
            }
        }
    }

    private boolean isIslandWorld(World world, Player p) {
        String expectedWorldName = "IslandWorld_" + p.getUniqueId();
        return world.getName().equals(expectedWorldName);
    }

    private MinionEgg fromItemStack(ItemStack itemStack) {
        if (itemStack.getType() == Material.ARMOR_STAND && itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();
            List<String> lore = meta.getLore();

            if (lore != null && lore.size() >= 4 && lore.get(0).equals(ChatColor.translateAlternateColorCodes('&', "&l&fMinion Details: "))) {
                MinionCategory category = MinionCategory.valueOf(ChatColor.stripColor(lore.get(1).split(":")[1].trim()));
                int speed = Integer.parseInt(ChatColor.stripColor(lore.get(2).split(":")[1].trim()));
                int quantity = Integer.parseInt(ChatColor.stripColor(lore.get(3).split(":")[1].trim()));
                int backpackSize = Integer.parseInt(ChatColor.stripColor(lore.get(4).split(":")[1].trim()));

                return new MinionEgg(category, speed, quantity, backpackSize);
            }
        }
        return null;
    }

    private UUID getPlayerUUID(Player player) {
        return player.getUniqueId();
    }

    private IslandManager getIslandManager() {
        return silentSkyCore.getIslandManagerInstance();
    }
}
