package br.com.livreon.model;

import br.com.livreon.function.Cliente;
import br.com.livreon.function.ClienteNormal;
import br.com.livreon.function.ClientePlus;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Biblioteca sistemaLivros = new Biblioteca();
        Cliente neemias = new ClienteNormal("Neemias", "nemias@gmail.com");
        Cliente marcos = new ClientePlus("Marcos", "marcos123@gmail.com");
        sistemaLivros.cadastrarLivro("Principe das trevas", "Barlon", 12323323);
        sistemaLivros.cadastrarLivro("Marba", "chiro", 321321321);
        sistemaLivros.cadastrarLivro("Show", "liro", 1231234);

        marcos.alugarLivro( "Marba");
        marcos.alugarLivro( "Principe das trevas");
        marcos.devolverLivro(sistemaLivros,"Show");

        marcos.minhaInfo();
        marcos.mostrarLivrosAlugadosCliete();

        sistemaLivros.mostrarTodosClientes();



    }
}