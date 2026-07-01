package com.system.paperflow.infrastructure.sqlite;

import com.system.paperflow.application.exception.UserPersistenceException;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.domain.entity.Coordinator;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.Reviewer;
import com.system.paperflow.domain.entity.User;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Optional;

public class SQLiteUserAdapter implements UserPersistence {

    private final String databaseUrl;

    public SQLiteUserAdapter(String databasePath) {
        this(Path.of(databasePath));
    }

    public SQLiteUserAdapter(Path databasePath) {
        loadSQLiteDriver();
        createParentDirectoryIfNecessary(databasePath);
        this.databaseUrl = "jdbc:sqlite:" + databasePath.toAbsolutePath().normalize();
        createTableIfNotExists();
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ? LIMIT 1";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, normalizeEmail(email));

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new UserPersistenceException("Nao foi possivel verificar o email do usuario.", exception);
        }
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (email, username, password, institution, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, normalizeEmail(user.getEmail()));
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getInstitution());
            statement.setString(5, resolveRole(user));

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new UserPersistenceException("Nao foi possivel salvar o usuario.", exception);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT email, username, password, institution, role FROM users WHERE email = ? LIMIT 1";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, normalizeEmail(email));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(mapResultSetToUser(resultSet));
            }
        } catch (SQLException exception) {
            throw new UserPersistenceException("Nao foi possivel buscar o usuario por email.", exception);
        }
    }

    private void loadSQLiteDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException exception) {
            throw new UserPersistenceException(
                    "Driver SQLite JDBC nao encontrado. Abra o projeto como Maven para carregar as dependencias do pom.xml.",
                    exception
            );
        }
    }

    private void createParentDirectoryIfNecessary(Path databasePath) {
        Path parent = databasePath.toAbsolutePath().normalize().getParent();

        if (parent == null) {
            return;
        }

        try {
            Files.createDirectories(parent);
        } catch (Exception exception) {
            throw new UserPersistenceException("Nao foi possivel criar a pasta do banco de dados.", exception);
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    email TEXT PRIMARY KEY,
                    username TEXT NOT NULL,
                    password TEXT NOT NULL,
                    institution TEXT NOT NULL,
                    role TEXT NOT NULL
                )
                """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException exception) {
            throw new UserPersistenceException("Nao foi possivel criar a tabela de usuarios.", exception);
        }
    }

    private String resolveRole(User user) {
        if (user instanceof Reviewer) {
            return "REVIEWER";
        }

        if (user instanceof Coordinator) {
            return "COORDINATOR";
        }

        return "RESEARCHER";
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        String email = resultSet.getString("email");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String institution = resultSet.getString("institution");
        String role = resultSet.getString("role");

        return switch (role) {
            case "REVIEWER" -> new Reviewer(username, email, password, institution, Collections.emptySet());
            case "COORDINATOR" -> new Coordinator(username, email, password, institution);
            default -> new Researcher(username, email, password, institution);
        };
    }
}
