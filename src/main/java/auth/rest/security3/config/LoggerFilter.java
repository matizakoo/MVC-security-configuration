package auth.rest.security3.config;

import auth.rest.security3.config.jwt.JwtGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class LoggerFilter extends OncePerRequestFilter {
    @Autowired
    private JwtGenerator jwtGenerator;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("Name of sch: " + name);
            System.out.println(" username form jwt: " + jwtGenerator.getUsernameFromJWT(getJwtFromCookie(request)));
        } catch (Exception e){
            System.out.println("exception");
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromCookie(HttpServletRequest request){
        if(request.getCookies() == null)
            return null;
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
