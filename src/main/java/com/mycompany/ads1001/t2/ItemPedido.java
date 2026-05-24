package com.mycompany.ads1001.t2;

public class ItemPedido {
    private Produto produto;
    private int quantidade;
    private double subTotal;

    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.subTotal = produto.getPreco() * quantidade;
    }

    public Produto getProduto()   { return produto; }
    public int getQuantidade()    { return quantidade; }
    public double getSubtotal()   { return subTotal; }
}
