package com.mycompany.ads1001.t2;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Produto {
    private int id;
    private String nome;
    private double preco;
    
    public Produto(int id , String nome , double preco){
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }
    
    // ==========================================
    // MÉTODO MAIS SIMPLES POSSÍVEL PARA SALVAR
    // ==========================================
    public boolean salvarNoBanco() {
        // O SQL que vai jogar o nome e o preço lá na tabela 'produtos'
        String sql = "INSERT INTO produtos (nome, preco) VALUES (?, ?)";
        
        try (Connection conn = ConnectionFrame.getConnection()) {
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, this.nome);   // Pega o nome deste produto
            stmt.setDouble(2, this.preco);  // Pega o preço deste produto
            
            stmt.executeUpdate(); // Manda pro MySQL
            return true;          // Se chegou aqui, salvou com sucesso!
            
        } catch (Exception e) {
            System.out.println("Erro ao salvar produto: " + e.getMessage());
            return false;         // Deu algum erro
        }
    }
    // ==========================================
    // MÉTODO PARA EXCLUIR DO BANCO
    // ==========================================
    public boolean excluirNoBanco() {
        // O SQL que vai apagar o produto baseado no ID
        String sql = "DELETE FROM produtos WHERE id = ?";
        
        try (Connection conn = ConnectionFrame.getConnection()) {
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, this.id);  // Usa o ID deste produto para saber qual apagar
            
            stmt.executeUpdate(); // Manda a ordem de exclusão pro MySQL
            return true;          // Se chegou aqui, excluiu com sucesso!
            
        } catch (Exception e) {
            System.out.println("Erro ao excluir produto: " + e.getMessage());
            return false;         // Deu algum erro
        }
    // Método para puxar todos os produtos cadastrados no banco para a sua tela
    public static List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        try (Connection conn = ConnectionFrame.getConnection()) {
            java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT * FROM produtos");
            java.sql.ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Produto(rs.getInt("id"), rs.getString("nome"), rs.getDouble("preco")));
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }

    
    
    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    
    public void setNome(String nome) { this.nome = nome; }
    public void setPreco(double preco) { this.preco = preco; }
}