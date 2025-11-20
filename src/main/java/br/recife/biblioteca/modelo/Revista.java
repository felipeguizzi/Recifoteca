package br.recife.biblioteca.modelo;

public class Revista extends Recurso {

    private int edicao;
    private String periodicidade;
    private static final double MULTA_POR_DIA = 1.00;
    private static final int DIAS_CARENCIA = 2;

    public Revista(Long id, String titulo, int anoPublicacao, int edicao, String periodicidade) {
        super(id, titulo, anoPublicacao);
        this.edicao = edicao;
        this.periodicidade = periodicidade;
    }
    
    public Revista(String titulo, int anoPublicacao, int edicao, String periodicidade) {
        super(titulo, anoPublicacao);
        this.edicao = edicao;
        this.periodicidade = periodicidade;
    }

    public int getEdicao() {
        return edicao;
    }

    public String getPeriodicidade() {
        return periodicidade;
    }

    @Override
    public double calcularMulta(long diasAtraso) {
        if (diasAtraso <= DIAS_CARENCIA) {
            return 0.0;
        }
        return (diasAtraso - DIAS_CARENCIA) * MULTA_POR_DIA;
    }

    @Override
    public String getDescricao() {
        return super.getDescricao() + ", Tipo: Revista, Edição: " + edicao;
    }
}
