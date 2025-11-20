package br.recife.biblioteca.modelo;

/**
 * Representa um usuário base da biblioteca.
 * Esta classe é abstrata e define os atributos e comportamentos comuns
 * a todos os tipos de usuários.
 */
public abstract class Usuario {

    private Long id;
    private String nome;
    private String email;

    public Usuario(Long id, String nome, String email) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("O e-mail é inválido.");
        }
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public Usuario(String nome, String email) {
        this(null, nome, email);
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
        }
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("O e-mail é inválido.");
        }
        this.email = email;
    }

    /**
     * Define o prazo padrão de empréstimo em dias para este tipo de usuário.
     * @return O número de dias.
     */
    public abstract int getPrazoDiasPadrao();

    /**
     * Define o fator de multa para este tipo de usuário.
     * Usado para calcular o valor final da multa por atraso.
     * @return O fator multiplicador da multa.
     */
    public abstract double getFatorMulta();

    @Override
    public String toString() {
        return "Usuario{"
                + "id=" + id + 
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", tipo='" + this.getClass().getSimpleName() + "'" +
                '}';
    }
}
