package ddip.me.ddipbe.application;

import ddip.me.ddipbe.application.dto.MemberDto;
import ddip.me.ddipbe.domain.Member;
import ddip.me.ddipbe.domain.exception.InvalidPasswordException;
import ddip.me.ddipbe.domain.exception.MemberNotFoundException;
import ddip.me.ddipbe.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "members", key = "#memberId")
    public MemberDto findById(long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        return new MemberDto(member);
    }

    public long signin(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new InvalidPasswordException();
        }

        return member.getId();
    }
}
