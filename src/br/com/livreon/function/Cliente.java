package br.com.livreon.function;

import br.com.livreon.model.Biblioteca;
import br.com.livreon.model.Livro;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
//classe pai
public abstract class Cliente {
    //Atributos
    private String nome;
    private String email;
    private String senha;
    //metodos
    public void alugarLivro(int idCliente, int idLivro, LocalDate devolução){};
    public void devolverLivro(Biblioteca l, String titulo){};
    public void minhaInfo() {}
    public void mostrarLivrosAlugadosCliete() {}
}
