package br.com.livreon.AppCliente;

import br.com.livreon.function.Cliente;
import br.com.livreon.function.ClienteBD;
import br.com.livreon.function.ClientePlus;
import lombok.Data;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;

import static java.time.LocalDate.*;

public class AppCliente {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        boolean estage1 = false;
        Cliente cliente = null;
        int opcao1;
        int opcao2;



        do {
            System.out.println("======Login======");
            System.out.println("1 - Logar  ");
            System.out.println("2 - Criar conta ");
            System.out.println("3 - Sair ");
            System.out.print("Digite ums opção: ");
            opcao1 = teclado.nextInt();
            switch (opcao1) {
                case 1:
                    cliente = ClienteBD.login(teclado);
                    estage1 = true;
                    break;
                case 2:
                    cliente = ClienteBD.criarConta(teclado);
                    estage1 = true;
                    break;
                case 3:
                    System.out.println("Saindo...");
                    estage1 = true;
                    break;
                default:
                    System.out.println("Opção inválida ");
                    estage1 = true;

            }
        } while(!estage1) ;

        if(cliente != null) {
            do {
                //Ações
                System.out.println("======Cliente=====");
                System.out.println("1 - Minhas Informações ");
                System.out.println("2 - Alugar livros ");
                System.out.println("3 - Devolver livros ");
                System.out.println("4 - Mostra Catálogo ");
                System.out.println("5 - Livros alugados ");
                System.out.println("6 - Atualizar Plano");
                System.out.println("7 - Sair ");
                System.out.print("Digite uma opção: ");
                opcao2 = teclado.nextInt();
                switch (opcao2) {
                    case 1:
                        ClienteBD.info(cliente);
                        break;
                    case 2:

                        System.out.println("Digite nome do livro: ");
                        String nomeDoLivro = teclado.next();
                        System.out.print("Digite a data de devolução: (yyyy/mm/dd) ");
                        String date = teclado.next();
                        LocalDate data = parse(date);
                        int idCliente = ClienteBD.pesquisaId(cliente);
                        int idLivro = ClienteBD.pesquisaTitulo(nomeDoLivro);
                        if(ClienteBD.pesquisaStaus(idCliente) == false){
                            ClienteBD.aluguel(idCliente, idLivro, data);
                            break;
                        }
                        else {
                            System.out.println("Livro alugado ou indisponivel");
                            break;
                        }

                    case 3:
                        System.out.print("Nome do livro que quer devolver: ");
                        String resposta = teclado.next();
                        int idLivroDevolucao = ClienteBD.pesquisaTitulo(resposta);
                            ClienteBD.devolver(ClienteBD.pesquisaId(cliente), idLivroDevolucao);
                        break;
                    case 4:
                        ClienteBD.mostrarLivros();
                    case 5:
                        ClienteBD.verLivrosAlugados(ClienteBD.pesquisaId(cliente));
                        break;
                    case 6:
                        cliente = new ClientePlus(cliente.getNome(), cliente.getEmail(), cliente.getSenha());
                        ClienteBD.atualizarPlano(ClienteBD.pesquisaId(cliente));
                        break;
                    case 7:
                        System.out.println("Encerrado! ");
                        break;

                }
            } while (opcao2 != 7);

        }
    }
}


