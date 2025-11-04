import React from 'react';
import { useNavigate } from 'react-router-dom'; // <-- // 1. NOVO: Importamos o hook de navegação

function Dashboard() {

    // 2. NOVO: Pegamos a função de navegar
    const navigate = useNavigate();

    // 3. NOVO: Criamos a função de Logout
    const handleLogout = () => {
        // Remove o token do "cofre"
        localStorage.removeItem('jwtToken');
        
        // Redireciona o usuário para a página de login
        navigate('/login');
    };

    return (
        <div>
            <h2>Dashboard</h2>
            <p>Você está logado!</p>
            {/* Na Fase 3, nossos "Quadros" (Boards) vão aparecer aqui */}

            <br /> 
            
            {/* 4. NOVO: O botão que chama nossa função */}
            <button onClick={handleLogout}>
                Sair (Logout)
            </button>
        </div>
    );
}

export default Dashboard;