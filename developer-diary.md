# Diário de bordo

Nesse arquivo vou centralizar os pensamentos que tive e as decisões que tomei durante a execução do projeto.

## Dia 01 - segunda-feira
### Escolha da stack
- Optei por SpringBoot + JPA para abstrair a camada de persistência dos dados, para que eu consiga focar na implementação da regra de negócio em si.
- Para o banco, optei por PostgreSQL. Sei que talvez não seja a melhor solução para resolver o problema proposto (com certeza não é), mas como quero focar na regra de negócio, achei que se eu optasse por algum banco que tenho 0 experiência, talvez eu ficasse travado em coisas básicas e não conseguisse entregar o desafio.
- Tenho total consciência de que se tratando de um autorizador para ser usado em produção, essa escolha deve ser feita com muuuuito mais calma. Pensando por alto, eu optaria por alguma solução cloud native, tipo [Quarkus](https://code.quarkus.io/), para evitar os downtimes causados pela demora do boot de aplicações Spring, por exemplo.
- Em relação a modelagem da entidade `Transaction`, optei por trabalhar com o valor financeiro sendo um `BigDecimal` que representará o valor total em centavos. Isso evita diversos erros de precisão que temos ao trabalhar com pontos flutuantes como `float` e `double`.
- Configurei Docker/Docker Compose para facilitar uma futura execução em um cluster Kubernetes, e também para que o revisor do código não precise se preocupar com o setup do projeto localmente.

## Dia 02 - terça-feira
- Hoje modelei a entidade `Account` e criei o relacionamento com a classe `Transaction`.
- Criei também as classes de service, repository e controller para lidar com a manipulação das operações com `Account`.
- Pesquisei sobre padrões de arquitetura para lidar com pagamentos e cheguei no [CockroachDB](https://www.cockroachlabs.com/), que parece ser o que há de mais eficaz pra se usar em produção nesse cenário.
- Atualizei a classe CreateTransactionDto para poder enviar o campo `"accountId"` na raiz do payload para `/authorize` ao invés de mandar algo do tipo `{ "account": { "accountId": "1" }, ... }`. Não sei se isso foi uma boa decisão, ou se eu deveria manter a modelagem de `Transaction` intacta. Decidi ir pelo payload de exemplo contido na especificação.
- Criei o endpoint `/authorize` que receberá a transação a ser processada, e o método `authorize` na AccountService para implementar todas as validações propostas no desafio. Também fiquei em dúvida aqui sobre se deveria existir uma service específica para isso. Optei por implementar o método no `AccountService`, pois no final de tudo, a entidade `Account` é que será modificada no banco.

## Dia 03 - quarta-feira
- Hoje estruturei uma collection básica no Insomnia pra ajudar a testar e validar as minhas alterações.
- Alterei o `DataLoader` para parar de criar transactions de teste no startup da aplicação.
- Usei as classes `Function` e `BiConsumer` para criar o `BalanceMapper`, que implementa o de -> para de MCC -> saldos.
- Implementei o item L1 do desafio no método `authorize` da `AccountServiceImpl`
- Estudei como o [Stripe](https://docs.stripe.com/api/idempotent_requests) implementa idempotência nas suas chamadas de API.

## Dia 04 - quinta-feira
- Refatorei o código para passar a usar Enums para os MCCs e para o código das respostas.
- Implementei a lógica do fallback para o cash balance no `AccountServiceImpl`
- Usei a annotation `@Transactional` do Spring Boot para evitar inconsistências durante a manipulação dos saldos.
- Posteriormente vou complementar a tabela `transaction` para conter de que saldo foi efetuada a operação, pois com a lógica de fallback pro cashbalance, essa informação passou a poder variar.
- Implementei a regra de negócio L3 que define precedência maior para merchants conhecidos

## Dia 05 - sexta-feira
- Refatorei o nome de algumas variáveis de ambiente para melhor entendimento.
- Escrevi o [README.md](README.md) com as instruções para a execução do projeto.
- Implementei a regra de negócio L4, que consiste em usar chaves de idempotência. O client da aplicação passa a ser responsável por gerar UUIDs v4 e passa-los no payload da transaction a fim de garantir que apenas transações únicas poderão ser efetuadas.
- Atualizei a [collection do Insomnia](insomnia-requests.json) para funcionar com chaves de idempotência dinâmicas.

## Notas gerais
- Foi um desafio e tanto. Aprendi muita coisa sobre o contexto de pagamentos, que é imenso por sinal. Tenho experiência em fintech, mas de um escopo totalmente distinto.
- Eu poderia ter escrito testes unitários mais eficazes, que testassem os pedaços de lógica de cada módulo em si. Os testes que existem hoje requerem que a aplicação esteja rodando localmente e funcionam mais como E2E.
- Sobre a regra de negócio L4, em um cenário de produção, com certeza o buraco é mais em baixo, devido a necessidade da base de dados estar distribuida, isso acarreta alguns problemas pra se lidar com as chaves de idempotência. Eu não consegui chegar a uma conclusão sobre como implementaria isso num sistema distribuido. Talvez um cluster de Redis distribuido dedicado a armazenar essas chaves, ou algo como o Apache Zookeeper para trabalhar com locks distribuidos.
