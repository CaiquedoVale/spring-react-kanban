package com.kanban.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanban.backend.model.Usuario;
import com.kanban.backend.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public Usuario registrarUsuario(@RequestBody Usuario novoUsuario) {

        novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        return usuarioSalvo;
    }
}
