package com.kanban.backend.config;

// Imports para o CORS (Relações Internacionais)
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays; // Para criar a lista de 'allowedOrigins'

// Imports Padrão do Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Para especificar métodos (POST, GET, etc.)

// Imports do Spring Security
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // Para definir a política "Stateless"
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // O "Criptógrafo"
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter; // O "Guarda" padrão do Spring

// Imports do NOSSO projeto
import com.kanban.backend.security.SecurityFilter; // O NOSSO "Guarda-Costas"

// Import estático para o .cors(withDefaults())
import static org.springframework.security.config.Customizer.withDefaults;


/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: SecurityConfig.java
 * ANALOGIA: O "Livro de Regras" do Quartel-General
 * -------------------------------------------------------------------------------------
 * * Este é o arquivo mais importante para a segurança.
 * * @Configuration: Diz ao Spring que esta é uma classe de configuração (ela "ensina" o Spring a se comportar).
 * @EnableWebSecurity: "Liga" o módulo de segurança web do Spring (ativa os "Guarda-Costas").
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. "CONTRATANDO" NOSSO GUARDA-COSTAS (SecurityFilter)
    // Estamos pedindo ao Spring: "Ei, me dê (Injete) uma instância daquela classe
    // SecurityFilter que nós criamos no pacote 'security'. Vamos precisar dela."
    @Autowired
    private SecurityFilter securityFilter;

    
    /**
     * -------------------------------------------------------------------------------------
     * BEAN: securityFilterChain (O "Livro de Regras" Principal)
     * -------------------------------------------------------------------------------------
     * * @Bean: Transforma este método em um "componente" gerenciado pelo Spring.
     * * Este método define a CADEIA DE FILTROS de segurança. É onde dizemos
     * explicitamente o que fazer com cada requisição que chega na nossa API.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        // Inicia a configuração das regras...
        http
            // ---------------------------------------------------------------------------------
            // REGRA 1: DESABILITAR O CSRF
            // (Cross-Site Request Forgery)
            // 
            // O CSRF é um tipo de ataque que "engana" o navegador de um usuário logado
            // para enviar requisições indesejadas. Ele depende de "Cookies de Sessão".
            // Como nossa API é 'STATELESS' (não usa sessões), estamos imunes a este
            // ataque. Desabilitá-lo é seguro e necessário.
            .csrf(csrf -> csrf.disable())

            // ---------------------------------------------------------------------------------
            // REGRA 2: POLÍTICA DE SESSÃO "STATELESS" (A REGRA DE OURO DO JWT)
            // 
            // .sessionCreationPolicy(SessionCreationPolicy.STATELESS):
            // Isso diz ao Spring: "NÃO crie 'sessões' (fichas) no servidor para os usuários."
            // O servidor não vai guardar "quem está logado". A única prova de
            // autenticação será o "Passaporte" (Token JWT) que o usuário enviará.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // ---------------------------------------------------------------------------------
            // REGRA 3: CONFIGURAR O CORS (O "DEPARTAMENTO DE RELAÇÕES INTERNACIONAIS")
            // 
            // .cors(withDefaults()):
            // Esta linha é o "gancho" que diz ao Spring Security:
            // "Procure por um Bean chamado 'corsConfigurationSource' (que definimos lá embaixo)
            // e use as regras dele para decidir quais 'países' (origens, ex: localhost:3000)
            // podem 'ligar' (fazer requisições) para a nossa API."
            .cors(withDefaults())
            
            // ---------------------------------------------------------------------------------
            // REGRA 4: DESABILITAR OS LOGINS PADRÃO DO SPRING
            // 
            // O Spring vem com uma página de login (/login) e um pop-up (HttpBasic)
            // que funcionam com SESSÕES. Como estamos usando 'STATELESS', eles não
            // servem para nós e só atrapalham.
            .formLogin(formLogin -> formLogin.disable()) // Desabilita a página de login padrão
            .httpBasic(httpBasic -> httpBasic.disable()) // Desabilita o pop-up de login padrão
            
            // ---------------------------------------------------------------------------------
            // REGRA 5: AUTORIZAR REQUISIÇÕES (O "PORTEIRO")
            // 
            // Esta é a parte principal. Definimos "quem pode ir aonde".
            // As regras são lidas de CIMA PARA BAIXO.
            .authorizeHttpRequests(auth -> auth
                
                // REGRA 5a: A "Porta de Registro" (POST /api/usuarios)
                // "Qualquer um ('permitAll') pode fazer um POST para /api/usuarios."
                .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                
                // REGRA 5b: A "Porta de Login" (POST /api/login)
                // "Qualquer um ('permitAll') pode fazer um POST para /api/login."
                .requestMatchers(HttpMethod.POST, "/api/login").permitAll()

                // REGRA 5c: "O RESTO" (Qualquer outra requisição)
                // "Para QUALQUER OUTRA REQUISIÇÃO ('anyRequest'), o usuário deve
                // ter a 'autoridade' (o 'crachá') de 'ROLE_USER'."
                // (Isso se conecta com o 'getAuthorities()' que definimos na entidade Usuario.java)
                .anyRequest().hasAuthority("ROLE_USER")
            )
            
            // ---------------------------------------------------------------------------------
            // REGRA 6: ADICIONAR NOSSO "GUARDA-COSTAS" (O FILTRO JWT)
            // 
            // Esta é a mágica final.
            // .addFilterBefore(securityFilter, AuthorizationFilter.class):
            // Diz ao Spring: "Pegue o 'securityFilter' (que nós 'contratamos' lá em cima)
            // e coloque-o na fila de segurança ANTES do 'Guarda' padrão do Spring
            // ('AuthorizationFilter')".
            // 
            // O que acontece:
            // 1. A requisição (ex: GET /api/quadros) chega.
            // 2. O NOSSO 'securityFilter' roda primeiro.
            // 3. Ele lê o token, valida, encontra o usuário e o "autentica".
            // 4. A requisição continua para o 'AuthorizationFilter' (o guarda do Spring).
            // 5. O guarda do Spring vê que o usuário JÁ ESTÁ AUTENTICADO e tem o "crachá"
            //    'ROLE_USER', então ele libera a passagem.
            .addFilterBefore(securityFilter, AuthorizationFilter.class); 
            
        // Constrói e "publica" o livro de regras
        return http.build();
    }

    
    /**
     * -------------------------------------------------------------------------------------
     * BEAN: passwordEncoder (O "Criptógrafo")
     * -------------------------------------------------------------------------------------
     * * @Bean: Estamos "contratando" um Criptógrafo (BCrypt) e o disponibilizando
     * para toda a aplicação.
     * * Qualquer parte do código (como o 'AuthController' ou o 'AuthenticationManager')
     * que pedir um 'PasswordEncoder' vai receber esta *mesma* instância.
     * Isso garante que a senha seja criptografada e comparada usando o mesmo algoritmo.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){ 
        return new BCryptPasswordEncoder();
    }

    
    /**
     * -------------------------------------------------------------------------------------
     * BEAN: authenticationManager (O "Chefe de Segurança")
     * -------------------------------------------------------------------------------------
     * * @Bean: Estamos "contratando" o Gerente de Autenticação.
     * * Este é o "cérebro" do login. É o componente que o Spring usa para
     * orquestrar o processo de login. É ele quem vai:
     * 1. Chamar o 'UserAuthService' (o "Verificador de Identidade") para buscar o usuário.
     * 2. Chamar o 'PasswordEncoder' (o "Criptógrafo") para comparar as senhas.
     * * Nós o expomos como um Bean para que nosso 'AuthController' possa usá-lo.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    
    /**
     * -------------------------------------------------------------------------------------
     * BEAN: corsConfigurationSource (O "Departamento de Relações Internacionais" - CORS)
     * -------------------------------------------------------------------------------------
     * * @Bean: Estamos "contratando" o serviço de CORS.
     * * Este Bean é "pescado" pela linha '.cors(withDefaults())' lá em cima.
     * Ele define as regras de "Cross-Origin Resource Sharing" (CORS),
     * que é a política de segurança do navegador que impede um "país"
     * (http://localhost:3000) de fazer "ligações" (APIs) para outro "país"
     * (http://localhost:8080), a menos que o segundo país (nós) permita.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // "De qual 'país' (origem) vamos aceitar ligações?"
        // Apenas do nosso app React em "localhost:3000".
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        
        // "Quais 'assuntos' (métodos) são permitidos?"
        // Todos os principais. O "OPTIONS" é crucial para o "teste de sondagem" (pre-flight).
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // "Quais 'documentos' (cabeçalhos) o 'país' pode nos enviar?"
        // "*" (asterisco) significa "aceitamos todos", incluindo o 'Authorization' (nosso token).
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // "Podemos aceitar 'documentos sensíveis' (credenciais)?"
        // Sim. Isso permite que o 'Authorization' (nosso token) seja enviado.
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // "Para quais 'departamentos' (rotas) estas regras se aplicam?"
        // Para todos que começam com "/api/**"
        source.registerCorsConfiguration("/api/**", configuration);
        
        return source;
    }
}