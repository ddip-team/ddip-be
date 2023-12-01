package ddip.me.ddipbe.application.dto;

import ddip.me.ddipbe.domain.Member;

public record MemberDto(long id, String email) {
    public MemberDto(Member member) {
        this(member.getId(), member.getEmail());
    }
}
