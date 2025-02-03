package com.gabcytn.spring_messaging.repository;

import com.gabcytn.spring_messaging.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class BlocksRepository {
    private final JdbcTemplate jdbcTemplate;

    public BlocksRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Boolean existsByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId) {
        final String sqlQuery = """
                SELECT
                    COUNT(blocked_id) AS count
                FROM
                    blocks
                WHERE
                    blocker_id = ?
                    AND
                    blocked_id = ?
                """;
        final ResultSetExtractor<Integer> extractor =
                (rs) -> rs.next() ? rs.getInt("count") : 0;
        final Object result = jdbcTemplate.query(sqlQuery, extractor, blockerId, blockedId);

        return result != null && (int) result > 0;
    }

    public List<User> findByBlockerId (UUID blockerId) {
        final String sqlQuery = """
                SELECT
                    users.id, users.username, users.profile_pic
                FROM
                    blocks
                JOIN
                    users
                ON
                    blocks.blocked_id = users.id
                WHERE
                    blocks.blocker_id = ?
                """;
        return jdbcTemplate.query(sqlQuery, userRowMapper(), blockerId);
    }

    public void save (UUID blockerId, UUID blockedId) {
        final String sqlQuery = """
                INSERT INTO blocks (blocker_id, blocked_id)
                VALUES (?, ?)
                """;
        jdbcTemplate.update(sqlQuery, blockerId, blockedId);
    }

    public void deleteByBlockerIdAndBlockedId (UUID blockerId, UUID blockedId) {
        final String sqlQuery = """
                DELETE FROM
                    blocks
                WHERE
                    blocker_id = ?
                    AND
                    blocked_id = ?
                """;
        jdbcTemplate.update(sqlQuery, blockerId, blockedId);
    }

    private RowMapper<User> userRowMapper () {
        return (rs, rowNum) -> {
            final User user = new User();
            user.setId(UUID.fromString(rs.getString("id")));
            user.setUsername(rs.getString("username"));
            user.setProfilePic(rs.getString("profile_pic"));
            return user;
        };
    }
}
