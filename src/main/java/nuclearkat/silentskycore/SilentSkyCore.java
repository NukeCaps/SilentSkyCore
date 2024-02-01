package nuclearkat.silentskycore;

import nuclearkat.silentskycore.islandcommandpackage.AdminGuiCommand;
import nuclearkat.silentskycore.islandcommandpackage.IslandCommand;
import nuclearkat.silentskycore.islandcommandpackage.islandcommandlisteners.AdminMenuListener;
import nuclearkat.silentskycore.islandcommandpackage.islandcommandlisteners.IslandCreationListener;
import nuclearkat.silentskycore.islandcommandpackage.islandcommandlisteners.islandmenulisteners.IslandMenuListener;
import nuclearkat.silentskycore.islandcommandpackage.islandcommandlisteners.IslandNamingListener;
import nuclearkat.silentskycore.islandcommandpackage.islandcommandlisteners.islandmenulisteners.IslandSettingsListener;
import nuclearkat.silentskycore.islandlogic.IslandManager;
import nuclearkat.silentskycore.mobcoins.MobCoinCommand;
import nuclearkat.silentskycore.mobcoins.entitydeathlisteners.BlazeListener;
import nuclearkat.silentskycore.mobcoins.entitydeathlisteners.SkeletonListener;
import nuclearkat.silentskycore.mobcoins.entitydeathlisteners.ZombieListener;
import nuclearkat.silentskycore.mobcoins.listeners.DefaultPlayerBalance;
import nuclearkat.silentskycore.mobcoins.listeners.MobCoinShopListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SilentSkyCore extends JavaPlugin {
    private IslandManager islandManager;
    private MobCoinCommand mobCoinCommand;
    @Override
    public void onEnable() {

        // Skyblock core
        File dataFolder = new File(getDataFolder(), "SilentSkyCore");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File schematicFile = new File(dataFolder, "skyblock_island.schem");

        islandManager = IslandManager.getInstance();
        mobCoinCommand = new MobCoinCommand();

        getCommand("island").setExecutor(new IslandCommand(islandManager));
        getCommand("mb").setExecutor(new MobCoinCommand());
        getCommand("islandadmin").setExecutor(new AdminGuiCommand(new IslandCommand(islandManager)));

        //Island Mechanics
        Bukkit.getPluginManager().registerEvents(new IslandSettingsListener(), this);
        Bukkit.getPluginManager().registerEvents(new IslandCreationListener(islandManager, schematicFile), this);
        Bukkit.getPluginManager().registerEvents(new IslandNamingListener(), this);
        Bukkit.getPluginManager().registerEvents(new IslandMenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new AdminMenuListener(), this);
        Bukkit.getPluginManager().registerEvents(IslandManager.getInstance(), this);

        // MobCoin Mechanics
        Bukkit.getPluginManager().registerEvents(new BlazeListener(mobCoinCommand), this);
        Bukkit.getPluginManager().registerEvents(new SkeletonListener(mobCoinCommand), this);
        Bukkit.getPluginManager().registerEvents(new ZombieListener(mobCoinCommand), this);
        Bukkit.getPluginManager().registerEvents(new DefaultPlayerBalance(mobCoinCommand), this);
        Bukkit.getPluginManager().registerEvents(new MobCoinShopListener(mobCoinCommand), this);

    }

    @Override
    public void onDisable() {

    }

    public IslandManager getIslandManagerInstance(){

        return islandManager;
    }

    public Plugin getInstance() {
        return this;
    }
}
