package br.recife.biblioteca.modelo;

public class Servidor extends Usuario {

    private static final int PRAZO_DIAS = 30;
    private static final double FATOR_MULTA = 0.5;

    public Servidor(Long id, String nome, String email) {
        super(id, nome, email);
    }
    
    public Servidor(String nome, String email) {
        super(nome, email);
    }

    @Override
    public int getPrazoDiasPadrao() {
        return PRAZO_DIAS;
    }

    @Override
    public double getFatorMulta() {
        return FATOR_MULTA;
    }
}
