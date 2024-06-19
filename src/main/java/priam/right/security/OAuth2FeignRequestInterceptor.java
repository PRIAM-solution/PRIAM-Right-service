package priam.right.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;

public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AbstractOAuth2Token) {
            AbstractOAuth2Token oauthToken = (AbstractOAuth2Token) authentication.getPrincipal();
            requestTemplate.header("Authorization", "Bearer " + oauthToken.getTokenValue());
        }
    }
}
