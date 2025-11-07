package me.gustavoo.ecomerce.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import me.gustavoo.ecomerce.services.models.SessionDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Extrai o username (subject) do token
    public String extractUuid(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Função genérica para extrair qualquer "claim" do token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Gera um token para o usuário
    public String generateToken(SessionDetails sessionDetails) {
        return generateToken(new HashMap<>(), sessionDetails);
    }

    // Gera um token com claims extras
    public String generateToken(Map<String, Object> extraClaims, SessionDetails sessionDetails) {
        return Jwts.builder()
            .claims(extraClaims)
            .subject(sessionDetails.uuid.toString())
            .issuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
            .expiration(Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant())) // 24 horas
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    // Valida se o token pertence ao usuário e não está expirado
    public boolean isTokenValid(String token, SessionDetails sessionDetails) {
        final String uuid = extractUuid(token);
        return (uuid.equals(sessionDetails.uuid)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extrai todos os claims do token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    // Obtém a chave de assinatura a partir da secret key
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
