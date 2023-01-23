package pl.moderr.impact.economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.moderr.impact.economy.commands.BalanceCommand;
import pl.moderr.impact.economy.commands.ImpactEconomyCommand;
import pl.moderr.impact.economy.container.UnknownUserException;
import pl.moderr.impact.economy.container.UserContainer;
import pl.moderr.impact.economy.container.UserIsLoadedException;
import pl.moderr.impact.economy.listeners.JoinQuitListener;
import pl.moderr.impact.economy.storage.ISavableStorage;
import pl.moderr.impact.economy.storage.IStorage;
import pl.moderr.impact.economy.storage.StorageType;
import pl.moderr.impact.economy.storage.file.FileStorage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ImpactEconomy extends JavaPlugin {
    public static final String VERSION = "0.1-SNAPSHOT";
    // Instance
    private static ImpactEconomy instance;
    public static ImpactEconomy getInstance(){
        return instance;
    }
    // Final variables
    private final Logger logger = getLogger();
    private final FileConfiguration config = getConfig();
    public final int decimalPrecision = config.getInt("decimal-precision");
    public final String moneyPrefix = config.getString("money-prefix");
    public final String moneySuffix = config.getString("money-suffix");

    //<editor-fold> Storage & User Container
    private IStorage storage;
    private void createStorageByType(StorageType type) throws IOException, InvalidConfigurationException {
        if(storage != null)
            return;
        if (type == StorageType.FILE) {
            storage = new FileStorage(this, "economy.yml");
        }
        logger.log(Level.INFO, String.format("Initialized storage %s", type.toString()));
    }
    public IStorage getStorage(){
        return storage;
    }
    private UserContainer container;
    public UserContainer getUserContainer(){
        return container;
    }
    //</editor-fold>

    //<editor-fold> Functions
    private void initializeStorage(){
        try {
            createStorageByType(StorageType.valueOf(Objects.requireNonNull(config.getString("storage-type")).toUpperCase()));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            setEnabled(false);
            return;
        }
        container = new UserContainer(storage);
    }
    private void loadAllPlayers(){
        logger.log(Level.INFO, "Loading all online players..");
        long loadMillis = System.currentTimeMillis();
        int loaded = 0;
        for(Player player : Bukkit.getOnlinePlayers()){
            if(container.isUserLoaded(player.getUniqueId())){
                continue;
            }
            try {
                container.loadUser(player.getUniqueId(), true, null);
                loaded += 1;
            } catch (UserIsLoadedException | UnknownUserException e) {
                logger.log(Level.WARNING, "Cannot load \"" + player.getName() + "\", kicking..");
                player.kick();
            }
        }
        logger.log(Level.INFO, String.format("Loaded %d/%d players in %dms", loaded, Bukkit.getOnlinePlayers().size(), System.currentTimeMillis() - loadMillis));
    }
    private void unloadAllPlayers(){
        logger.log(Level.INFO, "Loading all online players..");
        long unloadMillis = System.currentTimeMillis();
        int unloaded = 0;
        for(Player player : Bukkit.getOnlinePlayers()){
            if(!container.isUserLoaded(player.getUniqueId())){
                continue;
            }
            try {
                container.unloadUser(player.getUniqueId(), true);
                unloaded += 1;
            } catch (UnknownUserException e) {
                logger.log(Level.WARNING, "DATA LOST! Cannot save \"" + player.getName() + "\", user isn't registered in storage!");
            }
        }
        logger.log(Level.INFO, String.format("Unloaded %d/%d players in %dms", unloaded, Bukkit.getOnlinePlayers().size(), System.currentTimeMillis() - unloadMillis));
    }
    private void registerCommands(){
        Objects.requireNonNull(getCommand("impacteconomy")).setExecutor(new ImpactEconomyCommand());
        Objects.requireNonNull(getCommand("balance")).setExecutor(new BalanceCommand());
    }
    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinQuitListener(), this);
    }
    private void saveStorage(){
        if(storage instanceof ISavableStorage savableStorage){
            logger.log(Level.INFO, "Saving storage..");
            long startedSaving = System.currentTimeMillis();
            savableStorage.saveStorage();
            long saved = System.currentTimeMillis() - startedSaving;
            logger.log(Level.INFO, String.format("Saved storage in %dms", saved));
        }
    }
    //</editor-fold>

    // Plugin
    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        saveDefaultConfig();
        config.options().copyDefaults(true);
        saveConfig();
        initializeStorage();
        registerCommands();
        registerEvents();
        loadAllPlayers();
        logger.log(Level.INFO, String.format("Started in %dms", System.currentTimeMillis() - start));
    }

    @Override
    public void onDisable() {
        long start = System.currentTimeMillis();
        unloadAllPlayers();
        saveStorage();
        logger.log(Level.INFO, String.format("Disabled in %dms", System.currentTimeMillis() - start));
    }
}
