# A3-Unisul-Gestao-de-Qualidade

[![Integração contínua com Maven](https://github.com/amandaespindola/A3-Unisul-Gestao-de-Qualidade/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/amandaespindola/A3-Unisul-Gestao-de-Qualidade/actions/workflows/maven.yml) 
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=amandaespindola_A3-Unisul-Gestao-de-Qualidade&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=amandaespindola_A3-Unisul-Gestao-de-Qualidade)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=amandaespindola_A3-Unisul-Gestao-de-Qualidade&metric=coverage)](https://sonarcloud.io/component_measures?id=amandaespindola_A3-Unisul-Gestao-de-Qualidade&metric=coverage)

## SisUni

Sistema desenvolvido para gerenciar o cadastro de professores e alunos em instituições de ensino.

## Sobre o Projeto

Este projeto foi desenvolvido como parte da disciplina de Gestão de Qualidade de Software e consiste na análise de código legado, refatoração aplicando boas práticas de desenvolvimento e implementação de testes para garantir a qualidade e manutenibilidade do sistema. O projeto foi desenvolvido sobre o SisUni, sistema desenvolvido como trabalho final da UC de Programação e Soluções Computacionais.

## Integrantes do Grupo

| RA | Nome Completo | GitHub |
|---|---|---|
| 10722118915 | Amanda Espindola Aguiar da Silva | [@amandaespindola](https://github.com/amandaespindola) |
| 10722123569 | Laura Maria Firta Foes | [@laurafoes](https://github.com/laurafoes) |
| 10724114182 | Matheus Bernardo de Souza | [@code-matheu](https://github.com/code-matheu) |
| 10724114382 | José Gustavo de Almeida Alves | [@Gustavoaals](https://github.com/Gustavoaals) |

## Tecnologias Utilizadas

### ⚙️ Core
- **Java 17** - Linguagem de programação
- **Maven 3.8+** - Gerenciamento de dependências e build

### 🎨 Interface e UI
- **Java Swing** - Framework para interface gráfica
- **FlatLaf 3.6.1** - Look and Feel moderno para Swing
- **JCalendar 1.4** - Componente de seleção de data

### 💾 Persistência
- **MySQL Connector 8.0.33** - Driver JDBC para conexão com MySQL

### 📊 Exportação de Dados
- **Apache POI 5.4.1** - Geração de arquivos Excel (.xlsx)

### 🧪 Testes
- **JUnit Jupiter 6.0.1** - Framework de testes unitários
- **SQLite JDBC 3.36.0.3** - Banco de dados em memória para testes
- **JaCoCo 0.8.8** - Cobertura de código

### 🔍 Qualidade e CI/CD
- **SonarCloud** - Análise estática de código
- **GitHub Actions** - Integração e entrega contínua
- **Xvfb** - Display virtual para execução de testes de interface

## Requisitos do Projeto

### Requisitos Funcionais

**RF01. Cadastro de Alunos**  
O sistema deve permitir registrar usuários do tipo Aluno com nome, idade, curso e fase.

**RF02. Cadastro de Professores**  
O sistema deve permitir registrar usuários do tipo Professor com nome, idade, campus, CPF, contato, título (graduação) e salário.

**RF03. Edição de Alunos**  
O sistema deve permitir a edição de todos os campos cadastrados de um aluno, exceto o identificador único (ID).

**RF04. Edição de Professores**  
O sistema deve permitir a edição de todos os campos cadastrados de um professor, exceto o identificador único (ID).

**RF05. Exclusão de Alunos**  
O sistema deve permitir a exclusão de alunos cadastrados, solicitando confirmação antes de executar a operação.

**RF06. Exclusão de Professores**  
O sistema deve permitir a exclusão de professores cadastrados, solicitando confirmação antes de executar a operação.

**RF07. Listagem de Alunos**  
O sistema deve exibir uma lista com todos os alunos cadastrados, apresentando as principais informações em formato de tabela.

**RF08. Listagem de Professores**  
O sistema deve exibir uma lista com todos os professores cadastrados, apresentando as principais informações em formato de tabela.

**RF09. Alternância entre Listagens**  
O sistema deve permitir que o usuário alterne entre a visualização da lista de alunos e a lista de professores através de interface clara (abas, botões ou menu).

**RF10. Exportação de Relatório de Dados de Alunos**  
O sistema deve permitir a exportação dos dados de todos os alunos cadastrados em formato Excel (.xlsx), contendo todos os campos cadastrados dos alunos.

**RF11. Exportação de Relatório de Dados de Professores**  
O sistema deve permitir a exportação dos dados de todos os professores cadastrados em formato Excel (.xlsx), contendo todos os campos cadastrados dos professores.

**RF12. Validação de Dados**  
O sistema deve validar, no momento do cadastro e edição de um usuário, o preenchimento correto dos campos digitáveis. Além disso, deve verificar se o CPF inserido já consta cadastrado na base.

### Requisitos Não Funcionais

**RNF01. Usabilidade**  
A interface Swing deve ser intuitiva e seguir padrões de design consistentes, permitindo que usuários sem treinamento técnico consigam utilizar o sistema com facilidade.

**RNF02. Performance de Interface**  
As operações de navegação entre telas e atualização de dados devem ocorrer em menos de 1 segundo para garantir uma experiência fluida ao usuário.

**RNF03. Performance de Exportação**  
A exportação de dados para Excel deve ser concluída em até 5 segundos para bases de até 10.000 registros.

**RNF04. Cobertura de Testes**  
O sistema deve manter uma cobertura de testes unitários de no mínimo 75% do código, com foco nas regras de negócio e validações.

**RNF05. Documentação**  
Todo o código deve ser documentado utilizando JavaDoc, incluindo descrição de classes, métodos, parâmetros e valores de retorno.

**RNF06. Gerenciamento de Dependências**  
O projeto deve utilizar Maven para gerenciamento de dependências, com todas as bibliotecas versionadas no arquivo pom.xml.

**RNF07. Persistência de Dados**  
O sistema deve garantir a integridade dos dados persistidos, evitando perda de informações em caso de falhas ou fechamento inesperado da aplicação.

## Como Executar

### Pré-requisitos
- JDK 17 ou superior
- Maven 3.8+
- MySQL Server

### Configuração do Banco de Dados

Antes de executar o sistema, é necessário criar o banco de dados MySQL. O projeto disponibiliza um script SQL para facilitar esse processo:
```bash
# Conecte-se ao MySQL como root ou usuário com privilégios
mysql -u root -p

# Execute o script de criação do banco
source db_alunos.sql

# Ou, alternativamente, execute o comando abaixo no terminal
mysql -u root -p < db_alunos.sql
```

### Compilação e Execução
```bash
# Clonar o repositório
git clone https://github.com/amandaespindola/A3-Unisul-Gestao-de-Qualidade.git

# Navegar até o diretório do projeto
cd A3-Unisul-Gestao-de-Qualidade

# Compilar o projeto
mvn clean compile

# Executar os testes
mvn test

# Gerar o pacote executável
mvn package

# Executar o JAR gerado
java -jar target/crudTest-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

## Pipeline CI/CD

O projeto utiliza GitHub Actions com três ambientes:

- **dev**: Execução de testes unitários
- **hmg**: Análise de qualidade com SonarCloud
- **prd**: Geração e publicação de artefatos

## Licença

Este projeto foi desenvolvido para fins acadêmicos na Universidade do Sul de Santa Catarina (UNISUL).
