# Sistema de Gerenciamento de Biblioteca — Atividade Prática (ADS)

Este repositório contém a solução da atividade prática de Arquiteturas de Software com Java.

Como compilar e executar:

1. Instale Java 17+ e Maven.
2. No diretório do projeto `biblioteca`, execute:

```bash
mvn package
java -jar target/biblioteca-1.0-SNAPSHOT.jar
```

O programa executa uma demonstração no console (cadastro de livro, cadastro de usuário, realização de empréstimo e devolução). O arquivo de log `biblioteca.log` será gerado no mesmo diretório.

Conteúdo e decisões de design:
- Arquitetura Hexagonal (Ports & Adapters): portas definidas no pacote `biblioteca.dominio.porta` e adaptadores em `biblioteca.infraestrutura.adaptador`.
- `EventBus<T>` genérico em `biblioteca.dominio.evento` para publicação de eventos (empréstimo realizado e devolução registrada).
- Adaptadores de repositório: memória (`*Memoria`) e CSV (`LivroRepositorioCsv`).
- Adaptadores de efeitos colaterais: `NotificacaoConsole` e `ServicoDeLog` (escreve em `biblioteca.log`).

Testes:
- Contém testes unitários básicos para a entidade `Livro` em `src/test/java` (JUnit 5) — para executar:

```bash
mvn test
```

Entrega e histórico de commits:
- O repositório contém um histórico com múltiplos commits representando o desenvolvimento (mínimo exigido atendido).
- O código está organizado conforme as estruturas de pacotes solicitadas na atividade.

Observações finais:
- Caso queira ajustar o `.gitignore` (por exemplo, incluir `*.log`), ele já foi atualizado.
- Se desejar, posso adicionar testes adicionais, melhorar a documentação ou preparar um arquivo ZIP pronto para envio.
