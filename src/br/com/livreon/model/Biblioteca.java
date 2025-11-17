package br.com.livreon.model;

import br.com.livreon.conector.MySqlConnector;
import br.com.livreon.function.Cliente;
import lombok.Data;

import javax.crypto.spec.PSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Classe da bblioteca/livraria
public class Biblioteca {
    //Atributos
    private List<Livro> conjutoDeLivro;
    //Criando a coleção
    public Biblioteca (){
        conjutoDeLivro = new ArrayList<>();
    }

    //Metodo que cadastra o livro e já adciona ele no DB
    public void cadastrarLivro(String titulo, String autor, int codico){
        Livro l = new Livro(titulo, autor, codico);
        conjutoDeLivro.add(l);
        //----Sql ADD
        String sql = "INSERT INTO livros (titulo, autor, codico, status) VALUES (?, ?, ?, ?)";
        try (Connection coon = MySqlConnector.getConnetion();
             PreparedStatement st = coon.prepareStatement(sql)) {
            st.setString(1, titulo);
            st.setString(2, autor);
            st.setInt(3, codico);
            st.setBoolean(4, l.getEmprestimo());
            int linhasAfetadas = st.executeUpdate();
            if(linhasAfetadas > 0){
                System.out.printf("Livro %s adicionado %n", titulo);
            }
        } catch (SQLException e) {
            System.out.println("ERRO em adicionar " + e.getMessage());
        }
    }

    //Classe para formatar boolean para usuario (encapsulada)
    public static String formatação(Boolean valor){
        if(valor == false)
        {
            return " -Disponivel!";
        }
        else{
            return " -Alugado!";
        }
    }
    //Classe que busca no DB todos os livros que estã disponiveis
    public void  mostrarLivrosDisponiveis(){
        //--- sql
        String sql = "SELECT * FROM livros WHERE status = 0";
        try(Connection coon = MySqlConnector.getConnetion();
            PreparedStatement tm = coon.prepareStatement(sql);
            ResultSet set = tm.executeQuery();) {
            while (set.next()){
                String titulo = set.getString("titulo");
                String autor = set.getString("autor");
                int codico = set.getInt("codico");
                boolean status = set.getBoolean("status");

                System.out.println("Titulo: " + titulo +
                        "| Autor: " + autor +
                        "| Código: " + codico +
                        "| Status: " + formatação(status));
            }
        } catch (SQLException e) {
            System.out.println("erro em mostrar" + e.getMessage());
        }
        //IMPORTANTEEEEE
        if (!conjutoDeLivro.isEmpty()){
            conjutoDeLivro.stream()
                    .filter(l -> l.getEmprestimo() == false)
                    .forEach(System.out::println);
        }
        else {
            System.out.println("Não exixte livros cadastrados");
        }
    }

    //Metodo que mostra todos os livros que já foram alugado em pesquisa com o DB
    public void mostrarLivrosAlugados(){
            //--- sql
            String sql = "SELECT * FROM livros WHERE status = 1";
            try(Connection coon = MySqlConnector.getConnetion();
                PreparedStatement tm = coon.prepareStatement(sql);
                ResultSet set = tm.executeQuery();) {
                while (set.next()){
                    String titulo = set.getString("titulo");
                    String autor = set.getString("autor");
                    int codico = set.getInt("codico");
                    boolean status = set.getBoolean("status");
                    System.out.println("Titulo: " + titulo +
                            "| Autor: " + autor +
                            "| Código: " + codico +
                            "| Status: " + formatação(status));
            }
        } catch (SQLException e) {
            System.out.println("erro em mostrar" + e.getMessage());
        }

    }

    //Analisa o codico do livro e deleta do DB
    public void excluirLivro(int codico){
        List<Livro> listaDeletada = new ArrayList<>();

            String sql = "DELETE FROM livros WHERE codico = ?";
            try(Connection con = MySqlConnector.getConnetion();
            PreparedStatement rt = con.prepareStatement(sql);) {
                rt.setInt(1, codico);
                int linhasAfetadas = rt.executeUpdate();
                if (linhasAfetadas > 0){
                    System.out.println("O livro foi deletado com sucesso");
                }
            } catch (SQLException e) {
                System.out.println("ERRO EM DELETAR O LIVRO" + e.getMessage());
            }
            if(!conjutoDeLivro.isEmpty()){
             conjutoDeLivro.stream()
                    .filter(l -> l.getISBN() == codico)
                    .toList();
        }
        conjutoDeLivro.removeAll(listaDeletada);
    }

