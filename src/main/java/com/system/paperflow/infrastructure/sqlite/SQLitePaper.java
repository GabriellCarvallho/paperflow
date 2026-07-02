package com.system.paperflow.infrastructure.sqlite;

import com.system.paperflow.application.exception.PaperPersistenceException;
import com.system.paperflow.application.persistence.PaperPersistence;
import com.system.paperflow.application.persistence.UserPersistence;
import com.system.paperflow.domain.entity.Event;
import com.system.paperflow.domain.entity.Paper;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.User;
import com.system.paperflow.domain.state.AcceptedState;
import com.system.paperflow.domain.state.RejectedState;
import com.system.paperflow.domain.state.SubmittedState;
import com.system.paperflow.domain.state.UnderReviewState;

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
import java.util.Set;

public class SQLitePaper implements PaperPersistence {

    private final String databaseUrl;
    private final UserPersistence userPersistence;

    public SQLitePaper(String databasePath, UserPersistence userPersistence) {
        this(Path.of(databasePath), userPersistence);
    }

    public SQLitePaper(Path databasePath, UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
        loadSQLiteDriver();
        createParentDirectoryIfNecessary(databasePath);
        this.databaseUrl = "jdbc:sqlite:" + databasePath.toAbsolutePath().normalize();
        createTableIfNotExists();
    }

    @Override
    public void save(Paper paper) {
        String sql = "INSERT OR REPLACE INTO papers "
                + "(id, title, summary, author_email, status, event_name) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, paper.getId());
            statement.setString(2, paper.getTitle());
            statement.setString(3, paper.getSummary());
            statement.setString(4, paper.getAuthor().getEmail());
            statement.setString(5, paper.getStatus());
            statement.setString(6, paper.getEvent().getName());

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new PaperPersistenceException("Nao foi possivel salvar o artigo.");
        }
    }

    @Override
    public Optional<Paper> findById(String id) {
        String sql = "SELECT id, title, summary, author_email, status, event_name FROM papers WHERE id = ? LIMIT 1";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapResultSetToPaper(resultSet));
            }
        } catch (SQLException exception) {
            throw new PaperPersistenceException("Nao foi possivel buscar o artigo por id.");
        }
    }

    @Override
    public List<Paper> findAll() {
        String sql = "SELECT id, title, summary, author_email, status, event_name FROM papers";

        List<Paper> papers = new ArrayList<>();

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                papers.add(mapResultSetToPaper(resultSet));
            }
            return papers;
        } catch (SQLException exception) {
            throw new PaperPersistenceException("Nao foi possivel listar os artigos.");
        }
    }

    private void loadSQLiteDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException exception) {
            throw new PaperPersistenceException(
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
            throw new PaperPersistenceException("Nao foi possivel criar a pasta do banco de dados.");
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }

    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS papers (
                    id TEXT PRIMARY KEY,
                    title TEXT NOT NULL,
                    summary TEXT NOT NULL,
                    author_email TEXT NOT NULL,
                    status TEXT NOT NULL,
                    event_name TEXT NOT NULL
                )
                """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException exception) {
            throw new PaperPersistenceException("Nao foi possivel criar a tabela de artigos.");
        }
    }

    private Paper mapResultSetToPaper(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String title = resultSet.getString("title");
        String summary = resultSet.getString("summary");
        String authorEmail = resultSet.getString("author_email");
        String status = resultSet.getString("status");
        String eventName = resultSet.getString("event_name");

        User authorUser = userPersistence.findByEmail(authorEmail)
                .orElseThrow(() -> new PaperPersistenceException(
                        "Autor do artigo nao encontrado: " + authorEmail));

        if (!(authorUser instanceof Researcher author)) {
            throw new PaperPersistenceException("Autor do artigo nao e um pesquisador valido.");
        }

        Event placeholderEvent = new Event(eventName, "", LocalDate.now(), LocalDate.now(), LocalDate.now());

        Paper paper = new Paper(id, title, summary, author,
                new ArrayList<>(), Set.of(), placeholderEvent);

        applyStatus(paper, status);

        return paper;
    }

    private void applyStatus(Paper paper, String status) {
        switch (status) {
            case "Under Review" -> paper.setState(new UnderReviewState());
            case "Accepted" -> paper.setState(new AcceptedState());
            case "Rejected" -> paper.setState(new RejectedState());
            default -> paper.setState(new SubmittedState());
        }
    }
}
