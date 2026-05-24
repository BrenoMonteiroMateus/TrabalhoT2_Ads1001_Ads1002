package com.mycompany.ads1001.t2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Produto {
    private int id;
    private String nome;
    private double preco;
    
    public Produto(int id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    // ==========================================
    // MÉTODO PARA SALVAR NO BANCO
    // ==========================================
    public boolean salvarNoBanco() {
        String sql = "INSERT INTO produtos (nome, preco) VALUES (?, ?)";
        try (Connection conn = ConnectionFrame.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, this.nome);
            stmt.setDouble(2, this.preco);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao salvar produto: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // MÉTODO PARA EXCLUIR DO BANCO
    // ==========================================
    public boolean excluirNoBanco() {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (Connection conn = ConnectionFrame.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, this.id);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao excluir produto: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // MÉTODO PARA LISTAR TODOS DO BANCO
    // ==========================================
    public static List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        try (Connection conn = ConnectionFrame.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM produtos ORDER BY nome");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Produto(rs.getInt("id"), rs.getString("nome"), rs.getDouble("preco")));
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }

    public int getId()    { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }

    public void setNome(String nome)   { this.nome = nome; }
    public void setPreco(double preco) { this.preco = preco; }

    @Override
    public String toString() { return nome; } // Para exibir certo no JComboBox
}