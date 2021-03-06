package zuka.cloud.demo.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {
    // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
    private final String JWT_SECRET_KEY = "MinhChuan";

    //Thời gian có hiệu lực của chuỗi jwt
    private final long JWT_EXPIRATION = 60000;

    // Tạo ra jwt từ thông tin user
    public String generateToken(org.springframework.security.core.userdetails.User user) {
        // Lấy thông tin user
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        // Tạo chuỗi json web token từ id của user.
        return Jwts.builder()
                .setSubject(user.getUsername())                         //"sub"     "sub": "batman"
                .setIssuedAt(now)                                       //"iss"     "iss": "jira:1314039",
                .setExpiration(expiryDate)                              //"exp"     "exp": 1300819380,
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
