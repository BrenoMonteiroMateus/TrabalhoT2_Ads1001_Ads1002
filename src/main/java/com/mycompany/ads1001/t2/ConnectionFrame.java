package com.mycompany.ads1001.t2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFrame {
    
    // Altere para os dados do seu banco
    private static final String URL = "";
    private static final String USUARIO = "";
    private static final String SENHA = "";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return null;
        }
    }
}
    