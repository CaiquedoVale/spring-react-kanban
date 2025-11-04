package com.kanban.backend.service;

// Imports do NOSSO Projeto
import com.kanban.backend.model.Usuario;
import com.kanban.backend.repository.UsuarioRepository;

// Imports do Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Imports do Spring Security
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // A exceção que *devemos* lançar

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: UserAuthService.java
 * ANALOGIA: O "Verificador de Identidade"
 * -------------------------------------------------------------------------------------
 * * @Service: "Contrata" esta classe como um "Especialista" (Bean) do Spring.
 *
 * * O que é 'UserDetailsService'?
 * Esta é uma interface do Spring Security que tem UMA ÚNICA MISSÃO:
 * "Dado um 'username' (no nosso caso, o email), busque e retorne os
 * 'detalhes' do usuário (UserDetails)".
 *
 * * Por quê?
 * O "Chefe de Segurança" ('AuthenticationManager') não sabe como falar
 * com seu banco de dados. Esta classe é o "tradutor" ou "assistente"
 * que ele usa para encontrar o usuário.
 */
@Service
public class UserAuthService implements UserDetailsService {

    // 1. "CONTRATANDO" O ARQUIVISTA
    // Pede ao Spring que "injete" (nos dê uma instância) do nosso
    // 'UsuarioRepository' (o "Arquivista").
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * -------------------------------------------------------------------------------------
     * MÉTODO: loadUserByUsername (A "Busca de Dossiê")
     * -------------------------------------------------------------------------------------
     * Este é o único método exigido pela interface 'UserDetailsService'.
     * O 'AuthenticationManager' vai chamar este método automaticamente
     * durante o processo de login.
     *
     * @param email (O 'username' que o usuário digitou no formulário de login)
     * @return UserDetails (O "Dossiê" do usuário que o Spring Security entende)
     * @throws UsernameNotFoundException (A exceção que o Spring espera se o usuário não for encontrado)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. CHAMA O ARQUIVISTA
        // Usa o 'UsuarioRepository' para buscar no banco de dados
        // usando o método 'findByEmail' que nós criamos.
        return usuarioRepository.findByEmail(email)
            
            // 2. E SE NÃO ENCONTRAR?
            // .orElseThrow(): Se o 'findByEmail' retornar um "Optional" vazio
            // (usuário não existe), nós devemos (OBRIGATORIAMENTE)
            // lançar a exceção 'UsernameNotFoundException'.
            // O Spring Security vai pegar essa exceção e convertê-la
            // em um erro 401/403 (Credenciais Inválidas) para o usuário.
            .orElseThrow(() -> 
                new UsernameNotFoundException("Usuário não encontrado com o email: " + email)
            );
        
        // 3. E SE ENCONTRAR?
        // Se o usuário for encontrado, o '.orElseThrow' é ignorado e
        // o método simplesmente retorna o objeto 'Usuario' encontrado.
        //
        // (Isso funciona porque, na Fase 2, nós fizemos a nossa classe
        // 'Usuario' "implementar" a interface 'UserDetails', então o Spring
        // já a entende como um "Dossiê" válido).
    }
}