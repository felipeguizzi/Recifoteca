package br.recife.biblioteca.repositorio;

import br.recife.biblioteca.modelo.Recurso;
import br.recife.biblioteca.modelo.Livro;
import br.recife.biblioteca.modelo.MidiaDigital;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class RecursoRepository {

    private final List<Recurso> recursos = new ArrayList<>();
    // Mapa para simular a tabela de estoque: <id_recurso, [total, disponivel]>
    private final Map<Long, int[]> estoque = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public Recurso save(Recurso recurso) {
        if (recurso.getId() == null) {
            long newId = idCounter.getAndIncrement();
            recurso.setId(newId);
            recursos.add(recurso);
            // Mídias digitais não têm estoque físico
            if (!(recurso instanceof MidiaDigital)) {
                 estoque.put(newId, new int[]{0, 0});
            }
        } else {
            // Atualizar
            int index = -1;
            for (int i = 0; i < recursos.size(); i++) {
                if (recursos.get(i).getId().equals(recurso.getId())) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                recursos.set(index, recurso);
            } else {
                recursos.add(recurso);
            }
        }
        return recurso;
    }

    public List<Recurso> findAll() {
        return new ArrayList<>(recursos);
    }
    
    public Optional<Recurso> findById(Long id) {
        return recursos.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    public boolean deleteById(Long id) {
        estoque.remove(id);
        return recursos.removeIf(r -> r.getId().equals(id));
    }
    
    // --- Métodos de Estoque ---

    public void adicionarUnidades(Long id, int quantidade) {
        if (!estoque.containsKey(id) || quantidade <= 0) return;
        
        findById(id).ifPresent(recurso -> {
            if (recurso instanceof MidiaDigital) {
                 throw new UnsupportedOperationException("Mídia digital não possui estoque físico.");
            }
            int[] stock = estoque.get(id);
            stock[0] += quantidade; // total
            stock[1] += quantidade; // disponível
        });
    }

    public boolean removerUnidades(Long id, int quantidade) {
         if (!estoque.containsKey(id) || quantidade <= 0) return false;

         Optional<Recurso> recursoOpt = findById(id);
         if (recursoOpt.isPresent() && recursoOpt.get() instanceof MidiaDigital) {
             throw new UnsupportedOperationException("Mídia digital não possui estoque físico.");
         }

        int[] stock = estoque.get(id);
        if (stock[1] >= quantidade && (stock[0] - quantidade) >= (stock[0] - stock[1])) {
            stock[0] -= quantidade; // total
            stock[1] -= quantidade; // disponível
            return true;
        }
        return false;
    }
    
    public int getEstoqueDisponivel(Long id) {
        if (!estoque.containsKey(id)) return 0;
        return estoque.get(id)[1];
    }
    
     public int getEstoqueTotal(Long id) {
        if (!estoque.containsKey(id)) return 0;
        return estoque.get(id)[0];
    }

    public boolean emprestarUnidade(Long id) {
        if (!estoque.containsKey(id)) {
            // Se não está no mapa de estoque, pode ser uma mídia digital
             return findById(id).map(r -> r instanceof MidiaDigital).orElse(false);
        }
        int[] stock = estoque.get(id);
        if (stock[1] > 0) {
            stock[1]--; // Decrementa disponível
            return true;
        }
        return false;
    }

    public void devolverUnidade(Long id) {
        if (!estoque.containsKey(id)) return; // Mídia digital não precisa ter estoque devolvido

        int[] stock = estoque.get(id);
        if (stock[1] < stock[0]) {
            stock[1]++; // Incrementa disponível
        }
    }
}
