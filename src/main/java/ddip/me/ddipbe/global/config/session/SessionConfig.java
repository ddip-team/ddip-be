package ddip.me.ddipbe.global.config.session;

import ddip.me.ddipbe.global.config.session.SessionMemberIdResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class SessionConfig implements WebMvcConfigurer {

    @Autowired
    private SessionMemberIdResolver sessionMemberIdResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(sessionMemberIdResolver);
    }
}
