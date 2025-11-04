package com.kanban.backend.security;

// Imports do NOSSO Projeto
import com.kanban.backend.repository.UsuarioRepository; // O "Arquivista"
import com.kanban.backend.service.TokenService;      // O "Mestre dos Passaportes"

// Imports do Jakarta (para rodar no servidor)
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Imports do Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// Imports do Spring Security
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder; // O "Cofre de Segurança"
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: SecurityFilter.java
 * ANALOGIA: O "Guarda-Costas" (ou "Segurança da Boate")
 * -------------------------------------------------------------------------------------
 * * @Component: "Contrata" esta classe como um componente (Bean) genérico do Spring.
 * O 'SecurityConfig' vai usá-lo (via @Autowired).
 *
 * * extends OncePerRequestFilter:
 * Define que esta classe é um "Filtro". Ela vai "interceptar" TODAS as requisições
 * HTTP que chegarem no servidor.
 * 'OncePerRequestFilter' garante que ele rode apenas UMA VEZ por requisição.
 */
@Component
public class SecurityFilter extends OncePerRequestFilter {

    // 1. "CONTRATANDO" OS ESPECIALISTAS
    
    @Autowired
    private TokenService tokenService; // O "Mestre dos Passaportes" (para validar o token)

    @Autowired
    private UsuarioRepository usuarioRepository; // O "Arquivista" (para buscar o "Dossiê" do usuário)

    
    /**
     * -------------------------------------------------------------------------------------
     * MÉTODO: doFilterInternal (O "Procedimento de Revista")
     * -------------------------------------------------------------------------------------
     * Este é o método principal do filtro. Todo o tráfego da API passa por aqui
     * ANTES de chegar nos 'Controllers'.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        // PASSO 1: "Revistar" o usuário
        // Chama nosso método privado para pegar o "Passaporte" (Token)
        // do cabeçalho (Header) da requisição.
        var token = this.recuperarToken(request);

        // PASSO 2: Verificar se um "Passaporte" foi apresentado
        if(token != null) {
            // Se um token foi enviado...

            // PASSO 3: Validar o "Passaporte"
            // Entrega o token ao "Mestre dos Passaportes" (TokenService) para validação.
            // Graças à nossa correção no 'TokenService', se o token for inválido
            // (expirado, assinatura falsa), este método retornará "" (vazio).
            var email = tokenService.validarToken(token);
            
            // PASSO 4: Buscar o "Dossiê" (se o "Passaporte" for válido)
            if (email != null && !email.isEmpty()) {
                // Se o 'validarToken' retornou um email (ou seja, o token é válido)...
                
                // Vamos ao "Arquivo Central" (Repositório) e pegamos o "Dossiê"
                // completo do usuário (o objeto 'Usuario', que é um 'UserDetails').
                UserDetails usuario = usuarioRepository.findByEmail(email)
                                          .orElseThrow(() -> new RuntimeException("Usuário não encontrado no filtro (token válido, mas usuário não existe no DB)"));
                                          // Se o token for válido, mas o usuário foi deletado
                                          // do banco, lançamos um erro.

                // PASSO 5: "Autenticar" o usuário para esta requisição
                
                // Criamos o "pacote de autenticação" oficial do Spring Security,
                // contendo o "Dossiê" ('usuario') e os "Crachás" ('getAuthorities()').
                // (O 'null' é para as credenciais (senha), que não são necessárias
                //  aqui, pois o token já provou quem ele é).
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                
                // PASSO 6: O "ATO DE LOGIN"
                // Esta é a linha mais importante.
                // Nós "colocamos" o "pacote de autenticação" do usuário dentro do
                // "Cofre de Segurança" ('SecurityContextHolder').
                //
                // A partir deste momento, para esta requisição específica, o Spring
                // Security considera este usuário como 100% AUTENTICADO.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // PASSO 7: "Liberar a Catraca"
        //
        // filterChain.doFilter(request, response):
        // Independentemente do que aconteceu (se o usuário foi autenticado ou não),
        // nós passamos a requisição adiante para o próximo filtro da fila.
        //
        // - Se o usuário NÃO tinha token: Ele segue "anônimo".
        // - Se o usuário TINHA token e foi autenticado: Ele segue "logado".
        //
        // O próximo filtro ('AuthorizationFilter') é quem vai barrá-lo ou não
        // com base nas regras do 'SecurityConfig' (ex: .hasAuthority("ROLE_USER")).
        filterChain.doFilter(request, response);
    }

    
    /**
     * -------------------------------------------------------------------------------------
     * MÉTODO: recuperarToken (O "Extrator de Passaporte")
     * -------------------------------------------------------------------------------------
     * Método de ajuda privado para ler o cabeçalho 'Authorization'
     * e extrair a string pura do token.
     */
    private String recuperarToken(HttpServletRequest request) {
        // Pega o valor do cabeçalho "Authorization"
        var authHeader = request.getHeader("Authorization");
        
        // Se o cabeçalho não existir (usuário não enviou o token)
        if(authHeader == null) {
            return null;
        }

        // Se o cabeçalho existir, ele virá no formato "Bearer [token...]"
        // (Como configuramos no Postman e no nosso 'axiosConfig').
        //
        // Nós "substituímos" a palavra "Bearer " por "" (vazio),
        // deixando apenas a string "eyJhbGci..." do token.
        return authHeader.replace("Bearer ", "");
    }
}