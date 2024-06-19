package priam.right.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor() {
        return new OAuth2FeignRequestInterceptor();
    }
}