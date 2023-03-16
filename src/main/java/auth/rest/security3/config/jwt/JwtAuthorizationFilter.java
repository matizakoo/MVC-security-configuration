//package auth.rest.security3.config.jwt;
//
//import auth.rest.security3.service.CookieService;
//import auth.rest.security3.service.CustomUserDetailsService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@Slf4j
//public class JwtAuthorizationFilter extends OncePerRequestFilter {
//    @Autowired
//    private JwtGenerator jwtGenerator;
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        String username = request.getParameter("username");
//        System.out.println("req. parm: username " + username);
//        UserDetails userDetails = null;
//        try {
//            userDetails = customUserDetailsService.loadUserByUsername(username);
//            if(userDetails==null)
//                throw new IllegalArgumentException("Nie ma");
//        }catch (Exception e){
//            filterChain.doFilter(request, response);
//            return;
//        }
//        System.out.println("User has role: " + userDetails.getUsername() + " " + userDetails.getAuthorities());
//        System.out.println("po");
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.getAuthorities());
//        CookieService cookie = new CookieService("jwt", jwtGenerator.generateToken(authenticationToken), true, request.isSecure(), "/", jwtGenerator.getExpirationInMillis());
//        response.addCookie(cookie);
//        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        filterChain.doFilter(request, response);
//    }
//}
