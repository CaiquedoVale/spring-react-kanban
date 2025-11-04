package com.kanban.backend.controller;

// Imports do NOSSO Projeto
import com.kanban.backend.dto.QuadroRequestDTO;
import com.kanban.backend.model.Quadro;
import com.kanban.backend.model.Usuario;
import com.kanban.backend.repository.QuadroRepository;
import com.kanban.backend.service.QuadroService; 

// Imports do Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // <-- NOVO: Para ler o ID da URL
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Imports do Spring Security
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: QuadroController.java
 * ANALOGIA: O "Gerente de Quadros" (e Porteiro das Rotas Protegidas)
 * -------------------------------------------------------------------------------------
 * * O Controller responsável por todas as operações (CRUD) nos Quadros.
 * * É o único ponto de acesso do Front-end para a gestão de Quadros.
 */
@RestController
@RequestMapping("/api/quadros")
public class QuadroController {

    // 1. INJEÇÃO DO ARQUIVISTA (QuadroRepository)
    @Autowired
    private QuadroRepository quadroRepository;

    // 2. INJEÇÃO DO ESPECIALISTA (QuadroService)
    @Autowired
    private QuadroService quadroService; 
    
    
    // ---------------------------------------------------------------------------------
    // ENDPOINT 1: Listar Todos os Quadros do Usuário
    // ---------------------------------------------------------------------------------
    /**
     * Rota: GET /api/quadros
     * Objetivo: Buscar e listar todos os quadros que pertencem ao usuário logado.
     */
    @GetMapping
    public ResponseEntity<List<Quadro>> getQuadrosDoUsuario() {
        
        // PASSO 1: Identifica o usuário logado (Autenticado pelo SecurityFilter)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        // PASSO 2: Chama o Arquivista otimizado (findByUsuario com @EntityGraph)
        List<Quadro> quadros = quadroRepository.findByUsuario(usuarioLogado);

        // PASSO 3: Retorna 200 OK com a lista de quadros.
        return ResponseEntity.ok(quadros);   
    }

    
    // ---------------------------------------------------------------------------------
    // ENDPOINT 2: Criar um Novo Quadro
    // ---------------------------------------------------------------------------------
    /**
     * Rota: POST /api/quadros
     * Objetivo: Criar um novo Quadro E as três Colunas padrão para ele.
     */
    @PostMapping
    public ResponseEntity<Quadro> criarQuadro(@RequestBody QuadroRequestDTO quadroRequest) {
        
        // PASSO 1: Identifica o usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        // PASSO 2: Chama o Especialista (QuadroService)
        // O serviço encapsula a lógica de "Criar Quadro + Criar Colunas".
        Quadro novoQuadro = quadroService.criarQuadroComColunasPadrao(
            quadroRequest.getNome(),
            usuarioLogado
        );

        // PASSO 3: Retorna 201 Created (sucesso na criação)
        return ResponseEntity.status(201).body(novoQuadro);
    }

    
    // ---------------------------------------------------------------------------------
    // ENDPOINT 3: Buscar Quadro por ID
    // ---------------------------------------------------------------------------------
    /**
     * Rota: GET /api/quadros/{id}
     * Objetivo: Busca um Quadro específico e garante que o usuário logado é o dono.
     * @PathVariable Long id: Lê o ID que veio na URL (ex: /api/quadros/5).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Quadro> getQuadroPorId(@PathVariable Long id) {
        
        // 1. Identifica o usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        // 2. Busca o Quadro otimizado (findById com @EntityGraph)
        Quadro quadro = quadroRepository.findById(id)
            .orElse(null); 

        // 3. CHECAGEM DE SEGURANÇA 1: O quadro existe?
        if (quadro == null) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        // 4. CHECAGEM DE SEGURANÇA 2: O usuário é o dono? (Regra de Negócio)
        // Se o ID do dono do quadro for diferente do ID do usuário logado, nega o acesso.
        if (!quadro.getUsuario().getId().equals(usuarioLogado.getId())) {
            return ResponseEntity.status(403).build(); // 403 Forbidden (Acesso negado)
        }
        
        // 5. Sucesso! Retorna 200 OK com o Quadro (e suas colunas).
        return ResponseEntity.ok(quadro);
    }
}