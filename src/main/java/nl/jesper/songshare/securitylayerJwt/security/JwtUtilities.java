package nl.jesper.songshare.securitylayerJwt.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;


/**
 * A utility class that provides methods for generating, validating, and extracting data from JSON Web Tokens (JWT).
 */
@Slf4j
@Component
public class JwtUtilities {

    /**
     * The secret key used for signing JWTs.
     */
    private final SecretKey secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * The duration (in milliseconds) that a JWT is valid for.
     */
    private static final long jwtExpiration = 1000 * 60 * 60 * 10;

    /**
     * Extracts the username from a JWT.
     * @param token The JWT to extract the username from.
     * @return The username contained within the JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts all claims from a JWT.
     *
     * @param token The JWT to extract the claims from.
     * @return A {@link Claims} object containing all of the claims from the JWT.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Extracts a specific claim from a JWT.
     * @param token          The JWT to extract the claim from.
     * @param claimsResolver A function that extracts the desired claim from a {@link Claims} object.
     * @param <T>            The type of the claim to extract.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts the expiration date from a JWT.
     * @param token The JWT to extract the expiration date from.
     * @return The expiration date of the JWT.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Validates a JWT.
     * @param token        The JWT to validate.
     * @param userDetails The {@link UserDetails} object representing the user associated with the JWT.
     * @return {@code true} if the JWT is valid and associated with the given user, {@code false} otherwise.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractUsername(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks whether a JWT is expired.
     * @param token The JWT to check.
     * @return {@code true} if the JWT is expired, {@code false} otherwise.
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT.
     * @param username The username of the user associated with the JWT.
     * @param roles The roles assigned to the user.
     * @return The generated JWT.
     */
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder().setSubject(username).claim("role", roles).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    /**
     * Validates a given JWT token.
     * @param token The JWT token to be validated.
     * @return True if the token is valid, false if it is not.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }

    /**
     * Retrieves the JWT token from the Authorization header of the HTTP request.
     * @param httpServletRequest The HttpServletRequest object that contains the Authorization header.
     * @return The JWT token if it exists in the Authorization header and starts with "Bearer ", or null otherwise.
     */
    public String getToken(HttpServletRequest httpServletRequest) {
        final String bearerToken = httpServletRequest.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } // The part after "Bearer "
        return null;
    }

}
