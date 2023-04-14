package nl.jesper.songshare.securitylayerJwt.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 The SpringSecurityConfig class configures Spring Security for the application.
 This class enables web security and registers a {@link JwtAuthenticationFilter} and a {@link CustomUserDetailsService}
 in the Spring context. The {@link JwtAuthenticationFilter} is used to handle authentication and
 authorization of requests, while the {@link CustomUserDetailsService} is used to retrieve user details for authentication.
 The {@link PasswordEncoder} bean is used to encode and decode passwords for storage and authentication.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Defines the SecurityFilterChain for the application. The filter chain is used to configure which requests are
     * allowed, which requests need authentication and which authentication method to use.
     * @param http The HttpSecurity object that is used to configure the filter chain.
     * @return The SecurityFilterChain that is used by the application.
     * @throws Exception When there is an error configuring the HttpSecurity object.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/users/register").permitAll()
                .requestMatchers("/users/login").permitAll()
                .requestMatchers("/songs").hasAnyAuthority("USER","ADMIN")
                .requestMatchers("/songs/**").hasAnyAuthority("USER","ADMIN")
//                .requestMatchers("/songs/download").hasAuthority("USER")
//                .requestMatchers("/songs/search").hasAuthority("USER")
//                .requestMatchers("/songs/myuploads").hasAuthority("USER")
                .requestMatchers("/admin/**").hasAuthority("ADMIN");
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures the AuthenticationManager bean for the application. The AuthenticationManager is used to authenticate
     * users in the application.
     * @param authenticationConfiguration The AuthenticationConfiguration that is used to create the AuthenticationManager bean.
     * @return The AuthenticationManager bean.
     * @throws Exception When there is an error configuring the AuthenticationManager bean.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Creates a bean of PasswordEncoder that delegates to other PasswordEncoder implementations based on the prefix of the encoded password.
     * Uses DelegatingPasswordEncoder to enable the application to encode and match passwords using different PasswordEncoder implementations.
     * @return The PasswordEncoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
