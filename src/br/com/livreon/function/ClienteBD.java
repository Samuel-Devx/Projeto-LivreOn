package br.com.livreon.function;

import br.com.livreon.conector.MySqlConnector;
import br.com.livreon.model.Biblioteca;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

import static br.com.livreon.model.Biblioteca.formatação;

//Funcionamento cliente banco de dedos
public class ClienteBD {

    //metodos para pegar informações do cliente no banco de dados
    public static void info(Cliente c) {
        String sql = "SELECT * FROM clientes WHERE email = ? ";
        try (Connection conector = MySqlConnector.getConnetion();
             PreparedStatement preparo = conector.prepareStatement(sql);) {
            preparo.setString(1, c.getEmail());
            ResultSet set = preparo.executeQuery();
        while (set.next()){
            String nome = set.getString("nome");
            String email = set.getString("email");
            int quantidadeLivros = set.getInt("Qtd_livro");

            String tipoCliente = set.getString("tipo_cliente");
            System.out.println("Nome: " + nome +
                    "| Livros Alugados: " + quantidadeLivros +
                    "| Tipo Plano: " + tipoCliente);
        }
        } catch (SQLException e) {
            System.out.println("ERRO EM EXIBIR SUAS INFORMAÇÕES" + e.getMessage());
        }
    }
    //Metodo validacao de email
    public static boolean validarEmail(String email){
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email != null && email.matches(emailRegex);

    }
    //Metodo validacao de senha
    public static boolean validarSenha(String senha){
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return  senha != null && senha.matches(regex);
    }

