package nuclearkat.silentskycore.islandlogic;

import com.sk89q.worldedit.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Chest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

import java.util.*;

public class IslandManager implements Listener {
    private Map<UUID, Island> islands;
    private static IslandManager instance;

    private IslandManager() {
        this.islands = new HashMap<>();
    }

    public static IslandManager getInstance() {
        synchronized (IslandManager.class) {
            if (instance == null) {
                instance = new IslandManager();
            }
        }
        return instance;
    }

    public World createIsland(UUID playerId, File schematicFile) {
        try (ClipboardReader reader = ClipboardFormats.findByFile(schematicFile).getReader(Files.newInputStream(schematicFile.toPath()))) {
            Clipboard clipboard = reader.read();
            BlockVector3 chestLocationInSchematic = BlockVector3.at(-6, 68, -5);
            List<ItemStack> defaultItems = getDefaultItems();

            World islandWorld = Bukkit.createWorld(new WorldCreator("IslandWorld_" + playerId.toString())
                    .environment(World.Environment.NORMAL)
                    .generateStructures(false)
                    .generator(new ChunkGenerator()));

            if (islandWorld != null) {
                WorldBorder worldBorder = islandWorld.getWorldBorder();
                int chunkSize = 3;
                int borderSize = chunkSize * 16;
                worldBorder.setSize(borderSize);
                worldBorder.setCenter(-2, -5);

                islandWorld.setSpawnLocation(-2, 69, -5);
                islandWorld.getChunkAt(new Location(islandWorld, 0, 64, 0)).load();

                BlockVector3 pastePosition = BlockVector3.at(0, 64, 0);
                pasteSchematic(islandWorld, clipboard, pastePosition);

                Location chestLocation = new Location(islandWorld, -6, 68, -5);
                Block chestBlock = chestLocation.getBlock();

                if (chestBlock.getType() == Material.CHEST) {
                    Chest chest = (Chest) chestBlock.getState();
                    Inventory inventory = chest.getInventory();
                    inventory.clear();
                    defaultItems.forEach(inventory::addItem);
                } else {
                    System.out.println("Chest not in the correct location in schematic!");
                }

                islands.put(playerId, new Island(islandWorld));
            } else {
                System.out.println("Failed to create the island world!");
            }

            return islandWorld;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Island creation failed!");
            return null;
        }
    }

    private void pasteSchematic(World world, Clipboard clipboard, BlockVector3 pastePosition) {
            try (EditSession pasteSession = WorldEdit.getInstance().newEditSession(new BukkitWorld(world))) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(pasteSession)
                        .to(pastePosition)
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            } catch (WorldEditException e) {
                e.printStackTrace();
            }
    }

    private List<ItemStack> getDefaultItems() {
            List<ItemStack> defaultItems = new ArrayList<>();
            defaultItems.add(new ItemStack(Material.STONE_SWORD));
            defaultItems.add(new ItemStack(Material.STONE_AXE));
            defaultItems.add(new ItemStack(Material.STONE_PICKAXE));
            defaultItems.add(new ItemStack(Material.STONE_SHOVEL));
            defaultItems.add(new ItemStack(Material.STONE_HOE));
            defaultItems.add(new ItemStack(Material.WATER_BUCKET));
            defaultItems.add(new ItemStack(Material.LAVA_BUCKET));
            defaultItems.add(new ItemStack(Material.LEATHER_HELMET));
            defaultItems.add(new ItemStack(Material.LEATHER_CHESTPLATE));
            defaultItems.add(new ItemStack(Material.LEATHER_LEGGINGS));
            defaultItems.add(new ItemStack(Material.LEATHER_BOOTS));
            defaultItems.add(new ItemStack(Material.OAK_SAPLING));
            defaultItems.add(new ItemStack(Material.SAND, 16));
            defaultItems.add(new ItemStack(Material.CACTUS, 16));
            defaultItems.add(new ItemStack(Material.WHEAT_SEEDS, 16));
            defaultItems.add(new ItemStack(Material.BONE_MEAL, 16));
            return defaultItems;
    }

    public void deleteIsland(UUID playerId) {
            islands.remove(playerId);
    }

    public Island getIsland(UUID playerId) {
            return islands.get(playerId);
    }

    public void teleportPlayerToIsland(Player p, World island) {
        Location spawnLocation = island.getSpawnLocation();
        p.teleport(spawnLocation);
    }
}
