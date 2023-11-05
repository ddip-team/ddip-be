package ddip.me.ddipbe.presentation.dto;

import ddip.me.ddipbe.application.MemberService;
import ddip.me.ddipbe.domain.Member;
import ddip.me.ddipbe.global.annotation.SessionMemberId;
import ddip.me.ddipbe.global.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("me")
    public MemberMeResponse getMe(@SessionMemberId Long memberId) {
        Member member = memberService.findById(memberId);
        return new MemberMeResponse(member.getId(), member.getEmail());
    }

    @PostMapping("signup")
    public long signup(@RequestBody SignupRequest signupRequest) {
        return memberService.signup(signupRequest.getEmail(), signupRequest.getPassword());
    }

    @PostMapping("signin")
    public long signin(@RequestBody SigninRequest signinRequest, HttpServletRequest request) {
        long memberId = memberService.signin(signinRequest.getEmail(), signinRequest.getPassword());
        SessionUtil.setMemberId(request.getSession(), memberId);
        return memberId;
    }

    @PostMapping("signout")
    public void signout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}