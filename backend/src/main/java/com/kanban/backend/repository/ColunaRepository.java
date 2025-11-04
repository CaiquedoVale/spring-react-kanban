package com.kanban.backend.repository;

// Imports do NOSSO Projeto
import com.kanban.backend.model.Coluna; // A entidade que este repositório gerencia

// Imports do Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: ColunaRepository.java
 * ANALOGIA: O "Arquivista das Colunas"
 * -------------------------------------------------------------------------------------
 * * @Repository: Marca a interface como um componente de acesso a dados.
 * * * extends JpaRepository<Coluna, Long>:
 * Herda todos os métodos CRUD básicos para a entidade 'Coluna', cuja chave primária
 * é do tipo 'Long'.
 *
 * * Por que este Repositório é simples?
 * Ele não precisa de métodos personalizados porque a lógica de busca e criação de colunas
 * é gerenciada pelo 'QuadroService', que usa métodos básicos como '.saveAll()' (para
 * salvar as 3 colunas de uma vez).
 */
@Repository
public interface ColunaRepository extends JpaRepository<Coluna, Long> {
    
    // Métodos herdados:
    // .save(coluna)
    // .findById(id)
    // .findAll()
    // .delete(coluna)
    
    // (Futuramente, se implementarmos o recurso "Mover Coluna de Quadro", 
    //  adicionaríamos aqui um método personalizado, mas por enquanto, está ótimo assim.)
}