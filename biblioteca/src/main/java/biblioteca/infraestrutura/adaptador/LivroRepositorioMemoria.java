package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Livro;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;

import java.util.*;

public class LivroRepositorioMemoria implements PortaLivroRepositorio {
    private final Map<Long, Livro> storage = new HashMap<>();

    @Override
    public void salvar(Livro livro) { storage.put(livro.getId(), livro); }

    @Override
    public Optional<Livro> buscarPorId(Long id) { return Optional.ofNullable(storage.get(id)); }

    @Override
    public List<Livro> listarTodos() { return new ArrayList<>(storage.values()); }

    @Override
    public void remover(Long id) { storage.remove(id); }
}
