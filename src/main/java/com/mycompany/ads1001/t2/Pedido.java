
package com.mycompany.ads1001.t2;

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

    public List<ItemPedido> getItens() { return itens; }
    public double getTotal() { return total; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
    

