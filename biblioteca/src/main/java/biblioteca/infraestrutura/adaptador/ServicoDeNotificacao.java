package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.evento.EventBus;

import java.util.function.Consumer;

public class ServicoDeNotificacao {
    private final PortaNotificacaoWrapper wrapper;

    public ServicoDeNotificacao(PortaNotificacaoWrapper wrapper) { this.wrapper = wrapper; }

    public Consumer<EmprestimoRealizadoEvento> handler() {
        return evt -> {
            // In a real system we would lookup usuario and emprestimo; here we rely on wrapper to provide lookup
            wrapper.notificarEmprestimo(evt.usuarioId(), evt.emprestimoId());
        };
    }

    public interface PortaNotificacaoWrapper {
        void notificarEmprestimo(Long usuarioId, Long emprestimoId);
    }
}
