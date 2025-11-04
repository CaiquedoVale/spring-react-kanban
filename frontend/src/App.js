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


/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: App.js
 * ANALOGIA: O "Controlador de Tráfego" (ou "Mapa Principal")
 * -------------------------------------------------------------------------------------
 * * Este é o componente "pai" de toda a nossa aplicação.
 * * O 'index.js' chama este arquivo, e este arquivo decide
 * qual "página" (componente) deve ser mostrada.
 */
function App() {
  return (
    /**
     * <BrowserRouter>
     * O "cérebro" do Roteador. Ele deve "abraçar" (ser pai de)
     * toda a sua aplicação que depende de rotas. Ele "lê" a URL
     * do navegador (ex: /login) e informa aos seus filhos.
     */
    <BrowserRouter>
      <div className="App">
        <header className="App-header">
          <h1>Bem-vindo ao Kanban</h1>
          
          {/*
           * <Routes>
           * O "Livro de Regras" do Roteador.
           * Ele olha a URL atual e renderiza a *primeira* <Route>
           * que "bater" (match) com o 'path' (caminho).
           */}
          <Routes>
            
            {/* -------------------------------------------------- */}
            {/* GRUPO 1: Rotas Públicas */}
            {/* (O usuário NÃO precisa estar logado para vê-las) */}
            {/* -------------------------------------------------- */}

            {/* A rota "index" (padrão) */}
            {/* 'index' é um atalho para 'path="/"'. Se o usuário acessar */}
            {/* 'http://localhost:3000/', ele verá o componente de Login. */}
            <Route index element={<Login />} /> 
            
            {/* A rota de Login (explícita) */}
            {/* Se o usuário acessar '/login', ele também vê o Login. */}
            <Route path="/login" element={<Login />} />
            
            {/* A rota de Registro */}
            {/* Se o usuário acessar '/registrar', ele vê o Registro. */}
            <Route path="/registrar" element={<Registro />} />
            
            
            {/* -------------------------------------------------- */}
            {/* GRUPO 2: Rotas Protegidas */}
            {/* (O usuário SÓ pode ver se estiver logado) */}
            {/* -------------------------------------------------- */}

            {/*
             * A MÁGICA: A "Rota de Layout" (O "Segurança da Boate")
             * Esta <Route> não tem um 'path'. Ela apenas tem um 'element'.
             * Isso a transforma em uma "rota pai" ou "invólucro".
             *
             * Ela diz: "Para QUALQUER rota 'filha' definida aqui dentro,
             * você deve PRIMEIRO renderizar o meu 'element', que é o
             * <ProtectedRoute />."
             */}
            <Route element={<ProtectedRoute />}>
              
              {/*
               * A Rota "Filha" (Protegida)
               * Como esta rota está "aninhada" (é filha) da rota acima,
               * ela só será renderizada se o <ProtectedRoute /> permitir
               * (ou seja, se o usuário tiver o token).
               *
               * Se o <ProtectedRoute> permitir, ele vai renderizar o <Outlet />,
               * e o React vai "encaixar" o <Dashboard /> nesse <Outlet>.
               */}
              <Route path="/dashboard" element={<Dashboard />} />
              
              {/*
               * EXEMPLO FUTURO:
               * Se quiséssemos adicionar uma página de "Perfil" (que também
               * precisa de login), nós simplesmente a adicionaríamos aqui:
               * <Route path="/perfil" element={<Perfil />} />
               * Ela automaticamente ganharia a "proteção" do pai.
               */}
              
            </Route> {/* Fim do "invólucro" de proteção */}

          </Routes>
        </header>
      </div>
    </BrowserRouter>
  );
}

export default App;