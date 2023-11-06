package ddip.me.ddipbe.application;

import ddip.me.ddipbe.application.exception.AlreadySignedUpException;
import ddip.me.ddipbe.application.exception.InvalidPasswordException;
import ddip.me.ddipbe.application.exception.MemberNotFoundException;
import ddip.me.ddipbe.domain.Member;
import ddip.me.ddipbe.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    }

    public long signup(String email, String password) {
        if (memberRepository.existsByEmail(email)) {
            throw new AlreadySignedUpException();
        }

        Member member = new Member(email, passwordEncoder.encode(password));
        member = memberRepository.save(member);

        return member.getId();
    }

    public long signin(String email, String password) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new InvalidPasswordException();
        }

        return member.getId();
    }
}
