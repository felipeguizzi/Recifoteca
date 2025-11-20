package br.recife.biblioteca.modelo;

import java.time.LocalDate;

/**
 * Representa o ato de um empréstimo de um recurso por um usuário.
 * Esta classe demonstra Agregação, pois existe em associação com
 * Usuario e Recurso, mas eles têm ciclos de vida independentes.
 */
public class Emprestimo {

    private Long id;
    private Recurso recurso; // Agregação
    private Usuario usuario; // Agregação
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;
    private double multaGerada;

    public Emprestimo(Recurso recurso, Usuario usuario, LocalDate dataEmprestimo, LocalDate dataPrevistaDevolucao) {
        this.recurso = recurso;
        this.usuario = usuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }
    
    public double getMultaGerada() {
        return multaGerada;
    }
    
    public void setMultaGerada(double multa) {
        this.multaGerada = multa;
    }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", recurso=" + recurso.getTitulo() +
                ", usuario=" + usuario.getNome() +
                ", dataEmprestimo=" + dataEmprestimo +
                ", dataPrevistaDevolucao=" + dataPrevistaDevolucao +
                ", dataDevolucao=" + (dataDevolucao != null ? dataDevolucao : "Pendente") +
                '}';
    }
}
