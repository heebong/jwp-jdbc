package nextstep.jdbc;

import nextstep.jdbc.exception.DatabaseAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class JdbcTemplate {
    private final DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql) {
        update(sql, pstmt -> {
        });
    }

    public void update(String sql, Object... objects) {
        update(sql, pstmt -> mappingPreparedStatement(pstmt, objects));
    }

    private void update(String sql, PreparedStatementMapping consumer) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            consumer.adjustTo(pstmt);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    public <T> T query(String sql, ResultSetHandler<T> resultSetHandler) {
        return query(sql, pstmt -> {
        }, resultSetHandler);
    }

    public <T> T query(String sql, ResultSetHandler<T> resultSetHandler, Object... objects) {
        return query(sql, pstmt -> mappingPreparedStatement(pstmt, objects), resultSetHandler);
    }

    private <T> T query(String sql, PreparedStatementMapping mapping, ResultSetHandler<T> resultSetHandler) {
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            mapping.adjustTo(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                return resultSetHandler.toObject(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    public <T> Optional<T> queryForObject(String sql, ObjectMapper<T> objectMapper) {
        return queryForObject(sql, pstmt -> {
        }, objectMapper);
    }

    public <T> Optional<T> queryForObject(String sql, ObjectMapper<T> objectMapper, Object... objects) {
        return queryForObject(sql, pstmt -> mappingPreparedStatement(pstmt, objects), objectMapper);
    }

    private <T> Optional<T> queryForObject(String sql, PreparedStatementMapping mapping, ObjectMapper<T> objectMapper) {
        return queryForObjects(sql, mapping, objectMapper).stream().findFirst();
    }

    public <T> List<T> queryForObjects(String sql, ObjectMapper<T> objectMapper) {
        return queryForObjects(sql, pstmt -> {
        }, objectMapper);
    }

    public <T> List<T> queryForObjects(String sql, ObjectMapper<T> objectMapper, Object... objects) {
        return queryForObjects(sql, pstmt -> mappingPreparedStatement(pstmt, objects), objectMapper);
    }

    private <T> List<T> queryForObjects(String sql, PreparedStatementMapping mapping, ObjectMapper<T> objectMapper) {
        return query(sql, mapping, rs -> {
            List<T> objects = new ArrayList<>();
            while (rs.next()) {
                objects.add(objectMapper.toObject(rs));
            }
            return objects;
        });
    }

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private void mappingPreparedStatement(PreparedStatement pstmt, Object... objects) {
        int PREPARED_STATEMENT_FIRST_INDEX = 1;
        int ARRAY_FIRST_INDEX = 0;

        IntStream.range(ARRAY_FIRST_INDEX, objects.length).forEach(index -> {
            try {
                pstmt.setObject(PREPARED_STATEMENT_FIRST_INDEX + index, objects[index]);
            } catch (SQLException | NullPointerException e) {
                throw new DatabaseAccessException(e);
            }
        });
    }
}
