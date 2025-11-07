package br.com.livreon.conector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;

public class MySqlConnector {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/livraria";
    private static final String USER = "root";
    private static final String SENHA = "192119";

   public static Connection getConnetion(){
       try {
           return DriverManager.getConnection(URL,USER,SENHA);
       } catch (SQLException e) {
           System.out.println("ERRO em conectar com o banco de dados" + e.getMessage());
           return null;
       }
   }

}
