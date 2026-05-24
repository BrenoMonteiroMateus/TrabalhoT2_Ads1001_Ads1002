
package com.mycompany.ads1001.t2;


public class Produto {
    private int id;
    private String nome;
    private double preco;
    
    public Produto(int id , String nome , double preco){
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        
    }
    
    public int getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }

    
    public void setNome(String nome) { this.nome = nome; }
    public void setPreco(double preco) { this.preco = preco; }
}
