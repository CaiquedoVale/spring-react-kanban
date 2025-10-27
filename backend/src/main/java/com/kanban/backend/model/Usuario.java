package com.kanban.backend.model;

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
public class Usuario {
    
    @Id //significa que e uma chave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)//usa o auto-incremento do MySQL
    private Long id;
    @Column(nullable = false) //nullable = false, diz que nao pode ser nulo
    private String nome;
    @Column(nullable = false, unique = true)// nullable =  false, significa que nao pode ser nulo e unique = true significa que deve ser unico
    private String email;
    @Column(nullable = false)
    private String senha;
}
