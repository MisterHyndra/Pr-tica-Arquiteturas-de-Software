package biblioteca.dominio;

public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private SituacaoUsuario situacao;

    public Usuario(Long id, String nome, String email, SituacaoUsuario situacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.situacao = situacao;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public SituacaoUsuario getSituacao() { return situacao; }

    public void suspender() { this.situacao = SituacaoUsuario.SUSPENSO; }
    public void ativar() { this.situacao = SituacaoUsuario.ATIVO; }
}
