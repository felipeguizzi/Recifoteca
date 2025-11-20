package br.recife.biblioteca.repositorio;

import br.recife.biblioteca.modelo.Emprestimo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class EmprestimoRepository {

    private final List<Emprestimo> emprestimos = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public Emprestimo save(Emprestimo emprestimo) {
        if (emprestimo.getId() == null) {
            long newId = idCounter.getAndIncrement();
            emprestimo.setId(newId);
            emprestimos.add(emprestimo);
        } else {
            // Atualizar
            int index = -1;
            for (int i = 0; i < emprestimos.size(); i++) {
                if (emprestimos.get(i).getId().equals(emprestimo.getId())) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                emprestimos.set(index, emprestimo);
            } else {
                emprestimos.add(emprestimo);
            }
        }
        return emprestimo;
    }

    public List<Emprestimo> findAll() {
        return new ArrayList<>(emprestimos);
    }
    
    public List<Emprestimo> findByUsuarioId(Long usuarioId) {
        return emprestimos.stream()
                .filter(e -> e.getUsuario().getId().equals(usuarioId))
                .collect(Collectors.toList());
    }
    
    public Optional<Emprestimo> findAtivoByRecursoIdAndUsuarioEmail(Long recursoId, String userEmail) {
        return emprestimos.stream()
                .filter(e -> e.getRecurso().getId().equals(recursoId) &&
                               e.getUsuario().getEmail().equalsIgnoreCase(userEmail) &&
                               e.getDataDevolucao() == null)
                .findFirst();
    }
    
    public List<Emprestimo> findAtivos() {
         return emprestimos.stream()
                .filter(e -> e.getDataDevolucao() == null)
                .collect(Collectors.toList());
    }
}
