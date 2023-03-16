package auth.rest.security3.controller;

import auth.rest.security3.config.jwt.JwtGenerator;
import auth.rest.security3.config.jwt.JwtTokenUtil;
import auth.rest.security3.dto.JwtResponse;
import auth.rest.security3.dto.UserCredentialsDTO;
import auth.rest.security3.service.CookieService;
import auth.rest.security3.service.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
@Slf4j
@AllArgsConstructor
public class AuthController {
    private final JwtGenerator jwtGenerator;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
//    @PostMapping("/auth")
//    public ResponseEntity<?> signIn(@RequestBody UserCredentialsDTO userCredentialsDTO, HttpServletRequest request, HttpServletResponse response){
//        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(userCredentialsDTO.getUsername());
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.getAuthorities());
//
//        if(bCryptPasswordEncoder.matches(userCredentialsDTO.getPassword(), userDetails.getPassword())){
//            final String token = jwtTokenUtil.generateToken(userDetails);
//            response.addCookie(new CookieService("jwt", jwtGenerator.generateToken(authenticationToken), true, request.isSecure(), "/", jwtGenerator.getExpirationInMillis()));
//            return ResponseEntity.ok(new JwtResponse(token));
//        }
//        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//    }

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
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
