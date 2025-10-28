import React, { useState } from 'react';
import axios from 'axios'; // <-- 1. IMPORTAMOS O AXIOS

function Registro() {
    const [nome, setNome] = useState('');
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [mensagem, setMensagem] = useState(''); // <-- 2. Estado para feedback

    // Função que será chamada quando o formulário for enviado
    const handleSubmit = async (event) => { // <-- 3. Marcamos como 'async'
        event.preventDefault(); 
        
        // 4. CRIAMOS O OBJETO PARA ENVIAR
        const novoUsuario = {
            nome: nome,
            email: email,
            senha: senha
        };

        // 5. USAMOS O AXIOS PARA FAZER O POST
        try {
            // Enviamos o 'novoUsuario' para a URL do nosso back-end
            const response = await axios.post('http://localhost:8080/api/usuarios', novoUsuario);
            
            console.log('Usuário registrado:', response.data);
            setMensagem(`Usuário ${response.data.nome} registrado com sucesso! (ID: ${response.data.id})`);

            // Limpar o formulário
            setNome('');
            setEmail('');
            setSenha('');

        } catch (error) {
            console.error('Erro ao registrar:', error);
            if (error.response) {
                // O back-end retornou um erro (ex: email já existe)
                setMensagem(`Erro: ${error.response.data.message || 'Não foi possível registrar'}`);
            } else {
                // Erro de rede ou o back-end está offline
                setMensagem('Erro: Não foi possível conectar ao servidor.');
            }
        }
    };

    return (
        <div>
            <h2>Formulário de Registro</h2>
            <form onSubmit={handleSubmit}>
                {/* ... (Os campos do formulário continuam iguais) ... */}
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
            
            {/* 6. EXIBIMOS A MENSAGEM DE SUCESSO OU ERRO */}
            {mensagem && <p>{mensagem}</p>}
        </div>
    );
}

export default Registro;