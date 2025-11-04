package com.kanban.backend.service;

// Imports de Entidades e Repositórios
import com.kanban.backend.model.Coluna;
import com.kanban.backend.model.Quadro;
import com.kanban.backend.model.Usuario;
import com.kanban.backend.repository.ColunaRepository;
import com.kanban.backend.repository.QuadroRepository;

// Imports do Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Imports do Java
import java.util.List;

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: QuadroService.java
 * ANALOGIA: O "Especialista em Construção de Quadros"
 * -------------------------------------------------------------------------------------
 * * @Service: Marca a classe como um componente de "Lógica de Negócio" (Bean)
 * para que o Spring possa injetá-la em outros componentes (ex: QuadroController).
 * * Este Serviço lida com a regra de negócio central da Fase 3:
 * "Ao criar um quadro, crie automaticamente as colunas padrão."
 */
@Service
public class QuadroService {
    
    // 1. CONTRATAÇÃO DE ARQUIVISTAS
    // Injetamos os repositórios necessários para salvar os dados no banco.
    @Autowired
    private QuadroRepository quadroRepository;
    
    @Autowired
    private ColunaRepository colunaRepository;
    
    
    /**
     * ---------------------------------------------------------------------------------
     * MÉTODO CENTRAL: criarQuadroComColunasPadrao (Lógica de Negócio)
     * ---------------------------------------------------------------------------------
     * Este método encapsula a regra de criação do Quadro, garantindo que as regras
     * sejam seguidas.
     *
     * @param nomeQuadro O nome que o usuário deu ao Quadro.
     * @param usuario O objeto 'Usuario' (o dono) que o sistema de segurança identificou.
     * @return O Quadro recém-criado.
     */
    public Quadro criarQuadroComColunasPadrao(String nomeQuadro, Usuario usuario) {
        
        // PASSO 1: Criar e Salvar o Quadro Principal
        Quadro novoQuadro = new Quadro();
        novoQuadro.setNome(nomeQuadro);
        novoQuadro.setUsuario(usuario); // Associa o dono
        
        // Salva o quadro no banco para que ele receba seu ID único.
        Quadro quadroSalvo = quadroRepository.save(novoQuadro);

        // PASSO 2: Definir as Colunas Padrão (Regra de Negócio)
        List<String> nomesColunas = List.of("A Fazer", "Fazendo", "Feito");

        // PASSO 3: Mapear Nomes para Entidades 'Coluna'
        // Usamos Streams para iterar sobre a lista de nomes e criar um objeto Coluna para cada um.
        List<Coluna> colunasPadrao = nomesColunas.stream().map(nomeColuna -> {
            Coluna coluna = new Coluna();
            coluna.setNome(nomeColuna);
            coluna.setQuadro(quadroSalvo); // Associa a Coluna ao Quadro recém-salvo (ID)
            return coluna;
        }).toList();

        // PASSO 4: Salvar Todas as Colunas de uma Vez
        // Usamos 'saveAll' para otimizar o acesso ao banco.
        colunaRepository.saveAll(colunasPadrao);
        
        // Retorna o quadro que foi salvo e agora possui um ID.
        return quadroSalvo;
    }
}