package com.kanban.backend.repository;

import org.springframework.stereotype.Repository;
import com.kanban.backend.model.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository // Diz ao Spring que esta é uma interface de Repositório
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    // JpaRepository<Usuario, Long> significa:
    // "Quero um repositório para a entidade 'Usuario', 
    // onde a Chave Primária (ID) é do tipo 'Long'."
    
    // (Não precisamos escrever mais nada aqui por enquanto!)
    // O JpaRepository já nos dá métodos como:
    // .save()
    // .findById()
    // .findAll()
    // .delete()
    // e muitos outros, de graça!

    Optional<Usuario> findByEmail(String email);
    


}