package pl.moderr.impact.economy.storage.mysql;

import pl.moderr.impact.economy.data.User;
import pl.moderr.impact.economy.storage.IStorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

public class MysqlStorage implements IStorage {

    private final Connection connection;

    public MysqlStorage(String host, String database, int port, String user, String password) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", host, port, database), user, password);
    }

    @Override
    public boolean hasUser(UUID uuid) {
        return false;
    }

    @Override
    public void createUser(User user) {

    }

    @Override
    public User getUser(UUID uuid) {
        return null;
    }

    @Override
    public void saveUser(User user) {

    }
}
