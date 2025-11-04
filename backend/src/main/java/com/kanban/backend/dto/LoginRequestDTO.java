package com.kanban.backend.dto;

import lombok.Data;

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: LoginRequestDTO.java
 * ANALOGIA: O "Formulário de Login"
 * -------------------------------------------------------------------------------------
 * * O que é um DTO (Data Transfer Object)?
 * É uma classe "burra" que serve apenas para carregar dados entre processos
 * (neste caso, entre o Front-end (React) e o Back-end (Spring)).
 *
 * * Por que usar um DTO?
 * 1.  **Segurança:** Evita que o front-end envie dados indesejados
 * diretamente para nossa entidade de banco de dados (ex: um campo 'role: "ADMIN"').
 * 2.  **Contrato:** Define um "contrato" claro para a API. O front-end SABE
 * que precisa enviar um JSON com "email" e "senha".
 * 3.  **Flexibilidade:** Podemos mudar nossa entidade 'Usuario' (ex: adicionar
 * 'telefone') sem quebrar a API de login, pois ela só espera este DTO.
 *
 * * @Data (Lombok):
 * Esta é uma anotação "mágica" do Lombok. Em tempo de compilação, ela
 * automaticamente escreve para nós:
 * - Todos os Getters (ex: getEmail(), getSenha())
 * - Todos os Setters (ex: setEmail(), setSenha())
 * - Um construtor com todos os argumentos (ex: LoginRequestDTO(String email, String senha))
 * - Os métodos .equals(), .hashCode() e .toString()
 * Ela transforma esta classe simples em uma classe de dados Java completa.
 */
@Data
public class LoginRequestDTO {

    // O Spring/Jackson (conversor de JSON) vai ler o JSON que chega do front-end
    // e tentar "encaixar" os campos aqui.
    // { "email": "...", "senha": "..." }

    private String email;
    private String senha;
}