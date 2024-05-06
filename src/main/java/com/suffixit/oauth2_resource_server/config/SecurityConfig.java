package com.suffixit.oauth2_resource_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author nasimkabir
 * ১/১/২৪
 */
@Configuration
public class SecurityConfig {
    private static final String JWT_ROLE_NAME = "authorities";
    private static final String ROLE_PREFIX = "";

    private final CORSCustomizer corsCustomizer;

    public SecurityConfig(CORSCustomizer corsCustomizer) {
        this.corsCustomizer = corsCustomizer;
    }


/*    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.oauth2ResourceServer(
        c -> c.jwt(
            j -> j.jwkSetUri("http://localhost:8080/oauth2/jwks")
        )
    );

       *//* http.oauth2ResourceServer(
                c -> c.opaqueToken(
                        o -> o.introspectionUri("http://localhost:8080/oauth2/introspect")
                                .introspectionClientCredentials("rs", "secret")
                )
        );*//*

        http.authorizeHttpRequests(
                c -> c.anyRequest().authenticated()
        );
        corsCustomizer.corsCustomizer(http);
        return http.build();
    }*/

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        corsCustomizer.corsCustomizer(http);
        return http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated())
                /*.oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                        .decoder(JwtDecoders.fromIssuerLocation("http://localhost:9191"))
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        ))*/
                /*.oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtSpec -> jwtSpec.jwkSetUri("http://localhost:9191/oauth2/jwks")))*/
                .oauth2ResourceServer(oauth2-> oauth2.opaqueToken( o -> o.introspectionUri("http://localhost:9191/oauth2/introspect")
                        .introspectionClientCredentials("client", "secret")))
                .build();
    }


    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(JWT_ROLE_NAME);
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(ROLE_PREFIX);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
