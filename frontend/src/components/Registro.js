import React, { useState } from 'react'; // 'useState' é a "memória" do componente
import apiClient from '../api/axiosConfig'; // O "Mensageiro Inteligente" (nosso axios pré-configurado)
import { Link } from 'react-router-dom'; // Para o link "Voltar ao Login"

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: Registro.js
 * ANALOGIA: O "Posto de Recrutamento"
 * -------------------------------------------------------------------------------------
 * * O que é?
 * Este componente renderiza o formulário de "Novo Usuário" e contém a lógica
 * para enviar os dados para a API de registro do back-end.
 */
function Registro() {
    
    // ---------------------------------------------------------------------------------
    // "MEMÓRIA" DO COMPONENTE (Estados)
    // ---------------------------------------------------------------------------------
    // Criamos uma "memória" para cada campo do formulário e para
    // a mensagem de feedback.
    const [nome, setNome] = useState('');
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [mensagem, setMensagem] = useState(''); 

    
    /**
     * ---------------------------------------------------------------------------------
     * A "AÇÃO" PRINCIPAL (Handler de Submit)
     * ---------------------------------------------------------------------------------
     * Esta função é chamada quando o usuário clica no botão "Registrar".
     * É 'async' porque "espera" ('await') a resposta da API.
     */
    const handleSubmit = async (event) => {
        // 1. Impede que o formulário HTML recarregue a página inteira.
        event.preventDefault(); 
        
        // 2. Cria o "Dossiê" (objeto/payload) para enviar ao back-end.
        // O back-end espera um objeto 'Usuario' (que tem 'nome', 'email', 'senha').
        const novoUsuario = {
            nome: nome,
            email: email,
            senha: senha
        };

        // 3. O "Bloco de Risco" (try...catch)
        // Tentamos registrar. Se o back-end der erro (ex: email já existe),
        // nós "pegamos" (catch) o erro e mostramos a 'mensagem'.
        try {
            // 4. A "Ligação" para o Back-end
            // "Ei, 'Mensageiro Inteligente' (apiClient), faça um POST para a
            // 'Porta de Registro' (/api/usuarios) e entregue este 'Dossiê' (novoUsuario)."
            const response = await apiClient.post('/api/usuarios', novoUsuario);
            
            // ----- SE CHEGOU AQUI, O REGISTRO FOI UM SUCESSO! -----
            
            console.log('Usuário registrado:', response.data);
            
            // 5. Dá um feedback amigável para o usuário.
            setMensagem(`Usuário ${response.data.nome} registrado com sucesso!`);

            // 6. Limpa os campos do formulário (boa prática de UX)
            setNome('');
            setEmail('');
            setSenha('');

        } catch (error) {
            // ----- SE CHEGOU AQUI, O REGISTRO FALHOU! -----
            console.error('Erro ao registrar:', error);
            
            // O 'error.response' existe se o *back-end* nos deu um erro
            if (error.response) {
                // Ex: "Email já cadastrado" (este seria um erro 400 ou 500)
                // (Para o futuro: o ideal é o back-end mandar um JSON de erro
                //  ex: { "message": "Email já em uso" } )
                setMensagem(`Erro: ${error.response.data.message || 'Não foi possível registrar'}`);
            } else {
                // Se 'error.response' não existir, é um erro de rede
                // (ex: back-end está offline).
                setMensagem('Erro: Não foi possível conectar ao servidor.');
            }
        }
    };

    /**
     * ---------------------------------------------------------------------------------
     * O "VISUAL" (Renderização do JSX)
     * ---------------------------------------------------------------------------------
     */
    return (
        <div>
            <h2>Formulário de Registro</h2>
            {/* "Quando este formulário for 'submetido', chame a função handleSubmit" */}
            <form onSubmit={handleSubmit}>
                
                {/* * "Componentes Controlados"
                 * O 'value' (o que é mostrado) é 100% controlado pela
                 * "memória" (estado 'nome').
                 * O 'onChange' (o que acontece quando digitamos)
                 * 100% controla a "memória" (atualiza o estado 'nome').
                 */}
                <div>
                    <label>Nome:</label>
                    <input 
                        type="text" 
                        value={nome} 
                        onChange={(e) => setNome(e.target.value)} 
                        required 
                    />
                </div>
                <div>
                    <label>Email:</label>
                    <input 
                        type="email" 
                        value={email} 
                        onChange={(e) => setEmail(e.target.value)} 
                        required 
                    />
                </div>
                <div>
                    <label>Senha:</label>
                    <input 
                        type="password" 
                        value={senha} 
                        onChange={(e) => setSenha(e.target.value)} 
                        required 
                    />
                </div>
                <button type="submit">Registrar</button>
            </form>
            
            {/* "Renderização Condicional" da mensagem de feedback */}
            {mensagem && <p>{mensagem}</p>}

            {/* O "Link de Navegação" para voltar ao Login */}
            <p>
                  Já tem uma conta? <Link to="/login">Faça o login aqui</Link>
            </p>
        </div>
    );
}

export default Registro;