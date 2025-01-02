package com.gabcytn.spring_messaging.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ConversationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public ConversationsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer create () {
        final String sqlQuery = """
                INSERT INTO conversations (request)
                VALUES (true)
                RETURNING id
                """;
        final RowMapper<Integer> idRowMapper = (rs, rowNum) -> rs.getInt("id");

        return jdbcTemplate.queryForObject(sqlQuery, idRowMapper);
    }

    public void setRequestFalseById (int id) {
        final String sqlQuery = """
                UPDATE conversations
                SET request = false
                WHERE id = ?
                """;
        jdbcTemplate.update(sqlQuery, id);
    }

    public void saveMembers(int conversationId, UUID user1, UUID user2) {
        final String sqlQuery = """
                INSERT INTO conversation_members (conversation_id, user_id)
                VALUES (?, ?), (?, ?)
                """;
        jdbcTemplate.update(sqlQuery, conversationId, user1, conversationId, user2);
    }

    public Integer existsByMembers(UUID user1, UUID user2) {
        final String sqlQuery = """
                SELECT
                    cm1.conversation_id
                FROM
                    conversation_members AS cm1
                JOIN
                    conversation_members AS cm2
                ON
                    cm1.conversation_id = cm2.conversation_id
                WHERE
                    cm1.user_id = ?
                    AND
                    cm2.user_id = ?
                """;
        final ResultSetExtractor<Integer> extractor = (rs) -> rs.next() ? rs.getInt("count") : 0;
        final Object result = jdbcTemplate.query(sqlQuery, extractor, user1, user2);
        return result != null ? (int) result : 0;
    }

    public Boolean existsById (int id) {
        final String sqlQuery = """
                SELECT
                    COUNT(id) AS id
                FROM
                    conversations
                WHERE
                    id = ?
                """;
        final ResultSetExtractor<Integer> extractor = (rs) -> rs.next() ? rs.getInt("count") : 0;
        Object result = jdbcTemplate.query(sqlQuery, extractor, id);

        return result != null && (int) result > 0;
    }
}
