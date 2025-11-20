# Rlibre TUI - Lista de Tarefas (Versão em Memória)

Esta é a ordem de implementação para construir a aplicação `rlibre` com uma TUI (Terminal User Interface) e persistência de dados em memória.

### Fase 1: Estrutura e Modelo de Dados

1.  **[Concluído] Configuração do Projeto:**
    *   Criar a estrutura de diretórios do projeto Java: `src/main/java/br/recife/biblioteca`, `tests/`.
    *   Criar um script de build (`compile.sh`) e execução (`rlibre.sh`).

2.  **[Concluído] Implementação do Modelo de Domínio (POJOs):**
    *   `br.recife.biblioteca.modelo`:
        *   `abstract class Usuario` e suas especializações: `Aluno`, `Servidor`, `Visitante`.
        *   `interface Emprestavel`.
        *   `abstract class Recurso` e suas especializações: `Livro`, `Revista`, `MidiaDigital`.
        *   `class Capitulo`.
        *   `class Emprestimo`.

### Fase 2: Persistência e Lógica de Negócios

3.  **[Pendente] Camada de Repositório (Em Memória):**
    *   `br.recife.biblioteca.repositorio`:
        *   `UsuarioRepository`: Métodos CRUD para usuários usando uma `List<Usuario>`.
        *   `RecursoRepository`: Métodos CRUD para recursos usando uma `List<Recurso>`.
        *   `EmprestimoRepository`: Métodos para gerenciar empréstimos usando uma `List<Emprestimo>`.
        *   Os repositórios devem lidar com a geração de IDs.

4.  **[Pendente] Camada de Serviço (Regras de Negócio):**
    *   `br.recife.biblioteca.servico`:
        *   `BibliotecaService`: Orquestra as operações, utilizando os repositórios em memória.
        *   Implementar a lógica de `emprestar()` (verificar disponibilidade, regras por tipo de usuário).
        *   Implementar a lógica de `devolver()` (cálculo de multas).
        *   Implementar a geração de relatórios (disponíveis, atrasados, histórico).

### Fase 3: Interface e Finalização

5.  **[Pendente] Implementação da Interface de Terminal (TUI):**
    *   `br.recife.biblioteca.ui`:
        *   `BibliotecaTUI`: Classe principal com o método `main`.
        *   Implementar um loop principal que exibe um menu de opções.
        *   Usar `java.util.Scanner` para ler a entrada do usuário.
        *   Chamar os métodos apropriados da `BibliotecaService` com base na escolha do usuário.
        *   Exibir os resultados de forma clara no terminal.

6.  **[Pendente] Testes Unitários:**
    *   `tests/`:
        *   Criar pelo menos 5 testes JUnit para validar:
            *   Cálculo de multa para diferentes tipos de recursos.
            *   Prazos de empréstimo por tipo de usuário.
            *   Lógica de composição (Livro e Capítulo).
            *   Exceção ao tentar emprestar item indisponível.
            *   Operações do repositório em memória.

7.  **[Pendente] Documentação:**
    *   Atualizar o `README.md` com as instruções de compilação e execução da TUI.
    *   Documentar as decisões de projeto (OO, agregação, composição, persistência em memória).