package ddip.me.ddipbe.domain.repository;

import ddip.me.ddipbe.domain.SuccessRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SuccessRecordRepository extends JpaRepository<SuccessRecord, Long> {

    @Query("SELECT p FROM SuccessRecord p JOIN FETCH p.event WHERE p.event.uuid = :uuid AND p.token = :token")
    Optional<SuccessRecord> findByEventUuidAndToken(UUID uuid, String token);  // TODO: 쿼리 최적화

    boolean existsByEventUuidAndToken(UUID uuid, String token);

    Page<SuccessRecord> findAllByEventUuid(UUID uuid, Pageable pageable);
}
