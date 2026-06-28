# Sistema de Logística Urbana e Otimização de Rotas

Este repositório contém uma solução robusta e profissional para resolver problemas complexos de logística territorial e roteamento de fretes urbanos. O projeto foi desenvolvido simulando cenários reais de engenharia de software de alta performance.

---

## Stack Tecnológica e Recursos Utilizados

- **Java 25**: Utilização de recursos modernos da linguagem para escrita de código limpo e performático.
    - **Records**: Para modelagem de dados de domínio imutáveis e sem código boilerplate.
    - **Stream API & Programação Funcional**: Para manipulação, filtragem e mapeamento de dados com alta legibilidade.
    - **Pattern Matching**: Estruturas de controle modernas para o processador de arquivos.
- **Maven**: Gerenciamento de build e ciclo de vida da aplicação.
- **JUnit 5 (v5.11.4)**: Cobertura completa com testes unitários e de integração automatizados.
- **Git & GitHub**: Versionamento utilizando boas práticas e histórico semântico de commits.

---

## Desafios Resolvidos

### Parte 1: Identificação Territorial por CEP
- **Problema**: Ler um catálogo de dados contendo cidades e suas respectivas faixas de cobertura de CEP, identificando a qual localidade um CEP alvo pertence.
- **Solução Técnica**: Implementação de uma máquina de estados simples para leitura de arquivos textuais de seção única e desenvolvimento de algoritmos de busca inclusiva para limites numéricos de CEPs, tratando cenários reais de **faixas de CEP sobrepostas** (onde um CEP pode pertencer a múltiplos territórios).

### Parte 2: Otimização de Roteamento de Frete (Menor Custo)
- **Problema**: Calcular a rota mais barata de transporte entre dois CEPs que pertencem a cidades não adjacentes (sem conexão direta).
- **Solução Técnica**:
    - Modelagem de dados estruturada em Grafos (Vértices e Arestas Valoradas).
    - Implementação do **Algoritmo de Dijkstra** utilizando filas de prioridade (`PriorityQueue`) para garantir performance de busca em redes complexas.
    - **Matriz de Decisão Avançada**: O sistema avalia todas as combinações possíveis de cidades de origem e destino geradas por CEPs sobrepostos, escolhendo o caminho que resulta no menor custo global de transporte.

---

## Estrutura Arquitetural do Projeto

O projeto adota o padrão internacional de diretórios do Maven, isolando o código de produção dos ambientes de testes automatizados, além de separar as regras de negócio em pacotes modulares:

```plaintext
desafio-logistica-cep/
├── pom.xml                  # Configurações do Maven e dependências (Java 25 + JUnit 5)
├── README.md                # Documentação técnica do projeto
└── src/
    ├── main/java/com/logistica/
    │   ├── parte1/          # Escopo do primeiro desafio territorial
    │   │   └── model/Cidade.java
    │   └── parte2/          # Escopo do segundo desafio de roteamento com grafos
    │       ├── model/Aresta.java
    │       ├── model/RotaResultado.java
    │       └── service/CalculadorRotaService.java
    │       └── service/LogisticaArquivoParser.java
    └── test/java/com/logistica/
        ├── parte1/CidadeTest.java
        └── parte2/LogisticaArquivoParserTest.java # Massa de testes com cenários reais
```

---

## Como Executar os Testes Automatizados

Para certificar a resiliência do sistema e validar as rotas logísticas calculadas, você pode rodar a suite de testes através do Maven. Certifique-se de ter o JDK 25 instalado em sua máquina.

Execute o comando abaixo no terminal do projeto:
```bash
mvn test
```

Os testes utilizam injeção de diretórios temporários (`@TempDir`) do JUnit 5, garantindo que arquivos de texto sejam criados e destruídos na memória do computador de forma isolada durante a execução, sem gerar resíduos no disco rígido do sistema.
