package br.recife.biblioteca.servico;

import br.recife.biblioteca.modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BibliotecaServiceTest {

    private BibliotecaService service;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @BeforeEach
    void setUp() {
        service = new BibliotecaService();
    }

    @Test
    void testCriarEListarUsuario() {
        Usuario aluno = service.criarUsuario("João Silva", "joao@example.com", "aluno");
        assertNotNull(aluno);
        assertEquals("João Silva", aluno.getNome());
        assertEquals("joao@example.com", aluno.getEmail());
        assertTrue(service.listarUsuarios().contains(aluno));
    }

    @Test
    void testNaoPodeCriarUsuarioComEmailDuplicado() {
        service.criarUsuario("Maria Souza", "maria@example.com", "servidor");
        assertThrows(IllegalArgumentException.class, () -> {
            service.criarUsuario("Maria Nova", "maria@example.com", "aluno");
        });
    }

    @Test
    void testCriarEListarRecurso() {
        Recurso livro = service.criarLivro("Clean Code", 2008);
        assertNotNull(livro);
        assertEquals("Clean Code", livro.getTitulo());
        assertTrue(service.listarRecursos().contains(livro));
    }

    @Test
    void testCalculoMultaLivro() {
        Livro livro = new Livro("Teste Multa Livro", 2020);
        // Multa por dia: 2.50. Atraso de 5 dias = 12.50
        assertEquals(12.50, livro.calcularMulta(5), 0.001);
        assertEquals(0.0, livro.calcularMulta(0), 0.001);
        assertEquals(0.0, livro.calcularMulta(-1), 0.001);
    }

    @Test
    void testCalculoMultaRevistaComCarencia() {
        Revista revista = new Revista("Teste Multa Revista", 2021, 1, "Mensal");
        // Multa por dia: 1.00. Carência: 2 dias.
        // Atraso de 1 dia -> 0.0
        assertEquals(0.0, revista.calcularMulta(1), 0.001);
        // Atraso de 2 dias -> 0.0
        assertEquals(0.0, revista.calcularMulta(2), 0.001);
        // Atraso de 3 dias -> (3-2)*1.00 = 1.00
        assertEquals(1.00, revista.calcularMulta(3), 0.001);
        // Atraso de 5 dias -> (5-2)*1.00 = 3.00
        assertEquals(3.00, revista.calcularMulta(5), 0.001);
    }

    @Test
    void testCalculoMultaMidiaDigital() {
        MidiaDigital midia = new MidiaDigital("Teste Multa Midia", 2022, 100.0, "PDF");
        // Multa fixa: 5.00
        assertEquals(5.00, midia.calcularMulta(1), 0.001);
        assertEquals(5.00, midia.calcularMulta(10), 0.001);
        assertEquals(0.0, midia.calcularMulta(0), 0.001);
    }

    @Test
    void testEmprestarEDevolverLivroComAtraso() throws Exception {
        Usuario aluno = service.criarUsuario("Aluno Teste", "aluno@test.com", "aluno");
        Recurso livro = service.criarLivro("Livro Teste", 2023);
        service.adicionarUnidadesRecurso(livro.getId(), 1);

        // Empréstimo: 01/01/2025, Previsto: 15/01/2025 (Prazo Aluno 15 dias)
        Emprestimo emp = service.emprestarRecurso(livro.getId(), aluno.getEmail(), "01/01/2025", "15/01/2025");
        assertNotNull(emp);
        assertEquals(0, service.getEstoqueDisponivel(livro.getId()));

        // Devolução com atraso: 20/01/2025 (5 dias de atraso)
        // Livro: 2.50/dia. Aluno: fator 1.0. Total: 5 * 2.50 * 1.0 = 12.50
        Emprestimo empDevolvido = service.devolverRecurso(livro.getId(), aluno.getEmail(), "20/01/2025");
        assertEquals(12.50, empDevolvido.getMultaGerada(), 0.001);
        assertEquals(1, service.getEstoqueDisponivel(livro.getId()));
    }

    @Test
    void testEmprestarLivroDuasVezesNaoPermitido() throws Exception {
        Usuario aluno = service.criarUsuario("Aluno Duplo", "aluno2@test.com", "aluno");
        Recurso livro = service.criarLivro("Livro Duplo", 2024);
        service.adicionarUnidadesRecurso(livro.getId(), 1);

        service.emprestarRecurso(livro.getId(), aluno.getEmail(), "01/01/2025", "15/01/2025");
        assertEquals(0, service.getEstoqueDisponivel(livro.getId()));

        assertThrows(IllegalStateException.class, () -> {
            service.emprestarRecurso(livro.getId(), aluno.getEmail(), "02/01/2025", "16/01/2025");
        });
    }

    @Test
    void testComposicaoLivroCapitulo() {
        Livro livro = (Livro) service.criarLivro("Livro com Capítulos", 2020);
        service.adicionarCapituloEmLivro(livro.getId(), 1, "Introdução", 20);
        service.adicionarCapituloEmLivro(livro.getId(), 2, "Desenvolvimento", 50);

        Optional<Recurso> livroRecuperadoOpt = service.buscarRecursoPorId(livro.getId());
        assertTrue(livroRecuperadoOpt.isPresent());
        Livro livroRecuperado = (Livro) livroRecuperadoOpt.get();

        assertEquals(2, livroRecuperado.getCapitulos().size());
        assertEquals("Introdução", livroRecuperado.getCapitulos().get(0).getTitulo());
        assertEquals(50, livroRecuperado.getCapitulos().get(1).getPaginas());
    }
    
    @Test
    void testDeletarUsuarioComEmprestimoAtivoNaoPermitido() throws Exception {
        Usuario aluno = service.criarUsuario("User Com Empr", "user@emprestimo.com", "aluno");
        Recurso livro = service.criarLivro("Livro Emprestado", 2023);
        service.adicionarUnidadesRecurso(livro.getId(), 1);
        service.emprestarRecurso(livro.getId(), aluno.getEmail(), "01/01/2025", "15/01/2025");

        assertThrows(IllegalArgumentException.class, () -> {
            service.deletarUsuario(aluno.getEmail());
        });
    }

    @Test
    void testDeletarRecursoComEmprestimoAtivoNaoPermitido() throws Exception {
        Usuario aluno = service.criarUsuario("User Recurso Empr", "user@recurso.com", "aluno");
        Recurso livro = service.criarLivro("Recurso Emprestado", 2023);
        service.adicionarUnidadesRecurso(livro.getId(), 1);
        service.emprestarRecurso(livro.getId(), aluno.getEmail(), "01/01/2025", "15/01/2025");

        assertThrows(IllegalArgumentException.class, () -> {
            service.deletarRecurso(livro.getId());
        });
    }

    @Test
    void testHistoricoUsuarioComMultas() throws Exception {
        Usuario aluno = service.criarUsuario("Histórico Multa", "hist@multa.com", "aluno");
        Recurso livro = service.criarLivro("Livro para Multa", 2023);
        service.adicionarUnidadesRecurso(livro.getId(), 1);

        service.emprestarRecurso(livro.getId(), aluno.getEmail(), "01/01/2025", "15/01/2025");
        service.devolverRecurso(livro.getId(), aluno.getEmail(), "20/01/2025"); // 5 dias atraso

        assertEquals(1, service.calcularNumeroMultasAtrasoUsuario(aluno.getEmail()));

        // Segundo empréstimo sem multa
        service.adicionarUnidadesRecurso(livro.getId(), 1); // Repor estoque
        service.emprestarRecurso(livro.getId(), aluno.getEmail(), "01/02/2025", "15/02/2025");
        service.devolverRecurso(livro.getId(), aluno.getEmail(), "15/02/2025"); // Sem atraso

        assertEquals(1, service.calcularNumeroMultasAtrasoUsuario(aluno.getEmail())); // Ainda 1 multa
    }
    
    @Test
    void testPrazoEmprestimoUsuario() throws Exception {
        Usuario aluno = service.criarUsuario("Prazo Aluno", "prazo.aluno@test.com", "aluno"); // Prazo 15 dias
        Recurso livro = service.criarLivro("Livro Prazo", 2023);
        service.adicionarUnidadesRecurso(livro.getId(), 1);

        // Empréstimo dentro do prazo
        assertDoesNotThrow(() -> service.emprestarRecurso(livro.getId(), aluno.getEmail(), "01/01/2025", "15/01/2025"));
        service.devolverRecurso(livro.getId(), aluno.getEmail(), "10/01/2025");
        service.adicionarUnidadesRecurso(livro.getId(), 1); // Repor estoque para o próximo teste

        // Empréstimo excedendo o prazo
        assertThrows(IllegalArgumentException.class, () -> {
            service.emprestarRecurso(livro.getId(), aluno.getEmail(), "01/01/2025", "17/01/2025"); // 16 dias
        });
    }
}
