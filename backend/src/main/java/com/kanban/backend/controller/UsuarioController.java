package com.kanban.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanban.backend.model.Usuario;
import com.kanban.backend.repository.UsuarioRepository;

@RestController // Diz ao Spring que esta classe é um Controller de API REST (retorna JSON)
@RequestMapping("/api/usuarios") // Define o prefixo da URL para todos os endpoints nesta classe
public class UsuarioController {

    // Injeção de Dependência:
    // O Spring vai automaticamente criar uma instância do UsuarioRepository
    // e "injetá-la" nesta variável para podermos usar.
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Endpoint de Registro
    // @PostMapping diz que este método responde a requisições HTTP POST
    // O endpoint completo será: POST http://localhost:8080/api/usuarios
    @PostMapping
    public Usuario registrarUsuario(@RequestBody Usuario novoUsuario) {
        // @RequestBody diz ao Spring: "Pegue o JSON que veio no corpo
        // da requisição e transforme-o em um objeto 'Usuario'."
        
        // (Nas próximas fases, vamos adicionar validação e criptografia da senha aqui)

        // Salva o novo usuário no banco de dados e o retorna
        // O .save() já faz o "INSERT INTO" para nós.
        return usuarioRepository.save(novoUsuario);
    }
}
