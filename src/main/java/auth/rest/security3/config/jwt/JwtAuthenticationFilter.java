package auth.rest.security3.config.jwt;

import auth.rest.security3.service.CookieService;
import auth.rest.security3.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.*;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JwtAuthenticationFilter");
        System.out.println("Filtr dane: " + request.getParameter("username") + " " +request.getParameter("password"));
        System.out.println(request.getRequestURI());
        if(request.getRequestURI().startsWith("/admin")) {
//            String jwtFromRequest = request.getHeader("Authorization").substring(7);
            String jwtFromRequest = getJwtFromCookie(request);
            System.out.println(jwtFromRequest);
            System.out.println("lec");
            if (StringUtils.hasText(jwtFromRequest) && jwtGenerator.validateToken(jwtFromRequest)) {
//            System.out.println("username form jwt: " + jwtGenerator.getUsernameFromJWT(tokenFromCookie));
                String username = jwtGenerator.getUsernameFromJWT(jwtFromRequest);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                List<String> roles = jwtGenerator.getRolesFromJwt(jwtFromRequest);
                Set<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
                System.out.println("User has role: " + userDetails.getUsername() + " " + userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
//        else {
//            String username = request.getParameter("username");
//            String password = request.getParameter("password");
//            System.out.println("Logowanie: " + username + "   " + password);
//            String header = request.getHeader("Authorization");
//            if(header == null){
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            System.out.println("Header: " + header);
////            String credentials = new String(Base64.getDecoder().decode(header.substring(6)));
////            String newUsername = credentials.split(":")[0];
////            String newPassword = credentials.split(":")[1];
////            System.out.println("Dane logowania z basic autha: " + newUsername + " " + newPassword + " " + credentials);
//
//
////            if(username == null || password == null){
////            if(newUsername == null || newPassword == null){
////                filterChain.doFilter(request, response);
////                System.out.println("check");
////                return;
////            }
//
//            if(username == null || password == null){
//                filterChain.doFilter(request, response);
//                System.out.println("check");
//                return;
//            }
//
//
//            System.out.println("check 2");
//            try {
//                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
//                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//                if(passwordEncoder.matches(password, userDetails.getPassword())){
//                    UsernamePasswordAuthenticationToken authRequest =
//                            new UsernamePasswordAuthenticationToken(
//                                    userDetails,
//                                    null,
//                                    userDetails.getAuthorities());
//                    authRequest.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authRequest);
//
//                    String newToken = jwtGenerator.generateToken(authRequest);
//                    CookieService jwtCookie = new CookieService("jwt", newToken, true, request.isSecure(),
//                            "/", jwtGenerator.getExpirationInMillis());
//
//                    response.addCookie(jwtCookie);
//                    filterChain.doFilter(request, response);
//                    return;
//                }
//            }catch (UsernameNotFoundException e){
//                filterChain.doFilter(request, response);
//            }
//        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromCookie(HttpServletRequest request){
        if(request.getCookies() == null)
            return null;
        Optional<Cookie> optionalCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "Authorization".equals(cookie.getName()))
                .findFirst();
        return optionalCookie.map(Cookie::getValue).orElse(null);
    }
}