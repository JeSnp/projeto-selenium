package com.exemplo.selenium.api;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;



public class LoginApiTest extends BaseApiTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("cenariosDeLogin")
    void deveTestarCenariosDeLogin(
            String descricao,
            String username,
            String password,
            int statusEsperado,
            String mensagemEsperada
    ) {

        String body = String.format(
            "{ \"username\": \"%s\", \"password\": \"%s\" }",
            username,
            password
        );

        io.restassured.response.ValidatableResponse response =
            request()
                .body(body)
            .when()
                .post("/login")
            .then()
                .statusCode(statusEsperado);

        if (statusEsperado == 200) {
            response
                .body("token", notNullValue())
                .body("perfil", anyOf(equalTo("ADMIN"), equalTo("USER")));
        } else {
            response
                .body("mensagem", containsString(mensagemEsperada));
        }
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> cenariosDeLogin() {
        return Stream.of(
            arguments("Login válido", "user", "123456", 200, null),
            arguments("Credenciais inválidas", "user", "senha_errada", 401, "Usuário ou senha incorretos"),
            arguments("Usuário sem permissão", "user_sem_permissao", "123456", 403, "sem permissão"),
            arguments("Usuário bloqueado", "user_bloqueado", "123456", 423, "bloqueado")
        );
    }
}
