package com.kanban.backend.repository;

// Imports do NOSSO Projeto
import com.kanban.backend.model.Usuario; // A entidade que este repositório gerencia

// Imports do Spring
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Imports do Java
import java.util.Optional; // Para o retorno "seguro" (pode ou não encontrar)

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: UsuarioRepository.java
 * ANALOGIA: O "Arquivista dos Dossiês de Agentes" (Usuários)
 * -------------------------------------------------------------------------------------
 * * @Repository: Diz ao Spring que esta interface é um "Repositório" (um "Bean")
 * para acessar o banco de dados.
 *
 * * extends JpaRepository<Usuario, Long>:
 * Esta é a "Herança Mágica" do Spring Data JPA.
 * Nós dizemos: "Spring, crie um 'Arquivista' para a entidade 'Usuario',
 * onde a Chave Primária (ID) é do tipo 'Long'".
 *
 * Como você mesmo comentou, isso nos dá de graça métodos como:
 * - .save(usuario)        (Salva ou atualiza um usuário)
 * - .findById(id)        (Busca um usuário pelo ID)
 * - .findAll()           (Busca TODOS os usuários)
 * - .deleteById(id)      (Deleta um usuário pelo ID)
 * - ...e muitos outros!
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * -------------------------------------------------------------------------------------
     * MÉTODO: findByEmail (A "Busca Personalizada" - Query Derivada)
     * -------------------------------------------------------------------------------------
     * Este é o método "especial" que o nosso 'UserAuthService'
     * (o "Verificador de Identidade") usa para encontrar um usuário durante o login.
     *
     * O Spring Data JPA lê o nome do método:
     * "find" + "By" + "Email"
     * E entende:
     * "Ah, ele quer um método para 'buscar' (find) 'pelo campo' (By) 'email'
     * (o nome do campo na entidade Usuario.java)".
     *
     * Ele vai gerar automaticamente o SQL:
     * "SELECT * FROM usuarios WHERE email = ?"
     *
     * * Por que 'Optional<Usuario>'?
     * 'Optional' é um "contêiner" do Java. Ele diz: "Eu vou procurar, mas
     * *talvez* eu não encontre o usuário". Isso é muito mais seguro do que
     * retornar 'null'.
     * Isso força o 'UserAuthService' a verificar se o usuário foi
     * encontrado, usando '.orElseThrow()', o que previne bugs.
     *
     * @param email A string de email (o "nome de código") que queremos procurar.
     * @return Um 'Optional' contendo o 'Usuario' (se encontrado) ou vazio (se não).
     */
    Optional<Usuario> findByEmail(String email);

}