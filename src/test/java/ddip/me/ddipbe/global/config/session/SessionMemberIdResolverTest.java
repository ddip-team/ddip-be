package ddip.me.ddipbe.global.config.session;

import ddip.me.ddipbe.global.annotation.SessionMemberId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SessionMemberIdResolverTest {

    @DisplayName("어노테이션과 타입이 일치하면 true를 반환한다.")
    @Test
    public void supportsParameter_withAnnotationAndCorrectType_returnsTrue() {
        MethodParameter methodParameter = Mockito.mock(MethodParameter.class);
        when(methodParameter.getParameterAnnotation(SessionMemberId.class)).thenReturn(Mockito.mock(SessionMemberId.class));
        when(methodParameter.getParameterType()).thenReturn((Class) Long.class);

        SessionMemberIdResolver resolver = new SessionMemberIdResolver();

        assertThat(resolver.supportsParameter(methodParameter)).isTrue();
    }

    @DisplayName("어노테이션이 없으면 false를 반환한다.")
    @Test
    public void supportsParameter_withoutAnnotation_returnsFalse() {
        MethodParameter methodParameter = Mockito.mock(MethodParameter.class);
        when(methodParameter.getParameterAnnotation(SessionMemberId.class)).thenReturn(null);
        when(methodParameter.getParameterType()).thenReturn((Class) Long.class);

        SessionMemberIdResolver resolver = new SessionMemberIdResolver();

        assertThat(resolver.supportsParameter(methodParameter)).isFalse();
    }

    @DisplayName("타입이 일치하지 않으면 false를 반환한다.")
    @Test
    public void supportsParameter_withIncorrectType_returnsFalse() {
        MethodParameter methodParameter = Mockito.mock(MethodParameter.class);
        when(methodParameter.getParameterAnnotation(SessionMemberId.class)).thenReturn(Mockito.mock(SessionMemberId.class));
        when(methodParameter.getParameterType()).thenReturn((Class) Object.class);

        SessionMemberIdResolver resolver = new SessionMemberIdResolver();

        assertThat(resolver.supportsParameter(methodParameter)).isFalse();
    }

    @DisplayName("타입이 primitive type이면 false를 반환한다.")
    @Test
    public void supportsParameter_withPrimitiveType_returnsFalse() {
        MethodParameter methodParameter = Mockito.mock(MethodParameter.class);
        when(methodParameter.getParameterAnnotation(SessionMemberId.class)).thenReturn(Mockito.mock(SessionMemberId.class));
        when(methodParameter.getParameterType()).thenReturn((Class) long.class);

        SessionMemberIdResolver resolver = new SessionMemberIdResolver();

        assertThat(resolver.supportsParameter(methodParameter)).isFalse();
    }
}