    //Metodo para criar conta
    public static Cliente criarConta (Scanner teclado) {
        System.out.print("Digite seu nome: ");
        String nome = teclado.next();
        System.out.print("Digite seu Email: ");
        String email = teclado.next();
        System.out.print("Digite sua senha: ");
        String senha = teclado.next();
        System.out.print("Tipo de cliente: (Normal/Plus):  ");
        String tipo = teclado.next();

        //Validação
        if(!validarEmail(email)){
            System.out.println("Email inválido");
            return null;
        }
        if (!validarSenha(senha)){
            System.out.println("Senha inválida! Ela deve ter:");
            System.out.println("- Mínimo 8 caracteres");
            System.out.println("- Letra maiúscula e minúscula");
            System.out.println("- Número");
            System.out.println("- Caractere especial");
            return null;
        }

        //logando
        String sqCheck = "SELECT * FROM clientes WHERE email = ?";
        try (Connection con = MySqlConnector.getConnetion()) {

            String sqlInsert = "INSERT INTO clientes (nome, email, senha, Qtd_livro, tipo_cliente) value (?, ?, MD5(?), ?, ?)";
            PreparedStatement preparoInsert = con.prepareStatement(sqlInsert);

            preparoInsert.setString(1, nome);
            preparoInsert.setString(2, email);
            preparoInsert.setString(3, senha);
            preparoInsert.setInt(4, 0);
            preparoInsert.setString(5, tipo);
            int insert = preparoInsert.executeUpdate();
            if (insert > 0) {
                Cliente novoCliente;
                if (tipo.equalsIgnoreCase("Normal")) {
                    novoCliente = new ClienteNormal(nome, email, senha);
                } else {
                    novoCliente = new ClientePlus(nome, email, senha);
                }
                System.out.println("Conta criada com sucesso ✅");
                return novoCliente;
            }
            else {
                System.out.println("❌ Erro em criar conta");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro em criar sua conta" + e.getMessage());
            return null;
        }
    }


    //Metodo logar
    public static Cliente login(Scanner teclado){
        System.out.print("Digite seu email: ");
        String email = teclado.next();
        System.out.print("Digite sua senha: ");
        String senha = teclado.next();
        String sql = "SELECT * FROM clientes WHERE email = ? and senha = MD5(?) ";
        try(Connection con = MySqlConnector.getConnetion();
            PreparedStatement preparo = con.prepareStatement(sql);
        ) {
            preparo.setString(1, email);
            preparo.setString(2, senha);
            ResultSet rs = preparo.executeQuery();

            if(rs.next()){
                String tipo = rs.getString("tipo_cliente");
                Cliente cliente;
                if (tipo.equalsIgnoreCase("Cliente normal")){
                    cliente = new ClienteNormal(rs.getString("nome"), rs.getString("email"), rs.getString("senha"));
                }
                else{
                    cliente = new ClientePlus(rs.getString("nome"), rs.getString("email"), rs.getString("senha"));
                }
                System.out.println("✅ Login bem-sucedido! Bem-vindo!" + rs.getString("nome") + "(" + tipo + ")");
                return cliente;
            }
            else{
                System.out.println("❌ERRO em conectar no banco de dados");
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
        //pesquisa de Status
        public static Boolean pesquisaStaus(int id){
            String sql = "SELECT status FROM vw_tabela_cliente_status WHERE id = ?";
            try(Connection con = MySqlConnector.getConnetion();
            PreparedStatement st = con.prepareStatement(sql)) {
                st.setInt(1, id);
                ResultSet resultSet = st.executeQuery();
                if(resultSet.next()){
                    return resultSet.getBoolean("status");
                }
                else {
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("Erro em retornar o status do livro" + e.getMessage());
                return true;
        }
    }
        //Metodo para alugar o livro(insert no DB)
        public static  void aluguel (int idCliente, int idLivro, LocalDate devolucao){
            String sql = "INSERT INTO aluguel (id_cliente, id_livro, data_aluguel, data_devolucao) VALUES  (?, ?, ?, ?)";
            String sqlBusca = "SELECT tipo_cliente, Qtd_livro FROM clientes WHERE ID = ?";
            try(Connection con = MySqlConnector.getConnetion()) {
                //Verificação cliente normal
                PreparedStatement setBusca = con.prepareStatement(sqlBusca);
                setBusca.setInt(1,idCliente);
                ResultSet rtBusca = setBusca.executeQuery();
                if(!rtBusca.next()){
                    System.out.println("ERRO EM ENCONTRAR SUA CONTA ❌");
                }

                String tipo = rtBusca.getString("tipo_cliente");
                int quantidade  = rtBusca.getInt("Qtd_livro");
    
                if(tipo.equalsIgnoreCase("normal") && quantidade >=1){
                    System.out.println("❌ O cliente normal não pode alugar mais de 1 livro, atualize seu plano!");
                    return;
                }
    
                //Insert na tabela
                PreparedStatement set = con.prepareStatement(sql);
                set.setInt(1,idCliente);
                set.setInt(2, idLivro);
                set.setDate(3, Date.valueOf(LocalDate.now()));
                set.setDate(4, Date.valueOf(devolucao));
                int linhasAfetadaInsert = set.executeUpdate();
    
                if(linhasAfetadaInsert > 0){
                    System.out.println("✅ Alugado com sucesso!");
                }
            } catch (SQLException e) {
                System.out.println("ERRO em alugar! " + e.getMessage());
            }
        }

    //Metodo retorna id do livro
    public static int pesquisaTitulo(String nomeLivro) {
        String sql = "SELECT id FROM livros WHERE titulo = ?";
        try (Connection con = MySqlConnector.getConnetion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nomeLivro);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            } else {
                System.out.println("⚠ Livro não encontrado!");
                return 0;
            }

        } catch (SQLException e) {
            System.out.println("❌ ERRO ao pesquisar título: " + e.getMessage());
            return 0;
        }
    }

    //Metodo que retorna ID
    public static int pesquisaId(Cliente cliente){
        String sqlPesquisa = "SELECT * FROM clientes WHERE email = ?";
        try(Connection con = MySqlConnector.getConnetion();
        PreparedStatement rt = con.prepareStatement(sqlPesquisa);
        ) {
            rt.setString(1, cliente.getEmail());
            ResultSet set = rt.executeQuery();
           if(set.next()){
               return set.getInt("id");
           }
           else{
               System.out.println("Erro em encontra o livro");
                return 0;
           }
        } catch (SQLException e) {
            System.out.println("ERRO EM BUSCAR ID ❌" + e.getMessage());
            return 0;
        }

    }

    //Metodo devolução do livro (atulizando o DB)
    public static  void devolver (int idCliente, int idLivro){
        String sql = "DELETE FROM aluguel WHERE id_cliente = ? and id_livro = ?";
        try(Connection con = MySqlConnector.getConnetion()) {
            PreparedStatement set = con.prepareStatement(sql);
            set.setInt(1, idCliente);
            set.setInt(2, idLivro);
            int linhasAfetaas = set.executeUpdate();

            if(linhasAfetaas > 0){
                System.out.println("Devolvido com sucesso! ");
            }
            else {
                System.out.println("Erro em alugar ");
            }
        } catch (SQLException e) {
            System.out.println("ERRO em alugar! ");
        }
    }
    //Mostrar livros
    public static void mostrarLivros( ){
        String sql = "SELECT * FROM livros";
        try(Connection con = MySqlConnector.getConnetion();
        PreparedStatement preparo = con.prepareStatement(sql);
        ResultSet set = preparo.executeQuery()) {
            while (set.next()){
                String nome = set.getString("titulo");
                String autor = set.getString("autor");
                boolean status = set.getBoolean("status");
                System.out.println("Titulo: " + nome +
                        "| Autor: " + autor +
                        "| Status: " + Biblioteca.formatação(status));
            }

        } catch (SQLException e) {
            System.out.println("Erro em mostrar biblioteca " + e.getMessage());
        }
    }


    //Metodo ver os livros alugados e prazo de entrega
    public static void verLivrosAlugados(int id){
        String sql = "SELECT * FROM vw_devolucao WHERE ID = ?";
        String pesquisa = "SELECT * FROM clientes;";
        try(Connection con = MySqlConnector.getConnetion();
        PreparedStatement set = con.prepareStatement(sql);
            PreparedStatement quantidade = con.prepareStatement(pesquisa);
        ) {
            //Pesquisa
            ResultSet result= quantidade.executeQuery();
            int quantidadeDeLivro = 0;
            if(result.next()){
                quantidadeDeLivro = result.getInt("Qtd_livro");
            }
            if(quantidadeDeLivro == 0){
                System.out.println("Voce não alugou livros 📚");
                return;
            }

            set.setInt(1, id);
            ResultSet get = set.executeQuery();
            while (get.next()){
                String livro = get.getString("Titulo");
                String devolucao = String.valueOf(get.getDate("Devolução"));
                System.out.println("Livro: " + livro +
                        "| Devolução: " + devolucao );

            }
        } catch (SQLException e) {
            System.out.println("Erro em mostrar a informação " + e.getMessage());
        }
    }


    //Atalizar plano
    public static void atualizarPlano(int id){
        String sql = "UPDATE clientes SET tipo_cliente = 'Plus' WHERE id = ?";
        try (Connection con = MySqlConnector.getConnetion();
        PreparedStatement set = con.prepareStatement(sql);){
            set.setInt(1, id);
            int linhaAfetadas = set.executeUpdate();
            if(linhaAfetadas > 0){
                System.out.println("🎉 Parabéns agora você é um cliente PLUS");

            }
            else {
                System.out.println("ERRO em atulizar sua conta");
            }
        } catch (SQLException e) {
            System.out.println("Erro em atulçizar seu plano " + e.getMessage());
        }

    }
}

