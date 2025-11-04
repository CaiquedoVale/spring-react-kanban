import React, { useState, useEffect } from 'react'; // Hooks essenciais do React
// Imports do Roteador
import { useParams, useNavigate } from 'react-router-dom'; // useParams (ler URL) e useNavigate (redirecionar)
import apiClient from '../api/axiosConfig'; // O "Mensageiro Inteligente" (já anexa o token)

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: PaginaQuadro.js
 * ANALOGIA: O "Núcleo do Kanban" (A Visão Detalhada)
 * -------------------------------------------------------------------------------------
 * * O que é?
 * É a página de detalhe de um quadro. Ela é responsável por:
 * 1. Ler o ID do quadro na URL.
 * 2. Chamar a API protegida para buscar o quadro (e suas colunas).
 * 3. Renderizar as colunas.
 */
function PaginaQuadro() {
    // 1. LER O ID DA URL
    // { id } pega o parâmetro da rota '/quadro/:id'
    const { id } = useParams();
    const navigate = useNavigate();

    // ----------------------------------------------------
    // "MEMÓRIA" DO COMPONENTE (Estados)
    // ----------------------------------------------------
    const [quadro, setQuadro] = useState(null); // Armazena os dados do quadro (nome, colunas, etc.)
    const [loading, setLoading] = useState(true); // Indica se a busca na API está em andamento
    const [erro, setErro] = useState(null); // Armazena mensagens de erro (404, 403, etc.)
    
    
    /**
     * ----------------------------------------------------
     * HOOK DE EFEITO (Busca de Dados Protegida)
     * ----------------------------------------------------
     * O 'useEffect' roda quando o componente é carregado e sempre que o 'id' muda.
     */
    useEffect(() => {
        const fetchQuadro = async () => {
            try {
                // 1. CHAMA A API PROTEGIDA (com o ID lido na URL)
                const response = await apiClient.get(`/api/quadros/${id}`);
                setQuadro(response.data);
                setLoading(false);
            } catch (error) {
                console.error("Erro ao buscar o quadro:", error);
                
                // 2. TRATAMENTO DE ERROS DE SEGURANÇA E ROTA
                if (error.response) {
                    if (error.response.status === 404) {
                        setErro("Quadro não encontrado."); // Não existe na URL
                    } else if (error.response.status === 403) {
                        setErro("Você não tem permissão para acessar este quadro."); // Existe, mas pertence a outro usuário
                    } else if (error.response.status === 401) {
                         // Token expirado: limpa o passaporte e redireciona para login
                        localStorage.removeItem('jwtToken');
                        navigate('/login');
                    }
                } else {
                    setErro("Falha de rede ao carregar o quadro."); // Back-end offline
                }
                setLoading(false);
            }
        };

        fetchQuadro();
    }, [id, navigate]); // Dependências: Roda se o 'id' da URL mudar ou se a função 'navigate' mudar

    // ----------------------------------------------------
    // LÓGICA DE RENDERIZAÇÃO CONDICIONAL
    // ----------------------------------------------------
    
    // Se ainda estiver carregando, mostra o feedback
    if (loading) {
        return <h2>Carregando Quadro...</h2>;
    }

    // Se houve um erro (404, 403, etc.), mostra a mensagem de erro
    if (erro) {
        return <h2 style={{color: 'red'}}>{erro}</h2>;
    }

    // Se tudo deu certo, mostra o quadro
    return (
        <div>
            <h2>Quadro: {quadro.nome}</h2>
            <p>ID do Quadro: {quadro.id}</p>
            
            {/*
             * Removemos a linha 'Dono: {quadro.usuario.nome}' para evitar erros de
             * 'undefined' no Front-end, já que o Back-End escondeu o objeto 'usuario'
             * por questões de segurança (para quebrar loops de JSON).
             */}
            
            <hr/>
            
            <h3>Colunas do Kanban:</h3>
            <div style={{ display: 'flex', justifyContent: 'space-around', width: '80%', margin: '0 auto' }}>
                
                {/* O MAP FINALMENTE USA A LISTA REAL DE COLUNAS! */}
                {/* O 'quadro.colunas' existe porque o Back-End usou @EntityGraph(colunas) */}
                {quadro.colunas.map((coluna) => (
                    <div 
                        key={coluna.id} // ID real da coluna do banco
                        style={{ 
                            border: '1px solid #ccc', 
                            padding: '10px', 
                            flex: 1, 
                            margin: '0 5px', 
                            backgroundColor: '#f9f9f9'
                        }}
                    >
                        {/* Exibe o nome que veio do banco: "A Fazer", "Fazendo", "Feito" */}
                        <h4>{coluna.nome}</h4> 
                        <p style={{fontStyle: 'italic', color: '#666'}}>Nenhuma tarefa.</p>
                    </div>
                ))}
            </div>
            
            <button onClick={() => navigate('/dashboard')}>Voltar ao Dashboard</button>
        </div>
    );
}

export default PaginaQuadro;