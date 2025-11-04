package com.kanban.backend.config;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays; // Importe este

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import com.kanban.backend.security.SecurityFilter; // <-- NOVO IMPORT
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // <-- NOVO IMPORT
import static org.springframework.security.config.Customizer.withDefaults;




@Configuration // Diz ao Spring que esta é uma classe de configuração
@EnableWebSecurity // Habilita a configuração de segurança web
public class SecurityConfig {

    @Autowired // <-- ADICIONE ISSO
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(withDefaults()) // Isto vai usar o seu Bean e cuidar do OPTIONS
            .formLogin(formLogin -> formLogin.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                .anyRequest().hasAuthority("ROLE_USER")
            )
            // Use a ordem de filtro que te pedi antes (AuthorizationFilter)
            .addFilterBefore(securityFilter, AuthorizationFilter.class); 
            
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){ 
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // ESTE É O NOVO BEAN QUE CRIA AS REGRAS DE CORS
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite requisições do seu React
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        // Permite todos os métodos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Permite todos os cabeçalhos
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Permite credenciais (nosso token)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica essas regras para todas as rotas "/api/**"
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
