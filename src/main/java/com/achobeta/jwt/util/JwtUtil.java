package com.achobeta.jwt.util;

import com.achobeta.common.enums.GlobalServiceStatusCode;
import com.achobeta.exception.GlobalServiceException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    /**
     * 过期时间小于该值就刷新token
     */
    public static final long REFRESH_TIME = TimeUnit.MINUTES.toMillis(20);

    /**
     * 生成jwt
     * 使用Hs256算法
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */

    public static String createJWT(@NotNull SecretKey secretKey, @NotNull long ttlMillis, @NotNull Map<String, Object> claims) {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey)
                // 设置过期时间
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥
     * @param token     加密后的token
     * @return
     */
    public static Claims parseJWT(@NotNull SecretKey secretKey, @NotNull String token) {
        Claims claims = null;
        try {
            // 得到DefaultJwtParser
            claims = Jwts.parser()
                    // 设置签名的秘钥
                    .setSigningKey(secretKey)
                    // 设置需要解析的jwt
                    .parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            String message = String.format("无效的token:'%s'", token);
            throw new GlobalServiceException(message, GlobalServiceStatusCode.USER_ACCOUNT_USE_BY_OTHERS);
        }
        return claims;
    }

    public static Date getTokenExpiration(@NotNull SecretKey secretKey, @NotNull String token) {
        // TODO 可能会成为性能瓶颈
        Claims claims = parseJWT(secretKey, token);
        //返回该token的过期时间
        return claims.getExpiration();
    }

    // 通过明文密钥生成加密后的秘钥 secretKey
    public static SecretKey generalKey(@NotNull String secretKey) {
        byte[] encodedKey = Base64.getDecoder().decode(secretKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        log.info("生成的key -> {}", key.getAlgorithm());
        return key;
    }

    public static boolean judgeApproachExpiration(String token, SecretKey secretKey, long refreshTime) {
        long cur = System.currentTimeMillis();
        long exp = getTokenExpiration(secretKey, token).getTime();
        return (exp - cur) < refreshTime;
    }
}
