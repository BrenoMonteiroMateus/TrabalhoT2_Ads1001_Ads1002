/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ads1001.t2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Breno e Bia
 */
public class TrabalhoT2ADS1001_1002 {

    public static void main(String[] args) {
    // Tenta obter a conexão
    try (Connection conn = ConnectionFrame.getConnection()) {
        if (conn != null && !conn.isClosed()) {
            System.out.println("Sucesso! Conexão estabelecida com o banco de dados.");
        }
    } catch (SQLException e) {
        System.err.println("Falha na conexão: " + e.getMessage());
    }
}
    }

