package com.kanban.backend.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // Lombok: Cria getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok: Cria um construtor sem argumentos
@AllArgsConstructor // Lombok: Cria um construtor com todos os argumentos
@Entity // JPA: Diz ao Spring que esta classe é uma tabela no banco
@Table(name = "usuarios") // JPA: Define o nome da tabela (plural de 'usuario')
public class Usuario implements UserDetails{
    
    @Id //significa que e uma chave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)//usa o auto-incremento do MySQL
    private Long id;
    @Column(nullable = false) //nullable = false, diz que nao pode ser nulo
    private String nome;
    @Column(nullable = false, unique = true)// nullable =  false, significa que nao pode ser nulo e unique = true significa que deve ser unico
    private String email;
    @Column(nullable = false)
    private String senha;

        // --- Métodos da Interface UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Agora, estamos dizendo que todo usuário tem o "crachá" de "ROLE_USER"
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha; // O Spring vai usar este método para pegar a senha
    }

    @Override
    public String getUsername() {
        return this.email; // O Spring vai usar este método para pegar o "username" (nosso email)
    }

    // Métodos para controle de conta (não vamos usar agora, mas é obrigatório)
    @Override
    public boolean isAccountNonExpired() {
        return true; // Conta não expirada
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Conta não bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credenciais não expiradas
    }

    @Override
    public boolean isEnabled() {
        return true; // Conta habilitada
    }

    
}
