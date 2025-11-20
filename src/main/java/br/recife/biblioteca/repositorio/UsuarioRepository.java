package br.recife.biblioteca.repositorio;

import br.recife.biblioteca.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class UsuarioRepository {

    private final List<Usuario> usuarios = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public Usuario save(Usuario usuario) {
        if (usuario.getId() == null) {
            // Novo usuário, gerar ID
            long newId = idCounter.getAndIncrement();
            usuario.setId(newId);
            usuarios.add(usuario);
        } else {
            // Atualizar usuário existente
            int index = -1;
            for (int i = 0; i < usuarios.size(); i++) {
                if (usuarios.get(i).getId().equals(usuario.getId())) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                usuarios.set(index, usuario);
            } else {
                // Se não encontrar, adiciona como novo (com o ID que já tinha)
                usuarios.add(usuario);
            }
        }
        return usuario;
    }

    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios); // Retorna cópia para proteger encapsulamento
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
    
    public Optional<Usuario> findById(Long id) {
        return usuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public boolean deleteByEmail(String email) {
        return usuarios.removeIf(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
