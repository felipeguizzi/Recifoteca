# Recifoteca: Um Sistema de Gestão de Empréstimos da Biblioteca Cidadã

## Descrição Geral do Projeto

Este projeto apresenta um sistema de controle de empréstimos para a **Biblioteca Cidadã**, conforme solicitado.
A aplicação permite:

* Registrar usuários (alunos, servidores e visitantes);
* Gerenciar o acervo (livros, revistas e mídias digitais);
* Controlar o processo de empréstimo e devolução, incluindo o cálculo de multas por atraso.

A interface funciona no terminal (TUI – *Terminal User Interface*) e os dados são armazenados apenas em memória durante a execução.

---

## Conceitos de Programação Orientada a Objetos (POO) Aplicados

O desenvolvimento demonstra claramente os principais pilares da POO:

### **Encapsulamento**

* Todos os atributos das classes (`Usuario`, `Recurso`, `Emprestimo` e `Capitulo`) são declarados como `private`.
* O acesso é realizado somente via métodos públicos (`getters` e `setters`).
* Os construtores e os setters verificam dados inválidos (como ano negativo ou título vazio) para manter a consistência dos objetos.
* Em `Livro`, a lista interna de capítulos é exposta ao exterior apenas como uma lista imutável para impedir alterações diretas.

### **Herança e Polimorfismo**

* **Herança**:

  * A classe abstrata `Usuario` é estendida por `Aluno`, `Servidor` e `Visitante`, cada uma com suas regras próprias para cálculo de prazo e multiplicador de multa.
  * `Recurso` é a base para `Livro`, `Revista` e `MidiaDigital`, que definem como a multa é calculada.
* **Polimorfismo**:

  * Métodos como `getPrazoDiasPadrao()` e `calcularMulta()` variam conforme o tipo real do objeto, mesmo quando manipulados pela referência da classe base.

### **Abstração**

* `Recurso` é uma classe abstrata que define atributos e comportamentos essenciais para todo item do acervo.
* A interface `Emprestavel` determina um contrato para objetos que podem ser emprestados (`emprestar()`, `devolver()`, `getDataPrevistaDevolucao()`).
* A classe `Emprestimo` gerencia o processo atualmente, mas o design permite expansão futura.

### **Relações Entre Objetos**

* **Agregação**:

  * O `BibliotecaService` reúne coleções de usuários, recursos e empréstimos por meio de repositórios.
  * Um empréstimo referencia um usuário e um recurso, mas ambos existem independentemente dele.
* **Composição**:

  * `Livro` contém objetos `Capitulo`, que só existem dentro de um livro.
    Remover o livro implica perder seus capítulos.

---

## Decisões de Arquitetura

* **Persistência em Memória**:
  Os dados são mantidos em `ArrayLists` dentro de repositórios dedicados, simulando um banco em RAM. Ao encerrar a aplicação, tudo é apagado.
* **Interface TUI**:
  A experiência é baseada em menus, tornando mais simples navegar entre as opções.
* **Sem Frameworks Externos**:
  Todo o projeto usa apenas Java padrão, exceto JUnit para testes.
* **Geração Automática de IDs**:
  Cada repositório cria IDs com `AtomicLong`, imitando autoincremento.
* **Tratamento de Erros**:
  Erros como empréstimos inválidos, recurso indisponível ou usuário inexistente são capturados e mostrados ao usuário de forma clara.

---

## Como Compilar e Executar

### Requisitos

* JDK 17 ou superior instalado.

### Compilar

Na raiz do projeto, rode:

```bash
./compile.sh
```

Os arquivos `.java` serão compilados para o diretório `out/`.

### Executar a Aplicação (TUI)

Use:

```bash
./rlibre.sh
```

A interface no terminal será iniciada.

### Rodar Testes Unitários

Execute:

```bash
./run_tests.sh
```

O script baixa o JUnit Standalone (se necessário) e executa todos os testes em `tests`.

---

## Casos de Uso para Validação Manual

Após iniciar a aplicação com `./rlibre.sh`, você pode testar o sistema seguindo os passos:

1. **Cadastrar três usuários**
   Menu Principal → *Gerenciar Usuários* → *Criar Usuário*.
   Registre um Aluno, um Servidor e um Visitante.

2. **Cadastrar três tipos de recursos**
   Menu Principal → *Gerenciar Recursos*.

   * Adicione um Livro (criando ao menos três capítulos).
   * Depois uma Revista e uma Mídia Digital.

3. **Empréstimo e devolução atrasada de um Livro (para Aluno)**

   * Liste os recursos e anote o ID do livro.
   * Em *Empréstimos*, empreste o livro ao aluno.
   * Em seguida, faça a devolução informando uma data posterior à prevista para ver a multa.

4. **Emprestar Mídia Digital a um Visitante**

   * Tente colocar uma data de devolução muito distante para verificar a validação de prazo.

5. **Visualizar relatórios**

   * *Listar Empréstimos Ativos*
   * *Listar Empréstimos Atrasados*

6. **Testar a restrição de recurso já emprestado**

   * Tente realizar um novo empréstimo do mesmo recurso físico sem antes devolvê-lo.

7. **Exibir capítulos de um Livro**

   * Listando os recursos, a descrição do livro deve mostrar também seus capítulos.

---

Se quiser, posso **melhorar**, **reduzir**, **formalizar**, ou **transformar em PDF** esse texto. Basta pedir!


