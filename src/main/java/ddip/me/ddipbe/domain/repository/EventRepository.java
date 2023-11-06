package ddip.me.ddipbe.domain.repository;

import ddip.me.ddipbe.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event,Long> {

    Optional<Event> findByUuid(UUID uuid);
}
