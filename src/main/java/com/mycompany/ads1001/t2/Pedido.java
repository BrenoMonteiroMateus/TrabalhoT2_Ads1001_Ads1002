package com.mycompany.ads1001.t2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private List<ItemPedido> itens;
    private double total;

    public Pedido() {
        this.itens = new ArrayList<>();
        this.total = 0.0;
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        total += item.getSubtotal();
    }

    public void removerItem(ItemPedido item) {
        itens.remove(item);
        total -= item.getSubtotal();
    }

    public void salvarNoBanco(String cliente) {
        try (Connection conn = ConnectionFrame.getConnection()) {
            
            PreparedStatement p1 = conn.prepareStatement("INSERT INTO pedidos (identificacao_cliente, valor_total) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            p1.setString(1, cliente);
            p1.setDouble(2, this.total);
            p1.executeUpdate();

            ResultSet rs = p1.getGeneratedKeys();
            if (rs.next()) this.id = rs.getInt(1);

            PreparedStatement p2 = conn.prepareStatement("INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)");
            for (ItemPedido i : itens) {
                p2.setInt(1, this.id);
                p2.setInt(2, i.getProduto().getId());
                p2.setInt(3, i.getQuantidade());
                p2.setDouble(4, i.getProduto().getPreco());
                p2.executeUpdate();
            }
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public List<ItemPedido> getItens() { return itens; }
    public double getTotal() { return total; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}

 