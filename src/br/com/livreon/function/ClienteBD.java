package br.com.livreon.function;

import br.com.livreon.conector.MySqlConnector;

import java.sql.*;
import java.time.LocalDate;

//Funcionamento cliente banco de dedos
public class ClienteBD {
    //metodos para pegar informações do cliente no banco de dados
    protected static void info(Cliente c) {
        String sql = "SELECT * FROM clientes";
        try (Connection conector = MySqlConnector.getConnetion();
             PreparedStatement preparo = conector.prepareStatement(sql);
             ResultSet set = preparo.executeQuery();) {

            String nome = set.getString("nome");
            String email = set.getString("email");
            String livro = set.getString("titulo_cliente");
            int quantidadeLivros = set.getInt("Qtd_livro");
            String tipoCliente = set.getString("tipo_cliente");
            System.out.println("Nome: " + nome +
                    "| Livro : " + livro +
                    "| Livros Augudos: " + quantidadeLivros +
                    "| Tipo Plano: " + tipoCliente);
        } catch (SQLException e) {
            System.out.println("ERR EM EXIBIR SUAS INFORMAÇÕES" + e.getMessage());
        }
    }
    //Metodo aluga o livro no banco de dados
    protected static void alugarLivro(String nomeLivro, Cliente c){
    String sql = "UPDATE clientes SET titulo_cliente = ? AND Qtd_livro = ? WHERE email = ?";
    try(Connection con = MySqlConnector.getConnetion();
    PreparedStatement rt = con.prepareStatement(sql)){
        rt.setString(1, nomeLivro);
        rt.setInt(2, 1);
        rt.setString(3,c.getEmail());
        int linhasAfetadas = rt.executeUpdate();
        if(linhasAfetadas > 0){
            System.out.printf("%n O livro %s foi alugado com sucesso!%n", linhasAfetadas);
        }
    } catch (SQLException e) {
        System.out.println("ERRO ALUGAR ESSE LIVRO " + e.getMessage());
            }
        }

    protected static  void aluguel (int idCliente, int idLivro, LocalDate devolucao){
        String sql = "INSERT INTO aluguel (id_cliente, id_livro, data_aluguel, data_devolucao) VALUES  (?, ?, ?, ?)";
        try(Connection con = MySqlConnector.getConnetion()) {
            PreparedStatement set = con.prepareStatement(sql);
            set.setInt(1,idCliente);
            set.setInt(2, idLivro);
            set.setDate(3, Date.valueOf(LocalDate.now()));
            set.setDate(4, Date.valueOf(devolucao));

            String sqlUpdate = "UPDATE livros SET status = true WHERE id = ? ";
            PreparedStatement setStatus = con.prepareStatement(sqlUpdate);
            setStatus.setInt(1, idLivro);

            int linhasAfetaas = set.executeUpdate();
            if(linhasAfetaas > 0){
                System.out.println("Aluguado com sucesso!");
            }
        } catch (SQLException e) {
            System.out.println("ERRO em alugar! ");
        }
    }
}

