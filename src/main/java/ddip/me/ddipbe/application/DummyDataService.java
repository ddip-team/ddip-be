package ddip.me.ddipbe.application;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.UUID;

@Profile("dev")
@Service
@RequiredArgsConstructor
public class DummyDataService {

    private final JdbcTemplate jdbcTemplate;

    private static final int TOTAL_MEMBERS = 1000;
    private static final int EVENTS_PER_MEMBER = 5;
    private static final int SUCCESS_RECORDS_PER_EVENT = 10;

    @Transactional
    public UUID initDataAndReturnOngoingEventUuid() {
        // 존재하는 데이터 모두 삭제
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbcTemplate.execute("TRUNCATE TABLE success_record");
        jdbcTemplate.execute("TRUNCATE TABLE event");
        jdbcTemplate.execute("TRUNCATE TABLE member");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");

        // Batch insert members
        jdbcTemplate.batchUpdate("INSERT INTO member (email, password) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, "testuser" + (i + 1) + "@test.com");
                        ps.setString(2, "password");
                    }

                    public int getBatchSize() {
                        return TOTAL_MEMBERS;
                    }
                }
        );

        // Batch insert events
        UUID ongoingEventUuid = UUID.randomUUID();
        ZonedDateTime now = ZonedDateTime.now();
        jdbcTemplate.batchUpdate("INSERT INTO event (uuid, title, limit_count, remain_count, success_content, thumbnail_image_url, start_date_time, end_date_time, created_at, updated_at, member_id) VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                int j = i % EVENTS_PER_MEMBER;

                ZonedDateTime startDateTime, endDateTime;
                if (j == 0) {
                    // Ongoing event
                    startDateTime = now.minusDays(1);
                    endDateTime = now.plusDays(1);
                } else if (j < EVENTS_PER_MEMBER - 1) {
                    // Expired events
                    startDateTime = now.minusDays(2);
                    endDateTime = now.minusDays(1);
                } else {
                    // Upcoming event
                    startDateTime = now.plusDays(1);
                    endDateTime = now.plusDays(2);
                }

                UUID eventUuid;
                if (j == 0 && i == TOTAL_MEMBERS * EVENTS_PER_MEMBER / 2) {
                    eventUuid = ongoingEventUuid;
                } else {
                    eventUuid = UUID.randomUUID();
                }

                ps.setString(1, eventUuid.toString());
                ps.setString(2, "Event " + (i + 1));
                ps.setInt(3, SUCCESS_RECORDS_PER_EVENT);
                ps.setInt(4, j > 0 && j < EVENTS_PER_MEMBER - 1 ? 0 : SUCCESS_RECORDS_PER_EVENT);
                ps.setString(5, "SuccessContent");
                ps.setString(6, "ThumbnailImageUrl");
                ps.setTimestamp(7, java.sql.Timestamp.valueOf(startDateTime.toLocalDateTime()));
                ps.setTimestamp(8, java.sql.Timestamp.valueOf(endDateTime.toLocalDateTime()));
                ps.setObject(9, now);
                ps.setObject(10, now);
                ps.setLong(11, i / EVENTS_PER_MEMBER + 1);
            }

            public int getBatchSize() {
                return TOTAL_MEMBERS * EVENTS_PER_MEMBER;
            }
        });

        // Batch insert success records
        jdbcTemplate.batchUpdate("INSERT INTO success_record (token, event_id, created_at, updated_at) VALUES (?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        long memberIdx = i / (EVENTS_PER_MEMBER - 2) / SUCCESS_RECORDS_PER_EVENT;
                        int idx = i / SUCCESS_RECORDS_PER_EVENT % (EVENTS_PER_MEMBER - 2);
                        long id = memberIdx * EVENTS_PER_MEMBER + idx + 2;
                        ps.setString(1, UUID.randomUUID().toString());
                        ps.setLong(2, id);
                        ps.setObject(3, now);
                        ps.setObject(4, now);
                    }

                    public int getBatchSize() {
                        return TOTAL_MEMBERS * (EVENTS_PER_MEMBER - 2) * SUCCESS_RECORDS_PER_EVENT;
                    }
                }
        );

        return ongoingEventUuid;
    }

    @Transactional
    public void resetEvent(UUID uuid) {
        jdbcTemplate.update("UPDATE event SET remain_count = limit_count WHERE uuid = UUID_TO_BIN(?)", uuid.toString());
        jdbcTemplate.update("DELETE FROM success_record WHERE event_id = (SELECT id FROM event WHERE uuid = UUID_TO_BIN(?))", uuid.toString());
    }
}
