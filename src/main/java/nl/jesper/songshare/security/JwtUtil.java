package nl.jesper.songshare.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import nl.jesper.songshare.SongShareConfig;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parserBuilder;


@Service
public class JwtUtil {

    private final SongShareConfig songShareConfig;
    private static final int expireInMs = 60000 * 30; //30 minutes
    private final static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public JwtUtil(SongShareConfig songShareConfig) {
        this.songShareConfig = songShareConfig;
    }

    public String generate(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(songShareConfig.getDomain_name())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireInMs))
                .signWith(key)
                .compact();
    }

    public boolean validate(String token) {
        if (getUsername(token) != null && isExpired(token)) {
            return true;
        }
        return false;
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public boolean isExpired(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

//        Jws<Claims> jws;
////
//        try {
//            jws = Jwts.parserBuilder()  // (1)
//                    .setSigningKey(key)         // (2)
//                    .build()                    // (3)
//                    .parseClaimsJws(token); // (4)
//
//            // we can safely trust the JWT
//
//        } catch (JwtException e) {       // (5)
//
//                // we *cannot* use the JWT as intended by its creator
//            }
    }
}
