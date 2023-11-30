package ddip.me.ddipbe.application;

import ddip.me.ddipbe.domain.Member;
import ddip.me.ddipbe.domain.exception.AlreadySignedUpException;
import ddip.me.ddipbe.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public long signup(String email, String password) {
        if (memberRepository.existsByEmail(email)) {
            throw new AlreadySignedUpException();
        }

        Member member = new Member(email, passwordEncoder.encode(password));
        member = memberRepository.save(member);

        return member.getId();
    }
}
