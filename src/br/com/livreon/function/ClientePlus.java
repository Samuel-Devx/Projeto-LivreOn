package br.com.livreon.function;
import br.com.livreon.model.Biblioteca;
import br.com.livreon.model.Livro;

import java.util.ArrayList;
import java.util.List;

public class ClientePlus extends Cliente{
    private int livrosAlugados;
    private List<Livro> listaLivro = new ArrayList<>();
    private String tipoCliente;
    //Construtor
    public ClientePlus(String nome, String email) {
        super(nome, email);
        tipoCliente = "Cliente Plus";
        ClienteNormal.adiocionarAoBanco(this);
    }

    public void alugarLivro(Biblioteca nomeBiblioteca, String titulo){
        livrosAlugados++;
        Livro escolhido = nomeBiblioteca.pesquisaPorTitulo(titulo);
        escolhido.setEmprestimo(true);
        listaLivro.add(escolhido);
    };
    public void devolverLivro(Biblioteca nomeBiblioteca, String titulo){

        if(listaLivro.contains(listaLivro)){
            livrosAlugados--;
            Livro escolhido = nomeBiblioteca.pesquisaPorTitulo(titulo);
            escolhido.setEmprestimo(false);
            listaLivro.remove(escolhido);
        }
        else {
            System.out.printf("Você não o livro : %s %n", titulo);
        }

    };
    public void minhaInfo() {
        System.out.println(this);
    }
    public void mostrarLivrosAlugadosCliete(){
        listaLivro.stream()
                .forEach(System.out::println);
    }


    public void formatacaoInfo(){
        if(listaLivro == null){
            System.out.println("Nenhum Livro");
        }
    }


    public String toString() {
        return " |  Nome : " + getNome() + "\n" +
                " | Livros alugado: " + livrosAlugados + "\n";

    }



}
