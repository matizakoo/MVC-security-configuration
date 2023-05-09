package auth.rest.security3.config.jwt;

import auth.rest.security3.domain.Person;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.IOException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Date;

@Component
public class JwtGeneratorLdap {
    private KeyStore keyStore;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream inputStream = getClass().getResourceAsStream("/sm.jks");
            keyStore.load(inputStream, "password".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | java.io.IOException e) {
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

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("sm", "password".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new RuntimeException("Exception occured while retrieving public key from keystore");
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
            throw new RuntimeException("Exception occured while retrieving public key from keystore");
        }
    }

    public String getUsernameFromJWTLdap(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getPublickey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
