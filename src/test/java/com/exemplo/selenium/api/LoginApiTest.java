package com.exemplo.selenium.api;

import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

public class LoginApiTest extends BaseApiTest {

    @Test
    public void deveRetornar200ComTokenEPerfil() {
        String body = "{ \"username\": \"user\", \"password\": \"123456\" }";


        request()
            .body(body)
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .body("token", notNullValue())
            .body("perfil", anyOf(equalTo("ADMIN"), equalTo("USER")));
    }

    @Test
    public void deveRetornar401ComCredenciaisInvalidas() {
        String body = "{ \"username\": \"user\", \"password\": \"123456\" }";
;

        request()
            .body(body)
        .when()
            .post("/login")
        .then()
            .statusCode(401)
            .body("mensagem", containsString("Usuário ou senha incorretos"));
    }

    @Test
    public void deveRetornar403ParaAcessoNegado() {
        String body = "{ \"username\": \"user\", \"password\": \"123456\" }";

        request()
            .body(body)
        .when()
            .post("/login")
        .then()
            .statusCode(403)
            .body("mensagem", containsString("sem permissão"));
    }

    @Test
    public void deveRetornar423ParaUsuarioBloqueado() {
        String body = "{ \"username\": \"user\", \"password\": \"123456\" }";

        request()
            .body(body)
        .when()
            .post("/login")
        .then()
            .statusCode(423)
            .body("mensagem", containsString("bloqueado"));
    }
}
