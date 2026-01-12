# Projeto de Automação de Testes – Java / Selenium / RestAssured

## Descrição
Projeto desenvolvido como prova técnica para a vaga de Analista de Automação de Testes.
Contém automação de testes de UI (Selenium) e API (RestAssured), utilizando boas práticas de automação e estrutura Maven.

---

## Tecnologias
- Java 11+
- Maven
- Selenium WebDriver
- RestAssured
- JUnit
- PostgreSQL

---

## Estrutura do Projeto
```
src
 ├── main
 │   └── java
 │       └── com.exemplo.selenium
 └── test
     └── java
         └── com.exemplo.selenium
             ├── ui
             └── api
```

---

## Pré-requisitos
- Java JDK 11 ou superior
- Maven configurado
- Google Chrome
- ChromeDriver compatível

---

## Configuração do Ambiente
As configurações ficam no arquivo:

```
src/test/resources/application.properties
```

Exemplo:
```
base.url=https://staging.k2
browser=chrome
timeout=5
```

---

## Como Executar os Testes

### Executar todos os testes
```bash
mvn clean test
```

### Executar apenas testes de UI
```bash
mvn test -Dtest=*UITest
```

### Executar apenas testes de API
```bash
mvn test -Dtest=*ApiTest
```

---

## Documentação
Os cenários de teste e análises SQL estão no arquivo:

```
cenarios-de-teste.md
```
