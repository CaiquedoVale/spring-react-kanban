import axios from 'axios';

// 1. Cria uma "instância" do axios
// Nós vamos usar esta instância em vez do axios "global"
const apiClient = axios.create({
    baseURL: 'http://localhost:8080' // A URL base da nossa API
});

// 2. O Interceptor (O "Inspetor")
apiClient.interceptors.request.use(
    (config) => {
        // 3. Pega o token do localStorage
        const token = localStorage.getItem('jwtToken');
        
        // 4. Se o token existir, anexa ele no cabeçalho
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        
        return config; // Deixa a requisição continuar
    },
    (error) => {
        // Em caso de erro na requisição
        return Promise.reject(error);
    }
);

export default apiClient;