    //Metodo que pesquisa por code no DB
    public void pesquisaPorCode (int codicoPesquisa){
        //--- sql
        String sql = "SELECT * FROM livros WHERE codico = ? ";
        try(Connection coon = MySqlConnector.getConnetion();
            PreparedStatement tm = coon.prepareStatement(sql);
            ) {
            tm.setInt(1, codicoPesquisa);
            try(ResultSet set = tm.executeQuery();){
            while (set.next()){
                String titulo = set.getString("titulo");
                String autor = set.getString("autor");
                int codi = set.getInt("codico");
                boolean status = set.getBoolean("status");

                System.out.println("Titulo: " + titulo +
                        "| Autor: " + autor +
                        "| Código: " + codi +
                        "| Status: " + formatação(status));
            }
        }} catch (SQLException e) {
            System.out.println("erro em mostrar" + e.getMessage());
        }
        if(!conjutoDeLivro.isEmpty()){
                conjutoDeLivro.stream()
                        .filter(l -> l.getISBN() == codicoPesquisa)
                        .forEach(System.out::println);
                }
                else {
                    System.out.println("Esse livro não esta cadastrado na bibliotaca");
                }
    }
    //Pesquisar no DB o titulo do livro
    public Livro pesquisaPorTitulo (String tituloPesquisa){
        //--- sql
        String sql = "SELECT * FROM livros WHERE titulo = ? ";
        try(Connection coon = MySqlConnector.getConnetion();
            PreparedStatement tm = coon.prepareStatement(sql);
        ) {
            tm.setString(1, tituloPesquisa);
            try(ResultSet set = tm.executeQuery();){
                while (set.next()){
                    String titulo = set.getString("titulo");
                    String autor = set.getString("autor");
                    int codi = set.getInt("codico");
                    boolean status = set.getBoolean("status");

                    System.out.println("Titulo: " + titulo +
                            "| Autor: " + autor +
                            "| Código: " + codi +
                            "| Status: " + formatação(status));
                }
            }} catch (SQLException e) {
            System.out.println("erro em mostrar" + e.getMessage());
        }
        return null;
    }
    //Mostrar todos os clientes Cadastrados
    public void mostrarTodosClientes(){
        String sql = "SELECT * FROM clientes";
        try(Connection con = MySqlConnector.getConnetion();
        PreparedStatement preparo = con.prepareStatement(sql);
        ResultSet set = preparo.executeQuery()){
            while (set.next()){
                String nome = set.getString("nome");
                String email = set.getString("email");
                int quantidade = set.getInt("Qtd_livro");
                String tipo = set.getString("tipo_cliente");
                System.out.println("Nome: " + nome +
                        "| Email: " + email +
                        "| Quantidade de Livros: " + quantidade +
                        "| Tipo cliente: " + tipo);
            }


    } catch (SQLException e) {
            System.out.println("ERRO EM EXIBIR A TABELA " + e.getMessage());
        }
    }
    //Mostra tabela geral de aluguel
    public void tabelaGeralAluguel(){
        String sql = "SELECT * FROM tabela_geral";
        try (Connection con  = MySqlConnector.getConnetion();
        PreparedStatement set = con.prepareStatement(sql);
        ResultSet resultado = set.executeQuery()) {
            while (resultado.next()) {
                String nome = resultado.getString("Nome");
                Date data = resultado.getDate("Devolução");
                String livro = resultado.getString("Livro");
                System.out.println("Nome: " + nome +
                        "| Data Devolução: " + data +
                        "| Livro: " + livro);
            }
        } catch (SQLException e) {
            System.out.println("Erro em exibir " + e.getMessage());
        }
    }
    //Mostra a quantidade de Cliente cadastrados
    public void quantidadeClientesCadastrados(){
        String sql = "SELECT count(id) AS  Quantidade_Clientes FROM clientes";
        try(Connection coon = MySqlConnector.getConnetion();
        PreparedStatement rt = coon.prepareStatement(sql);
        ResultSet set = rt.executeQuery()){
            while (set.next()){
                int quantidadeCliente = set.getInt("Quantidade_Clientes");
                System.out.printf("----Clientes logados----%n Quantidade: %d %n", quantidadeCliente);
            }
        } catch (SQLException e) {
            System.out.println("Erro em conferir quantidade " + e.getMessage());
        }
    }
    }












