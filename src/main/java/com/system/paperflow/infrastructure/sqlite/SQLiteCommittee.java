package com.system.paperflow.infrastructure.sqlite;

import com.system.paperflow.application.exception.CommitteePersistenceException;
import com.system.paperflow.application.persistence.CommitteePersistence;
import com.system.paperflow.domain.entity.CommitteeInvitation;
import com.system.paperflow.domain.entity.Coordinator;
import com.system.paperflow.domain.entity.Researcher;
import com.system.paperflow.domain.entity.Reviewer;
import com.system.paperflow.domain.entity.Topic;
import com.system.paperflow.domain.entity.User;
import com.system.paperflow.domain.enums.InvitationStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SQLiteCommittee implements CommitteePersistence {

    private final String databaseUrl;

    public SQLiteCommittee(String databasePath) {
        this(Path.of(databasePath));
    }

    public SQLiteCommittee(Path databasePath) {
        loadSQLiteDriver();
        createParentDirectoryIfNecessary(databasePath);
        this.databaseUrl = "jdbc:sqlite:" + databasePath.toAbsolutePath().normalize();
        createTablesIfNotExists();
    }

    @Override
    public void saveInvitation(CommitteeInvitation invitation) {
        String sql = """
                INSERT INTO committee_invitations (
                    id,
                    coordinator_email,
                    reviewer_email,
                    status,
                    created_at,
                    answered_at
                ) VALUES (?, ?, ?, ?, ?, ?)
                ON CONFLICT(id) DO UPDATE SET
                    coordinator_email = excluded.coordinator_email,
                    reviewer_email = excluded.reviewer_email,
                    status = excluded.status,
                    created_at = excluded.created_at,
                    answered_at = excluded.answered_at
                """;

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            fillInvitationStatement(statement, invitation);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new CommitteePersistenceException("Nao foi possivel salvar o convite do comite.", exception);
        }
    }

    @Override
    public Optional<CommitteeInvitation> findInvitationById(String invitationId) {
        String sql = invitationSelectSql() + " WHERE i.id = ? LIMIT 1";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, invitationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(mapInvitation(resultSet));
            }
        } catch (SQLException exception) {
            throw new CommitteePersistenceException("Nao foi possivel buscar o convite do comite.", exception);
        }
    }

    @Override
    public Optional<CommitteeInvitation> findPendingInvitationByReviewerEmail(String reviewerEmail) {
        String sql = invitationSelectSql() + " WHERE i.reviewer_email = ? AND i.status = ? LIMIT 1";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, normalizeEmail(reviewerEmail));
            statement.setString(2, InvitationStatus.PENDING.name());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(mapInvitation(resultSet));
            }
        } catch (SQLException exception) {
            throw new CommitteePersistenceException("Nao foi possivel buscar convite pendente do usuario.", exception);
        }
    }

    @Override
    public void updateInvitationStatus(CommitteeInvitation invitation) {
        String sql = """
                UPDATE committee_invitations
                SET status = ?, answered_at = ?
                WHERE id = ?
                """;

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, invitation.getStatus().name());
            statement.setString(2, formatDate(invitation.getAnsweredAt()));
            statement.setString(3, invitation.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new CommitteePersistenceException("Nao foi possivel atualizar o status do convite.", exception);
        }
    }

    @Override
    public void saveReviewerExpertise(String invitationId, Reviewer reviewer, Set<Topic> expertiseAreas) {
        String deleteSql = "DELETE FROM committee_reviewer_expertise WHERE invitation_id = ?";
        String insertSql = """
                INSERT INTO committee_reviewer_expertise (
                    invitation_id,
                    reviewer_email,
                    topic_name,
                    topic_description
                ) VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = connect()) {
            connection.setAutoCommit(false);

            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setString(1, invitationId);
                deleteStatement.executeUpdate();
            }

            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                for (Topic topic : expertiseAreas) {
                    insertStatement.setString(1, invitationId);
                    insertStatement.setString(2, normalizeEmail(reviewer.getEmail()));
                    insertStatement.setString(3, topic.getName());
                    insertStatement.setString(4, topic.getDescription());
                    insertStatement.addBatch();
                }

                insertStatement.executeBatch();
            }

            connection.commit();
        } catch (SQLException exception) {
            throw new CommitteePersistenceException("Nao foi possivel salvar as areas de expertise do revisor.", exception);
        }
    }

    @Override
    public List<CommitteeInvitation> findAllInvitations() {
        String sql = invitationSelectSql() + " ORDER BY i.created_at";

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            List<CommitteeInvitation> invitations = new ArrayList<>();

            while (resultSet.next()) {
                invitations.add(mapInvitation(resultSet));
            }

            return invitations;
        } catch (SQLException exception) {
            throw new CommitteePersistenceException("Nao foi possivel listar os convites do comite.", exception);
        }
    }

    @Override
    public List<Reviewer> findAcceptedReviewers() {
        String sql = """
                SELECT DISTINCT
                    u.username,
                    u.email,
                    u.password,
                    u.institution,
                    i.id AS invitation_id
                FROM committee_invitations i
                INNER JOIN users u ON u.email = i.reviewer_email
                WHERE i.status = ?
                ORDER BY u.username
                """;

        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, InvitationStatus.ACCEPTED.name());

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Reviewer> reviewers = new ArrayList<>();

                while (resultSet.next()) {
                    String invitationId = resultSet.getString("invitation_id");
                    Set<Topic> expertiseAreas = findExpertiseAreas(connection, invitationId);

                    reviewers.add(new Reviewer(
                            resultSet.getString("username"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("institution"),
                            expertiseAreas
                    ));
                }

                return reviewers;
            }
        } catch (SQLException exception) {
            throw new CommitteePersistenceException("Nao foi possivel listar os revisores do comite.", exception);
        }
    }

    private Set<Topic> findExpertiseAreas(Connection connection, String invitationId) throws SQLException {
        String sql = """
                SELECT topic_name, topic_description
                FROM committee_reviewer_expertise
                WHERE invitation_id = ?
                ORDER BY topic_name
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, invitationId);

            try (ResultSet resultSet = statement.executeQuery()) {
                Set<Topic> topics = new HashSet<>();

                while (resultSet.next()) {
                    topics.add(new Topic(
                            resultSet.getString("topic_name"),
                            resultSet.getString("topic_description")
                    ));
                }

                return topics;
            }
        }
    }

    private void fillInvitationStatement(PreparedStatement statement, CommitteeInvitation invitation) throws SQLException {
        statement.setString(1, invitation.getId());
        statement.setString(2, normalizeEmail(invitation.getCoordinatorEmail()));
        statement.setString(3, normalizeEmail(invitation.getReviewerEmail()));
        statement.setString(4, invitation.getStatus().name());
        statement.setString(5, formatDate(invitation.getCreatedAt()));
        statement.setString(6, formatDate(invitation.getAnsweredAt()));
    }

    private String invitationSelectSql() {
        return """
                SELECT
                    i.id,
                    i.coordinator_email,
                    i.reviewer_email,
                    i.status,
                    i.created_at,
                    i.answered_at,
                    coordinator.username AS coordinator_username,
                    coordinator.email AS coordinator_user_email,
                    coordinator.password AS coordinator_password,
                    coordinator.institution AS coordinator_institution,
                    invited.username AS invited_username,
                    invited.email AS invited_user_email,
                    invited.password AS invited_password,
                    invited.institution AS invited_institution,
                    invited.role AS invited_role
                FROM committee_invitations i
                INNER JOIN users coordinator ON coordinator.email = i.coordinator_email
                INNER JOIN users invited ON invited.email = i.reviewer_email
                """;
    }

    private CommitteeInvitation mapInvitation(ResultSet resultSet) throws SQLException {
        Coordinator coordinator = new Coordinator(
                resultSet.getString("coordinator_username"),
                resultSet.getString("coordinator_user_email"),
                resultSet.getString("coordinator_password"),
                resultSet.getString("coordinator_institution")
        );

        User invitedUser = mapInvitedUser(resultSet);

        return new CommitteeInvitation(
                resultSet.getString("id"),
                coordinator,
                invitedUser,
                InvitationStatus.valueOf(resultSet.getString("status")),
                parseDate(resultSet.getString("created_at")),
                parseDate(resultSet.getString("answered_at"))
        );
    }

    private User mapInvitedUser(ResultSet resultSet) throws SQLException {
        String username = resultSet.getString("invited_username");
        String email = resultSet.getString("invited_user_email");
        String password = resultSet.getString("invited_password");
        String institution = resultSet.getString("invited_institution");
        String role = resultSet.getString("invited_role");

        return switch (role) {
            case "REVIEWER" -> new Reviewer(username, email, password, institution, Collections.emptySet());
            case "COORDINATOR" -> new Coordinator(username, email, password, institution);
            default -> new Researcher(username, email, password, institution);
        };
    }

    private void loadSQLiteDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException exception) {
            throw new CommitteePersistenceException(
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
            throw new CommitteePersistenceException("Nao foi possivel criar a pasta do banco de dados.", exception);
        }
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }

    private void createTablesIfNotExists() {
        String invitationTableSql = """
                CREATE TABLE IF NOT EXISTS committee_invitations (
                    id TEXT PRIMARY KEY,
                    coordinator_email TEXT NOT NULL,
                    reviewer_email TEXT NOT NULL,
                    status TEXT NOT NULL,
                    created_at TEXT NOT NULL,
                    answered_at TEXT,
                    FOREIGN KEY(coordinator_email) REFERENCES users(email),
                    FOREIGN KEY(reviewer_email) REFERENCES users(email)
                )
                """;

        String expertiseTableSql = """
                CREATE TABLE IF NOT EXISTS committee_reviewer_expertise (
                    invitation_id TEXT NOT NULL,
                    reviewer_email TEXT NOT NULL,
                    topic_name TEXT NOT NULL,
                    topic_description TEXT NOT NULL,
                    PRIMARY KEY(invitation_id, topic_name),
                    FOREIGN KEY(invitation_id) REFERENCES committee_invitations(id)
                )
                """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(invitationTableSql);
            statement.execute(expertiseTableSql);
        } catch (SQLException exception) {
            throw new CommitteePersistenceException("Nao foi possivel criar as tabelas do comite tecnico.", exception);
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private String formatDate(LocalDateTime value) {
        return value == null ? null : value.toString();
    }

    private LocalDateTime parseDate(String value) {
        return value == null ? null : LocalDateTime.parse(value);
    }
}
