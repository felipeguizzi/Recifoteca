package br.recife.biblioteca.modelo;

public class MidiaDigital extends Recurso {

    private double tamanhoMB;
    private String formatoArquivo;
    private static final double MULTA_FIXA = 5.00;

    public MidiaDigital(Long id, String titulo, int anoPublicacao, double tamanhoMB, String formatoArquivo) {
        super(id, titulo, anoPublicacao);
        this.tamanhoMB = tamanhoMB;
        this.formatoArquivo = formatoArquivo;
    }
    
    public MidiaDigital(String titulo, int anoPublicacao, double tamanhoMB, String formatoArquivo) {
        super(titulo, anoPublicacao);
        this.tamanhoMB = tamanhoMB;
        this.formatoArquivo = formatoArquivo;
    }

    public double getTamanhoMB() {
        return tamanhoMB;
    }

    public String getFormatoArquivo() {
        return formatoArquivo;
    }

    @Override
    public double calcularMulta(long diasAtraso) {
        // Multa fixa se houver qualquer atraso
        return diasAtraso > 0 ? MULTA_FIXA : 0.0;
    }

    @Override
    public String getDescricao() {
        return super.getDescricao() + ", Tipo: Mídia Digital, Formato: " + formatoArquivo + ", Tamanho: " + tamanhoMB + "MB";
    }
}
