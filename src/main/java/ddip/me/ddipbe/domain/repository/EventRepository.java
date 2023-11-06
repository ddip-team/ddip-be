package ddip.me.ddipbe.domain.repository;

import ddip.me.ddipbe.domain.Event;
import ddip.me.ddipbe.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByUuid(UUID uuid);

    List<Event> findAllByMember(Member member);

    List<Event> findAllByStartBeforeAndEndAfter(LocalDateTime start, LocalDateTime end);
}
