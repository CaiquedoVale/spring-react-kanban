package com.kanban.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/api/**") // Permite CORS para todas as rotas que começam com /api/
            .allowedOrigins("http://localhost:3000") // Permite pedidos vindos DESTE endereço
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite estes métodos HTTP
            .allowedHeaders("*") // Permite todos os cabeçalhos (headers)
            .allowCredentials(true); // Permite o envio de credenciais (ex: cookies)
    }
}
