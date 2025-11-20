package br.recife.biblioteca.modelo;

import java.time.LocalDate;

/**
 * Interface para objetos que podem ser emprestados.
 * Define o contrato para operações de empréstimo e devolução.
 */
public interface Emprestavel {
    
    /**
     * Tenta emprestar este item a um usuário.
     * @param u O usuário que está pegando o item emprestado.
     * @return true se o empréstimo foi bem-sucedido, false caso contrário.
     */
    boolean emprestar(Usuario u);
    
    /**
     * Devolve este item.
     */
    void devolver();
    
    /**
     * Retorna a data prevista para a devolução deste item.
     * @return A data de devolução prevista, ou null se não estiver emprestado.
     */
    LocalDate getDataPrevistaDevolucao();
}
