package auth.rest.security3.controller;

import auth.rest.security3.config.SecurityConfig;
import auth.rest.security3.config.jwt.JwtGenerator;
import auth.rest.security3.dto.UserCredentialsDTO;
import auth.rest.security3.service.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
@Slf4j
@AllArgsConstructor
public class AuthController {
    private final JwtGenerator jwtGenerator;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public String token(@RequestBody UserCredentialsDTO userCredentialsDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCredentialsDTO.getUsername(), userCredentialsDTO.getPassword()));
        return jwtGenerator.generateToken(authentication);
    }

    @GetMapping("/test")
    public String test() {
        return "test /jwt";
    }

    @DeleteMapping("/authlogout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie: cookies) {
                cookie.setValue(null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
