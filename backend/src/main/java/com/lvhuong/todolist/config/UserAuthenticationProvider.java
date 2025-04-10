package com.lvhuong.todolist.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lvhuong.todolist.domains.dto.UserDto;
import com.lvhuong.todolist.domains.entities.UserEntity;
import com.lvhuong.todolist.mappers.Mapper;
import com.lvhuong.todolist.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class UserAuthenticationProvider {

    @Value("${security.jwt.token.secret-key:secret-value}")
    private String secretKey;

    private final UserService userService;
    private final Mapper<UserEntity, UserDto> userMapper;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String login){
        Date now = new Date();
        // in hours
        float validityTime = 1f; // in hours
        Date validity = new Date(now.getTime() + (long)(3_600_000 * validityTime));

        return JWT.create()
                .withIssuer(login)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication validateToken(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();

        DecodedJWT decodedJWT = verifier.verify(token);

        UserDto userDto = userService.findByLogin(decodedJWT.getIssuer());

        return new UsernamePasswordAuthenticationToken(userDto, null, Collections.emptyList());
    }
}


