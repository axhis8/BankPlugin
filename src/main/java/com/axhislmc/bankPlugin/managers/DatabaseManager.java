package com.axhislmc.bankPlugin.managers;

import com.axhislmc.bankPlugin.BankPlugin;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseManager {
    private HikariDataSource dataSource;
    private final BankPlugin plugin;

    public DatabaseManager(BankPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        if (plugin.getDataFolder().mkdirs())
            plugin.getLogger().info("Data folder has been created.");
        this.dataSource = new HikariDataSource();
        this.dataSource.setJdbcUrl("jdbc:sqlite:" + new File(plugin.getDataFolder(), "data.db"));

        this.initTables();
    }

    private void initTables() {
        final String sql = "CREATE TABLE IF NOT EXISTS bank_accounts (" +
                           "uuid TEXT PRIMARY KEY, " +
                           "name TEXT, " +
                           "balance DOUBLE DEFAULT 0.0, " +
                           "joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

        try (final Connection connection = getConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)){

            statement.executeUpdate();
            plugin.getLogger().info("Successfully loaded Database.");
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public void addPlayerJoin(Player player) {
        final String sql = "INSERT OR IGNORE INTO bank_accounts (uuid, name) VALUES (?, ?);";
        final String updateName = "UPDATE bank_accounts SET name = ? WHERE uuid = ?;";

        try (final Connection connection = getConnection()) {

            try (final PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, player.getUniqueId().toString());
                statement.setString(2, player.getName());
                statement.executeUpdate();
            }

            // If name has been changed
            try (final PreparedStatement statement = connection.prepareStatement(updateName)) {
                statement.setString(1, player.getName());
                statement.setString(2, player.getUniqueId().toString());
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public double getBalance(UUID uuid) {
        final String sql = "SELECT balance FROM bank_accounts WHERE uuid = ?;";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }

        return 0.0;
    }

    public void setBalance(UUID uuid, double amount) {
        final String sql = "UPDATE bank_accounts SET balance = ? WHERE uuid = ?;";

        try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, amount);
            statement.setString(2, uuid.toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public List<Map.Entry<UUID, Double>> getTopList(int limit) {
        List<Map.Entry<UUID, Double>> topList = new ArrayList<>();

        final String sql = "SELECT uuid, balance FROM bank_accounts ORDER BY balance DESC LIMIT ?";

        try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    try {
                        UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                        double balance = resultSet.getDouble("balance");

                        topList.add(new AbstractMap.SimpleEntry<>(uuid, balance));
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().severe(e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe(e.getMessage());
        }

        return topList;
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
