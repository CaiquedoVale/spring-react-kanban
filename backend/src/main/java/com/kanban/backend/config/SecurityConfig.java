package com.kanban.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration // Diz ao Spring que esta é uma classe de configuração
@EnableWebSecurity // Habilita a configuração de segurança web
public class SecurityConfig {

    @Bean //Cria um "Bean" (um objeto gerenciado) que define as regras de segurança
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        // Esta é a configuração que "libera tudo"
        http
            .csrf(csrf -> csrf.disable()) // Desabilita o CSRF (vamos falar disso depois)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() // Permite TODAS as requisições ("/**")
                .anyRequest().authenticated() // (Qualquer outra requisição exigiria auth, mas "/**" já cobriu tudo)
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder PasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
