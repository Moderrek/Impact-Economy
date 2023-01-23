package pl.moderr.impact.economy.storage;

import pl.moderr.impact.economy.data.User;

import java.util.UUID;

public interface IStorage {

    /**
     * Returns true, if storage has a saved {@link User}
     * @param uuid User ID
     * @return true/false | is storage has entry of this UUID
     */
    boolean hasUser(UUID uuid);

    /**
     * Create entry in storage of {@link User}
     * @param user User instance
     */
    void createUser(User user);

    /**
     * Returns a {@link User} instance
     * @param uuid User ID
     * @return User instance by `uuid`, if exists
     */
    User getUser(UUID uuid);

    /**
     * Update the {@link User} entry<br>
     * <i>User entry must be created before.</i>
     * @param user User instance
     */
    void saveUser(User user);
}
