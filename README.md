# POO
Projeto de programação orientada aos objetos
Sistema de Gestão de Hotel — README
1. O que é este projeto

Este projeto é uma aplicação em Java que simula um sistema de gestão de um hotel, permitindo gerir utilizadores e reservas de forma simples através de uma interface em linha de comandos (CLI).

A aplicação foi desenvolvida no contexto da disciplina de Programação Orientada a Objetos, tendo como foco a aplicação prática de conceitos como herança, abstração, composição, enums, comparadores e persistência de dados em JSON.

2. Como funciona o projeto (visão geral)

Quando a aplicação é iniciada:

Os dados são carregados a partir de ficheiros JSON (clientes e reservas).

O utilizador escolhe o tipo de acesso:

Administrador

Cliente

Dependendo do tipo de utilizador, são apresentados menus diferentes.

O utilizador interage com o sistema através de opções numéricas.

Sempre que são feitas alterações, os dados são novamente guardados em JSON.

O sistema funciona inteiramente em memória durante a execução, garantindo persistência entre execuções através dos ficheiros.

3. Tipos de Utilizador e Interação
👨‍💼 Administrador

O Administrador tem acesso total ao sistema.
Pode:

Criar novos clientes

Consultar todos os clientes

Consultar todas as reservas existentes

Ordenar reservas por diferentes critérios

Visualizar informação global do hotel

Fluxo típico do administrador:

Iniciar a aplicação

Escolher opção “Administrador”

Selecionar operações através do menu

Visualizar resultados no terminal

👤 Cliente

O Cliente tem acesso limitado às suas próprias informações.
Pode:

Criar reservas

Consultar as suas próprias reservas

Visualizar dados associados à sua conta

Fluxo típico do cliente:

Iniciar a aplicação

Escolher opção “Cliente”

Introduzir identificação

Criar ou consultar reservas associadas

4. Como interagir com a aplicação

A interação é feita exclusivamente através do terminal:

O utilizador escolhe opções introduzindo números

As operações seguem sempre uma sequência guiada

O sistema valida todas as entradas antes de aceitar dados

Exemplo:

1 - Criar reserva
2 - Listar reservas
3 - Ordenar reservas
0 - Sair


O sistema responde sempre com mensagens claras de sucesso ou erro.

5. Persistência de Dados (JSON)

Os dados são guardados em ficheiros JSON, o que permite:

Manter informação entre execuções

Visualizar os dados manualmente

Evitar o uso de bases de dados externas

O que é guardado:

Clientes

Reservas

Relações entre clientes e reservas

A classe responsável por esta funcionalidade é a BaseDeDados, que utiliza a biblioteca Gson para converter objetos Java em JSON e vice-versa.

6. Organização Interna do Projeto (como funciona por dentro)

De forma simplificada:

Hotel

Classe central do sistema

Contém listas de clientes e reservas

Aplica filtros e ordenações

Utilizador (classe abstrata)

Define atributos comuns

É herdada por Admin e Cliente

Reserva

Representa uma reserva individual

Contém dados como data e número do quarto

Comparators

Definem regras de ordenação das reservas

Permitem ordenar sem alterar a classe Reserva

Validador

Garante que os dados introduzidos são válidos

Evita erros e estados inválidos

7. Ordenação e Filtros

O sistema permite ordenar reservas por diferentes critérios, como por exemplo:

Data

Número do quarto

A ordenação é feita através de Comparator, aplicados na classe Hotel, o que permite reutilização e flexibilidade.

Também são aplicados filtros para:

Mostrar apenas reservas de um cliente

Mostrar apenas reservas que cumpram certas condições

8. Como executar o projeto
Opção 1 — IDE (recomendado)

Abrir o projeto no IntelliJ

Garantir que a biblioteca Gson está adicionada

Executar a classe Main

Opção 2 — Terminal

Compilar o projeto

Executar a classe principal

Seguir as instruções no terminal

9. O que acontece se algo correr mal

Entradas inválidas são rejeitadas

O utilizador é informado do erro

O sistema não termina inesperadamente

Os dados não são corrompidos

10. Resumo final

Este projeto demonstra:

Como criar uma aplicação orientada a objetos em Java

Como estruturar um sistema simples mas funcional

Como persistir dados sem bases de dados externas

Como criar interações claras via terminal