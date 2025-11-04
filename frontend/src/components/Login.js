import React, { useState } from 'react'; // 'useState' é a "memória" do componente
import { Link, useNavigate } from 'react-router-dom'; // 'Link' (navegação) e 'useNavigate' (redirecionamento)
import apiClient from '../api/axiosConfig'; // O "Mensageiro Inteligente" (nosso axios pré-configurado)

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: Login.js
 * ANALOGIA: O "Posto de Identificação"
 * -------------------------------------------------------------------------------------
 * * O que é?
 * Este componente renderiza o formulário de login e contém toda a lógica
 * para "autenticar" um usuário (chamar a API, salvar o token, redirecionar).
 */
function Login() {
    
    // ---------------------------------------------------------------------------------
    // "MEMÓRIA" DO COMPONENTE (Estados)
    // ---------------------------------------------------------------------------------
    // 'useState' nos permite dar "memória" ao componente para ele "lembrar"
    // o que está sendo digitado e qual mensagem deve exibir.
    
    const [email, setEmail] = useState(''); // "Memória" para o campo de email
    const [senha, setSenha] = useState(''); // "Memória" para o campo de senha
    const [mensagem, setMensagem] = useState(''); // "Memória" para a mensagem de feedback
    
    // ---------------------------------------------------------------------------------
    // "FERRAMENTAS" DO COMPONENTE (Hooks)
    // ---------------------------------------------------------------------------------
    
    // 'useNavigate' é o "Teletransportador" do React Router.
    // Nós o usamos para "empurrar" o usuário para outra rota (ex: /dashboard)
    // após o login ser bem-sucedido.
    const navigate = useNavigate(); 

    
    /**
     * ---------------------------------------------------------------------------------
     * A "AÇÃO" PRINCIPAL (Handler de Submit)
     * ---------------------------------------------------------------------------------
     * Esta função é chamada quando o usuário clica no botão "Entrar".
     * É 'async' porque ela precisa "esperar" ('await') a resposta da API.
     */
    const handleSubmit = async (event) => {
        // 1. Impede que o formulário HTML recarregue a página inteira.
        event.preventDefault();

        // 2. Cria o "Formulário de Entrada" (DTO) que o back-end espera.
        const loginRequest = {
            email: email,
            senha: senha
        };

        // 3. O "Bloco de Risco" (try...catch)
        // Nós "tentamos" (try) fazer a chamada de API. Se "falhar" (catch),
        // nós pegamos o erro e mostramos uma mensagem amigável.
        try {
            // 4. A "Ligação" para o Back-end
            // "Ei, 'Mensageiro Inteligente' (apiClient), faça um POST para o
            // 'Porteiro' (AuthController) na porta '/api/login', e entregue
            // este 'Formulário' (loginRequest)."
            const response = await apiClient.post('/api/login', loginRequest);

            // ----- SE CHEGOU AQUI, O LOGIN FOI UM SUCESSO! -----
            
            // 5. Desempacotar a Resposta
            // Pega o "Passaporte" (Token) de dentro do "Envelope de Resposta" (DTO)
            // (A resposta é: { "token": "eyJhbGci..." })
            const token = response.data.token;
            
            // 6. O "Ato de Salvar no Cofre"
            // Esta é a linha-chave: Armazenamos o "Passaporte" (token)
            // no "Cofre" do navegador (localStorage).
            // A partir de agora, o 'ProtectedRoute' e o 'axiosConfig'
            // podem "ver" este token e saber que o usuário está logado.
            localStorage.setItem('jwtToken', token);
            
            setMensagem('Login bem-sucedido! Redirecionando...');

            // 7. O "Teletransporte"
            // "Trabalho feito. 'Teletransportador' (navigate),
            // envie o usuário para o '/dashboard'."
            navigate('/dashboard'); 

        } catch (error) {
            // ----- SE CHEGOU AQUI, O LOGIN FALHOU! -----
            console.error('Erro ao fazer login:', error);
            
            // O 'error.response' existe se o *back-end* nos deu um erro
            // (ex: "senha errada") em vez de um erro de rede.
            if (error.response && (error.response.status === 403 || error.response.status === 401)) {
                // Estes são os erros que o Spring Security envia
                // (Forbidden/Unauthorized). Sabemos que é "Email ou senha inválidos".
                setMensagem('Erro: Email ou senha inválidos.');
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
            <h2>Login</h2>
            {/* "Quando este formulário for 'submetido', chame a função handleSubmit" */}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Email: </label>
                    {/*
                     * Este é um "Componente Controlado".
                     * O 'value' (o que é mostrado) é 100% controlado pela
                     * nossa "memória" (estado 'email').
                     * O 'onChange' (o que acontece quando digitamos)
                     * 100% controla a "memória" (atualiza o estado 'email').
                     */}
                    <input 
                        type="email" 
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Senha: </label>
                    <input 
                        type="password" 
                        value={senha}
                        onChange={(e) => setSenha(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Entrar</button>
            </form>
            
            {/*
             * "Renderização Condicional"
             * "Se (&&) a 'mensagem' não for uma string vazia,
             * então renderize um <p> com essa mensagem dentro."
             */}
            {mensagem && <p>{mensagem}</p>}

            {/*
             * O "Link de Navegação"
             * 'Link to' é a versão do React para a tag <a> do HTML.
             * Ele diz ao 'BrowserRouter' (no App.js) para mudar a rota
             * sem recarregar a página inteira.
             */}
            <p>
                Não tem uma conta? <Link to="/registrar">Registre-se aqui</Link>
            </p>
        </div>
    );
}

export default Login;