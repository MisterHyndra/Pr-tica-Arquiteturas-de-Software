package biblioteca.dominio;

import java.time.LocalDate;

public class Emprestimo {
    private Long id;
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataRetirada;
    private LocalDate dataPrevistaDevolucao;
    private SituacaoEmprestimo situacao;

    public Emprestimo(Long id, Livro livro, Usuario usuario, LocalDate dataRetirada, LocalDate dataPrevistaDevolucao) {
        this.id = id;
        this.livro = livro;
        this.usuario = usuario;
        this.dataRetirada = dataRetirada;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.situacao = SituacaoEmprestimo.ATIVO;
    }

    public Long getId() { return id; }
    public Livro getLivro() { return livro; }
    public Usuario getUsuario() { return usuario; }
    public LocalDate getDataRetirada() { return dataRetirada; }
    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public SituacaoEmprestimo getSituacao() { return situacao; }

    public void registrarDevolucao(LocalDate dataDevolucao) {
        if (situacao != SituacaoEmprestimo.ATIVO) return;
        this.situacao = dataDevolucao.isAfter(dataPrevistaDevolucao) ? SituacaoEmprestimo.ATRASADO : SituacaoEmprestimo.DEVOLVIDO;
        livro.registrarDevolucao();
    }
}
