package ddip.me.ddipbe.domain.repository;

import ddip.me.ddipbe.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}
