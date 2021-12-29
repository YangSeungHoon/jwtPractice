package com.jwt.practice.jwtpractice.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.practice.jwtpractice.config.auth.PrincipalDetails;
import com.jwt.practice.jwtpractice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

//원래 이 필터는 /login이라는 요청에서 usernmae,password를 전송하면 이 필터가 동작한다.
//그러나 restapi로 진행하면서 form로그인을 사용하지 않기때문에 Securityconfig에서 formlogin을 disable 해버렸다.
//그래서 해당 필터가 동작하지 않는다. 그러나 우리는 이 필터가 필요하기 떄문에 다시 달아줘야한다.

// 1. username, password 받아서
// 2. 정상인지 로그인 시도를 해본다. authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출된다.
// 그러면 loadUserByUsername가 실행된다.
// 3. PrincipalDetails를 세션에 담고 (권한 관리를 위해서)
// 4. JWT 토큰을 만들어서 응답해주면 된다.

// 1. 유저네임, 패스워드 로그인 정상
// 2. JWT 토큰 생성
// 3. 클라이언트하테 JWT토큰을 준다
// 4. 클라이언트가 요청할때마다 JWT토큰을 가져와서 요청한다
// 5. 서버는 이 JWT토큰이 유효한지를 판단한다. ( 이 역할을 수행하는 필터를 만들어줘야한다.)
// 6.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    // /login 요청에 대하여 로그인 시도를 위해 실행되는 함수.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


        ObjectMapper om = new ObjectMapper();
        try {
            User user = om.readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //attemptAuthentication실행 후 인증이 정상적으로 되었다면 successfulAuthentication가 실행된다.
    // JWT 토큰을 만들어서 request요청한 사용자에게 JWT 토큰을 response해주면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME)) //토큰 만료시간
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET)); //서버만 알고있는 시크릿 코드

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
    }
}
