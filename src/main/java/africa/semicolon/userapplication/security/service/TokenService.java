package africa.semicolon.userapplication.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import africa.semicolon.userapplication.security.roles.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String secretKey;

    private static final String ISSUER = "Zend_it";
    private static final long EXPIRATION_TIME = 864_000_000;

    public String generateToken(String username, List<Role> roles) {
        List<String> roleStrings = roles.stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());

        return JWT.create()
                .withSubject(username)
                .withIssuer(ISSUER)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("roles", roleStrings)
                .sign(Algorithm.HMAC512(secretKey.getBytes()));
    }

    public DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secretKey.getBytes()))
                .withIssuer(ISSUER)
                .build();

        try {
            return verifier.verify(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token");
        }
    }

    public List<Role> getRolesFromToken(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        List<String> roleStrings = decodedJWT.getClaim("roles").asList(String.class);
        return roleStrings.stream()
                .map(Role::valueOf)
                .collect(Collectors.toList());
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getSubject();
    }
}
