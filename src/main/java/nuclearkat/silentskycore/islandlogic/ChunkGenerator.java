package nuclearkat.silentskycore.islandlogic;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChunkGenerator extends org.bukkit.generator.ChunkGenerator {

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        // Return an empty list, as we don't want any additional block populators.
        return new ArrayList<>();
    }

    @Override
    public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkGenerator.ChunkData chunkData = createChunkData(world);

        // Fill the chunkData with air blocks
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < world.getMaxHeight(); y++) {
                    chunkData.setBlock(x, y, z, Material.AIR.createBlockData());
                }
            }
        }

        return chunkData;
    }
}
