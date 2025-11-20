package br.recife.biblioteca.modelo;

public class Aluno extends Usuario {

    private static final int PRAZO_DIAS = 15;
    private static final double FATOR_MULTA = 1.0;

    public Aluno(Long id, String nome, String email) {
        super(id, nome, email);
    }
    
    public Aluno(String nome, String email) {
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
