package tech.brito.apiresttexoit.util;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static java.util.Objects.isNull;

@Component
public class DatabaseCleaner {

    private Connection connection;
    private final DataSource dataSource;

    public DatabaseCleaner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void clearTables() {
        try (var connection = dataSource.getConnection()) {
            this.connection = connection;
            checkTestDatabase();
            tryToClearTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            this.connection = null;
        }
    }

    private void checkTestDatabase() throws SQLException {
        String catalog = connection.getCatalog();

        if (isNull(catalog) || !catalog.toUpperCase().endsWith("TEST")) {
            throw new RuntimeException("Cannot clear database tables because '" + catalog + "' is not a test database (suffix 'test' not found).");
        }
    }

    private void tryToClearTables() throws SQLException {
        var statement = connection.createStatement();
        statement.addBatch("SET REFERENTIAL_INTEGRITY FALSE");
        statement.addBatch("TRUNCATE TABLE MOVIE_PRODUCER");
        statement.addBatch("TRUNCATE TABLE MOVIE RESTART IDENTITY");
        statement.addBatch("SET REFERENTIAL_INTEGRITY TRUE");
        statement.executeBatch();
    }
}