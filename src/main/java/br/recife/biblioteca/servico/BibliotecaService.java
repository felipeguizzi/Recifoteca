package br.recife.biblioteca.servico;

import br.recife.biblioteca.modelo.*;
import br.recife.biblioteca.repositorio.EmprestimoRepository;
import br.recife.biblioteca.repositorio.RecursoRepository;
import br.recife.biblioteca.repositorio.UsuarioRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BibliotecaService {

    private final UsuarioRepository usuarioRepository;
    private final RecursoRepository recursoRepository;
    private final EmprestimoRepository emprestimoRepository;

    public BibliotecaService() {
        this.usuarioRepository = new UsuarioRepository();
        this.recursoRepository = new RecursoRepository();
        this.emprestimoRepository = new EmprestimoRepository();
    }
    
    // =================================================================
    // Operações de Usuário
    // =================================================================

    public Usuario criarUsuario(String nome, String email, String tipo) {
        Usuario usuario;
        switch (tipo.toLowerCase()) {
            case "aluno":
                usuario = new Aluno(nome, email);
                break;
            case "servidor":
                usuario = new Servidor(nome, email);
                break;
            case "visitante":
                usuario = new Visitante(nome, email);
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuário inválido: " + tipo);
        }
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Usuário com e-mail '" + email + "' já existe.");
        }
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public boolean deletarUsuario(String email) {
        // Antes de deletar, verificar se há empréstimos ativos para este usuário
        Optional<Usuario> userToDelete = usuarioRepository.findByEmail(email);
        if (userToDelete.isPresent()) {
            List<Emprestimo> emprestimosDoUsuario = emprestimoRepository.findByUsuarioId(userToDelete.get().getId());
            boolean temEmprestimoAtivo = emprestimosDoUsuario.stream()
                                            .anyMatch(e -> e.getDataDevolucao() == null);
            if (temEmprestimoAtivo) {
                throw new IllegalArgumentException("Não é possível deletar usuário com empréstimos ativos.");
            }
        }
        return usuarioRepository.deleteByEmail(email);
    }
    
    // =================================================================
    // Operações de Recurso
    // =================================================================

    public Recurso criarLivro(String titulo, int anoPublicacao) {
        return recursoRepository.save(new Livro(titulo, anoPublicacao));
    }

    public Recurso criarRevista(String titulo, int anoPublicacao, int edicao, String periodicidade) {
        return recursoRepository.save(new Revista(titulo, anoPublicacao, edicao, periodicidade));
    }

    public Recurso criarMidiaDigital(String titulo, int anoPublicacao, double tamanhoMB, String formatoArquivo) {
        return recursoRepository.save(new MidiaDigital(titulo, anoPublicacao, tamanhoMB, formatoArquivo));
    }

    public List<Recurso> listarRecursos() {
        return recursoRepository.findAll();
    }
    
    public Optional<Recurso> buscarRecursoPorId(long id) {
        return recursoRepository.findById(id);
    }

    public void adicionarUnidadesRecurso(long id, int quantidade) {
        Optional<Recurso> recursoOpt = recursoRepository.findById(id);
        if (recursoOpt.isEmpty()) {
            throw new IllegalArgumentException("Recurso não encontrado.");
        }
        if (recursoOpt.get() instanceof MidiaDigital) {
            throw new UnsupportedOperationException("Mídia digital não possui estoque físico.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser positiva.");
        }
        recursoRepository.adicionarUnidades(id, quantidade);
    }

    public boolean removerUnidadesRecurso(long id, int quantidade) {
        Optional<Recurso> recursoOpt = recursoRepository.findById(id);
        if (recursoOpt.isEmpty()) {
            throw new IllegalArgumentException("Recurso não encontrado.");
        }
        if (recursoOpt.get() instanceof MidiaDigital) {
            throw new UnsupportedOperationException("Mídia digital não possui estoque físico.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser positiva.");
        }
        return recursoRepository.removerUnidades(id, quantidade);
    }
    
    public int getEstoqueDisponivel(long id) {
        Optional<Recurso> recursoOpt = recursoRepository.findById(id);
        if (recursoOpt.isEmpty()) {
            throw new IllegalArgumentException("Recurso não encontrado.");
        }
        if (recursoOpt.get() instanceof MidiaDigital) {
            return 1; // Mídia digital está sempre "disponível" para empréstimo
        }
        return recursoRepository.getEstoqueDisponivel(id);
    }
    
    public int getEstoqueTotal(long id) {
        Optional<Recurso> recursoOpt = recursoRepository.findById(id);
        if (recursoOpt.isEmpty()) {
            throw new IllegalArgumentException("Recurso não encontrado.");
        }
        if (recursoOpt.get() instanceof MidiaDigital) {
            return 1; // Mídia digital está sempre "disponível" para empréstimo
        }
        return recursoRepository.getEstoqueTotal(id);
    }

    public boolean deletarRecurso(Long id) {
        // Verificar se há empréstimos ativos para este recurso
        List<Emprestimo> emprestimosDoRecurso = emprestimoRepository.findAll().stream()
                                                .filter(e -> e.getRecurso().getId().equals(id) && e.getDataDevolucao() == null)
                                                .collect(Collectors.toList());
        if (!emprestimosDoRecurso.isEmpty()) {
            throw new IllegalArgumentException("Não é possível deletar recurso com empréstimos ativos.");
        }
        return recursoRepository.deleteById(id);
    }
    
    public void adicionarCapituloEmLivro(Long livroId, int numero, String titulo, int paginas) {
        Optional<Recurso> recursoOpt = recursoRepository.findById(livroId);
        if (recursoOpt.isEmpty()) {
            throw new IllegalArgumentException("Livro não encontrado.");
        }
        if (!(recursoOpt.get() instanceof Livro)) {
            throw new IllegalArgumentException("O recurso com ID " + livroId + " não é um livro.");
        }
        Livro livro = (Livro) recursoOpt.get();
        livro.adicionarCapitulo(new Capitulo(numero, titulo, paginas));
        recursoRepository.save(livro); // Salva o livro atualizado
    }
    
    // =================================================================
    // Operações de Empréstimo
    // =================================================================
    
    public Emprestimo emprestarRecurso(long recursoId, String userEmail, String dataEmprestimoStr, String dataPrevistaStr) throws Exception {
        LocalDate dataEmprestimo = LocalDate.parse(dataEmprestimoStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate dataPrevista = LocalDate.parse(dataPrevistaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(userEmail);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        Optional<Recurso> recursoOpt = recursoRepository.findById(recursoId);
        if (recursoOpt.isEmpty()) {
            throw new IllegalArgumentException("Recurso não encontrado.");
        }
        
        // Verificar se o recurso já está emprestado pelo mesmo usuário (apenas para mídias digitais ou se a regra fosse impedir múltiplos empréstimos do mesmo item pelo mesmo usuário)
        // Para in-memory, vamos considerar que um recurso físico é emprestado uma unidade por vez
        if (recursoRepository.emprestarUnidade(recursoId) == false) {
             // Se emprestarUnidade retornou false, significa que não há estoque ou não é mídia digital e não pode ser emprestado
            if (recursoOpt.get() instanceof MidiaDigital) {
                // Para mídias digitais, verificamos se o usuário já tem um empréstimo ativo
                if (emprestimoRepository.findAtivoByRecursoIdAndUsuarioEmail(recursoId, userEmail).isPresent()) {
                    throw new IllegalStateException("Usuário já possui este recurso digital emprestado.");
                }
            } else {
                 throw new IllegalStateException("Recurso sem estoque disponível para empréstimo.");
            }
        }

        Usuario usuario = usuarioOpt.get();
        Recurso recurso = recursoOpt.get();
        
        // Validações
        if (dataPrevista.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("Data de devolução prevista (" + dataPrevistaStr + ") não pode ser anterior à data de empréstimo (" + dataEmprestimoStr + ").");
        }
        
        long prazoEmprestimoDias = ChronoUnit.DAYS.between(dataEmprestimo, dataPrevista);
        if (prazoEmprestimoDias > usuario.getPrazoDiasPadrao()) {
            throw new IllegalArgumentException("Prazo de empréstimo excede o limite para este tipo de usuário (" + usuario.getPrazoDiasPadrao() + " dias).");
        }

        Emprestimo emprestimo = new Emprestimo(recurso, usuario, dataEmprestimo, dataPrevista);
        return emprestimoRepository.save(emprestimo);
    }

    public Emprestimo devolverRecurso(long recursoId, String userEmail, String dataDevolucaoStr) throws Exception {
        LocalDate dataDevolucao = LocalDate.parse(dataDevolucaoStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        Optional<Emprestimo> emprestimoOpt = emprestimoRepository.findAtivoByRecursoIdAndUsuarioEmail(recursoId, userEmail);
        if (emprestimoOpt.isEmpty()) {
            throw new IllegalArgumentException("Nenhum empréstimo ativo encontrado para o Recurso ID " + recursoId + " e Usuário " + userEmail + ".");
        }

        Emprestimo emprestimo = emprestimoOpt.get();
        
        if (dataDevolucao.isBefore(emprestimo.getDataEmprestimo())) {
            throw new IllegalArgumentException("Data de devolução (" + dataDevolucaoStr + ") não pode ser anterior à data de empréstimo (" + emprestimo.getDataEmprestimo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ").");
        }

        emprestimo.setDataDevolucao(dataDevolucao);
        
        // Cálculo de multa
        long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataPrevistaDevolucao(), dataDevolucao);
        if (diasAtraso > 0) {
            double multaBase = emprestimo.getRecurso().calcularMulta(diasAtraso);
            double fatorUsuario = emprestimo.getUsuario().getFatorMulta();
            emprestimo.setMultaGerada(multaBase * fatorUsuario);
        } else {
            emprestimo.setMultaGerada(0.0);
        }
        
        // Devolve a unidade ao estoque, se for recurso físico
        if (!(emprestimo.getRecurso() instanceof MidiaDigital)) {
            recursoRepository.devolverUnidade(recursoId);
        }
        
        return emprestimoRepository.save(emprestimo);
    }
    
    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepository.findAtivos();
    }
    
    public List<Emprestimo> listarEmprestimosAtrasados() {
        LocalDate hoje = LocalDate.now();
        return emprestimoRepository.findAtivos().stream()
                .filter(e -> hoje.isAfter(e.getDataPrevistaDevolucao()))
                .collect(Collectors.toList());
    }

    public List<Emprestimo> historicoDeEmprestimosPorUsuario(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        return emprestimoRepository.findByUsuarioId(usuarioOpt.get().getId());
    }
    
    public int calcularNumeroMultasAtrasoUsuario(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        
        return (int) emprestimoRepository.findByUsuarioId(usuarioOpt.get().getId()).stream()
                .filter(e -> e.getDataDevolucao() != null && e.getMultaGerada() > 0)
                .count();
    }
}
