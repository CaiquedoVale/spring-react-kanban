import axios from 'axios';

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: axiosConfig.js
 * ANALOGIA: O "Mensageiro Inteligente" (ou "Inspetor de Requisições")
 * -------------------------------------------------------------------------------------
 * * O que é?
 * Este arquivo não é um componente React. É um "módulo de serviço".
 * Nós o usamos para criar uma *instância personalizada* do 'axios'
 * (nosso "carteiro" de requisições HTTP).
 *
 * * Por quê?
 * Para que não tenhamos que repetir a URL do back-end (http://localhost:8080)
 * e, o mais importante, para que não tenhamos que "anexar" o Token JWT
 * manualmente em toda chamada de API. Este arquivo faz isso
 * automaticamente.
 */


// PASSO 1: Criar a "Instância Personalizada"
//
// Em vez de usar o 'axios' padrão, criamos o nosso 'apiClient'.
// É como "contratar" um carteiro que SÓ trabalha para nós.
const apiClient = axios.create({
    
    // 'baseURL': O "Endereço Padrão" do nosso back-end.
    // Agora, em vez de 'axios.post("http://localhost:8080/api/login")',
    // nós vamos apenas 'apiClient.post("/api/login")'.
    // Se a URL da API mudar, só mudamos aqui!
    baseURL: 'http://localhost:8080'
});


// PASSO 2: O "Interceptor" (O "Inspetor")
//
// .interceptors.request.use(...):
// Aqui, nós "ensinamos" o nosso 'apiClient' (o "carteiro").
// "Antes de você 'enviar' (.request) qualquer 'carta' (requisição),
// você DEVE 'usar' (.use) esta função de 'inspeção'."
apiClient.interceptors.request.use(
    
    // Esta função é o "Checkpoint de Inspeção".
    // Ela recebe a 'config' (a "carta") que está prestes a ser enviada.
    (config) => {
        
        // "Inspetor, olhe no 'cofre' (localStorage)..."
        const token = localStorage.getItem('jwtToken');
        
        // "...e veja se o 'Passaporte' (token) está lá."
        if (token) {
            
            // "Se estiver, GRAMPEIE (anexe) este cabeçalho (Header) na 'carta'."
            // Esta é a linha que adiciona:
            // "Authorization: Bearer eyJhbGci..."
            // em TODAS as requisições feitas com 'apiClient'
            // (exceto login/registro, pois o token ainda não existe).
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        
        // "Pronto. A 'carta' está inspecionada. Pode enviar."
        return config;
    },
    
    // Esta função (opcional) é o que fazer se o "Inspetor"
    // falhar ANTES de enviar (ex: um erro de configuração).
    (error) => {
        return Promise.reject(error);
    }
);

// PASSO 3: Exportar o "Mensageiro"
// Nós exportamos o 'apiClient' (nosso "carteiro" treinado) para que
// os outros componentes (Login, Registro, Dashboard) possam
// "importá-lo" e usá-lo.
export default apiClient;