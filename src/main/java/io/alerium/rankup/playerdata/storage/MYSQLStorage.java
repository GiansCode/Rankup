package io.alerium.rankup.playerdata.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.alerium.rankup.RankupPlugin;
import io.alerium.rankup.playerdata.PlayerData;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MYSQLStorage implements Storage {
    
    private final RankupPlugin plugin = RankupPlugin.getInstance();

    private final String hostname;
    private final String username;
    private final String password;
    private final String database;
    private final int port;
    
    private HikariDataSource dataSource;
    
    public MYSQLStorage() {
        ConfigurationSection section = plugin.getConfiguration().getConfig().getConfigurationSection("storage.mysql");
        
        hostname = section.getString("hostname");
        username = section.getString("username");
        password = section.getString("password");
        database = section.getString("database");
        port = section.getInt("port");
    }
    
    @Override
    public void setup() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.setPoolName("PrisonBestPool");

        dataSource = new HikariDataSource(config);
        
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS playerdata (uuid VARCHAR(36) PRIMARY KEY NOT NULL, rank VARCHAR(16) NOT NULL);")
        ) {
            
            statement.execute();
        }
    }

    @Override
    public PlayerData loadData(UUID uuid, String defaultRank) throws Exception {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM playerdata WHERE uuid = ?;");
        ) {
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            
            if (result.next()) {
                String rank = result.getString("rank");
                result.close();
                return new PlayerData(uuid, rank);
            } 
            
            result.close();
            return createData(uuid, defaultRank);
        }
    }

    @Override
    public void saveData(PlayerData playerData) throws Exception {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("UPDATE playerdata SET rank = ? WHERE uuid = ?;");
        ) {
            
            statement.setString(1, playerData.getRank());
            statement.setString(2, playerData.getUuid().toString());
            
            statement.execute();
        }
    }

    private PlayerData createData(UUID uuid, String defaultRank) throws SQLException {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO playerdata (uuid, rank) VALUES (?, ?);");
        ) {
            
            statement.setString(1, uuid.toString());
            statement.setString(2, defaultRank);
            statement.execute();
        }
        
        return new PlayerData(uuid, defaultRank);
    }

}
