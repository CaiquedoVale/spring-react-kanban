package com.kanban.backend.model;

// --- Imports do Spring Security ---
// Estes imports transformam esta classe em um "Crachá de Identidade"
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// --- Imports do Java ---
import java.util.Collection;
import java.util.List; // (Import duplicado removido)

// --- Imports do Jackson (para o @JsonIgnore) ---
import com.fasterxml.jackson.annotation.JsonIgnore; // IMPORTANTE para evitar loops de JSON

// --- Imports do Jakarta Persistence (JPA) ---
// Estas são as "Ferramentas de Construção" da tabela
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// --- Imports do Lombok (O "Assistente de Automação") ---
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: Usuario.java
 * ANALOGIA: O "Dossiê Oficial do Agente" (Usuário)
 * -------------------------------------------------------------------------------------
 * * Esta é a classe mais importante do projeto. Ela tem DUAS FUNÇÕES:
 * 1. @Entity (JPA):    É a "Planta Baixa" da tabela 'usuarios' no banco de dados.
 * 2. UserDetails (Seg): É o "Crachá de Identidade" que o Spring Security usa.
 *
 * * @Data (Lombok): Cria Getters, Setters, .toString(), etc.
 * * @NoArgsConstructor (Lombok): Cria um construtor vazio (exigido pelo JPA).
 * * @AllArgsConstructor (Lombok): Cria um construtor com todos os campos.
 * * @Entity (JPA): "Spring, esta classe É uma tabela."
 * * @Table(name = "usuarios") (JPA): "O nome da tabela no banco deve ser 'usuarios'".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails { // <-- A MÁGICA DA SEGURANÇA!
    
    // ---------------------------------------------------------------------------------
    // PARTE 1: A "PLANTA BAIXA" DA TABELA (CAMPOS JPA)
    // ---------------------------------------------------------------------------------

    /**
     * @Id: Chave Primária.
     * @GeneratedValue(strategy = GenerationType.IDENTITY): Usa o auto-incremento do MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column(nullable = false): A coluna 'nome' não pode ser nula.
     */
    @Column(nullable = false)
    private String nome;

    /**
     * @Column(nullable = false, unique = true):
     * A coluna 'email' não pode ser nula E deve ser única (não podem existir dois
     * usuários com o mesmo email).
     * Este é o nosso "username" (nome de código do agente).
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * @Column(nullable = false): A coluna 'senha' não pode ser nula.
     * Esta coluna vai armazenar a senha JÁ CRIPTOGRAFADA (pelo BCrypt).
     */
    @Column(nullable = false)
    private String senha;

    /**
     * ---------------------------------------------------------------------------------
     * RELACIONAMENTO: Os "Quadros" do Usuário
     * ---------------------------------------------------------------------------------
     *
     * @OneToMany: Define o relacionamento: "UM Usuário... pode ter... MUITOS Quadros".
     *
     * @mappedBy = "usuario":
     * A linha MAIS IMPORTANTE. Diz ao JPA: "Eu (Usuario) NÃO sou o dono
     * deste relacionamento. A classe 'Quadro', no campo 'usuario', é a dona.
     * Apenas 'espelhe' o que ela fizer. Não crie uma tabela de junção."
     *
     * @cascade = CascadeType.ALL:
     * "Regra de Cascata": Se eu (Usuario) for deletado, por favor,
     * delete EM CASCATA todos os 'Quadros' que me pertencem.
     *
     * @orphanRemoval = true:
     * "Remoção de Órfãos": Se um quadro for removido desta lista (ex: usuario.getQuadros().remove(0)),
     * ele se torna um "órfão" e deve ser deletado do banco de dados.
     *
     * @JsonIgnore: (CRÍTICO!)
     * O "Quebra-Loop". Diz ao conversor de JSON (Jackson): "Quando você for
     * transformar este 'Usuario' em JSON, IGNORE este campo 'quadros'".
     * Isso impede o loop: Usuario -> Lista<Quadro> -> Usuario -> Lista<Quadro>...
     */
    @OneToMany(
        mappedBy = "usuario", 
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<Quadro> quadros;

    
    // ---------------------------------------------------------------------------------
    // PARTE 2: O "CRACHÁ DE IDENTIDADE" (MÉTODOS DO 'UserDetails')
    // ---------------------------------------------------------------------------------
    // O Spring Security não sabe o que é um "Usuario". Mas ele sabe o que é
    // um 'UserDetails'. Ao implementar estes métodos, nós "traduzimos"
    // nossa classe para o formato que a segurança entende.
    // O 'UserAuthService' retorna esta classe, e o Spring Security
    // usa estes métodos.

    /**
     * Os "Crachás" (Autorização)
     * Retorna a lista de "autoridades" (perfis, roles) que o usuário tem.
     * É isso que o `.hasAuthority("ROLE_USER")` no 'SecurityConfig' verifica.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por enquanto, todo usuário que se registra é um "USER" padrão.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * A "Senha Secreta" (Autenticação)
     * O Spring Security chama este método para pegar a senha CRIPTOGRAFADA
     * do banco e compará-la com a senha que o usuário digitou (que o Spring
     * também criptografa).
     */
    @Override
    public String getPassword() {
        return this.senha;
    }

    /**
     * O "Nome de Código" (Autenticação)
     * O Spring Security chama este método para saber qual campo é o
     * "identificador" principal (o username). No nosso caso, é o email.
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    // --- Métodos de Controle de Conta ---
    // (O Spring exige estes métodos para checar se a conta está ativa,
    //  trancada, expirada, etc. Por enquanto, só retornamos 'true'.)

    @Override
    public boolean isAccountNonExpired() {
        return true; // A conta não expirou
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // A conta não está trancada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // As credenciais não expiraram
    }

    @Override
    public boolean isEnabled() {
        return true; // A conta está habilitada
    }
}