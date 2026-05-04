package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;

import java.util.*;

public class EmprestimoRepositorioMemoria implements PortaEmprestimoRepositorio {
    private final Map<Long, Emprestimo> storage = new HashMap<>();

    @Override
    public void salvar(Emprestimo emprestimo) { storage.put(emprestimo.getId(), emprestimo); }

    @Override
    public Optional<Emprestimo> buscarPorId(Long id) { return Optional.ofNullable(storage.get(id)); }

    @Override
    public List<Emprestimo> listarTodos() { return new ArrayList<>(storage.values()); }

    @Override
    public void remover(Long id) { storage.remove(id); }
}
