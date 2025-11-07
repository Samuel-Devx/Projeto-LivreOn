package br.com.livreon.function;

import br.com.livreon.conector.MySqlConnector;
import br.com.livreon.model.Biblioteca;
import br.com.livreon.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
//Cliente normal
public class ClienteNormal extends Cliente {
    //Atributos
    private static int livrosAlugado;
    private static Livro escolhido;
    private static String tipoCliente;

    //Construtor
    public ClienteNormal(String nome, String email) {
        super(nome, email);

        tipoCliente = "Cliente normal";
        adiocionarAoBanco(this);
    }

    //Metodo que adiciona o cliente dentro do encapsulado
    private static void adcionarClienteNomal(String nome, String email){
        Cliente c = new ClienteNormal(nome, email);
        adiocionarAoBanco(c);
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
    //Metodo de alugar livro atualizando banco de dados
    public void alugarLivroBD(String titulo) {
        ClienteBD.alugarLivro(titulo,this);
    }
    //alugar livro local
    @Override
    public void alugarLivro(String titulo ) {
        ClienteBD.alugarLivro(titulo,this);
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
    //Metodo encapsulado para adicionar o cliente no DB
    protected static void adiocionarAoBanco(Cliente c){
            String sql = "INSERT INTO clientes (nome, email, titulo_cliente , Qtd_livro , tipo_cliente) VALUES (?, ?, ?, ?, ?)";
            try(Connection conector = MySqlConnector.getConnetion();
                PreparedStatement preparo = conector.prepareStatement(sql)) {

                preparo.setString(1, c.getNome());
                preparo.setString(2, c.getEmail());
                preparo.setNull(3, Types.VARCHAR);
                preparo.setInt(4, 0);
                preparo.setString( 5,"Cliente normal");

                int linhasAfetadas = preparo.executeUpdate();
                if(linhasAfetadas > 0){
                    System.out.println("Cliente adicionado com sucesso!");
                }
            } catch (SQLException e) {
                System.out.println("ERRO EM ADD O CLIENTE DENTRO DO BANCO DE DADOS" + e.getMessage());
            }
        }

    }

