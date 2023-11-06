package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.MemberService;
import ddip.me.ddipbe.domain.Member;
import ddip.me.ddipbe.global.annotation.SessionMemberId;
import ddip.me.ddipbe.global.dto.ResponseEnvelope;
import ddip.me.ddipbe.global.util.SessionUtil;
import ddip.me.ddipbe.presentation.dto.request.SigninRequest;
import ddip.me.ddipbe.presentation.dto.request.SignupRequest;
import ddip.me.ddipbe.presentation.dto.response.MemberIdResponse;
import ddip.me.ddipbe.presentation.dto.response.MemberMeResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("me")
    public ResponseEnvelope<MemberMeResponse> getMe(@SessionMemberId Long memberId) {
        Member member = memberService.findById(memberId);

        return new ResponseEnvelope<>(new MemberMeResponse(member.getId(), member.getEmail()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("signup")
    public ResponseEnvelope<MemberIdResponse> signup(@RequestBody SignupRequest signupRequest) {
        long signedUpMemberId = memberService.signup(signupRequest.getEmail(), signupRequest.getPassword());

        return new ResponseEnvelope<>(new MemberIdResponse(signedUpMemberId));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("signin")
    public ResponseEnvelope<MemberIdResponse>  signin(@RequestBody SigninRequest signinRequest, HttpServletRequest request) {
        long memberId = memberService.signin(signinRequest.getEmail(), signinRequest.getPassword());
        SessionUtil.setMemberId(request.getSession(), memberId);

        return new ResponseEnvelope<>(new MemberIdResponse(memberId));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("signout")
    public ResponseEnvelope<?> signout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return new ResponseEnvelope<>(null);
    }
}