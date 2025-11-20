package br.recife.biblioteca.modelo;

/**
 * Representa um capítulo de um livro.
 * Esta classe demonstra Composição, pois um Capítulo não existe
 * independentemente de um Livro.
 */
public class Capitulo {

    private int numero;
    private String titulo;
    private int paginas;

    public Capitulo(int numero, String titulo, int paginas) {
        if (numero <= 0) {
            throw new IllegalArgumentException("O número do capítulo deve ser positivo.");
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título do capítulo não pode ser vazio.");
        }
        if (paginas <= 0) {
            throw new IllegalArgumentException("O número de páginas deve ser positivo.");
        }
        this.numero = numero;
        this.titulo = titulo;
        this.paginas = paginas;
    }
    
    // Getters
    public int getNumero() {
        return numero;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getPaginas() {
        return paginas;
    }

    @Override
    public String toString() {
        return "Capitulo " + numero + ": '" + titulo + "' (" + paginas + " pags)";
    }
}
