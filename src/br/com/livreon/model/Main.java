package br.com.livreon.model;

import br.com.livreon.function.Cliente;
import br.com.livreon.function.ClienteNormal;
import br.com.livreon.function.ClientePlus;

import java.time.LocalDate;


public class Main {
    public static void main(String[] args) {
        Biblioteca sistemaLivros = new Biblioteca();
        Cliente neemias = new ClienteNormal("Neemias", "nemias@gmail.com");
        sistemaLivros.mostrarTodosClientes();
        neemias.alugarLivro(1, 1, LocalDate.of(2025, 12, 10));


    }
}