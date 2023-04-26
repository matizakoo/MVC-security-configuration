package auth.rest.security3.controller;

import auth.rest.security3.config.bruteforce.BruteForceService;
import auth.rest.security3.config.jwt.CustomAuthHeader;
import auth.rest.security3.config.jwt.JwtGenerator;
import auth.rest.security3.config.twofa.SMSService;
import auth.rest.security3.domain.Users;
import auth.rest.security3.service.UsersService;
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

import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping(AdminController.url)
@Slf4j
public class AdminController {
    public static final String url = "/admin";
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SMSService smsService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private BruteForceService bruteForceService;
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
                "    <label for=\"twofa\">2FA:</label>\n" +
                "    <input type=\"text\" id=\"twofa\" name=\"twofa\" required=\"required\" />\n" +
                "    <br/>\n" +
                "    <button type=\"submit\">sign in</button>\n" +
                "</form>\n" +
                "<form action=\"/admin/button\" method=\"post\">\n" +
                "    <label for=\"username\">Username:</label>\n" +
                "    <input type=\"text\" id=\"username\" name=\"username\" required=\"required\" autofocus=\"autofocus\" />\n" +
                "    <br/>\n" +
                "    <label for=\"password\">Password:</label>\n" +
                "    <input type=\"password\" id=\"password\" name=\"password\" required=\"required\" />\n" +
                "    <br/>\n" +
                "    <button type=\"submit\">2fa</button>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>";
    }


    @PostMapping("/button")
    public ResponseEntity<Object> button(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("button");
        String twofa = String.valueOf(new Random().nextInt(9999) + 1000);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getParameter("username"), request.getParameter("password")));
            Users users = usersService.findByUsername(request.getParameter("username")).get();
            users.setTwofa(twofa);
            System.out.println(twofa);
            smsService.send2FACode(users.getNumber(), twofa);
            usersService.save(users);
        } catch (AuthenticationException e) {
            String username = request.getParameter("username");
            bruteForceService.registerLoginFailure(username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(HttpServletRequest request, HttpServletResponse response){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getParameter("username"), request.getParameter("password")));
            System.out.println("po auth");
            if(bruteForceService.isBruteForce(request.getParameter("username"))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            System.out.println("po brute force");
            Users users = usersService.findByUsername(request.getParameter("username")).get();
            System.out.println("po findby " + request.getParameter("twofa") + " " + users.getTwofa());
            if(!users.getTwofa().equals(request.getParameter("twofa"))){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            System.out.println("po wsyzsktim");

            bruteForceService.resetBruteCounter(request.getParameter("username"));

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
            String username = request.getParameter("username");
            bruteForceService.registerLoginFailure(username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
//        System.out.println(AdminController.class + " logout " + request.getParameter("username") + " " + request.getParameter("password"));
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
