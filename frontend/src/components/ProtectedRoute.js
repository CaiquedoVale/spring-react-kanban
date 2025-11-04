import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

function ProtectedRoute() {
    // 1. Verifica se o token existe no localStorage
    const token = localStorage.getItem('jwtToken');

    // 2. Verifica se o token é válido
    // (Por enquanto, só checamos se ele "existe". Uma versão mais avançada
    // poderia "decodificar" o token para ver se ele "expirou" de verdade)
    const isAuth = token ? true : false;

    // 3. O "Outlet" é o "filho" (a página real, ex: Dashboard)
    // O "Navigate" é o componente de redirecionamento
    return isAuth ? <Outlet /> : <Navigate to="/login" replace />;
    // Se isAuth for 'true', renderiza o <Outlet> (a página protegida)
    // Se isAuth for 'false', redireciona o usuário para /login
}

export default ProtectedRoute;