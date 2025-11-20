package br.recife.biblioteca.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Livro extends Recurso {

    private final List<Capitulo> capitulos = new ArrayList<>();
    private static final double MULTA_POR_DIA = 2.50;

    public Livro(Long id, String titulo, int anoPublicacao) {
        super(id, titulo, anoPublicacao);
    }
    
    public Livro(String titulo, int anoPublicacao) {
        super(titulo, anoPublicacao);
    }

    // Composição: Livro gerencia seus capítulos
    public void adicionarCapitulo(Capitulo capitulo) {
        if (capitulo != null) {
            this.capitulos.add(capitulo);
        }
    }

    public List<Capitulo> getCapitulos() {
        // Retorna uma cópia imutável para proteger o encapsulamento
        return Collections.unmodifiableList(capitulos);
    }

    @Override
    public double calcularMulta(long diasAtraso) {
        if (diasAtraso <= 0) {
            return 0.0;
        }
        return diasAtraso * MULTA_POR_DIA;
    }

    @Override
    public String getDescricao() {
        return super.getDescricao() + ", Tipo: Livro, Capítulos: " + capitulos.size();
    }
}
