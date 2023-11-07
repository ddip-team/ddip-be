package ddip.me.ddipbe.presentation.dto.response;

import lombok.Getter;

@Getter
public class MemberMeRes {

    private final long id;
    private final String email;

    public MemberMeRes(long id, String email) {
        this.id = id;
        this.email = email;
    }
}