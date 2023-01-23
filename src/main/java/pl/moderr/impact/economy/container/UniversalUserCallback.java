package pl.moderr.impact.economy.container;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pl.moderr.impact.economy.data.User;

public interface UniversalUserCallback {
    /**
     * <b>Not required to save</b>
     * @param player Player instance
     * @param user User instance
     */
    void online(Player player, User user);
    /**
     * Remember: <b>When setting new value remember to save user instance!</b>
     * @param offlinePlayer Offline player instance
     * @param user User instance
     */
    void offline(OfflinePlayer offlinePlayer, User user);
    /**
     * Called when player isn't online and never played on server
     */
    void playerNeverPlayed();
    /**
     * Called when player played on server but he isn't registered
     */
    void playerNotRegistered();
    /**
     * Called when player is online but not loaded
     */
    void notLoaded();
}
