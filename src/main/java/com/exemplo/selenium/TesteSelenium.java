package com.exemplo.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TesteSelenium {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.get("https://staging.k2.amarassist.com.br/");
            System.out.println("Título da página: " + driver.getTitle());
        } finally {
            driver.quit();
        }
    }
}
