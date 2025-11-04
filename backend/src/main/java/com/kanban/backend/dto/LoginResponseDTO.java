package com.kanban.backend.dto;

import lombok.Data;

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: LoginResponseDTO.java
 * ANALOGIA: O "Envelope de Resposta" com o Passaporte (Token)
 * -------------------------------------------------------------------------------------
 * * O que é?
 * Este DTO (Data Transfer Object) é o "contrato de saída" para um login bem-sucedido.
 * Ele define o formato exato do JSON que nosso back-end enviará ao front-end
 * após o 'AuthController' (Porteiro) confirmar a identidade do usuário.
 *
 * * @Data (Lombok):
 * Assim como no LoginRequestDTO, esta anotação do Lombok cria automaticamente:
 * - O Getter (ex: getToken())
 * - O Setter (ex: setToken())
 * - O método .toString()
 * - E outros métodos úteis.
 */
@Data
public class LoginResponseDTO {

    // O único "item" dentro do nosso envelope de resposta.
    // O Spring/Jackson (conversor de JSON) vai transformar esta classe
    // em um JSON assim:
    // { "token": "eyJhbGciOiJ..." }
    private String token;

    /**
     * Construtor
     *
     * Nós criamos um construtor que aceita o token.
     * Isso torna nosso 'AuthController' (Porteiro) mais limpo.
     * Em vez de:
     * LoginResponseDTO response = new LoginResponseDTO();
     * response.setToken(token);
     * return response;
     *
     * Nós podemos fazer em uma linha:
     * return new LoginResponseDTO(token);
     */
    public LoginResponseDTO(String token) {
        this.token = token;
    }
}