package com.exemplo.selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class LoginTest {

    @Test
    public void testLoginGestaoCarteiraContratos() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://staging.k2.amarassist.com.br/");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // ============================
            // LOGIN
            // ============================
            WebElement emailField = wait.until(
                ExpectedConditions.elementToBeClickable(By.name("email"))
            );
            emailField.sendKeys("jefferson.oliveira@amarassist.com.br");

            WebElement passwordField = driver.findElement(By.name("password"));
            passwordField.sendKeys("kiwi123@");

            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
            loginButton.click();

            wait.until(ExpectedConditions.urlContains("home"));


            // ============================
            // MENU → GESTÃO DE CARTEIRA
            // ============================
            WebElement gestaoCarteiraLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@class='nav-link-title' and text()='Gestão de Carteira']")
                )
            );

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", gestaoCarteiraLink);
            gestaoCarteiraLink.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("wallet_management")));


            // ============================
            // MENU → CONTRATOS
            // ============================
            WebElement contratosLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[@class='nav-link-title' and text()='Contratos']")
                )
            );

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", contratosLink);
            contratosLink.click();

            wait.until(ExpectedConditions.urlContains("contract"));


          
            
            boolean encontrou = false;
            List<WebElement> contratos = null;

            long start = System.currentTimeMillis();
            long timeoutMillis = 30000;

            while (System.currentTimeMillis() - start < timeoutMillis) {

                // 1) `/contract` ou `/contract/123`
                contratos = driver.findElements(By.xpath("//a[contains(@href, '/contract')]"));
                if (!contratos.isEmpty()) {
                    encontrou = true;
                    break;
                }

                // 2) elementos com classe que contenha "contract"
                List<WebElement> classMatches = driver.findElements(By.xpath("//*[contains(@class,'contract')]//a"));
                if (!classMatches.isEmpty()) {
                    contratos = classMatches;
                    encontrou = true;
                    break;
                }

                Thread.sleep(500);
            }

            if (!encontrou || contratos == null || contratos.isEmpty()) {
                salvarScreenshot(driver, "no-contract-found.png");

                System.out.println("DEBUG: Nenhum contrato encontrado.");
                System.out.println("DEBUG: URL atual = " + driver.getCurrentUrl());

                throw new AssertionError("Não existem contratos para clicar e validar.");
            }

            WebElement primeiroContrato = contratos.get(0);

            // pega href para extrair ID
            String href = primeiroContrato.getAttribute("href");
            System.out.println("DEBUG: href do primeiro contrato = " + href);

            String contractId = null;
            java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("/contract/(\\d+)")
                .matcher(href);

            if (m.find()) {
                contractId = m.group(1);
                System.out.println("DEBUG: contractId extraído = " + contractId);
            }

            // rola e clica
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", primeiroContrato);
            wait.until(ExpectedConditions.elementToBeClickable(primeiroContrato));
            primeiroContrato.click();


            // ============================
            // VALIDAÇÃO DA URL COM ID
            // ============================
            try {
                if (contractId != null) {
                    String expectedRegex = ".*contract.*/" + contractId + ".*";
                    wait.until(ExpectedConditions.urlMatches(expectedRegex));
                } else {
                    wait.until(ExpectedConditions.urlContains("contract"));
                }
            } catch (TimeoutException e) {
                salvarScreenshot(driver, "timeout-after-click.png");

                System.out.println("TIMEOUT aguardando URL com contractId. URL atual: " + driver.getCurrentUrl());
                throw e;
            }

            // ASSERT FINAL
            String urlAtual = driver.getCurrentUrl();
            System.out.println("URL final = " + urlAtual);

            if (contractId != null) {
                assertTrue(
                    "A URL deveria conter o contractId clicado (" + contractId + ")",
                    urlAtual.contains("/" + contractId)
                );
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }
    }


    // ============================
    // FUNÇÃO PARA SCREENSHOT
    // ============================
    private void salvarScreenshot(WebDriver driver, String nome) {
        try {
            Files.createDirectories(Paths.get("target/screenshots"));
            File scr = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(scr.toPath(),
                Paths.get("target/screenshots/" + nome),
                StandardCopyOption.REPLACE_EXISTING
            );
            System.out.println("Screenshot salvo em target/screenshots/" + nome);
        } catch (Exception ex) {
            System.err.println("Erro ao salvar screenshot: " + ex.getMessage());
        }
    }
}
