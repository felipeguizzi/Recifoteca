package br.recife.biblioteca.ui;

import br.recife.biblioteca.modelo.*;
import br.recife.biblioteca.servico.BibliotecaService;

import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BibliotecaTUI {

    private final BibliotecaService service;
    private final Scanner scanner;

    public BibliotecaTUI() {
        this.service = new BibliotecaService();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        BibliotecaTUI tui = new BibliotecaTUI();
        tui.run();
    }

    public void run() {
        System.out.println("Bem-vindo(a) à Biblioteca Cidadã!");
        int choice;
        do {
            displayMenu();
            choice = getUserChoice();

            try {
                switch (choice) {
                    case 1:
                        menuUsuarios();
                        break;
                    case 2:
                        menuRecursos();
                        break;
                    case 3:
                        menuEmprestimos();
                        break;
                    case 0:
                        System.out.println("Saindo da aplicação. Até mais!");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine(); // Consome a linha pendente e espera pelo Enter
        } while (choice != 0);
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Gerenciar Usuários");
        System.out.println("2. Gerenciar Recursos (Livros, Revistas, Mídias Digitais)");
        System.out.println("3. Gerenciar Empréstimos e Devoluções");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.next(); // Consumir a entrada inválida
            System.out.print("Escolha uma opção: ");
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha
        return choice;
    }

    private LocalDate lerData(String prompt) {
        while (true) {
            System.out.print(prompt + " (DD/MM/AAAA): ");
            String dataStr = scanner.nextLine();
            try {
                return LocalDate.parse(dataStr, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Use DD/MM/AAAA.");
            }
        }
    }

    // ===============================================
    // Menus Secundários
    // ===============================================

    private void menuUsuarios() {
        int choice;
        do {
            System.out.println("\n--- GERENCIAR USUÁRIOS ---");
            System.out.println("1. Criar Usuário");
            System.out.println("2. Listar Usuários");
            System.out.println("3. Buscar Usuário por Email");
            System.out.println("4. Deletar Usuário");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            choice = getUserChoice();

            try {
                switch (choice) {
                    case 1: criarUsuario(); break;
                    case 2: listarUsuarios(); break;
                    case 3: buscarUsuarioPorEmail(); break;
                    case 4: deletarUsuario(); break;
                    case 0: break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
            if (choice != 0) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        } while (choice != 0);
    }

    private void menuRecursos() {
        int choice;
        do {
            System.out.println("\n--- GERENCIAR RECURSOS ---");
            System.out.println("1. Criar Livro");
            System.out.println("2. Criar Revista");
            System.out.println("3. Criar Mídia Digital");
            System.out.println("4. Listar Recursos");
            System.out.println("5. Adicionar Unidades ao Estoque (Livro/Revista)");
            System.out.println("6. Remover Unidades do Estoque (Livro/Revista)");
            System.out.println("7. Visualizar Estoque de um Recurso");
            System.out.println("8. Deletar Recurso");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            choice = getUserChoice();

            try {
                switch (choice) {
                    case 1: criarLivro(); break;
                    case 2: criarRevista(); break;
                    case 3: criarMidiaDigital(); break;
                    case 4: listarRecursos(); break;
                    case 5: adicionarUnidadesRecurso(); break;
                    case 6: removerUnidadesRecurso(); break;
                    case 7: visualizarEstoqueRecurso(); break;
                    case 8: deletarRecurso(); break;
                    case 0: break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
            if (choice != 0) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        } while (choice != 0);
    }

    private void menuEmprestimos() {
        int choice;
        do {
            System.out.println("\n--- GERENCIAR EMPRÉSTIMOS ---");
            System.out.println("1. Realizar Empréstimo");
            System.out.println("2. Realizar Devolução");
            System.out.println("3. Listar Empréstimos Ativos");
            System.out.println("4. Listar Empréstimos Atrasados");
            System.out.println("5. Histórico de Empréstimos por Usuário");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            choice = getUserChoice();

            try {
                switch (choice) {
                    case 1: realizarEmprestimo(); break;
                    case 2: realizarDevolucao(); break;
                    case 3: listarEmprestimosAtivos(); break;
                    case 4: listarEmprestimosAtrasados(); break;
                    case 5: historicoEmprestimosUsuario(); break;
                    case 0: break;
                    default: System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
            if (choice != 0) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        } while (choice != 0);
    }

    // ===============================================
    // Implementações das Ações de Usuário
    // ===============================================

    private void criarUsuario() {
        System.out.print("Nome do usuário: ");
        String nome = scanner.nextLine();
        System.out.print("Email do usuário: ");
        String email = scanner.nextLine();
        System.out.print("Tipo (aluno, servidor, visitante): ");
        String tipo = scanner.nextLine();

        try {
            Usuario novoUsuario = service.criarUsuario(nome, email, tipo);
            System.out.println("Usuário criado com sucesso: " + novoUsuario);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao criar usuário: " + e.getMessage());
        }
    }

    private void listarUsuarios() {
        List<Usuario> usuarios = service.listarUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }
        System.out.println("\n--- LISTA DE USUÁRIOS ---");
        usuarios.forEach(System.out::println);
    }

    private void buscarUsuarioPorEmail() {
        System.out.print("Email do usuário a buscar: ");
        String email = scanner.nextLine();
        service.buscarUsuarioPorEmail(email)
                .ifPresentOrElse(
                        u -> System.out.println("Usuário encontrado: " + u),
                        () -> System.out.println("Usuário com email '" + email + "' não encontrado.")
                );
    }

    private void deletarUsuario() {
        System.out.print("Email do usuário a deletar: ");
        String email = scanner.nextLine();
        try {
            if (service.deletarUsuario(email)) {
                System.out.println("Usuário deletado com sucesso.");
            } else {
                System.out.println("Usuário com email '" + email + "' não encontrado.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao deletar usuário: " + e.getMessage());
        }
    }

    // ===============================================
    // Implementações das Ações de Recurso
    // ===============================================

    private void criarLivro() {
        System.out.print("Título do Livro: ");
        String titulo = scanner.nextLine();
        System.out.print("Ano de Publicação: ");
        int ano = getUserIntInput();
        try {
            Livro livro = (Livro) service.criarLivro(titulo, ano);
            System.out.println("Livro criado com sucesso: " + livro);
            System.out.print("Deseja adicionar capítulos a este livro? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                adicionarCapitulosAoLivro(livro.getId());
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao criar livro: " + e.getMessage());
        }
    }
    
    private void adicionarCapitulosAoLivro(Long livroId) {
        String continuar = "s";
        while (continuar.equalsIgnoreCase("s")) {
            System.out.println("\n--- Adicionar Capítulo ---");
            System.out.print("Número do Capítulo: ");
            int numero = getUserIntInput();
            System.out.print("Título do Capítulo: ");
            String titulo = scanner.nextLine();
            System.out.print("Número de Páginas: ");
            int paginas = getUserIntInput();
            
            try {
                service.adicionarCapituloEmLivro(livroId, numero, titulo, paginas);
                System.out.println("Capítulo adicionado com sucesso.");
            } catch (IllegalArgumentException e) {
                System.err.println("Erro ao adicionar capítulo: " + e.getMessage());
            }

            System.out.print("Adicionar outro capítulo? (s/n): ");
            continuar = scanner.nextLine();
        }
    }

    private void criarRevista() {
        System.out.print("Título da Revista: ");
        String titulo = scanner.nextLine();
        System.out.print("Ano de Publicação: ");
        int ano = getUserIntInput();
        System.out.print("Edição: ");
        int edicao = getUserIntInput();
        System.out.print("Periodicidade: ");
        String periodicidade = scanner.nextLine();

        try {
            Revista revista = (Revista) service.criarRevista(titulo, ano, edicao, periodicidade);
            System.out.println("Revista criada com sucesso: " + revista);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao criar revista: " + e.getMessage());
        }
    }

    private void criarMidiaDigital() {
        System.out.print("Título da Mídia Digital: ");
        String titulo = scanner.nextLine();
        System.out.print("Ano de Publicação: ");
        int ano = getUserIntInput();
        System.out.print("Tamanho em MB: ");
        double tamanhoMB = getUserDoubleInput();
        System.out.print("Formato do Arquivo: ");
        String formato = scanner.nextLine();

        try {
            MidiaDigital midia = (MidiaDigital) service.criarMidiaDigital(titulo, ano, tamanhoMB, formato);
            System.out.println("Mídia Digital criada com sucesso: " + midia);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao criar mídia digital: " + e.getMessage());
        }
    }

    private void listarRecursos() {
        List<Recurso> recursos = service.listarRecursos();
        if (recursos.isEmpty()) {
            System.out.println("Nenhum recurso cadastrado.");
            return;
        }
        System.out.println("\n--- LISTA DE RECURSOS ---");
        recursos.forEach(recurso -> {
            System.out.print(recurso.getDescricao());
            if (!(recurso instanceof MidiaDigital)) {
                System.out.print(" (Disponível: " + service.getEstoqueDisponivel(recurso.getId()) +
                                 "/" + service.getEstoqueTotal(recurso.getId()) + ")");
            } else if (recurso instanceof Livro) {
                System.out.print(", Capítulos: " + ((Livro)recurso).getCapitulos().size());
            }
            System.out.println();
        });
    }

    private void adicionarUnidadesRecurso() {
        System.out.print("ID do Recurso: ");
        long id = getUserLongInput();
        System.out.print("Quantidade a adicionar: ");
        int quantidade = getUserIntInput();

        try {
            service.adicionarUnidadesRecurso(id, quantidade);
            System.out.println("Unidades adicionadas com sucesso.");
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.err.println("Erro ao adicionar unidades: " + e.getMessage());
        }
    }

    private void removerUnidadesRecurso() {
        System.out.print("ID do Recurso: ");
        long id = getUserLongInput();
        System.out.print("Quantidade a remover: ");
        int quantidade = getUserIntInput();

        try {
            if (service.removerUnidadesRecurso(id, quantidade)) {
                System.out.println("Unidades removidas com sucesso.");
            } else {
                System.out.println("Não foi possível remover as unidades (quantidade insuficiente ou recurso não encontrado).");
            }
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.err.println("Erro ao remover unidades: " + e.getMessage());
        }
    }

    private void visualizarEstoqueRecurso() {
        System.out.print("ID do Recurso: ");
        long id = getUserLongInput();
        try {
            Optional<Recurso> recursoOpt = service.buscarRecursoPorId(id);
            if (recursoOpt.isPresent()) {
                Recurso recurso = recursoOpt.get();
                System.out.println("Recurso: " + recurso.getTitulo());
                if (recurso instanceof MidiaDigital) {
                    System.out.println("Mídia Digital não possui estoque físico.");
                } else {
                    System.out.println("Estoque Total: " + service.getEstoqueTotal(id));
                    System.out.println("Estoque Disponível: " + service.getEstoqueDisponivel(id));
                }
            } else {
                System.out.println("Recurso não encontrado.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao visualizar estoque: " + e.getMessage());
        }
    }

    private void deletarRecurso() {
        System.out.print("ID do Recurso a deletar: ");
        long id = getUserLongInput();
        try {
            if (service.deletarRecurso(id)) {
                System.out.println("Recurso deletado com sucesso.");
            } else {
                System.out.println("Recurso com ID '" + id + "' não encontrado.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao deletar recurso: " + e.getMessage());
        }
    }

    // ===============================================
    // Implementações das Ações de Empréstimo
    // ===============================================

    private void realizarEmprestimo() {
        System.out.print("ID do Recurso a emprestar: ");
        long recursoId = getUserLongInput();
        System.out.print("Email do Usuário: ");
        String userEmail = scanner.nextLine();
        String dataEmprestimoStr = lerData("Data do Empréstimo").format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String dataPrevistaStr = lerData("Data Prevista de Devolução").format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        try {
            Emprestimo novoEmprestimo = service.emprestarRecurso(recursoId, userEmail, dataEmprestimoStr, dataPrevistaStr);
            System.out.println("Empréstimo realizado com sucesso: " + novoEmprestimo);
        } catch (Exception e) {
            System.err.println("Erro ao realizar empréstimo: " + e.getMessage());
        }
    }

    private void realizarDevolucao() {
        System.out.print("ID do Recurso a devolver: ");
        long recursoId = getUserLongInput();
        System.out.print("Email do Usuário que emprestou: ");
        String userEmail = scanner.nextLine();
        String dataDevolucaoStr = lerData("Data da Devolução").format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        try {
            Emprestimo emprestimoDevolvido = service.devolverRecurso(recursoId, userEmail, dataDevolucaoStr);
            System.out.println("Devolução realizada com sucesso. Empréstimo: " + emprestimoDevolvido);
            if (emprestimoDevolvido.getMultaGerada() > 0) {
                System.out.printf("Multa gerada: R$ %.2f\n", emprestimoDevolvido.getMultaGerada());
            }
        } catch (Exception e) {
            System.err.println("Erro ao realizar devolução: " + e.getMessage());
        }
    }

    private void listarEmprestimosAtivos() {
        List<Emprestimo> ativos = service.listarEmprestimosAtivos();
        if (ativos.isEmpty()) {
            System.out.println("Nenhum empréstimo ativo.");
            return;
        }
        System.out.println("\n--- EMPRÉSTIMOS ATIVOS ---");
        ativos.forEach(System.out::println);
    }

    private void listarEmprestimosAtrasados() {
        List<Emprestimo> atrasados = service.listarEmprestimosAtrasados();
        if (atrasados.isEmpty()) {
            System.out.println("Nenhum empréstimo atrasado.");
            return;
        }
        System.out.println("\n--- EMPRÉSTIMOS ATRASADOS ---");
        atrasados.forEach(System.out::println);
    }

    private void historicoEmprestimosUsuario() {
        System.out.print("Email do Usuário para histórico: ");
        String email = scanner.nextLine();
        try {
            List<Emprestimo> historico = service.historicoDeEmprestimosPorUsuario(email);
            if (historico.isEmpty()) {
                System.out.println("Nenhum empréstimo encontrado para o usuário " + email + ".");
                return;
            }
            System.out.println("\n--- HISTÓRICO DE EMPRÉSTIMOS PARA " + email.toUpperCase() + " ---");
            historico.forEach(System.out::println);
            int multas = service.calcularNumeroMultasAtrasoUsuario(email);
            System.out.println("Número de multas por atraso aplicadas: " + multas);

        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao buscar histórico: " + e.getMessage());
        }
    }
    
    // ===============================================
    // Métodos Auxiliares de Entrada
    // ===============================================
    
    private int getUserIntInput() {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número inteiro.");
                scanner.next(); // Consumir a entrada inválida
            }
        }
    }

    private long getUserLongInput() {
        while (true) {
            try {
                long value = scanner.nextLong();
                scanner.nextLine(); // Consumir a quebra de linha
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número inteiro longo.");
                scanner.next(); // Consumir a entrada inválida
            }
        }
    }

    private double getUserDoubleInput() {
        while (true) {
            try {
                double value = scanner.nextDouble();
                scanner.nextLine(); // Consumir a quebra de linha
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número decimal.");
                scanner.next(); // Consumir a entrada inválida
            }
        }
    }
}
