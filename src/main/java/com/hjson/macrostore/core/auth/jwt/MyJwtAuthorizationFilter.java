package com.hjson.macrostore.core.auth.jwt;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hjson.macrostore.core.auth.session.MyUserDetails;
import com.hjson.macrostore.model.user.User;
import com.hjson.macrostore.model.user.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyJwtAuthorizationFilter extends BasicAuthenticationFilter {

    public MyJwtAuthorizationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
            String prefixJwt = request.getHeader(MyJwtProvider.HEADER);

            if(prefixJwt == null){
                chain.doFilter(request, response);
                return;
            }
            String jwt = prefixJwt.replace(MyJwtProvider.TOKEN_PREFIX, "");
            try {
                System.out.println("디버그 : 토큰 있음");
                DecodedJWT decodedJwt = MyJwtProvider.verify(jwt);
                Long id = decodedJwt.getClaim("id").asLong();
                String role = decodedJwt.getClaim("role").asString();
                UserRole userRole = UserRole.valueOf(role.toUpperCase());

                //토큰 검사로 id,role꺼내서, UserDetails객체 넣고,Authentication객체로, new UsernamePasswordAuthenticationToken으로
                // ContextHolder에 MyUserDetails, password(credencial), authorities에 넣는다.
                User user = User.builder().id(id).role(role).build();
                MyUserDetails userDetails = new MyUserDetails(user);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,userDetails.getPassword(),userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (SignatureVerificationException sve){
                log.error("토큰 검증 실패");
            }catch (TokenExpiredException tee) {
                log.error("토큰 만료");
            }finally {
                chain.doFilter(request, response);
            }
        }
}
