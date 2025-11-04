package com.kanban.backend; // Este é o "pacote raiz" (root package)

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * -------------------------------------------------------------------------------------
 * ARQUIVO: BackendApplication.java
 * ANALOGIA: A "Chave de Ignição" do Carro
 * -------------------------------------------------------------------------------------
 * * Esta é a classe principal, o ponto de entrada de toda a sua aplicação Spring Boot.
 *
 * * @SpringBootApplication:
 * Esta é a anotação "mágica" 3-em-1 do Spring Boot. Ela diz ao Spring para:
 * 1.  **@Configuration:** Tratar esta classe como uma fonte de configuração (Beans).
 * 2.  **@EnableAutoConfiguration:** "Adivinhar" e configurar automaticamente
 * as dependências que você adicionou no 'pom.xml' (como o servidor web
 * Tomcat, o Spring Data JPA, etc.).
 * 3.  **@ComponentScan:** "Escanear" este pacote (`com.kanban.backend`) e
 * todos os seus sub-pacotes (como `config`, `controller`, `service`, etc.)
 * procurando por "componentes" (@Service, @RestController, @Repository)
 * para "contratar" (registrar como Beans).
 */
@SpringBootApplication
public class BackendApplication {

    /**
     * -------------------------------------------------------------------------------------
     * MÉTODO: main
     * -------------------------------------------------------------------------------------
     * Este é o método padrão do Java, o ponto de partida de qualquer aplicação Java.
     *
     * * SpringApplication.run(BackendApplication.class, args):
     * Esta é a linha que "gira a chave".
     * Ela "inicia" o Spring Boot, que por sua vez:
     * - Inicia o servidor web (Tomcat) embutido.
     * - Executa o @ComponentScan para encontrar todos os seus arquivos.
     * - Conecta-se ao banco de dados (MySQL).
     * - Deixa a aplicação pronta para receber requisições HTTP na porta 8080 (ou a porta
     * que você definir no 'application.properties').
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}