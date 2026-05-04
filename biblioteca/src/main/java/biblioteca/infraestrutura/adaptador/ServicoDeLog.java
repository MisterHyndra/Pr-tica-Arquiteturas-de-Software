package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class ServicoDeLog {
    private final FileWriter writer;

    public ServicoDeLog(String caminho) throws IOException {
        this.writer = new FileWriter(caminho, true);
    }

    public Consumer<EmprestimoRealizadoEvento> emprestimoHandler() {
        return evt -> log(String.format("Emprestimo realizado id=%d usuario=%d livro=%d", evt.emprestimoId(), evt.usuarioId(), evt.livroId()));
    }

    public Consumer<DevolucaoRegistradaEvento> devolucaoHandler() {
        return evt -> log(String.format("Devolucao id=%d data=%s comAtraso=%s", evt.emprestimoId(), evt.dataDevolucao(), evt.comAtraso()));
    }

    private synchronized void log(String mensagem) {
        try (PrintWriter pw = new PrintWriter(writer)) {
            String ts = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            pw.println(ts + " - " + mensagem);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
