package com.system.paperflow.infrastructure.sqlite;

import com.system.paperflow.application.exception.EventPersistenceException;
import com.system.paperflow.application.persistence.EventPersistence;
import com.system.paperflow.domain.entity.Event;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteEvent implements EventPersistence {

    private final String databaseUrl;

    public SQLiteEvent(String databasePath) {
        this(Path.of(databasePath));
    }

    public SQLiteEvent(Path databasePath) {
        loadSQLiteDriver();
        createParentDirectoryIfNecessary(databasePath);
        this.databaseUrl = "jdbc:sqlite:" + databasePath.toAbsolutePath().normalize();
        createTableIfNotExists();
    }

    @Override
    public void save(Event event) {
        String sql = "INSERT INTO events (name, city, start_date, end_date, submission_deadline) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, event.getName());
            statement.setString(2, event.getCity());
            statement.setString(3, event.getStartDate().toString());
            statement.setString(4, event.getEndDate().toString());
            statement.setString(5, event.getSubmissionDeadline().toString());

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new EventPersistenceException("Nao foi possivel salvar o evento.");
        }
    }

    @Override
    public Optional<Event> findCurrentOpenEvent() {
        String sql = "SELECT name, city, start_date, end_date, submission_deadline FROM events "
                + "WHERE end_date >= ? ORDER BY start_date DESC LIMIT 1";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, LocalDate.now().toString());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapResultSetToEvent(resultSet));
            }
        } catch (SQLException exception) {
            throw new EventPersistenceException("Nao foi possivel buscar o evento atual.");
        }
    }

    @Override
    public List<Event> findAll() {
        String sql = "SELECT name, city, start_date, end_date, submission_deadline FROM events "
                + "ORDER BY start_date DESC";

        List<Event> events = new ArrayList<>();

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                events.add(mapResultSetToEvent(resultSet));
            }
            return events;
        } catch (SQLException exception) {
            throw new EventPersistenceException("Nao foi possivel listar os eventos.");
        }
    }

    private void loadSQLiteDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException exception) {
            throw new EventPersistenceException(
                    "Driver SQLite JDBC nao encontrado. Abra o projeto como Maven para carregar as dependencias do pom.xml."
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
            throw new EventPersistenceException("Nao foi possivel criar a pasta do banco de dados.");
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }

    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS events (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    city TEXT NOT NULL,
                    start_date TEXT NOT NULL,
                    end_date TEXT NOT NULL,
                    submission_deadline TEXT NOT NULL
                )
                """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException exception) {
            throw new EventPersistenceException("Nao foi possivel criar a tabela de eventos.");
        }
    }

    private Event mapResultSetToEvent(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        String city = resultSet.getString("city");
        LocalDate startDate = LocalDate.parse(resultSet.getString("start_date"));
        LocalDate endDate = LocalDate.parse(resultSet.getString("end_date"));
        LocalDate deadline = LocalDate.parse(resultSet.getString("submission_deadline"));

        return new Event(name, city, startDate, endDate, deadline);
    }
}
