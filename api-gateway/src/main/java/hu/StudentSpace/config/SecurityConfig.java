package hu.StudentSpace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private final String[] allowedOrigins = new String[]{
            "http://localhost:3000",
            "http://127.0.0.1:3000",
            "http://84.3.183.248:3000",
            "http://localhost:4200",
            "http://138.3.248.186",
            "https://138.3.248.186"
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()

                        // API documentation endpoints
                        .pathMatchers("GET", "/swagger-ui.html").permitAll()
                        .pathMatchers("GET", "/swagger-resources/**").permitAll()
                        .pathMatchers("GET", "/v3/api-docs/**").permitAll()
                        .pathMatchers("GET", "/webjars/**").permitAll()

                        // Anything else
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2ResourceServerSpec -> oauth2ResourceServerSpec
                        .jwt(jwtSpec -> jwtSpec
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(
                Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin",
                        "Connection", "Accept", "Origin", "X-Requested-With", "Access-Control-Request-Method",
                        "Access-Control-Request-Headers", "Access-Control-Allow-Credentials"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    private Converter<Jwt, Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new RealmRoleConverter());

        return jwt -> Mono.justOrEmpty(jwtConverter.convert(jwt));
    }
}
