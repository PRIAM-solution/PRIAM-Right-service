package priam.right.security;

import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        http.authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .and()
                .oauth2Login()
                .successHandler(new SimpleUrlAuthenticationSuccessHandler() {
                    @Override
                    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
                        // Get the original URL stored in the session or request parameter
                        String originalUrl = (String) request.getSession().getAttribute("originalUrl");
                        if (originalUrl != null) {
                            request.getSession().removeAttribute("originalUrl"); // Remove it from session
                            return originalUrl;
                        } else {
                            // If no original URL found, redirect to a default URL
                            return "/";
                        }
                    }
                })
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendRedirect(env.getProperty("spring.security.oauth2.client.provider.provider-client.authorization-uri")));
    }
}
