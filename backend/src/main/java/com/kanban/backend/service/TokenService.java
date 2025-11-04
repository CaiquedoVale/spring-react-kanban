package com.kanban.backend.service;

// Imports da biblioteca de JWT (da Auth0)
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;    // Exceção para ERRO AO CRIAR
import com.auth0.jwt.exceptions.JWTVerificationException; // Exceção para ERRO AO VERIFICAR

// Imports do NOSSO projeto
import com.kanban.backend.model.Usuario;

// Imports do Spring
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// Imports do Java (para data/hora)
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: TokenService.java
 * ANALOGIA: O "Mestre dos Passaportes" (ou "Mestre Seleiro")
 * -------------------------------------------------------------------------------------
 * * @Service: Diz ao Spring que esta é uma classe de "Serviço" (um "Especialista")
 * e a "contrata" como um Bean, para que outros possam "injetá-la" (usá-la).
 */
@Service
public class TokenService {

    /**
     * @Value: Esta é uma forma poderosa do Spring "injetar" valores
     * do seu arquivo 'application.properties'.
     *
     * ANALOGIA: Estamos entregando o "Selo Real Secreto" (a chave) para o
     * "Mestre dos Passaportes". Ele é o único que o terá.
     */
    @Value("${api.security.token.secret}")
    private String secret;

    
    /**
     * -------------------------------------------------------------------------------------
     * MÉTODO: gerarToken (A "Oficina de Criação de Passaportes")
     * -------------------------------------------------------------------------------------
     * Recebe o "Dossiê" (objeto Usuario) de um usuário autenticado.
     * Devolve uma string (o "Passaporte" JWT).
     */
    public String gerarToken(Usuario usuario) {
        try {
            // 1. PREPARAR O SELO
            // Pega o nosso 'secret' (o "Selo Real") e define o algoritmo
            // que será usado para "assar" a assinatura (HMAC256).
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            // 2. PREENCHER O PASSAPORTE
            String token = JWT.create()
                // "Emitido por:" (Quem carimbou o passaporte?)
                .withIssuer("kanban-api") 
                
                // "Portador:" (Quem é o dono deste passaporte?)
                // O 'subject' é a "identidade" principal do token. Usamos o email.
                .withSubject(usuario.getEmail()) 
                
                // "Válido até:" (Quando o passaporte expira?)
                // Chamamos nosso método privado para calcular "agora + 2 horas".
                .withExpiresAt(gerarDataExpiracao()) 
                
                // 3. ASSINAR
                // Pega o Header + Payload e "assa" eles com o nosso 'algoritmo' (o Selo)
                // para criar a Assinatura (Signature).
                .sign(algoritmo); 

            return token;

        } catch (JWTCreationException exception){ 
            // ---------------------------------------------------------------------
            // (CORREÇÃO DE BUG: Estava 'JWTVerificationException' aqui)
            // ---------------------------------------------------------------------
            // Se a *criação* do token falhar (ex: 'secret' inválido),
            // lance um erro de sistema (Runtime Exception).
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    
    /**
     * -------------------------------------------------------------------------------------
     * MÉTODO: validarToken (O "Posto de Verificação de Passaportes")
     * -------------------------------------------------------------------------------------
     * Recebe uma string de token (o "Passaporte" que o usuário mostrou).
     * Devolve o "Portador" (o email) se o passaporte for válido.
     * Devolve "" (vazio) se o passaporte for inválido.
     */
    public String validarToken(String token){
        try {
            // 1. PREPARAR A "LUPA" DE VERIFICAÇÃO
            // Pega o *nosso* "Selo Real" (secret) para *comparar* com o selo
            // do passaporte que o usuário deu.
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            // 2. EXECUTAR A VERIFICAÇÃO
            return JWT.require(algoritmo) // "Eu exijo que o token use este 'algoritmo'..."
                .withIssuer("kanban-api")  // "...tenha sido 'emitido por' 'kanban-api'..."
                .build()                   // "Pronto. Agora crie o 'Verificador'."
                .verify(token)             // "VERIFIQUE este 'token'!" (Esta é a linha que "explode")
                .getSubject();             // "Se passou, me devolva o 'Portador' (o email)."

        } catch (JWTVerificationException exception){ 
            // ---------------------------------------------------------------------
            // (CORREÇÃO DE BUG: Estava 'JWTCreationException' aqui)
            // ---------------------------------------------------------------------
            // ANALOGIA: O "Alarme de Roubo" tocou!
            // Se a verificação ".verify(token)" "explodiu" (porque o token
            // expirou, a assinatura é falsa, ou o 'issuer' está errado),
            // caímos aqui.
            // 
            // NÃO quebre o app. Apenas retorne uma string vazia.
            // O "Guarda-Costas" ('SecurityFilter') que chamou este método
            // vai ver a string vazia e saberá que deve barrar o usuário.
            return ""; 
        }
    }


    /**
     * Método de ajuda privado para calcular a data de expiração.
     */
    private Instant gerarDataExpiracao() {
        return LocalDateTime.now()
                .plusHours(2) // "Agora" + 2 horas
                .toInstant(ZoneOffset.of("-03:00")); // Convertido para o fuso de Brasília (GMT-3)
    }
}