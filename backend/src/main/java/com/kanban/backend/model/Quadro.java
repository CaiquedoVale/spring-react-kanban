package com.kanban.backend.model;

// Imports do Jakarta Persistence (JPA) - As "Ferramentas de Construção"
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn; // Para definir a "coluna de junção" (Chave Estrangeira)
import jakarta.persistence.ManyToOne; // Para definir o relacionamento "Muitos-para-Um"
import jakarta.persistence.Table;

// Imports do Lombok (O "Assistente de Automação" de código)
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: Quadro.java
 * ANALOGIA: A "Planta Baixa do Quadro" (Board)
 * -------------------------------------------------------------------------------------
 * * O que é uma Entidade (@Entity)?
 * É uma classe Java que o Spring (JPA/Hibernate) usa como "molde"
 * para criar ou gerenciar uma tabela no banco de dados.
 *
 * * @Data (Lombok):
 * Cria automaticamente todos os Getters, Setters, .toString(), .equals() e .hashCode().
 *
 * * @NoArgsConstructor (Lombok):
 * Cria um construtor "vazio" (ex: new Quadro()). O JPA precisa disso
 * para conseguir criar os objetos quando ele lê os dados do banco.
 *
 * * @AllArgsConstructor (Lombok):
 * Cria um construtor com todos os campos (ex: new Quadro(id, nome, usuario)).
 *
 * * @Entity (JPA):
 * A anotação mais importante. "Spring, esta classe É uma tabela."
 *
 * * @Table(name = "quadros") (JPA):
 * "Spring, o nome da tabela no banco de dados MySQL deve ser 'quadros'".
 * (Se omitíssemos isso, o nome seria 'quadro', igual ao da classe).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quadros")
public class Quadro {
    
    /**
     * @Id (JPA):
     * Define que este campo é a "Chave Primária" (Primary Key) da tabela.
     * É o identificador único de cada quadro.
     *
     * @GeneratedValue(strategy = GenerationType.IDENTITY):
     * Diz ao Spring: "Eu não vou te dizer qual é o ID. Deixe o próprio
     * banco de dados (MySQL) cuidar disso usando o 'auto-incremento'".
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column(nullable = false):
     * "Mapeie este campo para uma coluna. Esta coluna NÃO PODE ser nula."
     * (Todo quadro deve ter um nome).
     */
    @Column(nullable = false)
    private String nome;

    
    /**
     * -------------------------------------------------------------------------------------
     * RELACIONAMENTO: O "Dono" do Quadro
     * -------------------------------------------------------------------------------------
     *
     * @ManyToOne:
     * Define a "cardinalidade" do relacionamento.
     * Lemos da seguinte forma: "MUITOS Quadros... podem pertencer a... UM Usuário".
     *
     * @JoinColumn(name = "usuario_id", nullable = false):
     * Esta é a "implementação" física do relacionamento.
     * "Spring, para fazer essa conexão, crie uma coluna na minha tabela 'quadros'
     * e dê a ela o nome de 'usuario_id'."
     * O Spring vai usar esta coluna como uma "Chave Estrangeira" (Foreign Key)
     * que "aponta" para o 'id' da tabela 'usuarios'.
     * 'nullable = false' garante que um quadro NUNCA pode ser criado
     * sem um "dono" (usuário).
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    // (Mais tarde, na Fase 3, adicionaremos o relacionamento @OneToMany
    //  para as 'Colunas' que pertencem a este quadro)
}