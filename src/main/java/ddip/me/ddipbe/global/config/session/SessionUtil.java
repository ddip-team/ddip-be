package ddip.me.ddipbe.global.config.session;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    public static final String MEMBER_ID = "memberId";

    public static void setMemberId(HttpSession session, Long memberId) {
        session.setAttribute(MEMBER_ID, memberId);
    }

    public static Long getMemberId(HttpSession session) {
        return (Long) session.getAttribute(MEMBER_ID);
    }
}
