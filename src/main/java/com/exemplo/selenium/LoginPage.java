package com.exemplo.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;


    private By campoUsuario = By.id("username");
    private By campoSenha = By.id("password");
    private By botaoEntrar = By.id("loginButton");
    private By mensagemErro = By.id("errorMessage");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void abrirPagina() {
        driver.get("https://www.google.com"); 
    }

    public void preencherUsuario(String usuario) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(campoUsuario)).sendKeys(usuario);
    }

    public void preencherSenha(String senha) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(campoSenha)).sendKeys(senha);
    }

    public void clicarEntrar() {
        wait.until(ExpectedConditions.elementToBeClickable(botaoEntrar)).click();
    }

    public String obterMensagemErro() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(mensagemErro)).getText();
    }

    public boolean verificarRedirecionamento(String urlEsperada) {
        return wait.until(ExpectedConditions.urlContains(urlEsperada));
    }
}
