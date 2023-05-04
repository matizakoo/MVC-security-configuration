package auth.rest.security3.config.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.io.IOException;

public class ClearJwtCookie extends SimpleUrlLogoutSuccessHandler {
    private final String[] cookiesToClear;

    public ClearJwtCookie(String... cookiesToClear) {
        this.cookiesToClear = cookiesToClear;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        Cookie jwtCookie = new Cookie(CustomAuthHeader.AUTHORIZATION_HEADER, null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60 * 30);
        response.addCookie(jwtCookie);

        super.onLogoutSuccess(request, response, authentication);
    }
}
