package com.micro.core.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.JwtMap;

import java.util.Date;
import java.util.Map;

public class JwtTools
{

    public static String getNewJwtToken(String subject, Map<String, Object> headerInfo, int expireTime, String privateSecret)
    {
        String token = Jwts.builder()
                .setSubject(subject)
                .setHeader(headerInfo)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime * 1000))
                .signWith(SignatureAlgorithm.HS512, privateSecret)
                .compact();
        return token;
    }

    public static JwtMap getJwtInfo(String token, String jwtkey, String predix)
    {
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(jwtkey)
                .parseClaimsJws(token.replace(predix, ""));
        String user =  claimsJws.getBody().getSubject();

        JwtMap jwtInfo = (JwtMap) claimsJws.getHeader();

        return jwtInfo;
    }
}
