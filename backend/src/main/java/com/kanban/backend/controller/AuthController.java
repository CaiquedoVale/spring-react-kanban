package com.kanban.backend.controller;

// Imports do Spring (Framework)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Imports do Spring Security (Segurança)
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

// Imports do NOSSO Projeto
import com.kanban.backend.dto.LoginRequestDTO;    // O "formulário" de login que vem do front-end
import com.kanban.backend.dto.LoginResponseDTO;   // O "envelope" com o token que mandamos de volta
import com.kanban.backend.model.Usuario;          // A entidade do nosso banco
import com.kanban.backend.repository.UsuarioRepository; // O "Arquivista" de usuários
import com.kanban.backend.service.TokenService;      // O "Mestre dos Passaportes" (JWT)


/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: AuthController.java
 * ANALOGIA: O "Porteiro Principal" do Quartel-General
 * -------------------------------------------------------------------------------------
 * * @RestController: Diz ao Spring que esta classe é um "Porteiro" (Controller) e que
 * todas as respostas que ela der devem ser em formato JSON (ideal para APIs).
 * * * @RequestMapping("/api"): Define o "endereço base" do porteiro. Todas as "portas"
 * (endpoints) dentro desta classe começarão com "/api".
 * Ex: /api/login, /api/usuarios
 */
@RestController
@RequestMapping("/api")
public class AuthController {
    
    // ---------------------------------------------------------------------------------
    // DEPENDÊNCIAS (As "Contratações" dos nossos Especialistas)
    // ---------------------------------------------------------------------------------
    // Usamos @Autowired para pedir ao Spring que "injete" (nos dê uma instância)
    // dos "Beans" (especialistas) que "contratamos" no SecurityConfig.
    
    @Autowired
    private AuthenticationManager authenticationManager; // O "Chefe de Segurança" (para o login)

    @Autowired
    private UsuarioRepository usuarioRepository; // O "Arquivista" (para salvar/buscar usuários)

    @Autowired
    private PasswordEncoder passwordEncoder; // O "Criptógrafo" (para codificar senhas)

    @Autowired
    private TokenService tokenService; // O "Mestre dos Passaportes" (para gerar o token)


    /**
     * -------------------------------------------------------------------------------------
     * ENDPOINT: Registro de Usuário (A "Porta de Registro")
     * -------------------------------------------------------------------------------------
     * * @PostMapping("/usuarios"): Define que esta "porta" atende no endereço
     * completo "POST /api/usuarios".
     * * * @RequestBody Usuario novoUsuario: Diz ao Spring: "O JSON que chegar no corpo
     * desta requisição deve ser transformado em um objeto 'Usuario'".
     */
    @PostMapping("/usuarios")
    public Usuario registrarUsuario(@RequestBody Usuario novoUsuario) {

        // 1. CHAMA O CRIPTÓGRAFO
        // Pega a senha em texto puro (ex: "123456") que veio do JSON...
        // ...e a substitui pela versão criptografada (ex: "$2a$10$...")
        novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));

        // 2. CHAMA O ARQUIVISTA
        // Manda o "Arquivista" (Repository) salvar o objeto 'novoUsuario'
        // no banco de dados.
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        // 3. Devolve o usuário salvo (com o ID e a senha criptografada) como
        //    resposta JSON para o front-end.
        return usuarioSalvo;
    }


    /**
     * -------------------------------------------------------------------------------------
     * ENDPOINT: Login de Usuário (A "Porta de Login")
     * -------------------------------------------------------------------------------------
     * * @PostMapping("/login"): Define que esta "porta" atende no endereço
     * completo "POST /api/login".
     * * * @RequestBody LoginRequestDTO loginRequest: Recebe o "formulário" de login (DTO).
     * Note que NÃO usamos a entidade 'Usuario' aqui, apenas o DTO.
     */
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {

        // 1. MONTA O "PACOTE DE CREDENCIAIS"
        // O Spring Security não aceita o email e a senha soltos. Ele precisa
        // de um "pacote" oficial (um 'UsernamePasswordAuthenticationToken')
        // contendo o "username" (nosso email) e a "password" (nossa senha).
        var usernamePassword = new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(), 
            loginRequest.getSenha()
        );

        // 2. CHAMA O "CHEFE DE SEGURANÇA"
        // .authenticate(): Esta é a linha principal do login.
        // Nós entregamos o "pacote de credenciais" ao 'AuthenticationManager'.
        // Ele vai orquestrar todo o processo:
        //    a. Chama o 'UserAuthService' (o "Verificador") para buscar o usuário pelo email.
        //    b. Chama o 'PasswordEncoder' (o "Criptógrafo") para comparar as senhas.
        //    c. Se tudo der certo, ele retorna um objeto 'Authentication' completo.
        //    d. Se o email não existir ou a senha estiver errada, ele "explode" (joga uma exceção)
        //       automaticamente, o que resulta no erro 401/403 que vimos no Postman.
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        // 3. EXTRAI O "Dossiê" DO USUÁRIO
        // Se a linha de cima NÃO explodiu, o login foi um SUCESSO.
        // O 'auth.getPrincipal()' contém o "Dossiê" do usuário (o objeto UserDetails
        // que o nosso 'UserAuthService' retornou).
        // Nós fazemos o "cast" de volta para o nosso objeto 'Usuario' (pois sabemos que é ele).
        var usuario = (Usuario) auth.getPrincipal();

        // 4. CHAMA O "MESTRE DOS PASSAPORTES"
        // Agora que sabemos que o usuário é real, pedimos ao 'TokenService'
        // para gerar um "Passaporte" (Token JWT) para ele.
        String token = tokenService.gerarToken(usuario);

        // 5. ENVIA O "ENVELOPE" DE RESPOSTA
        // Retornamos um 'LoginResponseDTO' (nosso "envelope") contendo o token.
        // O Spring vai converter isso em: { "token": "eyJhbGciOiJ..." }
        return new LoginResponseDTO(token);
    }
}