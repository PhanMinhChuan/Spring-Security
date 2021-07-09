package zuka.cloud.demo.controller;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zuka.cloud.demo.jwt.JwtAuthenticationFilter;
import zuka.cloud.demo.jwt.JwtTokenProvider;
import zuka.cloud.demo.model.User;
import zuka.cloud.demo.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class JwtValidatorController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @PostMapping("/jwt")
    public String getIdRoleFromJwt(HttpServletRequest request) {  //@RequestParam(value="jwt", defaultValue = "") String jwt
        String jwt = jwtAuthenticationFilter.getJwtFromRequest(request);
        String JWT_SECRET = "MinhChuan";
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt);
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(jwt)
                    .getBody();

            int idUser =  Integer.parseInt(claims.getSubject());
            User userJwt = userRepository.findById(idUser);
            return "UserId: " + idUser + ", UserRole:";
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return null;
    }
}
