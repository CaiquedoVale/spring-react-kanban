package com.kanban.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kanban.backend.service.TokenService; // <-- NOVO IMPORT
import com.kanban.backend.dto.LoginResponseDTO; // <-- NOVO IMPORT

import com.kanban.backend.dto.LoginRequestDTO;
import com.kanban.backend.model.Usuario;
import com.kanban.backend.repository.UsuarioRepository;

// ... (outros imports)


@RestController
@RequestMapping("/api") // ATENÇÃO: Mudamos a rota para a raiz da API
public class AuthController {
    
    @Autowired
    private TokenService tokenService; // <-- NOSSO NOVO SERVIÇO

    // --- Dependências que já tínhamos ---
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- Nova Dependência para o Login ---
    @Autowired
    private AuthenticationManager authenticationManager;

    // --- SEU MÉTODO DE REGISTRO (agora em /api/usuarios) ---
    @PostMapping("/usuarios") // Endpoint agora é POST /api/usuarios
    public Usuario registrarUsuario(@RequestBody Usuario novoUsuario) {

        novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        return usuarioSalvo;
    }

    @PostMapping("/login")
    // 1. Mude o tipo de retorno de String para o nosso DTO
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(), 
            loginRequest.getSenha()
        );

        Authentication auth = authenticationManager.authenticate(usernamePassword);

        // 2. Graças à Ação 1 e 2, podemos fazer este "cast" com segurança
        var usuario = (Usuario) auth.getPrincipal();

        // 3. Chamamos nosso serviço para GERAR O TOKEN
        String token = tokenService.gerarToken(usuario);

        // 4. Retornamos o DTO com o token dentro
        return new LoginResponseDTO(token);
    }
}
