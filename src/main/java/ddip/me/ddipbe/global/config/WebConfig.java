package ddip.me.ddipbe.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableConfigurationProperties(WebConfigProperties.class)
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    WebConfigProperties webConfigProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(webConfigProperties.cors().allowedOrigins().toArray(String[]::new))
                .allowedMethods(webConfigProperties.cors().allowedMethods().toArray(String[]::new))
                .allowedHeaders(webConfigProperties.cors().allowedHeaders().toArray(String[]::new))
                .allowCredentials(webConfigProperties.cors().allowCredentials())
                .maxAge(3600);
    }
}
