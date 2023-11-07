package ddip.me.ddipbe.presentation.dto.response;

import lombok.Getter;

@Getter
public class MemberIdRes {

    private final long memberId;

    public MemberIdRes(long memberId) {
        this.memberId = memberId;
    }
}
