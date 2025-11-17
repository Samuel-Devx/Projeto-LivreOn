package br.com.livreon.function;

import br.com.livreon.conector.MySqlConnector;
import br.com.livreon.model.Biblioteca;
import br.com.livreon.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

//Cliente normal
public class ClienteNormal extends Cliente {
    //Atributos
    private static int livrosAlugado;
    private static Livro escolhido;
    private static String tipoCliente;

    //Construtor
    public ClienteNormal(String nome, String email, String senha) {
        super(nome, email, senha);
    }


    //Metodo de formatação para visibilidade do cliente
    private static String getTitulo(){
        if (escolhido == null){
            return "Nenhum Livro";
        }
        else {
            return escolhido.getTitulo();
        }

    }
    //Get quantidade
    private static  int getQuantidade (){
        return livrosAlugado;
    }
    //alugar livro local
    @Override
    public void alugarLivro(int idCliente, int idLivro, LocalDate devolução) {
            ClienteBD.aluguel(idCliente, idLivro, devolução);
    }

    //Metodo de devolver livro
    @Override
    public void devolverLivro(Biblioteca nomeBibliotca,String titulo){
        livrosAlugado--;
        if(livrosAlugado == 0){
            System.out.println("Livro devolvido com sucesso!");
        }
        else{
            Livro escolhido = nomeBibliotca.pesquisaPorTitulo(titulo);
            escolhido.setEmprestimo(false);
        }
    }
    //metodo local de mostrar a informação do cliente
    public void minhaInfo(){
        ClienteBD.info(this);
        System.out.println(this);
    }
    //metodo de formatação para exibição de livro
    public String formatacaoInfo(){
        if(escolhido == null){
            return "Nenhum Livro";
        }
        return escolhido.getTitulo();
    }


    //TO STRING
    @Override
    public String toString() {
        return " |  Nome : " + getNome() + "\n" +
                " | Livro escolhido: " + formatacaoInfo() +  "\n" +
                " | Livros alugado: " + livrosAlugado + "\n";

    }


    }

