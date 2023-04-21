package auth.rest.security3.controller;

import auth.rest.security3.config.jwt.CustomAuthHeader;
import auth.rest.security3.config.jwt.JwtGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AdminController.url)
@Slf4j
public class AdminController {
    public static final String url = "/admin";
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtGenerator jwtGenerator;
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @Operation(security = @SecurityRequirement(name = "admin"))
    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @GetMapping("/loginpage")
    public String loginPage(){
        System.out.println("/xD");
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\"/>\n" +
                "    <title>Login</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "hi helo\n" +
                "<form action=\"/admin/auth\" method=\"post\">\n" +
                "    <label for=\"username\">Username:</label>\n" +
                "    <input type=\"text\" id=\"username\" name=\"username\" required=\"required\" autofocus=\"autofocus\" />\n" +
                "    <br/>\n" +
                "    <label for=\"password\">Password:</label>\n" +
                "    <input type=\"password\" id=\"password\" name=\"password\" required=\"required\" />\n" +
                "    <br/>\n" +
                "    <button type=\"submit\">srititti</button>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>";
    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(HttpServletRequest request, HttpServletResponse response){
        System.out.println(AdminController.class + " auth " + request.getParameter("username") + " " + request.getParameter("password"));
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getParameter("username"), request.getParameter("password")));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);

            // Dodaj token JWT do ciasteczka
            Cookie jwtCookie = new Cookie(CustomAuthHeader.AUTHORIZATION_HEADER, token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60 * 30);

            response.addCookie(jwtCookie);
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println(AdminController.class + " logout " + request.getParameter("username") + " " + request.getParameter("password"));
        try {
            Cookie jwtCookie = new Cookie(CustomAuthHeader.AUTHORIZATION_HEADER, null);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60 * 30);

            response.addCookie(jwtCookie);
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
