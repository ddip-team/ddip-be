package ddip.me.ddipbe.domain.repository;

import ddip.me.ddipbe.domain.Member;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {

    Optional<Member> findById(Long id);

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Member save(Member member);
}
