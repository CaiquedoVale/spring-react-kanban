package com.kanban.backend.dto;

import lombok.Data;

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: QuadroRequestDTO.java
 * ANALOGIA: O "Formulário de Pedido de Criação de Quadro"
 * -------------------------------------------------------------------------------------
 * * O que é um DTO (Data Transfer Object)?
 * É uma classe simples que define o contrato de dados que o Back-End espera.
 *
 * * Por que usar um DTO?
 * 1.  **Segurança:** Garante que o Front-End *só* possa enviar o campo 'nome'.
 * (Impede que o usuário tente enviar um ID ou um objeto 'Usuario' inteiro
 * no corpo da requisição).
 * 2.  **Clareza:** Define exatamente o que o método 'POST /api/quadros' espera.
 *
 * * @Data (Lombok):
 * Cria automaticamente os Getters e Setters para o campo 'nome'.
 */
@Data
public class QuadroRequestDTO {
    
    // O único campo que o usuário precisa preencher para criar um novo Quadro.
    private String nome;
}