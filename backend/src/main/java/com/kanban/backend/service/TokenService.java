package com.kanban.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.kanban.backend.model.Usuario; // Precisamos do modelo
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.auth0.jwt.exceptions.JWTVerificationException; // <-- O IMPORT CORRETO

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // 1. Pega o "segredo" do nosso application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            // 2. Define o Algoritmo (HMAC256) com o nosso segredo
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            String token = JWT.create()
                .withIssuer("kanban-api") // Quem está emitindo o token
                .withSubject(usuario.getEmail()) // Quem é o "dono" do token
                .withExpiresAt(gerarDataExpiracao()) // Data de expiração
                .sign(algoritmo); // Assina o token com o algoritmo

            return token;

        } catch (JWTVerificationException exception){
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // Método para validar o token (vamos usar mais tarde, no Filtro)
    public String validarToken(String token){
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                .withIssuer("kanban-api")
                .build()
                .verify(token)
                .getSubject(); // Retorna o "Subject" (o email do usuário)

        } catch (JWTCreationException exception){
            return ""; // Se o token for inválido, retorna vazio
        }
    }


    // Define a data de expiração (ex: 2 horas a partir de agora)
    private Instant gerarDataExpiracao() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00")); // Fuso de Brasília
    }
}