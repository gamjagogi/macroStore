package com.hjson.macrostore.core.config;

import com.hjson.macrostore.core.auth.jwt.MyJwtAuthorizationFilter;
import com.hjson.macrostore.core.config.MyFilterRegistryConfig;
import com.hjson.macrostore.core.exception.Exception401;
import com.hjson.macrostore.core.exception.Exception403;
import com.hjson.macrostore.core.util.MyFilterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Slf4j
@Configuration
public class MySecurityConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }




    //jwt필터 등록
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception{
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new MyJwtAuthorizationFilter(authenticationManager));
            super.configure(builder);
        }
    }


    // 1. CSRF 해제
    // 2. iframe 거부
    // 3. cors 재설정
    // 4. jSessionId 사용 거부
    // 5. form 로긴 해제 (UsernamePasswordAuthenticationFilter 비활성화)
    // 6. 로그인 인증창이 뜨지 않게 비활성화
    // 7. 커스텀 필터 적용 (시큐리티 필터 교환)
    // 8. 인증 실패 처리
    // 10. 권한 실패 처리
    // 11. 인증, 권한 필터 설정
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.headers().frameOptions().disable();

        http.cors().configurationSource(configurationSource());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.formLogin().disable();

        http.httpBasic().disable();

        http.apply(new CustomSecurityFilterManager());

        http.exceptionHandling().authenticationEntryPoint(((request, response, authException) -> {
            log.warn("인증되지않은 사용자가 접근하려고 합니다. : " + authException.getMessage());
            MyFilterResponse.unAuthrized(response,new Exception401("인증되지 않았습니다."));
        }));

        http.exceptionHandling().accessDeniedHandler(((request, response, accessDeniedException) -> {
            log.warn("권한이 없는 사용자가 접근하려고 합니다. : "+ accessDeniedException.getMessage());
            MyFilterResponse.forbidden(response,new Exception403("권한이 없습니다."));
        }));

        http.authorizeRequests(
                authorize -> authorize.antMatchers("/s/**").authenticated()
                        .antMatchers("/manager/**")
                        .access("hasRole('ADMIN') or hasRole('MANAGER')")
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
        );

        return http.build();
    }

    public CorsConfigurationSource configurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
