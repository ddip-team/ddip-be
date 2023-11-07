package ddip.me.ddipbe.presentation.dto.request;

import lombok.Getter;

@Getter
public class SigninReq {

    private final String email;
    private final String password;

    public SigninReq(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
