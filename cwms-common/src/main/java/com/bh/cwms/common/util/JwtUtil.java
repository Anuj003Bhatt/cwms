package com.bh.cwms.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class JwtUtil {
    private static final String SECRET = "Secret";
    private static final String ROLES = "roles";
    private static final Long EXPIRATION = 36000000L;// EXPIRATION IN MILLIS ~ 10 Hours
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static String generateToken(String username, UUID userId, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        if (null != roles && !roles.isEmpty()) {
            claims.put("roles", roles);
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(signatureAlgorithm, SECRET).compact();
    }

    public static Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private static String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private static Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration)
                .before(new Date());
    }

    public static boolean validateToken(String token, String username) {
        final String userName = extractUserName(token);
        return (userName != null && userName.equals(username) && !isTokenExpired(token));
    }

    public static List<GrantedAuthority> generateAuthoritiesFromClaims(Claims claims) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (claims.get(ROLES) instanceof List<?>) {
            authorities.addAll(
                    generateAuthoritiesFromName((List<String>) claims.get(ROLES))
            );
        }
        return authorities;
    }

    private static List<GrantedAuthority> generateAuthoritiesFromName(List<String> auth) {
        return auth.stream().map(
                r -> (GrantedAuthority) () -> r
        ).toList();
    }
}
