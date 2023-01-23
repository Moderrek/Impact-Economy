package pl.moderr.impact.economy.storage.file;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pl.moderr.impact.economy.data.User;
import pl.moderr.impact.economy.storage.ISavableStorage;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileStorage implements ISavableStorage {
    private final File file;
    private final FileConfiguration config;
    public FileStorage(@NotNull Plugin owner, String path) throws IOException, InvalidConfigurationException {
        file = new File(owner.getDataFolder(), path);
        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        config = new YamlConfiguration();
        config.load(file);
    }
    @Override
    public boolean hasUser(@NotNull UUID uuid) {
        return config.contains(String.format("%s.amount", uuid));
    }
    @Override
    public void createUser(User user) {
        saveUser(user);
    }
    @Override
    public User getUser(UUID uuid) {
        return new User(uuid, config.getLong(String.format("%s.amount", uuid.toString())));
    }
    @Override
    public void saveUser(@NotNull User user) {
        config.set(String.format("%s.amount", user.getUUID().toString()), user.get());
    }
    @Override
    public void saveStorage() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
