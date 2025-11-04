import React from 'react';
import { useNavigate } from 'react-router-dom'; // O "Teletransportador" (para redirecionar)

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: Dashboard.js
 * ANALOGIA: O "Quartel-General" (A "Área Segura")
 * -------------------------------------------------------------------------------------
 * * O que é?
 * Este é o componente "pai" da área logada. É a primeira página que o
 * usuário vê após o login (porque o 'Login.js' o redireciona para cá).
 *
 * * Como está protegido?
 * O 'App.js' definiu que esta rota ('/dashboard') é "filha" do
 * 'ProtectedRoute' ("Segurança da Boate"). Portanto, o usuário
 * só consegue *ver* este componente se ele tiver um 'jwtToken' válido.
 */
function Dashboard() {

    // 1. "CONTRATANDO" O TELETRANSPORTADOR
    // Pegamos a função 'navigate' do 'useNavigate' hook.
    const navigate = useNavigate();

    /**
     * ---------------------------------------------------------------------------------
     * A "AÇÃO" DE SAIR (Handler de Logout)
     * ---------------------------------------------------------------------------------
     * Esta função é chamada quando o usuário clica no botão "Sair".
     */
    const handleLogout = () => {
        
        // PASSO 1: "Queimar o Passaporte"
        // Nós removemos o 'jwtToken' do "Cofre" (localStorage).
        // A partir deste exato momento, o 'ProtectedRoute' (Segurança)
        // passará a considerar o usuário como "não autenticado".
        localStorage.removeItem('jwtToken');
        
        // PASSO 2: "Escolta para a Saída"
        // "Teletransportamos" (redirecionamos) o usuário de volta
        // para a "Área Pública" (a página de Login).
        navigate('/login');
    };

    /**
     * ---------------------------------------------------------------------------------
     * O "VISUAL" (Renderização do JSX)
     * ---------------------------------------------------------------------------------
     */
    return (
        <div>
            <h2>Dashboard</h2>
            <p>Você está logado!</p>
            
            {/*
             * Este é o placeholder que pausamos na Fase 3.
             * Na próxima etapa, vamos substituir este <p> pela
             * lógica de 'useEffect' para *buscar* e *mostrar*
             * os quadros da API 'GET /api/quadros'.
             */}
            {/* Na Fase 3, nossos "Quadros" (Boards) vão aparecer aqui */}

            <br /> 
            
            {/* "Quando este botão for 'clicado', chame a função handleLogout" */}
            <button onClick={handleLogout}>
                Sair (Logout)
            </button>
        </div>
    );
}

export default Dashboard;