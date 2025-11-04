import React, { useState } from 'react'; // <-- Importa o useState
import { Link, useNavigate } from 'react-router-dom'; // <-- Importa o useNavigate
import apiClient from '../api/axiosConfig';

function Login() {
    // 1. Estados para o formulário e feedback
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [mensagem, setMensagem] = useState('');
    
    // 2. Hook do roteador para nos permitir "empurrar" o usuário
    const navigate = useNavigate(); 

    const handleSubmit = async (event) => {
        event.preventDefault();

        const loginRequest = {
            email: email,
            senha: senha
        };

        try {
            // 3. CHAMA A API DE LOGIN
            const response = await apiClient.post('/api/login', loginRequest);

            // 4. SUCESSO! Pega o token da resposta
            const token = response.data.token;
            
            // 5. SALVA O TOKEN NO LOCALSTORAGE
            // (O localStorage é um "cofre" no navegador)
            localStorage.setItem('jwtToken', token);
            
            setMensagem('Login bem-sucedido! Redirecionando...');

            // 6. REDIRECIONA O USUÁRIO para o Dashboard
            // (Essa página ainda não existe, mas vamos criá-la na Fase 3)
            navigate('/dashboard'); 

        } catch (error) {
            console.error('Erro ao fazer login:', error);
            if (error.response && (error.response.status === 403 || error.response.status === 401)) {
                // Erro de credenciais (email ou senha errada)
                setMensagem('Erro: Email ou senha inválidos.');
            } else {
                // Erro de rede ou o back-end está offline
                setMensagem('Erro: Não foi possível conectar ao servidor.');
            }
        }
    };

    return (
        <div>
            <h2>Login</h2>
            {/* 7. Liga o formulário ao nosso handleSubmit */}
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Email: </label>
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
            
            {/* Exibe mensagens de sucesso ou erro */}
            {mensagem && <p>{mensagem}</p>}

            <p>
                Não tem uma conta? <Link to="/registrar">Registre-se aqui</Link>
            </p>
        </div>
    );
}

export default Login;