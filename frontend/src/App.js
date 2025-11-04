import './App.css'; // Estilos CSS específicos para este componente

// --- Imports do Roteador ---
// As "ferramentas" que nos permitem criar um app de múltiplas páginas
import { BrowserRouter, Routes, Route } from 'react-router-dom';

// --- Imports dos Nossos Componentes ("Páginas") ---
import Registro from './components/Registro';
import Login from './components/Login';
import Dashboard from './components/Dashboard';

// --- Import do Nosso "Segurança da Boate" ---
import ProtectedRoute from './components/ProtectedRoute';

// --- Import do Núcleo do Kanban (Fase 3) ---
import PaginaQuadro from './components/PaginaQuadro'; 


/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: App.js
 * ANALOGIA: O "Controlador de Tráfego" (ou "Mapa Principal")
 * -------------------------------------------------------------------------------------
 * * Este é o componente "pai" de toda a nossa aplicação.
 * * Ele define o "Mapa" do site e aplica a segurança de forma declarativa.
 */
function App() {
  return (
    /**
     * <BrowserRouter>
     * O "cérebro" do Roteador. Ele lê a URL do navegador (ex: /login) e informa aos filhos.
     */
    <BrowserRouter>
      <div className="App">
        <header className="App-header">
          <h1>Bem-vindo ao Kanban</h1>
          
          {/*
           * <Routes>
           * O "Livro de Regras" do Roteador. Renderiza a *primeira* <Route> que der "match".
           */}
          <Routes>
            
            {/* -------------------------------------------------- */}
            {/* GRUPO 1: Rotas Públicas (Fase 2) */}
            {/* -------------------------------------------------- */}
            <Route index element={<Login />} />      {/* Rota Padrão (/) */}
            <Route path="/login" element={<Login />} />
            <Route path="/registrar" element={<Registro />} />
            
            
            {/* -------------------------------------------------- */}
            {/* GRUPO 2: Rotas Protegidas (Fases 2 e 3) */}
            {/* -------------------------------------------------- */}
            
            {/* 1. O "Invólucro de Proteção" */}
            <Route element={<ProtectedRoute />}>
              
              {/* 2. Rota do Dashboard (Lista de Quadros) */}
              <Route path="/dashboard" element={<Dashboard />} />
              
              {/* 3. Rota de Detalhe do Quadro (Fase 3) */}
              {/* Esta rota usa um PARÂMETRO DINÂMICO (:id) */}
              {/* Ex: Se a URL for /quadro/5, o componente PaginaQuadro vai ler o ID "5" */}
              <Route path="/quadro/:id" element={<PaginaQuadro />} /> 
              
            </Route>
            {/* -------------------------------------------------- */}

          </Routes>
        </header>
      </div>
    </BrowserRouter>
  );
}

export default App;