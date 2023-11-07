package ddip.me.ddipbe.presentation.dto.request;

import lombok.Getter;

@Getter
public class SignupReq {

    private final String email;
    private final String password;

    public SignupReq(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
