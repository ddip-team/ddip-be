package ddip.me.ddipbe.domain.repository;

import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.domain.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends Repository<Event, Long> {

    @Query("select e from Event e join fetch e.member m where e.uuid = :uuid")
    Optional<Event> findByUuid(UUID uuid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Event e where e.uuid = :uuid")
    Optional<Event> findByUuidForUpdate(UUID uuid);

    Page<Event> findAllByMember(Member member, Pageable pageable);

    @Query("select e from Event e where e.member = :member and e.eventDuration.startDateTime <= :now and e.eventDuration.endDateTime >= :now")
    Page<Event> findAllByMemberAndOpen(Member member, ZonedDateTime now, Pageable pageable);

    Event save(Event event);

    void delete(Event event);
}
