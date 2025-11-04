package com.kanban.backend.controller;

// Imports do NOSSO Projeto
import com.kanban.backend.model.Quadro;   // A entidade "Quadro"
import com.kanban.backend.model.Usuario; // A entidade "Usuario"
import com.kanban.backend.repository.QuadroRepository; // O "Arquivista" de Quadros

// Imports do Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Permite retornar respostas HTTP (ex: 200 OK)
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Imports do Spring Security
import org.springframework.security.core.Authentication; // O "pacote" que guarda a identidade do usuário
import org.springframework.security.core.context.SecurityContextHolder; // O "cofre" que guarda a autenticação

import java.util.List;

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: QuadroController.java
 * ANALOGIA: O "Porteiro dos Quadros"
 * -------------------------------------------------------------------------------------
 * * @RestController: Diz ao Spring que esta classe é um "Porteiro" (Controller)
 * e que suas respostas serão em JSON.
 * * * @RequestMapping("/api/quadros"): Define o "endereço base" deste porteiro.
 * Todas as "portas" (endpoints) aqui dentro começam com "/api/quadros".
 * * IMPORTANTE: No 'SecurityConfig', nós definimos que QUALQUER requisição
 * (exceto login/registro) precisa do "crachá" 'ROLE_USER'. Isso significa
 * que o "Guarda-Costas" ('SecurityFilter') vai rodar ANTES de qualquer
 * método nesta classe ser chamado.
 */
@RestController
@RequestMapping("/api/quadros")
public class QuadroController {

    // 1. "CONTRATANDO" O ARQUIVISTA
    // Pede ao Spring que "injete" (nos dê uma instância) do 'QuadroRepository'
    // que criamos.
    @Autowired
    private QuadroRepository quadroRepository;

    /**
     * -------------------------------------------------------------------------------------
     * ENDPOINT: Listar Quadros do Usuário
     * -------------------------------------------------------------------------------------
     * * @GetMapping: Define que esta "porta" atende no endereço "base" do controller,
     * ou seja, "GET /api/quadros".
     * * * ResponseEntity<List<Quadro>>: Define que a resposta será um "pacote" HTTP
     * que contém uma Lista de Quadros (em JSON).
     */
    @GetMapping
    public ResponseEntity<List<Quadro>> getQuadrosDoUsuario() {
        
        // ---------------------------------------------------------------------------------
        // PASSO 1: QUEM ESTÁ ME PERGUNTANDO?
        // ---------------------------------------------------------------------------------
        //
        // Nós precisamos saber "quem" é o usuário logado para filtrar os quadros.
        // Como o 'SecurityFilter' (Guarda-Costas) já rodou, ele já
        // validou o "Passaporte" (Token) e guardou a identidade do usuário
        // no "Cofre de Segurança" (o SecurityContextHolder).
        //
        // 1. Pega o "Cofre"
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // 2. Pega o "Dossiê" (o 'Principal') de dentro do cofre.
        //    (Nós sabemos que o 'Principal' é o nosso objeto 'Usuario'
        //     porque foi isso que colocamos lá no 'UserAuthService').
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        // ---------------------------------------------------------------------------------
        // PASSO 2: BUSCAR OS DADOS
        // ---------------------------------------------------------------------------------
        //
        // Agora que sabemos "quem" está logado, pedimos ao "Arquivista" (Repositório)
        // para buscar no banco de dados.
        // Chamamos o método 'findByUsuario' que nós mesmos criamos na interface.
        List<Quadro> quadros = quadroRepository.findByUsuario(usuarioLogado);

        // ---------------------------------------------------------------------------------
        // PASSO 3: RESPONDER
        // ---------------------------------------------------------------------------------
        //
        // .ok(quadros): Esta é uma forma elegante de dizer:
        // "Retorne um status HTTP 200 OK e coloque a 'lista de quadros'
        //  no corpo (body) da resposta."
        // O Spring vai automaticamente converter a List<Quadro> para um JSON [ ... ]
        // (que estará vazio, `[ ]`, se o usuário não tiver quadros).
        return ResponseEntity.ok(quadros);
    }
    
    // (Mais tarde, na Fase 3, adicionaremos os métodos POST, GET por ID, etc. aqui)
}