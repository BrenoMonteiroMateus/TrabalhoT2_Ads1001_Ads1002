
package com.mycompany.ads1001.t2;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList; // Import adicionado para não dar erro no ListarTodos
import java.util.List;      // Import adicionado para não dar erro no ListarTodos

public class Produto {
    private int id;
    private String nome;
    private double preco;
    
    public Produto(int id, String nome, double preco){
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }
    
    // ==========================================
    // MÉTODO MAIS SIMPLES POSSÍVEL PARA SALVAR
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
    } // <─── ESSA CHAVE ESTAVA FALTANDO AQUI PARA FECHAR O MÉTODO!

    // =========================================================================
    // Método para puxar todos os produtos cadastrados no banco para a sua tela
    // =========================================================================
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
    @Override
    public String toString() {
        return this.nome; 
    }

}
    
