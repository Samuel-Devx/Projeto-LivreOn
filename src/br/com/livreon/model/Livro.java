package br.com.livreon.model;

import lombok.Data;

@Data
public class Livro {
    private String titulo;
    private String autor;
    private int ISBN;
    private  Boolean emprestimo;
    //Construtor
    public Livro(String titulo, String autor, int ISBN) {
        this.titulo = titulo;
        this.autor = autor;
        this.ISBN = ISBN;
        emprestimo = false;
    }
    //Metodo de formatar o resultado
    public String resultado (){
        if (emprestimo == true ){
            return "Livro Alugado";
        }
        else {
            return "Livro Disponivel";
        }
    }
    //to string
    public String toString() {
        return " | Titulo: " + titulo + "\n" +
                " | Autor: " + autor+  "\n" +
                " | ISBN: " + ISBN +  "\n" +
                " | Status : " + resultado()  + "\n"  ;
    }
}
