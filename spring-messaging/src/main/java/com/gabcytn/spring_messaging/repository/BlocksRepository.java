package com.gabcytn.spring_messaging.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

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
}
