package nuclearkat.silentskycore.islandcommandpackage.islandMinions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MinionEgg extends ItemStack {
    private MinionCategory category;
    private int speed;
    private int quantity;
    private int backpackSize;

    public MinionEgg(MinionCategory category, int speed, int quantity, int backpackSize) {
        super(Material.ARMOR_STAND);
        this.category = category;
        this.speed = speed;
        this.quantity = quantity;
        this.backpackSize = backpackSize;

        updateMeta();
    }

    public MinionCategory getCategory() {
        return category;
    }

    public int getSpeed() {
        return speed;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getBackpackSize() {
        return backpackSize;
    }

    private void updateMeta() {
        ItemMeta meta = getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + getCategory().toString() + " Minion");
            List<String> minionLore = new ArrayList<>();
            minionLore.add(ChatColor.translateAlternateColorCodes('&', "&l&fMinion Details: "));
            minionLore.add(ChatColor.GRAY + "Speed: " + getSpeed());
            minionLore.add(ChatColor.GRAY + "Quantity: " + getQuantity());
            minionLore.add(ChatColor.GRAY + "Backpack Size: " + getBackpackSize());
            meta.setLore(minionLore);
            setItemMeta(meta);
        }
    }
}