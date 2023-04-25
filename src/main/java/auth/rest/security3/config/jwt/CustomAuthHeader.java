package auth.rest.security3.config.jwt;

import auth.rest.security3.service.CookieService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomAuthHeader implements AuthenticationSuccessHandler {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtGenerator jwtGenerator;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
//            System.out.println(CustomAuthHeader.class.toString() + " " + "test");
            String token = jwtGenerator.generateToken(authentication);
            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setMaxAge(24 * 60 * 60 * 10);
            cookie.setPath("/");
            response.addCookie(cookie);
    }
}
