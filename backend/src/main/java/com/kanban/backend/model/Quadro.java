package com.kanban.backend.model;

// --- Imports do Jakarta Persistence (JPA) ---
// Estas são as "Ferramentas de Construção" da tabela e relacionamentos.
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType; // Para definir a estratégia de carregamento (Lazy/Eager)
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn; 
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// --- Imports do Jackson (para controle de JSON) ---
import com.fasterxml.jackson.annotation.JsonIgnore; // VITAL para evitar loops na API

// --- Imports do Lombok e Java ---
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List; // Para a lista de colunas


/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: Quadro.java
 * ANALOGIA: A "Planta Baixa do Quadro" (Board)
 * -------------------------------------------------------------------------------------
 * * @Entity: Diz ao Spring (JPA/Hibernate) que esta classe é uma tabela no banco.
 * * @Table(name = "quadros"): Define o nome da tabela.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quadros")
public class Quadro {
    
    // ---------------------------------------------------------------------------------
    // PARTE 1: CAMPOS BÁSICOS
    // ---------------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    
    // ---------------------------------------------------------------------------------
    // PARTE 2: RELACIONAMENTO COM USUÁRIO (DONO)
    // ---------------------------------------------------------------------------------
    /**
     * @ManyToOne: MUITOS Quadros pertencem a UM Usuario.
     *
     * @JsonIgnore: VITAL! Esta anotação resolve o "Loop de JSON" quebra-cabeça:
     * (Quadro -> Usuario -> Lista<Quadros> -> Quadro...)
     * - Diz ao serializador de JSON para IGNORAR este campo ao serializar o Quadro.
     * - O Front-End (PaginaQuadro.js) não precisa mais do objeto Usuário inteiro.
     * - O carregamento é LAZY por padrão (Lazy Loading), o que é bom, pois
     * o campo só será buscado quando for acessado.
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore 
    private Usuario usuario;
    
    
    // ---------------------------------------------------------------------------------
    // PARTE 3: RELACIONAMENTO COM COLUNAS (Otimizado)
    // ---------------------------------------------------------------------------------
    /**
     * @OneToMany: UM Quadro pode ter MUITAS Colunas.
     * @mappedBy="quadro": O relacionamento é "espelhado" e gerenciado pelo campo 'quadro' na Coluna.java.
     *
     * @FetchType.EAGER (Removido!):
     * A linha 'fetch = FetchType.EAGER' foi removida, pois ela causava o **Problema N+1**
     * no 'GET /api/quadros' (50 quadros = 51 consultas).
     *
     * A otimização agora é feita com @EntityGraph no Repositório, o que é muito mais eficiente.
     */
    @OneToMany(
        mappedBy = "quadro", 
        cascade = CascadeType.ALL, 
        orphanRemoval = true,
        fetch = FetchType.LAZY // É LAZY por padrão, mas para clareza, o Lazy é o melhor para relações OneToMany.
    )
    // Não tem @JsonIgnore aqui, pois as Colunas SÃO a informação que o Front-end quer ver.
    private List<Coluna> colunas;
}