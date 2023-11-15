DROP PROCEDURE IF EXISTS insertDummyData;

CREATE PROCEDURE insertDummyData()
BEGIN
    DECLARE M INT DEFAULT 1;
    DECLARE I INT;
    DECLARE J INT;
    DECLARE UUID BINARY(16);
    DECLARE START_DATE DATETIME;
    DECLARE END_DATE DATETIME;
    DECLARE EVENT_ID BIGINT;
    DECLARE TOKEN VARCHAR(255);

    WHILE M <= 1000
        DO
            INSERT INTO Member(email, password) VALUES (CONCAT('testuser', M, '@test.com'), 'password');
            SET I := 1;
            WHILE I <= 5
                DO
                    SET UUID := UNHEX(REPLACE(UUID(), '-', ''));
                    IF I = 1 THEN -- 이벤트 진행중
                        SET START_DATE := NOW() - INTERVAL 1 DAY;
                        SET END_DATE := NOW() + INTERVAL 1 DAY;
                    ELSEIF I <= 4 THEN -- 만료된 이벤트
                        SET START_DATE := NOW() - INTERVAL 2 DAY;
                        SET END_DATE := NOW() - INTERVAL 1 DAY;
                    ELSE -- 이벤트 예정
                        SET START_DATE := NOW() + INTERVAL 1 DAY;
                        SET END_DATE := NOW() + INTERVAL 2 DAY;
                    END IF;
                    INSERT INTO Event(uuid, title, limit_count, remain_count, success_content, start_date_time,
                                      end_date_time, member_id)
                    VALUES (UUID, CONCAT('Event ', (M - 1) * 5 + I), 10, IF(I = 1, 10, IF(I <= 4, 0, 10)), 'Success',
                            START_DATE, END_DATE, M);
                    SET EVENT_ID := LAST_INSERT_ID();
                    IF I <= 4 THEN -- 만료된 이벤트에 대해서만 SuccessRecord를 생성
                        SET J := 1;
                        WHILE J <= 10
                            DO
                                SET TOKEN := (SELECT UUID());
                                INSERT INTO success_record(token, event_id) VALUES (TOKEN, EVENT_ID);
                                SET J := J + 1;
                            END WHILE;
                    END IF;
                    SET I := I + 1;
                END WHILE;
            SET M := M + 1;
        END WHILE;
END;
CALL InsertDummyData();
