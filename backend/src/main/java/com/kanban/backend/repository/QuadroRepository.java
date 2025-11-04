package com.kanban.backend.repository;

// Imports do NOSSO Projeto
import com.kanban.backend.model.Quadro;   // A entidade que este repositório gerencia
import com.kanban.backend.model.Usuario; // A entidade 'Usuario' (para o filtro)

// Imports do Spring
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Para retornar uma Lista de Quadros

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: QuadroRepository.java
 * ANALOGIA: O "Arquivista dos Quadros"
 * -------------------------------------------------------------------------------------
 * * @Repository: Diz ao Spring que esta interface é um "Repositório" (um "Bean")
 * e que ele deve ser usado para acessar o banco de dados.
 * (Com JpaRepository, isso é opcional, mas é uma excelente prática para clareza).
 *
 * * extends JpaRepository<Quadro, Long>:
 * Esta é a "Herança Mágica" do Spring Data JPA.
 * Nós dizemos: "Spring, crie um 'Arquivista' para a entidade 'Quadro',
 * onde a chave primária (ID) é do tipo 'Long'".
 *
 * Automaticamente, o Spring nos dá de graça métodos como:
 * - .save(quadro)        (Salva um quadro novo ou atualiza um existente)
 * - .findById(id)        (Busca um quadro pelo seu ID)
 * - .findAll()           (Busca TODOS os quadros no banco)
 * - .deleteById(id)      (Deleta um quadro pelo ID)
 * - ...e muitos outros!
 */
@Repository
public interface QuadroRepository extends JpaRepository<Quadro, Long> {

    /**
     * -------------------------------------------------------------------------------------
     * MÉTODO: findByUsuario (A "Busca Personalizada" - Query Derivada)
     * -------------------------------------------------------------------------------------
     * Nós precisamos de um método que não vem por padrão: "Buscar todos os quadros
     * que pertencem a um usuário específico".
     *
     * O Spring Data JPA é inteligente. Nós não precisamos escrever o SQL.
     * Nós apenas declaramos um método seguindo a convenção de nomenclatura:
     *
     * "find" + "By" + "Usuario"
     *
     * O Spring lê isso e entende:
     * "Ah, ele quer um método para 'buscar' (find) 'pelo campo' (By) 'usuario'
     * (o nome do campo na entidade Quadro.java)".
     *
     * Ele vai gerar automaticamente o SQL:
     * "SELECT * FROM quadros WHERE usuario_id = ?"
     *
     * O 'QuadroController' usa este método para listar os quadros do usuário logado.
     *
     * @param usuario O objeto 'Usuario' (o "dono") que queremos usar como filtro.
     * @return Uma Lista (List<Quadro>) de todos os quadros que pertencem a esse usuário.
     */
    List<Quadro> findByUsuario(Usuario usuario);

}