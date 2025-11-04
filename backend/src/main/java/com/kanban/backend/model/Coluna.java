package com.kanban.backend.model;

// Imports do Jakarta Persistence (JPA)
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Imports do Jackson (para controle de JSON)
import com.fasterxml.jackson.annotation.JsonIgnore; // VITAL para evitar loops na API

// Imports do Lombok
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: Coluna.java
 * ANALOGIA: A "Planta Baixa da Coluna" (ex: "A Fazer")
 * -------------------------------------------------------------------------------------
 * * O que é?
 * Define a estrutura de uma coluna dentro de um quadro.
 * * Este arquivo faz a conexão de que MUITAS Colunas pertencem a UM Quadro.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "colunas")
public class Coluna {
    
    // ---------------------------------------------------------------------------------
    // CAMPOS BÁSICOS
    // ---------------------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome; // Ex: "A Fazer", "Fazendo", "Feito"

    
    /**
     * ---------------------------------------------------------------------------------
     * RELACIONAMENTO COM O QUADRO (O Dono)
     * ---------------------------------------------------------------------------------
     *
     * @ManyToOne:
     * Define a cardinalidade: MUITAS Colunas... pertencem a... UM Quadro.
     *
     * @JoinColumn(name = "quadro_id"):
     * Cria a coluna "quadro_id" na tabela 'colunas' como Chave Estrangeira (Foreign Key).
     *
     * @JsonIgnore: VITAL!
     * ESSA é a solução para o loop de JSON:
     * 1.  O Back-End envia o Quadro para o Front-End.
     * 2.  O Quadro inclui sua Lista de Colunas (colunas: [...])
     * 3.  Se a Coluna incluísse o objeto Quadro, isso geraria um loop infinito.
     * 4.  O @JsonIgnore instrui o serializador (Jackson) a IGNORAR este campo
     * durante a serialização do objeto Coluna.
     * Isso quebra o loop e permite que os dados sejam enviados de forma limpa.
     */
    @ManyToOne
    @JoinColumn(name = "quadro_id", nullable = false)
    @JsonIgnore // <-- VITAL para quebrar o loop de serialização
    private Quadro quadro;
}