package com.exemplo.selenium;

import org.openqa.selenium.WebDriver;

public class TesteLogin {

    public static void main(String[] args) {
        WebDriver driver = DriverFactory.getDriver();
        LoginPage login = new LoginPage(driver);

      
        login.abrirPagina();
        login.preencherUsuario("user");
        login.preencherSenha("123456");
        login.clicarEntrar();

        if (login.verificarRedirecionamento("/dashboard")) {
            System.out.println("Login USER bem-sucedido!");
        } else {
            System.out.println("Falha no login USER!");
        }

        login.abrirPagina();
        login.preencherUsuario("visitor");
        login.preencherSenha("123456");
        login.clicarEntrar();

        String msg = login.obterMensagemErro();
        if (msg.contains("sem permissão")) {
            System.out.println("VISITOR bloqueado conforme esperado!");
        } else {
            System.out.println("VISITOR não deveria acessar!");
        }

        for (int i = 1; i <= 3; i++) {
            login.abrirPagina();
            login.preencherUsuario("user");
            login.preencherSenha("senhaErrada");
            login.clicarEntrar();
            System.out.println("Tentativa inválida #" + i);
        }

        if (login.obterMensagemErro().contains("bloqueado")) {
            System.out.println("Usuário bloqueado após 3 tentativas inválidas!");
        } else {
            System.out.println("O bloqueio não foi aplicado!");
        }

        DriverFactory.fecharDriver();
    }
}

