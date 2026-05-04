package biblioteca.dominio;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class LivroTest {

    @Test
    public void realizarEmprestimo_decrementaQuantidade() {
        Livro l = new Livro(1L, "Titulo", "Autor", "ISBN", 1);
        l.realizarEmprestimo();
        assertEquals(0, l.getQuantidadeDisponivel());
    }

    @Test
    public void realizarEmprestimo_semDisponibilidade_lancaExcecao() {
        Livro l = new Livro(1L, "Titulo", "Autor", "ISBN", 0);
        assertThrows(IllegalStateException.class, l::realizarEmprestimo);
    }

    @Test
    public void registrarDevolucao_aumentaQuantidade() {
        Livro l = new Livro(1L, "Titulo", "Autor", "ISBN", 0);
        l.registrarDevolucao();
        assertEquals(1, l.getQuantidadeDisponivel());
    }
}
