package BATTLES.InventarioeItem;

import BATTLES.EstruturaDeDados.ListaEncadeada;

// RENATO
public class Inventario {
    private ListaEncadeada<Item> itens;
    private int capacidadeMaxima;
    
    public Inventario(int capacidadeMaxima) {
        this.itens = new ListaEncadeada<>();
        this.capacidadeMaxima = capacidadeMaxima;
    }
    
    public boolean adicionarItem(Item item) {
        if (itens.tamanho() >= capacidadeMaxima) {
            System.out.println("Inventário cheio! Não foi possível adicionar " + item.getNome());
            return false;
        }
        
        Item existente = buscarItem(item.getId());
        if (existente != null) {
            existente.setQuantidade(existente.getQuantidade() + item.getQuantidade());
        } else {
            itens.adicionar(item);
        }
        return true;
    }
    
    public boolean removerItem(int idItem) {
        Item item = buscarItem(idItem);
        if (item != null) {
            if (item.getQuantidade() > 1) {
                item.setQuantidade(item.getQuantidade() - 1);
            } else {
                itens.remover(item);
            }
            return true;
        }
        return false;
    }
    
    public Item buscarItem(int idItem) {
        for (int i = 0; i < itens.tamanho(); i++) {
            Item item = itens.get(i);
            if (item.getId() == idItem) {
                return item;
            }
        }
        return null;
    }
    
    public void listarItens() {
        if (itens.tamanho() == 0) {
            System.out.println("O inventário está vazio!");
            return;
        }
        
        System.out.println("\n=== Itens no Inventário ===");
        for (int i = 0; i < itens.tamanho(); i++) {
            Item item = itens.get(i);
            System.out.println((i+1) + ". " + item.getNome() + 
                             " (x" + item.getQuantidade() + ") - " + 
                             item.getDescricao());
        }
    }
    
    public ListaEncadeada<Item> getItens() {
        return itens;
    }
    
    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }
}