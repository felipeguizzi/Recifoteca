package br.recife.biblioteca.modelo;

public class Visitante extends Usuario {

    private static final int PRAZO_DIAS = 7;
    private static final double FATOR_MULTA = 1.5;

    public Visitante(Long id, String nome, String email) {
        super(id, nome, email);
    }
    
    public Visitante(String nome, String email) {
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
