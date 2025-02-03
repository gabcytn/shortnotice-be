package com.gabcytn.spring_messaging.repository;

import com.gabcytn.spring_messaging.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findById(UUID uuid) {
        final String sqlQuery = """
                SELECT *
                FROM users
                WHERE id = ?
                """;
        return Optional.ofNullable(jdbcTemplate.query(sqlQuery, userResultSetExtractor(), uuid.toString()));
    }

    public Optional<User> findByUsername(String username) {
        String sqlQuery = """
                SELECT *
                FROM users
                WHERE username = ?
                """;
        return Optional.ofNullable(jdbcTemplate.query(sqlQuery, userResultSetExtractor(), username));
    }

    public void save(User user) {
        String sqlQuery = """
                INSERT INTO users (id, username, password)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(sqlQuery, user.getId(), user.getUsername(), user.getPassword());
    }

    public List<User> findByUsernameContainingIgnoreCase (UUID blockerId, String username) {
        final String sqlQuery = """
                SELECT
                    id, username, profile_pic
                FROM
                    users
                LEFT JOIN
                    blocks AS blocks1
                ON
                    blocks1.blocker_id = users.id
                LEFT JOIN
                    blocks AS blocks2
                ON
                    blocks2.blocked_id = users.id
                WHERE
                    LOWER(username) LIKE ?
                    AND
                        blocks1.blocked_id IS DISTINCT FROM ?
                    AND
                        blocks2.blocker_id IS DISTINCT FROM ?
                """;
        final RowMapper<User> userRowMapper = (rs, rowNum) -> {
            final User user = new User();
            user.setId(UUID.fromString(rs.getString("id")));
            user.setUsername(rs.getString("username"));
            user.setProfilePic(rs.getString("profile_pic"));
            return user;
        };

        return jdbcTemplate.query(sqlQuery, userRowMapper, username.toLowerCase() + "%", blockerId, blockerId);
    }

    private ResultSetExtractor<User> userResultSetExtractor() {
        return rs -> {
            if (rs.next()) {
                User user = new User();
                user.setId(UUID.fromString(rs.getString("id")));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setProfilePic(rs.getString("profile_pic"));
                user.setRole(rs.getString("role"));
                return user;
            }
            return null;
        };
    }
}
