package priam.right.security;

import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * This configuration ensures that all requests under "/api" are authenticated using OAuth2.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Environment env;

    public SecurityConfig(Environment env) {
        this.env = env;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .and()
                .oauth2Login()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) ->
                        response.sendRedirect(
                                env.getProperty("spring.security.oauth2.client.provider.provider-client.authorization-uri")
                        )
                );
    }
}
