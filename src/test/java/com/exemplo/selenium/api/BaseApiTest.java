package com.exemplo.selenium.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class BaseApiTest {

    static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        
        wireMockServer = new WireMockServer(8080);
        System.setProperty("file.encoding", "UTF-8");
        wireMockServer.start();

        
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.defaultParser = Parser.JSON;

        
        configureFor("localhost", 8080);

        

        
        stubFor(post(urlEqualTo("/login"))
            .withRequestBody(matchingJsonPath("$.username", equalTo("user")))
            .withRequestBody(matchingJsonPath("$.password", equalTo("123456")))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json; charset=UTF-8")
                .withStatus(200)
                .withBody("{\"token\":\"abc123\", \"perfil\":\"ADMIN\"}")));

        
        stubFor(post(urlEqualTo("/login"))
            .withRequestBody(matchingJsonPath("$.password", equalTo("senha_errada")))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json; charset=UTF-8")
                .withStatus(401)
                .withBody("{\"mensagem\":\"Usuário ou senha incorretos\"}")));

        
        stubFor(post(urlEqualTo("/login"))
            .withRequestBody(matchingJsonPath("$.username", equalTo("user_sem_permissao")))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json; charset=UTF-8")
                .withStatus(403)
                .withBody("{\"mensagem\":\"sem permissão\"}")));

        
        stubFor(post(urlEqualTo("/login"))
            .withRequestBody(matchingJsonPath("$.username", equalTo("user_bloqueado")))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json; charset=UTF-8")
                .withStatus(423)
                .withBody("{\"mensagem\":\"bloqueado\"}")));
    }

    @AfterAll
    public static void teardown() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }

    protected RequestSpecification request() {
        return RestAssured
            .given()
            .contentType("application/json");
    }
}
