package no.kantega;

import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DB {

    @SuppressWarnings("unchecked")
    public static <T> T queryForObject(Connection connection, @Language("SQL") String query, Object... params) throws SQLException {
        try (
                PreparedStatement statement = prepareStatement(connection, query, params);
                ResultSet resultSet = statement.executeQuery()
        ) {
            return resultSet.next() ? (T) resultSet.getObject(1) : null;
        }
    }

    public static <T> List<T> queryForList(Connection connection, @Language("SQL") String query, RowMapper<T> rowMapper, Object... params) throws SQLException {
        List<T> results = new ArrayList<>();
        try (
                PreparedStatement statement = prepareStatement(connection, query, params);
                ResultSet resultSet = statement.executeQuery()
        ) {
            int i = 0;
            while (resultSet.next()) {
                results.add(rowMapper.mapRow(i++, resultSet));
            }
        }
        return results;
    }

    public interface RowMapper<T> {
        T mapRow(int rowNumber, ResultSet resultSet) throws SQLException;
    }

    public static PreparedStatement prepareStatement(Connection connection, @Language("SQL") String query, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        fillParameters(statement, params);
        return statement;
    }

    private static void fillParameters(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
    }

    public static int executeUpdate(Connection connection, @Language("SQL") String query, Object... params) throws SQLException {
        try (PreparedStatement statement = prepareStatement(connection, query, params)) {
            return statement.executeUpdate();
        }
    }
}
