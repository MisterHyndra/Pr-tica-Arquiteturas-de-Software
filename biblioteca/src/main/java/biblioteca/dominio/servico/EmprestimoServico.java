package biblioteca.dominio.servico;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.evento.EventBus;
import biblioteca.dominio.porta.entrada.PortaEmprestimo;
import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EmprestimoServico implements PortaEmprestimo {
    private final PortaLivroRepositorio livroRepo;
    private final PortaUsuarioRepositorio usuarioRepo;
    private final PortaEmprestimoRepositorio emprestimoRepo;
    private final EventBus<EmprestimoRealizadoEvento> eventoBusEmprestimo;
    private final EventBus<DevolucaoRegistradaEvento> eventoBusDevolucao;

    public EmprestimoServico(PortaLivroRepositorio livroRepo,
                             PortaUsuarioRepositorio usuarioRepo,
                             PortaEmprestimoRepositorio emprestimoRepo,
                             EventBus<EmprestimoRealizadoEvento> eventoBusEmprestimo,
                             EventBus<DevolucaoRegistradaEvento> eventoBusDevolucao) {
        this.livroRepo = livroRepo;
        this.usuarioRepo = usuarioRepo;
        this.emprestimoRepo = emprestimoRepo;
        this.eventoBusEmprestimo = eventoBusEmprestimo;
        this.eventoBusDevolucao = eventoBusDevolucao;
    }

    @Override
    public Emprestimo realizarEmprestimo(Long usuarioId, Long livroId) {
        Usuario usuario = usuarioRepo.buscarPorId(usuarioId).orElseThrow(() -> new IllegalArgumentException("Usuario nao encontrado"));
        if (usuario.getSituacao() != biblioteca.dominio.SituacaoUsuario.ATIVO) {
            throw new IllegalStateException("Usuario suspenso");
        }
        Livro livro = livroRepo.buscarPorId(livroId).orElseThrow(() -> new IllegalArgumentException("Livro nao encontrado"));
        livro.realizarEmprestimo();
        livroRepo.salvar(livro);

        Long id = System.currentTimeMillis();
        LocalDate hoje = LocalDate.now();
        Emprestimo e = new Emprestimo(id, livro, usuario, hoje, hoje.plusDays(14));
        emprestimoRepo.salvar(e);

        eventoBusEmprestimo.publicar(new EmprestimoRealizadoEvento(e.getId(), usuario.getId(), livro.getId(), e.getDataRetirada()));
        return e;
    }

    @Override
    public void registrarDevolucao(Long emprestimoId) {
        Emprestimo e = emprestimoRepo.buscarPorId(emprestimoId).orElseThrow(() -> new IllegalArgumentException("Emprestimo nao encontrado"));
        LocalDate hoje = LocalDate.now();
        e.registrarDevolucao(hoje);
        emprestimoRepo.salvar(e);
        boolean comAtraso = e.getSituacao() == biblioteca.dominio.SituacaoEmprestimo.ATRASADO;
        eventoBusDevolucao.publicar(new DevolucaoRegistradaEvento(e.getId(), hoje, comAtraso));
    }

    @Override
    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepo.listarTodos().stream().filter(x -> x.getSituacao() == biblioteca.dominio.SituacaoEmprestimo.ATIVO).collect(Collectors.toList());
    }

    @Override
    public List<Emprestimo> verificarAtrasos() {
        return emprestimoRepo.listarTodos().stream().filter(x -> x.getSituacao() == biblioteca.dominio.SituacaoEmprestimo.ATRASADO).collect(Collectors.toList());
    }
}
