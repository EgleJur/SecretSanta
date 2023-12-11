package Secret.Santa.Secret.Santa.confs;

import Secret.Santa.Secret.Santa.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {


    private static final String[] WHITE_LIST = {"/api/v1/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui.html"};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /*  @Bean
      public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
          http
                  .csrf(AbstractHttpConfigurer::disable) // CSRF is typically disabled for REST APIs
                  .cors((cors) -> cors
                          .configurationSource(corsConfigurationSource())
                  )
                  .authorizeRequests()
                  .requestMatchers(HttpMethod.GET, "/api/v1/groups/**").authenticated()
                  .requestMatchers(HttpMethod.POST, "/api/v1/groups/**").authenticated()
                  .requestMatchers(HttpMethod.PUT, "/api/v1/groups/**").authenticated()
                  .requestMatchers(HttpMethod.DELETE, "/api/v1/groups/**").authenticated()
                  .requestMatchers(HttpMethod.GET, "/api/v1/gifts/**").authenticated()
                  .requestMatchers(HttpMethod.POST, "/api/v1/gifts/**").authenticated()
                  .requestMatchers(HttpMethod.PUT, "/api/v1/gifts/**").authenticated()
                  .requestMatchers(HttpMethod.DELETE, "/api/v1/gifts/**").authenticated()
                  .requestMatchers(HttpMethod.GET, "/generate-santa/**").authenticated()
                  .requestMatchers(HttpMethod.POST, "/generate-santa/**").authenticated()
                  .requestMatchers(HttpMethod.DELETE, "/generate-santa/**").authenticated()
                  .requestMatchers(HttpMethod.GET, "/api/v1/users/**").authenticated()
                  .requestMatchers(HttpMethod.POST, "/api/v1/users/**").authenticated()
                  .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").authenticated()
                  .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").authenticated()
                  .and()
                  .formLogin().disable() // Disable form login
                  .logout().disable(); // Disable logout

          return http.build();
      } */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors((cors) -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .authorizeHttpRequests(req ->
                                req
                                        .requestMatchers(WHITE_LIST)
                                        .permitAll()
//                                .requestMatchers(HttpMethod.GET, "/api/v1/groups/**").authenticated()
//                                .requestMatchers(HttpMethod.POST, "/api/v1/groups/**").authenticated()
//                                .requestMatchers(HttpMethod.PUT, "/api/v1/groups/**").authenticated()
//                                .requestMatchers(HttpMethod.DELETE, "/api/v1/groups/**").authenticated()
//                                .requestMatchers(HttpMethod.GET, "/api/v1/gifts/**").authenticated()
//                                .requestMatchers(HttpMethod.POST, "/api/v1/gifts/**").authenticated()
//                                .requestMatchers(HttpMethod.PUT, "/api/v1/gifts/**").authenticated()
//                                .requestMatchers(HttpMethod.DELETE, "/api/v1/gifts/**").authenticated()
//                                .requestMatchers(HttpMethod.GET, "/generate-santa/**").authenticated()
//                                .requestMatchers(HttpMethod.POST, "/generate-santa/**").authenticated()
//                                .requestMatchers(HttpMethod.DELETE, "/generate-santa/**").authenticated()
//                                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").authenticated()
//                                .requestMatchers(HttpMethod.POST, "/api/v1/users/**").authenticated()
//                                .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").authenticated()
//                                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").authenticated()
//                                .requestMatchers(WHITE_LIST_URL)
//                                .permitAll()
//                                .anyRequest().authenticated() //uncomment this
                                        .anyRequest().permitAll()
                        //.authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000/"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
