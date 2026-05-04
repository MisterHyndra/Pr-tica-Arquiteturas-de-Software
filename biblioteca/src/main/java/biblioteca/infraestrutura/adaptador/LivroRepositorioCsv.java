package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Livro;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;

import java.io.*;
import java.util.*;

public class LivroRepositorioCsv implements PortaLivroRepositorio {
    private final File arquivo;
    private final Map<Long, Livro> cache = new HashMap<>();

    public LivroRepositorioCsv(String caminho) {
        this.arquivo = new File(caminho);
        carregar();
    }

    private void carregar() {
        if (!arquivo.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(arquivo))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;
                Long id = Long.parseLong(parts[0]);
                String titulo = parts[1];
                String autor = parts[2];
                String isbn = parts[3];
                int qtd = Integer.parseInt(parts[4]);
                cache.put(id, new Livro(id, titulo, autor, isbn, qtd));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void persistir() {
        try (PrintWriter w = new PrintWriter(new FileWriter(arquivo))) {
            for (Livro l : cache.values()) {
                w.printf("%d,%s,%s,%s,%d%n", l.getId(), escape(l.getTitulo()), escape(l.getAutor()), l.getIsbn(), l.getQuantidadeDisponivel());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String escape(String s) { return s.replace("\n", " ").replace(",", ";"); }

    @Override
    public void salvar(Livro livro) { cache.put(livro.getId(), livro); persistir(); }

    @Override
    public Optional<Livro> buscarPorId(Long id) { return Optional.ofNullable(cache.get(id)); }

    @Override
    public List<Livro> listarTodos() { return new ArrayList<>(cache.values()); }

    @Override
    public void remover(Long id) { cache.remove(id); persistir(); }
}
