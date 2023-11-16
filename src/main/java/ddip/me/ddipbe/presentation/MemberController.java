package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.MemberService;
import ddip.me.ddipbe.domain.Member;
import ddip.me.ddipbe.global.annotation.SessionMemberId;
import ddip.me.ddipbe.global.dto.ResponseEnvelope;
import ddip.me.ddipbe.global.util.SessionUtil;
import ddip.me.ddipbe.presentation.dto.request.SigninReq;
import ddip.me.ddipbe.presentation.dto.request.SignupReq;
import ddip.me.ddipbe.presentation.dto.response.MemberIdRes;
import ddip.me.ddipbe.presentation.dto.response.MemberMeRes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("me")
    public ResponseEnvelope<MemberMeRes> getMe(@SessionMemberId Long memberId) {
        Member member = memberService.findById(memberId);

        return ResponseEnvelope.of(new MemberMeRes(member.getId(), member.getEmail()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("signup")
    public ResponseEnvelope<MemberIdRes> signup(@Valid @RequestBody SignupReq signupRequest) {
        Member member = memberService.signup(signupRequest.email(), signupRequest.password());

        return ResponseEnvelope.of(new MemberIdRes(member.getId()));
    }

    @PostMapping("signin")
    public ResponseEnvelope<MemberIdRes> signin(@Valid @RequestBody SigninReq signinRequest, HttpServletRequest request) {
        long memberId = memberService.signin(signinRequest.email(), signinRequest.password());
        SessionUtil.setMemberId(request.getSession(), memberId);

        return ResponseEnvelope.of(new MemberIdRes(memberId));
    }

    @PostMapping("signout")
    public ResponseEnvelope<?> signout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEnvelope.of(null);
    }
}