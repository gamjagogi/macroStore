package com.hjson.macrostore.core.auth.jwt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hjson.macrostore.model.token.RefreshTokenEntity;
import com.hjson.macrostore.model.token.TokenStatus;
import com.hjson.macrostore.model.user.User;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class MyJwtProvider {

    private static final String SUBJECT = "macroStore";
    private static final int EXP_ACCESS = 1000 * 60 * 60 * 24; //24시간
    private static final int EXP_REFRESH = 1000 * 60 * 60 * 24 * 7; // 7일
    public static final String TOKEN_PREFIX = "Bearer";

    public static final String HEADER = "Authorization";
    private static final String HEADER_REFRESH = "RefreshToken";

    private static final String ACCESS_SECRET = "gamja";
    private static final String REFRESH_SECRET = "gamjagogi";

    public static String createAccess(User user){
        String jwt = JWT.create()
                .withSubject(SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis()+ EXP_ACCESS))
                .withClaim("id",user.getId())
                .withClaim("role",user.getRole())
                .sign(Algorithm.HMAC256(ACCESS_SECRET));
        return TOKEN_PREFIX + jwt;
    }

    public static Pair<String, RefreshTokenEntity> createRefresh(){

        String uuid = UUID.randomUUID().toString();

        RefreshTokenEntity refreshToken = new RefreshTokenEntity(uuid, TokenStatus.VALID);

        String jwt = JWT.create()
                .withSubject(SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXP_REFRESH))
                .withClaim("uuid", uuid)
                .sign(Algorithm.HMAC256(REFRESH_SECRET));
        return Pair.of(TOKEN_PREFIX+jwt ,refreshToken);

    }

    public static DecodedJWT verify(String jwt) throws SignatureVerificationException,TokenExpiredException{
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(ACCESS_SECRET))
                .build().verify(jwt);
        return decodedJWT;
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(REFRESH_SECRET))
                    .build()
                    .verify(refreshToken.replace(TOKEN_PREFIX, ""));
            String uuid = decodedJWT.getClaim("uuid").asString();
            // 리프레시 토큰 유효성 검사 로직 추가
            RefreshTokenEntity refreshTokenEntity = // 리프레시 토큰 유효성 검사를 수행하고 RefreshTokenEntity 반환
            return refreshTokenEntity != null && refreshTokenEntity.getStatus() == TokenStatus.VALID;
        } catch (SignatureVerificationException sve) {
            return false;
        } catch (TokenExpiredException tee) {
            return false;
        }
    }
}
