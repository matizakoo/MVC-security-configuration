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
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        if (cookiesToClear != null) {
            for (String cookieName : cookiesToClear) {
                Cookie cookie = new Cookie(cookieName, null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        super.onLogoutSuccess(request, response, authentication);
    }
}
