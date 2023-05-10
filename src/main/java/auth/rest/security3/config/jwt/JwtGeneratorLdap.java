package auth.rest.security3.config.jwt;

import auth.rest.security3.domain.People;
import auth.rest.security3.domain.Person;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.*;

@Component
@Qualifier
public class JwtGeneratorLdap {
    private KeyStore keyStore;

    @Value("${ldap.admin.list}")
    private String[] adminList;

    public JwtGeneratorLdap() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream inputStream = getClass().getResourceAsStream("/sm.jks");
            keyStore.load(inputStream, "password".toCharArray());
        }catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateTokenLdap(Person person) {
        return Jwts.builder()
                .setSubject(person.getFullname())
                .claim("email", person.getMail())
                .claim("description", person.getDescription())
                .claim("id", person.getUid())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + JwtConstants.JWT_EXPIRATION))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public String tokenLdap(People people) {
        return Jwts.builder()
                .setSubject(people.getId().toString())
                .claim("roles", Arrays.asList(adminList).contains(people.getEmailAddress()) ? List.of(JwtRoles.ADMIN) : List.of(JwtRoles.USER))
                .claim("emailAddress", people.getEmailAddress())
                .claim("emailContext", people.getEmailContext())
                .claim("inMigrationProcess", people.isInMigrationProcess())
                .claim("context", people.getContext())
                .claim("lastLoginDate", people.getLastLoginDate())
                .claim("lastBadLoginDate", people.getLastBadLoginDate())
                .claim("businessId", people.getBusinessId())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(new Date().getTime() + JwtConstants.JWT_EXPIRATION))
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    public String getEmailContextFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getPublickey())
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("emailContext");
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("sm", "password".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new RuntimeException("Problem with private key from keystore");
        }
    }

    public boolean validateTokenLdap(String jwt) {
        Jwts
                .parser()
                .setSigningKey(getPublickey())
                .parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublickey() {
        try {
            return keyStore.getCertificate("sm").getPublicKey();
        } catch (KeyStoreException e) {
            throw new RuntimeException("Problem with public key ");
        }
    }

    public String getIdFromJwtLdap(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getPublickey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public List<String> getRolesFromLdapJwt(String tokenFromCookie) {
        List<String> roles = new ArrayList<>();
        Claims claims = Jwts.parser()
                .setSigningKey(getPublickey())
                .parseClaimsJws(tokenFromCookie)
                .getBody();
        List<String> authorities = (List<String>) claims.get("roles");
        for (String authority : authorities) {
            roles.add(authority);
        }
        return roles;
    }
}
