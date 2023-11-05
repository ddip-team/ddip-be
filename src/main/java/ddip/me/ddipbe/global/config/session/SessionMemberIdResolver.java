package ddip.me.ddipbe.global.config.session;

import ddip.me.ddipbe.global.annotation.SessionMemberId;
import ddip.me.ddipbe.global.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class SessionMemberIdResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isAnnotated = parameter.getParameterAnnotation(SessionMemberId.class) != null;
        boolean isAppropriateParamType
                = parameter.getParameterType().equals(Long.class);
        return isAnnotated && isAppropriateParamType;
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        assert request != null;

        HttpSession httpSession = request.getSession(false);
        if (httpSession != null) {
            return SessionUtil.getMemberId(httpSession);
        }
        return null;
    }
}
