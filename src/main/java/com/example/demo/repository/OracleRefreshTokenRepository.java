package com.example.demo.repository;

import com.example.demo.dto.RefreshTokenStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class OracleRefreshTokenRepository {

    private final JdbcTemplate jdbcTemplate;

    public OracleRefreshTokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RefreshTokenStatus> getRefreshTokenStatus() {

        String sql = """
            SELECT 
                USERNAME,
                MAX(TOKEN_ID) AS MAX_TOKEN_ID,

                MAX(CREATED_AT)
                    KEEP (DENSE_RANK LAST ORDER BY TOKEN_ID) AS CREATED_AT,

                MAX(EXPIRES_AT)
                    KEEP (DENSE_RANK LAST ORDER BY TOKEN_ID) AS EXPIRES_AT,

                MAX(REVOKED)
                    KEEP (DENSE_RANK LAST ORDER BY TOKEN_ID) AS REVOKED,

                CASE
                    WHEN MAX(EXPIRES_AT)
                        KEEP (DENSE_RANK LAST ORDER BY TOKEN_ID)
                        < LOCALTIMESTAMP
                    THEN 'EXPIRED'
                    ELSE 'ACTIVE'
                END AS EXPIRATION_STATUS,

                MAX(EXPIRES_AT)
                    KEEP (DENSE_RANK LAST ORDER BY TOKEN_ID)
                -
                MAX(CREATED_AT)
                    KEEP (DENSE_RANK LAST ORDER BY TOKEN_ID)
                    AS TOTAL_DURATION,

                MAX(EXPIRES_AT)
                    KEEP (DENSE_RANK LAST ORDER BY TOKEN_ID)
                -
                LOCALTIMESTAMP AS TIME_LEFT

            FROM AUTHJSB_REFRESH_TOKEN
            GROUP BY USERNAME
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            Timestamp createdTimestamp =
                    rs.getTimestamp("CREATED_AT");

            Timestamp expiresTimestamp =
                    rs.getTimestamp("EXPIRES_AT");

            return new RefreshTokenStatus(
                    rs.getString("USERNAME"),
                    rs.getLong("MAX_TOKEN_ID"),

                    createdTimestamp != null
                            ? createdTimestamp.toLocalDateTime()
                            : null,

                    expiresTimestamp != null
                            ? expiresTimestamp.toLocalDateTime()
                            : null,

                    rs.getInt("REVOKED") == 1,

                    rs.getString("EXPIRATION_STATUS"),

                    rs.getString("TOTAL_DURATION"),

                    rs.getString("TIME_LEFT")
            );
        });
    }
}