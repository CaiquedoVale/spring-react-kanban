import React from 'react';
import ReactDOM from 'react-dom/client'; // O "Encanador" que conecta o React ao Navegador
import './index.css'; // Estilos globais (para a aplicação inteira)
import App from './App'; // O "Cérebro" do nosso app (o 1º componente)
import reportWebVitals from './reportWebVitals'; // Para medir performance (opcional)

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: index.js
 * ANALOGIA: A "Chave de Ignição" do Front-End (React)
 * -------------------------------------------------------------------------------------
 * * Este é o ponto de entrada de TODA a sua aplicação React.
 * * O navegador não lê React (JSX). Ele lê HTML. Este arquivo é o "tradutor"
 * ou "ponte" que "injeta" seu código React dentro do HTML.
 */


// PASSO 1: ENCONTRAR O "PONTO DE ANCORAGEM" NO HTML
//
// Onde este 'root' está?
// Ele está no arquivo `public/index.html`. É a única `<div>` no
// `<body>` daquele arquivo: <div id="root"></div>
//
// ANALOGIA: O 'public/index.html' é a "casca vazia" do navio.
// O 'document.getElementById('root')' é o "suporte do motor" dentro dele.
//
// ReactDOM.createRoot(...) diz ao React: "Prepare-se para assumir o
// controle TOTAL de tudo que está dentro do elemento HTML com o ID 'root'".
const root = ReactDOM.createRoot(document.getElementById('root'));


// PASSO 2: "GIRAR A CHAVE" (Renderizar a Aplicação)
//
// root.render(...) é o comando que diz: "AGORA! Pegue o componente
// <App /> (e todos os seus 'filhos') e 'desenhe' (renderize)
// eles dentro do 'ponto de ancoragem' (o 'root')".
root.render(
  
  /**
   * <React.StrictMode>
   * ANALOGIA: O "Instrutor de Voo"
   *
   * Este é um "ajudante" que o React usa APENAS em modo de
   * desenvolvimento (ele desaparece no build final).
   * Ele "força" o React a fazer certas coisas duas vezes (como rodar
   * o 'useEffect') para "testar" seu código e garantir que você
   * não está usando padrões antigos ou inseguros (como "side effects"
   * inesperados).
   * Se você vir logs duplicados no console, o <StrictMode> é o culpado
   * (e isso é bom, ele está te ajudando a encontrar bugs).
   */
  <React.StrictMode>
    
    {/*
     * <App />
     * Este é o "ponto de partida" do SEU código.
     * A 'index.js' "passa o bastão" para a 'App.js'.
     * A 'App.js', por sua vez, vai carregar o Roteador (BrowserRouter),
     * que vai carregar os componentes de Login, Registro, Dashboard, etc.
     */}
    <App />
  
  </React.StrictMode>
);

// Função para medir a performance da página (Carregamento, etc.)
// É útil para otimização, mas não é crucial para a funcionalidade.
reportWebVitals();