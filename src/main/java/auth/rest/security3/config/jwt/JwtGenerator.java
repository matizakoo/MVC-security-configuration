package auth.rest.security3.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtGenerator {
    public String generateToken(Authentication authentication){
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", authentication.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + JwtConstants.JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, JwtConstants.JWT_SECRET)
                .compact();
    }

    public List<String> getRolesFromJwt(String tokenFromCookie){
        List<String> roles = new ArrayList<>();
        Claims claims = Jwts.parser()
                .setSigningKey(JwtConstants.JWT_SECRET)
                .parseClaimsJws(tokenFromCookie)
                .getBody();

        List<Map<String, String>> authorities = (List<Map<String, String>>) claims.get("roles");
        for (Map<String, String> authority : authorities) {
            roles.add(authority.get("authority"));
        }

        return roles;
    }

    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(JwtConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try{
            Jwts.parser()
                    .setSigningKey(JwtConstants.JWT_SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("JWT problem expired or incorrect :(");
        }
    }
}
