package com.maximilian.practice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.maximilian.practice.config.JwtConfig;
import com.maximilian.practice.domain.auth.AuthInfoResponse;
import com.maximilian.practice.domain.auth.Role;
import com.maximilian.practice.model.UserCredentials;
import com.maximilian.practice.rest.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final JwtConfig config;
    private final CustomUserDetailsService userService;

    public Authentication getAuthenticationFromToken(String token) {
        DecodedJWT decodedJWT = getDecodedJwt(token);
        String username = decodedJWT.getSubject();
        Collection<SimpleGrantedAuthority> authorities = getAuthorities(username, decodedJWT);
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    public String createTokenFor(UserCredentials user, String issuerUrl) {
        Algorithm algorithm = Algorithm.HMAC256(config.getSecret().getBytes(StandardCharsets.UTF_8));
        JWTCreator.Builder accessTokenBuilder = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Instant.now().plusMillis(config.getAccessTokenExpirationMS()))
                .withIssuer(issuerUrl)
                .withClaim(config.getRolesKey(), user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim(config.getUserIdKey(), user.getId());
        return accessTokenBuilder.sign(algorithm);
    }

    public AuthInfoResponse getAuthInfoFromToken(String token) {
        if (token == null) {
            throw new GeneralException("Missing token.");
        }
        if (token.startsWith(config.getAccessTokenPrefix())) {
            token = token.substring(config.getAccessTokenPrefix().length());
        }
        DecodedJWT decodedJwt = getDecodedJwt(token);
        String username = decodedJwt.getSubject();
        Collection<SimpleGrantedAuthority> authorities = getAuthorities(username, decodedJwt);
        Long userId = decodedJwt.getClaim(config.getUserIdKey()).as(Long.class);
        return new AuthInfoResponse(
                userId,
                username,
                authorities.stream().map(r -> Role.getByName(r.getAuthority())).toList()
        );
    }

    private DecodedJWT getDecodedJwt(String token) {
        Algorithm algorithm = Algorithm.HMAC256(config.getSecret().getBytes(StandardCharsets.UTF_8));
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    private Collection<SimpleGrantedAuthority> getAuthorities(String username, DecodedJWT decodedJwt) {
        UserCredentials user = userService.findByUsername(username);
        if (!user.isEnabled()) {
            throw new GeneralException("User is disabled. Token considered as invalid.", HttpStatus.UNAUTHORIZED);
        }
        String[] roles = decodedJwt.getClaim(config.getRolesKey()).asArray(String.class);
        Collection<SimpleGrantedAuthority> authorityCollection = new ArrayList<>();
        if (roles != null) {
            Arrays.stream(roles).forEach(role -> {
                if (user.getAuthorities().stream().anyMatch(r -> r.getAuthority().equalsIgnoreCase(role))) {
                    authorityCollection.add(new SimpleGrantedAuthority(role));
                }
            });
            // case when roles inside a token are not same as roles in real database
            if (authorityCollection.isEmpty()) {
                throw new GeneralException("Data inside a token is invalid.", HttpStatus.BAD_REQUEST);
            }
        }
        return authorityCollection;
    }

}
