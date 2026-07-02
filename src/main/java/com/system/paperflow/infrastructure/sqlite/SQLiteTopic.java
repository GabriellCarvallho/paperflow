package com.system.paperflow.infrastructure.sqlite;

import com.system.paperflow.application.exception.TopicPersistenceException;
import com.system.paperflow.application.persistence.TopicPersistence;
import com.system.paperflow.domain.entity.Topic;
import com.system.paperflow.domain.entity.TopicComponent;
import com.system.paperflow.domain.entity.TopicGroup;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteTopic implements TopicPersistence {

    private static final String TYPE_TOPIC = "TOPIC";
    private static final String TYPE_GROUP = "GROUP";

    private final String databaseUrl;

    public SQLiteTopic(String databasePath) {
        this(Path.of(databasePath));
    }

    public SQLiteTopic(Path databasePath) {
        loadSQLiteDriver();
        createParentDirectoryIfNecessary(databasePath);
        this.databaseUrl = "jdbc:sqlite:" + databasePath.toAbsolutePath().normalize();
        createTableIfNotExists();
    }

    @Override
    public void save(TopicComponent topicComponent) {
        try (Connection connection = connect()) {
            connection.setAutoCommit(false);
            saveComponent(connection, topicComponent, null);
            connection.commit();
        } catch (SQLException exception) {
            throw new TopicPersistenceException("Nao foi possivel salvar a arvore de areas tematicas.", exception);
        }
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM topics WHERE normalized_name = ? LIMIT 1";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, normalizeName(name));

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new TopicPersistenceException("Nao foi possivel verificar a area tematica.", exception);
        }
    }

    @Override
    public Optional<TopicComponent> findByName(String name) {
        String sql = "SELECT name, description, component_type FROM topics WHERE normalized_name = ? LIMIT 1";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, normalizeName(name));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(mapCurrentRow(connection, resultSet));
            }
        } catch (SQLException exception) {
            throw new TopicPersistenceException("Nao foi possivel buscar a area tematica.", exception);
        }
    }

    @Override
    public List<TopicComponent> findAllRoots() {
        String sql = """
                SELECT name, description, component_type
                FROM topics
                WHERE parent_normalized_name IS NULL
                ORDER BY name
                """;

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            List<TopicComponent> roots = new ArrayList<>();

            while (resultSet.next()) {
                roots.add(mapCurrentRow(connection, resultSet));
            }

            return roots;
        } catch (SQLException exception) {
            throw new TopicPersistenceException("Nao foi possivel listar as areas tematicas.", exception);
        }
    }

    private void saveComponent(Connection connection, TopicComponent component, String parentNormalizedName) throws SQLException {
        String sql = """
                INSERT INTO topics (normalized_name, name, description, component_type, parent_normalized_name)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT(normalized_name) DO UPDATE SET
                    name = excluded.name,
                    description = excluded.description,
                    component_type = excluded.component_type,
                    parent_normalized_name = excluded.parent_normalized_name
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String normalizedName = normalizeName(component.getName());

            statement.setString(1, normalizedName);
            statement.setString(2, component.getName().trim());
            statement.setString(3, component.getDescription().trim());
            statement.setString(4, component instanceof TopicGroup ? TYPE_GROUP : TYPE_TOPIC);
            statement.setString(5, parentNormalizedName);
            statement.executeUpdate();

            if (component instanceof TopicGroup topicGroup) {
                for (TopicComponent child : topicGroup.getChildren()) {
                    saveComponent(connection, child, normalizedName);
                }
            }
        }
    }

    private TopicComponent mapCurrentRow(Connection connection, ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String componentType = resultSet.getString("component_type");

        if (TYPE_GROUP.equals(componentType)) {
            TopicGroup group = new TopicGroup(name, description);

            for (TopicComponent child : findChildren(connection, normalizeName(name))) {
                group.addChild(child);
            }

            return group;
        }

        return new Topic(name, description);
    }

    private List<TopicComponent> findChildren(Connection connection, String parentNormalizedName) throws SQLException {
        String sql = """
                SELECT name, description, component_type
                FROM topics
                WHERE parent_normalized_name = ?
                ORDER BY name
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, parentNormalizedName);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<TopicComponent> children = new ArrayList<>();

                while (resultSet.next()) {
                    children.add(mapCurrentRow(connection, resultSet));
                }

                return children;
            }
        }
    }

    private void loadSQLiteDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException exception) {
            throw new TopicPersistenceException(
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
            throw new TopicPersistenceException("Nao foi possivel criar a pasta do banco de dados.", exception);
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }

    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS topics (
                    normalized_name TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    description TEXT NOT NULL,
                    component_type TEXT NOT NULL,
                    parent_normalized_name TEXT,
                    FOREIGN KEY(parent_normalized_name) REFERENCES topics(normalized_name)
                )
                """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException exception) {
            throw new TopicPersistenceException("Nao foi possivel criar a tabela de areas tematicas.", exception);
        }
    }

    private String normalizeName(String name) {
        return name.trim().toLowerCase();
    }
}