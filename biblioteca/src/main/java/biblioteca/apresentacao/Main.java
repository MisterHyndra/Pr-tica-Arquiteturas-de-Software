package biblioteca.apresentacao;

import biblioteca.dominio.Livro;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.evento.EventBus;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;
import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;
import biblioteca.dominio.servico.EmprestimoServico;
import biblioteca.infraestrutura.adaptador.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Cria EventBuses
        EventBus<EmprestimoRealizadoEvento> busEmprestimo = new EventBus<>();
        EventBus<DevolucaoRegistradaEvento> busDevolucao = new EventBus<>();

        // Adaptadores em memória
        PortaLivroRepositorio livroRepo = new LivroRepositorioMemoria();
        PortaUsuarioRepositorio usuarioRepo = new UsuarioRepositorioMemoria();
        PortaEmprestimoRepositorio emprestimoRepo = new EmprestimoRepositorioMemoria();

        // Serviço de empréstimo
        EmprestimoServico serv = new EmprestimoServico(livroRepo, usuarioRepo, emprestimoRepo, busEmprestimo, busDevolucao);

        // Handlers
        NotificacaoConsole notifConsole = new NotificacaoConsole();
        ServicoDeLog log = new ServicoDeLog("biblioteca.log");

        // Registrar handlers
        busEmprestimo.assinar((EmprestimoRealizadoEvento e) -> {
            // notificar: precisamos buscar usuario e emprestimo
            usuarioRepo.buscarPorId(e.usuarioId()).ifPresent(u -> emprestimoRepo.buscarPorId(e.emprestimoId()).ifPresent(emp -> notifConsole.notificarAtraso(u, emp)));
            log.emprestimoHandler().accept(e);
        });

        busDevolucao.assinar((DevolucaoRegistradaEvento e) -> {
            log.devolucaoHandler().accept(e);
        });

        // Fluxo demonstrativo com adaptadores em memória
        Livro l1 = new Livro(1L, "Clean Code", "Robert C. Martin", "978-0132350884", 2);
        Usuario u1 = new Usuario(1L, "Alice", "alice@example.com", biblioteca.dominio.SituacaoUsuario.ATIVO);
        livroRepo.salvar(l1);
        usuarioRepo.salvar(u1);

        var emprestimo = serv.realizarEmprestimo(u1.getId(), l1.getId());
        System.out.println("Emprestimo realizado id=" + emprestimo.getId());

        serv.registrarDevolucao(emprestimo.getId());
        System.out.println("Devolucao registrada para id=" + emprestimo.getId());

        // Agora demonstra troca de adaptador para CSV
        PortaLivroRepositorio livroRepoCsv = new LivroRepositorioCsv("livros.csv");
        PortaUsuarioRepositorio usuarioRepoCsv = new UsuarioRepositorioMemoria();
        PortaEmprestimoRepositorio emprestimoRepoCsv = new EmprestimoRepositorioMemoria();

        EmprestimoServico servCsv = new EmprestimoServico(livroRepoCsv, usuarioRepoCsv, emprestimoRepoCsv, busEmprestimo, busDevolucao);

        // Reusar handlers já registrados no bus
        Livro l2 = new Livro(2L, "Refactoring", "Martin Fowler", "978-0201485677", 1);
        livroRepoCsv.salvar(l2);
        Usuario u2 = new Usuario(2L, "Bob", "bob@example.com", biblioteca.dominio.SituacaoUsuario.ATIVO);
        usuarioRepoCsv.salvar(u2);

        var e2 = servCsv.realizarEmprestimo(u2.getId(), l2.getId());
        System.out.println("Emprestimo (CSV) id=" + e2.getId());
    }
}
