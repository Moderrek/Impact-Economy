package pl.moderr.impact.economy.container;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pl.moderr.impact.economy.data.User;
import pl.moderr.impact.economy.storage.IStorage;

import java.util.*;

public class UserContainer {

    private final Map<UUID, User> userMap = new HashMap<>();
    private final IStorage storage;
    public UserContainer(IStorage storage){
        if(storage == null)
            throw new RuntimeException("Storage cannot be null!");
        this.storage = storage;
    }

    private long defaultAmount = 0;
    public void setDefault(long amount){
        defaultAmount = amount;
    }
    public long getDefault(){
        return defaultAmount;
    }

    /**
     * Check is user instance loaded in memory
     * @param uuid User minecraft ID
     * @return is user loaded to memory
     */
    public boolean isUserLoaded(UUID uuid){
        return userMap.containsKey(uuid);
    }
    /**
     * Load user instance to memory
     * @param uuid User minecraft ID
     * @param createDefault if `true` and user doesn't exist will be created a default one.
     * @param callback
     * @throws UserIsLoadedException Thrown when user is loaded and trying to load
     * @throws UnknownUserException Thrown when `createDefault`=false and user doesn't exist.
     */
    public void loadUser(UUID uuid, boolean createDefault, LoadUserCallback callback) throws UserIsLoadedException, UnknownUserException {
        if(isUserLoaded(uuid)){
            throw new UserIsLoadedException();
        }
        if(!storage.hasUser(uuid)){
            if(!createDefault){
                throw new UnknownUserException();
            }
            User user = new User(uuid, defaultAmount);
            storage.createUser(user);
            userMap.put(uuid, user);
            if(callback != null)
                callback.onLoadAndCreate(user);
            return;
        }
        User user = storage.getUser(uuid);
        userMap.put(uuid, user);
        if(callback != null)
            callback.onLoad(user);
    }
    /**
     * Release user instance from memory
     * @param uuid User minecraft ID
     * @param save set to `true` when you want to save to storage
     * @throws UnknownUserException Thrown when trying to unload user, and he isn't loaded to memory
     */
    public void unloadUser(UUID uuid, boolean save) throws UnknownUserException {
        User user = getUser(uuid);
        if(save)
            storage.saveUser(user);
        userMap.remove(uuid);
    }
    /**
     * Getter to User instance
     * @param uuid User minecraft ID
     * @return User instance
     * @throws UnknownUserException Thrown when trying to get user, and he isn't loaded to memory
     */
    public User getUser(UUID uuid) throws UnknownUserException {
        if(!isUserLoaded(uuid)){
            throw new UnknownUserException();
        }
        return userMap.get(uuid);
    }
    /**
     * @param playerName Player nickname
     * @param callback Callback
     */
    public void getUniversalUser(String playerName, UniversalUserCallback callback){
        Player player = Bukkit.getPlayer(playerName);
        if(player != null){
            if(isUserLoaded(player.getUniqueId())){
                try {
                    callback.online(player, getUser(player.getUniqueId()));
                } catch (UnknownUserException e) {
                    e.printStackTrace();
                    callback.notLoaded();
                }
                return;
            }
            callback.notLoaded();
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        if(!offlinePlayer.hasPlayedBefore()){
            callback.playerNeverPlayed();
            return;
        }
        if(!storage.hasUser(offlinePlayer.getUniqueId())){
            callback.playerNotRegistered();
            return;
        }
        callback.offline(offlinePlayer, storage.getUser(offlinePlayer.getUniqueId()));
    }
    /**
     * @return Loaded users count
     */
    public int getUsersCount(){
        return userMap.size();
    }
    /**
     * @return Collection of all loaded users
     */
    public Collection<User> getUsers(){
        return userMap.values();
    }
}
