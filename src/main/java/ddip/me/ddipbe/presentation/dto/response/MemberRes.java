package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Member;
import lombok.Getter;

@Getter
public class MemberRes {

    private final long id;
    private final String email;

    public MemberRes(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
    }
}
