package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Usuario;
import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;

import java.util.*;

public class UsuarioRepositorioMemoria implements PortaUsuarioRepositorio {
    private final Map<Long, Usuario> storage = new HashMap<>();

    @Override
    public void salvar(Usuario usuario) { storage.put(usuario.getId(), usuario); }

    @Override
    public Optional<Usuario> buscarPorId(Long id) { return Optional.ofNullable(storage.get(id)); }

    @Override
    public List<Usuario> listarTodos() { return new ArrayList<>(storage.values()); }

    @Override
    public void remover(Long id) { storage.remove(id); }
}
