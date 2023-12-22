package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.MemberCommandService;
import ddip.me.ddipbe.application.MemberQueryService;
import ddip.me.ddipbe.application.dto.MemberDto;
import ddip.me.ddipbe.global.annotation.SessionMemberId;
import ddip.me.ddipbe.global.dto.ResponseEnvelope;
import ddip.me.ddipbe.global.util.SessionUtil;
import ddip.me.ddipbe.presentation.dto.request.SigninReq;
import ddip.me.ddipbe.presentation.dto.request.SignupReq;
import ddip.me.ddipbe.presentation.dto.response.MemberIdRes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    @GetMapping("me")
    @Cacheable(value = "members", key = "#memberId")
    public ResponseEnvelope<MemberDto> getMe(@SessionMemberId Long memberId) {
        MemberDto member = memberQueryService.findById(memberId);

        return ResponseEnvelope.of(member);
    }

    @PostMapping("signin")
    public ResponseEnvelope<MemberIdRes> signin(@Valid @RequestBody SigninReq signinRequest, HttpServletRequest request) {
        long memberId = memberQueryService.signin(signinRequest.email(), signinRequest.password());
        SessionUtil.setMemberId(request.getSession(), memberId);

        return ResponseEnvelope.of(new MemberIdRes(memberId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("signup")
    public ResponseEnvelope<MemberIdRes> signup(@Valid @RequestBody SignupReq signupRequest) {
        long memberId = memberCommandService.signup(signupRequest.email(), signupRequest.password());

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