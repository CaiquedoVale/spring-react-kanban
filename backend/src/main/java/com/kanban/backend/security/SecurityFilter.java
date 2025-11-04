package com.kanban.backend.security;

import com.kanban.backend.repository.UsuarioRepository;
import com.kanban.backend.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Diz ao Spring que esta é um componente genérico (um "Bean")
public class SecurityFilter extends OncePerRequestFilter { // Filtro que roda UMA VEZ por requisição

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        // 1. Pega o token do cabeçalho
        var token = this.recuperarToken(request);

        // 2. Se o token não for nulo
        if(token != null) {
            // 3. Valida o token e pega o email (subject)
            var email = tokenService.validarToken(token);
            
            // 4. Busca o usuário no banco pelo email
            // (Graças ao refactor anterior, o 'Usuario' é um 'UserDetails')
            UserDetails usuario = usuarioRepository.findByEmail(email)
                                      .orElseThrow(() -> new RuntimeException("Usuário não encontrado no filtro"));

            // 5. Cria o "pacote" de autenticação
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            
            // 6. Salva o usuário no "contexto" de segurança do Spring
            // (Isso "informa" ao Spring que o usuário está logado nesta requisição)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 7. Se não houver token (ou se já validou), continua para o próximo filtro/controller
        filterChain.doFilter(request, response);
    }

    // Método privado para extrair o token do Header
    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) {
            return null; // Sem token
        }
        
        // O token vem como "Bearer eyJhbGciOi...". 
        // Este "replace" remove o "Bearer " e deixa só o token.
        return authHeader.replace("Bearer ", "");
    }
}