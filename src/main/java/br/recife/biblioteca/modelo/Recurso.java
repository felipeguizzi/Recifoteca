package br.recife.biblioteca.modelo;

/**
 * Classe abstrata que representa um item do acervo da biblioteca.
 * Serve como base para Livro, Revista e MidiaDigital.
 */
public abstract class Recurso {

    private Long id;
    private String titulo;
    private int anoPublicacao;

    public Recurso(Long id, String titulo, int anoPublicacao) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título não pode ser nulo ou vazio.");
        }
        if (anoPublicacao <= 0) {
            throw new IllegalArgumentException("O ano de publicação deve ser um número positivo.");
        }
        this.id = id;
        this.titulo = titulo;
        this.anoPublicacao = anoPublicacao;
    }
    
    public Recurso(String titulo, int anoPublicacao) {
        this(null, titulo, anoPublicacao);
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título não pode ser nulo ou vazio.");
        }
        this.titulo = titulo;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        if (anoPublicacao <= 0) {
            throw new IllegalArgumentException("O ano de publicação deve ser um número positivo.");
        }
        this.anoPublicacao = anoPublicacao;
    }

    /**
     * Retorna uma descrição textual do recurso.
     * @return A descrição do recurso.
     */
    public String getDescricao() {
        return "ID: " + id + ", Título: '" + titulo + "', Ano: " + anoPublicacao;
    }

    /**
     * Calcula o valor da multa com base nos dias de atraso.
     * Este método é abstrato e deve ser implementado pelas subclasses.
     * @param diasAtraso O número de dias de atraso na devolução.
     * @return O valor da multa a ser paga.
     */
    public abstract double calcularMulta(long diasAtraso);

    @Override
    public String toString() {
        return getDescricao();
    }
}
