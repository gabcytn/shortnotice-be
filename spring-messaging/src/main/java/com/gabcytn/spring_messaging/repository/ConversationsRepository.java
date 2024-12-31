package com.gabcytn.spring_messaging.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class ConversationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public ConversationsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer create (boolean isRequest) {
        final String sqlQuery = """
                INSERT INTO conversations (request)
                VALUES (?)
                RETURNING id
                """;
        final RowMapper<Integer> idRowMapper = (rs, rowNum) -> rs.getInt("id");

        return jdbcTemplate.queryForObject(sqlQuery, idRowMapper, isRequest);
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
}
