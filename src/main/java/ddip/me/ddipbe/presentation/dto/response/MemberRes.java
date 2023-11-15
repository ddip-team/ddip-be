package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.Member;

public record MemberRes(long id, String email) {
    public MemberRes(Member member) {
        this(member.getId(), member.getEmail());
    }
}
