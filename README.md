# Cliente e Endereço API

Este é um projeto Spring Boot que gerencia clientes e seus endereços, com integração à API **ViaCEP** para consulta de dados de endereço a partir do CEP. A aplicação implementa operações CRUD para clientes e suas informações de endereço.

## Funcionalidades

- **Adicionar Cliente**: Insere um novo cliente com seus dados e endereço. A busca do endereço é feita a partir do CEP utilizando a API do ViaCEP.
- **Atualizar Cliente**: Permite a atualização dos dados de um cliente, incluindo a alteração de seu endereço.
- **Listar Clientes**: Retorna todos os clientes cadastrados.
- **Consultar Cliente por ID**: Retorna um cliente específico com base no seu ID.
- **Excluir Cliente**: Exclui um cliente pelo seu ID.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
  - Spring Web
  - Spring Data JPA
  - Spring Validation
- **H2 Database** (ou outro banco de dados relacional à sua escolha)
- **JPA/Hibernate** (para persistência de dados)
- **RestTemplate** (para integração com o ViaCEP)
- **JUnit e Mockito** (para testes unitários)

## Pré-requisitos

- **Java 17** ou superior
- **Maven** 3.6+ ou **Gradle**
- **Docker** (opcional, se desejar executar em container)

## Como Executar a Aplicação

### Localmente

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/cliente-endereco-api.git
   cd cliente-endereco-api

2. Compile e execute a aplicação com Maven:   
   ```bash
   mvn clean install
   mvn spring-boot:run

Ou com Gradle:
  ```bash
   mvn clean install
   mvn spring-boot:run
