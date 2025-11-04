import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import Registro from './components/Registro';
import Login from './components/Login';
import Dashboard from './components/Dashboard';
import ProtectedRoute from './components/ProtectedRoute'; // <-- Importado

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <header className="App-header">
          <h1>Bem-vindo ao Kanban</h1>
          
          <Routes>
            {/* Rotas Públicas */}
            <Route index element={<Login />} /> 
            <Route path="/login" element={<Login />} />
            <Route path="/registrar" element={<Registro />} />
            
            {/* --- Esta é a Mágica --- */}
            {/* 1. Criamos um "Elemento Pai" que é o nosso segurança */}
            <Route element={<ProtectedRoute />}>
              
              {/* 2. Todos os "filhos" aqui dentro estão protegidos */}
              <Route path="/dashboard" element={<Dashboard />} />
              {/* <Route path="/perfil" element={<Perfil />} />  (Exemplo futuro) */}
              
            </Route>
            {/* ------------------------- */}

          </Routes>
        </header>
      </div>
    </BrowserRouter>
  );
}

export default App;