import React, { useState, useEffect } from 'react'; // 'useState' (memória) e 'useEffect' (efeito)
import { Link, useNavigate } from 'react-router-dom'; // 'Link' (navegação) e 'useNavigate' (redirecionamento)
import apiClient from '../api/axiosConfig'; // O "Mensageiro Inteligente" (já anexa o token)

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: Dashboard.js (Versão 3.0)
 * ANALOGIA: O "Painel de Controle" e Listagem de Quadros
 * -------------------------------------------------------------------------------------
 * * Este componente é a "casa" do usuário logado.
 * * Ele é responsável por: 1) Buscar a lista de quadros. 2) Permitir a criação de novos quadros.
 */
function Dashboard() {
    const navigate = useNavigate();

    // ---------------------------------------------------------------------------------
    // "MEMÓRIA" DO COMPONENTE (Estados)
    // ---------------------------------------------------------------------------------
    const [quadros, setQuadros] = useState([]);         // Armazena a lista de quadros vindos da API
    const [mensagem, setMensagem] = useState('Carregando quadros...'); // Mensagem de feedback
    const [novoQuadroNome, setNovoQuadroNome] = useState(''); // Armazena o texto digitado para novo quadro
    
    
    /**
     * ---------------------------------------------------------------------------------
     * FUNÇÃO DE BUSCA (Core da Listagem)
     * ---------------------------------------------------------------------------------
     * Busca a lista de quadros do usuário na API protegida.
     * Deixamos a função FORA do useEffect para que a possamos chamar novamente
     * *após a criação* de um novo quadro.
     */
    const fetchQuadros = async () => {
        try {
            // 1. CHAMA A API PROTEGIDA (O 'apiClient' anexa o token automaticamente)
            const response = await apiClient.get('/api/quadros');
            
            // 2. SUCESSO! Salva a lista na "memória"
            setQuadros(response.data);

            // 3. Atualiza a mensagem de feedback
            if (response.data.length === 0) {
                setMensagem('Você ainda não tem quadros. Crie um!');
            } else {
                setMensagem('');
            }

        } catch (error) {
            // 4. FALHA DE SEGURANÇA (Token expirado/Inválido)
            if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                // Se a API nos rejeita, assumimos que o token é ruim
                localStorage.removeItem('jwtToken');
                navigate('/login'); // Escolta o usuário para a página de login
            } else {
                console.error('Erro ao buscar quadros:', error);
                setMensagem('Erro ao carregar quadros. Tente recarregar a página.');
            }
        }
    };

    
    /**
     * ---------------------------------------------------------------------------------
     * LÓGICA DE CRIAÇÃO (Core da Criação)
     * ---------------------------------------------------------------------------------
     * É chamada quando o formulário "Criar Novo Quadro" é submetido.
     */
    const handleCreateQuadro = async (event) => {
        event.preventDefault();

        if (!novoQuadroNome.trim()) {
            alert("O nome do quadro não pode estar vazio.");
            return;
        }

        try {
            // 1. CHAMA A API POST /api/quadros (O serviço cria o Quadro + 3 Colunas)
            await apiClient.post('/api/quadros', { nome: novoQuadroNome });
            
            // 2. SUCESSO! Limpa o campo de texto
            setNovoQuadroNome('');
            
            // 3. RECUPERA A LISTA DE DADOS ATUALIZADA
            // Chamamos a função de busca novamente, o que força o componente a
            // buscar a nova lista do Back-End e renderizar o quadro recém-criado.
            await fetchQuadros(); 

        } catch (error) {
            console.error('Erro ao criar quadro:', error);
            alert('Falha ao criar quadro. Tente novamente.');
        }
    };

    
    /**
     * ---------------------------------------------------------------------------------
     * HOOK DE EFEITO (Carregamento Inicial)
     * ---------------------------------------------------------------------------------
     */
    useEffect(() => {
        // Quando o componente carrega, chame a função de busca.
        fetchQuadros();
    }, [navigate, fetchQuadros]); // <-- CORRETO: Incluímos 'fetchQuadros' e 'navigate' nas dependências para evitar avisos do React.

    
    // Função de Logout (continua igual)
    const handleLogout = () => {
        localStorage.removeItem('jwtToken');
        navigate('/login');
    };

    // ---------------------------------------------------------------------------------
    // O "VISUAL" (Renderização do JSX)
    // ---------------------------------------------------------------------------------
    return (
        <div>
            <h2>Dashboard</h2>
            <button onClick={handleLogout}>
                Sair (Logout)
            </button>
            
            <hr />

            <h3>Criar Novo Quadro</h3>
            <form onSubmit={handleCreateQuadro}>
                <input
                    type="text"
                    placeholder="Nome do Novo Quadro"
                    value={novoQuadroNome}
                    onChange={(e) => setNovoQuadroNome(e.target.value)}
                    required
                />
                <button type="submit">Criar Quadro</button>
            </form>
            <hr />
            
            <h3>Seus Quadros</h3>
            {/* Lógica de Renderização (MAP) */}
            {/* Se a lista estiver vazia, mostra a mensagem, senão, mapeia a lista */}
            {quadros.length === 0 ? (
                <p>{mensagem}</p>
            ) : (
                <ul>
                    {/* .map() renderiza cada quadro como um item clicável */}
                    {quadros.map(quadro => (
                        <li key={quadro.id}>
                            {/* O Link nos leva para a rota protegida /quadro/[ID] */}
                            <Link to={`/quadro/${quadro.id}`}>
                                {quadro.nome}
                            </Link>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default Dashboard;