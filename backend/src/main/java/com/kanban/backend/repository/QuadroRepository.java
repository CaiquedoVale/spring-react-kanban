package com.kanban.backend.repository;

// --- Imports de Entidades ---
import com.kanban.backend.model.Quadro;
import com.kanban.backend.model.Usuario; 

// --- Imports do Spring Data JPA ---
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph; // VITAL: Para resolver o Problema N+1
import org.springframework.stereotype.Repository;

// --- Imports do Java ---
import java.util.List; // Para retornar a lista de quadros
import java.util.Optional; // Para o método findById


/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: QuadroRepository.java
 * ANALOGIA: O "Arquivista Otimizado" dos Quadros
 * -------------------------------------------------------------------------------------
 * * @Repository: Marca a interface como um componente de acesso a dados.
 * * extends JpaRepository<Quadro, Long>: Herda todos os métodos CRUD básicos para a 
 * entidade 'Quadro', cuja chave primária é do tipo 'Long'.
 * * Este Repositório foi customizado para resolver o Problema N+1 (Lazy Loading).
 */
@Repository
public interface QuadroRepository extends JpaRepository<Quadro, Long> {

    /**
     * ---------------------------------------------------------------------------------
     * MÉTODO 1: findByUsuario (Para a Lista do Dashboard)
     * ---------------------------------------------------------------------------------
     * Busca todos os quadros que pertencem a um determinado usuário.
     * * @EntityGraph(attributePaths = {"colunas", "usuario"}):
     * A otimização principal. Isso diz ao JPA/Hibernate para:
     * 1. Ignorar o comportamento Lazy (preguiçoso) padrão.
     * 2. CARREGAR os relacionamentos "colunas" e "usuario" EM UMA ÚNICA CONSULTA SQL
     * (geralmente via JOIN), eliminando o lento Problema N+1 no Dashboard.
     */
    @EntityGraph(attributePaths = {"colunas", "usuario"})
    List<Quadro> findByUsuario(Usuario usuario);

    /**
     * ---------------------------------------------------------------------------------
     * MÉTODO 2: findById (Para a Página de Detalhe do Quadro)
     * ---------------------------------------------------------------------------------
     * Sobrescreve o método findById padrão para aplicar a mesma otimização.
     * Garante que, ao buscar um único Quadro por ID (para a PaginaQuadro.js), 
     * as colunas sejam carregadas imediatamente e de forma otimizada.
     */
    @EntityGraph(attributePaths = {"colunas", "usuario"})
    Optional<Quadro> findById(Long id); 
}