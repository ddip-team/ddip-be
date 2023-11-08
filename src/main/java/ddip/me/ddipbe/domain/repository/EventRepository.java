package ddip.me.ddipbe.domain.repository;

import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.domain.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByUuid(UUID uuid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Event e where e.uuid = :uuid")
    Optional<Event> findByUuidForUpdate(UUID uuid);

    List<Event> findAllByMember(Member member);

    List<Event> findAllByStartDateTimeBeforeAndEndDateTimeAfter(LocalDateTime start, LocalDateTime end);
}
