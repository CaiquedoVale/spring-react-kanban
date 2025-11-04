import React from 'react';
// Imports do Roteador:
import { Navigate, Outlet } from 'react-router-dom';
// Navigate: O componente que "redireciona" o usuário.
// Outlet:   O "encaixe" onde a página filha (ex: Dashboard) será renderizada.

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: ProtectedRoute.js
 * ANALOGIA: O "Segurança da Boate" (ou "Porteiro VIP")
 * -------------------------------------------------------------------------------------
 * * O que é?
 * Este é um componente "invólucro" (wrapper) que usamos no 'App.js'
 * para "proteger" outras rotas.
 *
 * * Como funciona?
 * O 'App.js' o renderiza PRIMEIRO. Este componente então faz uma
 * verificação de segurança. Se a verificação passar, ele renderiza
 * o <Outlet /> (que é a página que o usuário realmente queria ver).
 * Se a verificação falhar, ele "expulsa" o usuário, redirecionando-o
 * (via <Navigate />) para a página de login.
 */
function ProtectedRoute() {
    
    // PASSO 1: O "Segurança" verifica o "cofre" (localStorage)
    // "O usuário tem um 'jwtToken' (Passaporte) guardado?"
    const token = localStorage.getItem('jwtToken');

    // PASSO 2: A Decisão Simples
    // "O token existe?"
    // (Por enquanto, só checamos se ele "existe". Uma versão mais avançada
    // poderia "decodificar" o token aqui mesmo para ver se ele "expirou".)
    const isAuth = token ? true : false;
    // (Isso é o mesmo que 'const isAuth = !!token;')

    
    // PASSO 3: A Ação (Renderização Condicional)
    // Esta é a lógica ternária ( if/else em uma linha ):
    //
    // CONDIÇÃO: isAuth ?
    // SE VERDADEIRO (Usuário está autenticado): <Outlet />
    // SE FALSO (Usuário NÃO está autenticado): <Navigate to="/login" replace />
    //
    return isAuth ? <Outlet /> : <Navigate to="/login" replace />;
}

export default ProtectedRoute;