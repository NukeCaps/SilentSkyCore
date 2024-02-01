package nuclearkat.silentskycore.islandcommandpackage.islandMinions;

import nuclearkat.silentskycore.SilentSkyCore;
import nuclearkat.silentskycore.islandlogic.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Field;
import java.util.*;

public class Minions extends BukkitRunnable{
        private UUID ownerUUID;
        private final MinionCategory category;
        private final Location location;
        private int speed;
        private int quantity;
        private int backpackSize;
        private Island island;
        private Inventory minionInventory;
        private BukkitTask minionTask;

        public Minions(UUID ownerUUID, MinionCategory category, Location location, Island island) {
            this.ownerUUID = ownerUUID;
            this.category = category;
            this.location = location;
            this.island = island;
            this.speed = 1;
            this.quantity = 1;
            this.backpackSize = 27;
            this.minionInventory = Bukkit.createInventory(null, backpackSize, ChatColor.AQUA + "Minion Inventory");
        }

        public void startMinionTask() {
        int interval = calculateInterval();
        int delay = 5;


        this.minionTask = this.runTaskTimer(SilentSkyCore.getPlugin(SilentSkyCore.class), delay, interval);
    }
        public void stopMinionTask() {
            if (minionTask != null && !minionTask.isCancelled()) {
                minionTask.cancel();
            }
        }

    @Override
    public void run() {
        switch (category) {
            case MINER:
                mineResources();
                break;
            case FARMER:
                farm();
                break;
            case GRINDER:
                grindMobs();
                break;
        }
    }
    private int calculateInterval(){return 20 - (speed * 5);}

    public void openMinionGUI(Player player) {
        player.openInventory(minionInventory);
    }

    private void mineResources() {
        Block targetBlock = location.getBlock();
        if (isMineableResource(targetBlock.getType())) {
            Material minedResource = targetBlock.getType();
            ItemStack getDrop = new ItemStack(minedResource);
            addItemToMinionInventory(getDrop);
        }
    }

    private boolean isMineableResource(Material material) {
        Set<Material> mineableResources = new HashSet<>(Arrays.asList(
                Material.STONE, Material.COBBLESTONE, Material.COAL_ORE,
                Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.GOLD_ORE,
                Material.IRON_ORE, Material.LAPIS_ORE, Material.REDSTONE_ORE
        ));
        return mineableResources.contains(material);
    }
    private void farm() {
        Block targetBlock = location.getBlock();

        if (targetBlock.getType() == Material.FARMLAND) {
            BlockState state = targetBlock.getState();

            if (state instanceof Farmland) {
                Farmland farmland = (Farmland) state;

                Material cropType = farmland.getMaterial();

                ItemStack harvestedCrop = getHarvestedCrop(cropType);
                addItemToMinionInventory(harvestedCrop);

                plantCrop(targetBlock, cropType);
            }
        }
    }
    private ItemStack getHarvestedCrop(Material cropType) {
        ItemStack harvestedCrop;

        switch (cropType) {
            case WHEAT:
                harvestedCrop = new ItemStack(Material.WHEAT);
                break;
            case CARROT:
                harvestedCrop = new ItemStack(Material.CARROT);
                break;
            case POTATO:
                harvestedCrop = new ItemStack(Material.POTATO);
                break;
            default:
                harvestedCrop = new ItemStack(Material.AIR);
        }
        return harvestedCrop;
    }
    private void plantCrop(Block farmland, Material cropType) {
        farmland.setType(cropType);

        BlockState state = farmland.getState();
        if (state instanceof Ageable) {
            Ageable ageable = (Ageable) state;
            ageable.setAge(0);
            farmland.getState().update();
        }
    }
    private void grindMobs() {
        for (Entity nearbyEntity : location.getWorld().getNearbyEntities(location, 5, 5, 5)) {
            if (isGrindableMob(nearbyEntity.getType())) {

                nearbyEntity.remove();

                int quantityLevel = getQuantityLevel();
                List<ItemStack> mobDrops = getMobDrops(nearbyEntity.getType(), quantityLevel);

                for (ItemStack mobDrop : mobDrops) {
                    addItemToMinionInventory(mobDrop);
                }
            }
        }
    }
    private boolean isGrindableMob(EntityType entityType) {
        Set<EntityType> grindableMobs = new HashSet<>(Arrays.asList(
                EntityType.ZOMBIE, EntityType.SKELETON, EntityType.CREEPER,
                EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.SILVERFISH,
                EntityType.ENDERMITE, EntityType.WITCH
        ));

        return grindableMobs.contains(entityType);
    }
    private List<ItemStack> getMobDrops(EntityType entityType, int quantityLevel) {
        int baseQuantity = 1;
        List<Material> mobMaterials = getMaterialsForMob(entityType);

        if (mobMaterials.isEmpty()) {
            return Collections.emptyList();
        }

        int totalQuantity = baseQuantity + quantityLevel;
        List<ItemStack> mobDrops = new ArrayList<>();
        mobDrops.add(new ItemStack(mobMaterials.get(0), totalQuantity));

        mobMaterials.subList(1, mobMaterials.size())
                .forEach(material -> mobDrops.add(new ItemStack(material, totalQuantity)));

        return mobDrops;
    }

    private List<Material> getMaterialsForMob(EntityType entityType) {
        List<Material> materials = new ArrayList<>();

        switch (entityType) {
            case ZOMBIE:
                materials.add(Material.ROTTEN_FLESH);
                break;
            case SKELETON:
                materials.add(Material.BONE);
                break;
            case CREEPER:
                materials.add(Material.GUNPOWDER);
                break;
            case SPIDER:
            case CAVE_SPIDER:
                materials.add(Material.SPIDER_EYE);
                materials.add(Material.STRING);
                break;
            case SILVERFISH:
                materials.add(Material.NETHER_STAR);
                break;
            case ENDERMITE:
                materials.add(Material.ENDER_EYE);
                break;
            case WITCH:
                materials.add(Material.SUGAR);
                materials.add(Material.GLOWSTONE_DUST);
                break;
            default:
                break;
        }

        return materials;
    }

    private int getBackpackSize(){
            return backpackSize;
    }

    private int getSpeedLevel(){
            return speed;
    }
    private int getQuantityLevel() {
        return quantity;
    }
    private void addItemToMinionInventory(ItemStack itemStack) {
        if (minionInventory.firstEmpty() != -1) {
            minionInventory.addItem(itemStack);
        }
    }
    public void upgradeSpeed() {
        upgradeIntProperty("speed", 3, 1);
    }

    public void upgradeQuantity() {
        upgradeIntProperty("quantity", 3, 1);
    }

    public void upgradeBackpack() {
        upgradeIntProperty("backpackSize", 45, 9);
    }

    private void upgradeIntProperty(String propertyName, int maxLevel, int increment) {
        try {
            Field field = getClass().getDeclaredField(propertyName);
            if (int.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);

                int value = (int) field.get(this);
                if (value < maxLevel) {
                    field.set(this, value + 1);

                    if (propertyName.equals("backpackSize")) {
                        field.set(this, value + increment);
                    }
                }

                field.setAccessible(false);
            } else {
                throw new IllegalArgumentException("Property " + propertyName + " is not of type int");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

