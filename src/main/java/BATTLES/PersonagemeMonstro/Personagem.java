package BATTLES.PersonagemeMonstro;

import BATTLES.ListaEncadeada;
import BATTLES.Inventario;
import BATTLES.Item;

public class Personagem extends Entidade {
    private ListaEncadeada<Habilidade> habilidades;
    private Inventario inventario; 
    private int danoExtra;
    private int experienciaAtual;
    private int experienciaParaProximoNivel;
    private Jogador jogador;
    
    public Personagem(String nome, int nivel, int vidaMaxima, int manaMaxima, Jogador jogador) {
        super(nome, nivel, vidaMaxima, manaMaxima);
        this.habilidades = new ListaEncadeada<>();
        this.inventario = new Inventario(20);
        this.experienciaAtual = 0;
        this.experienciaParaProximoNivel = calcularExperienciaParaProximoNivel();
        this.jogador = jogador;
        this.danoExtra = 0;
        
        habilidades.adicionar(new Habilidade(1, "Ataque Básico", "Um ataque simples", 0, 10, 1));
        habilidades.adicionar(new Habilidade(2, "Cura Leve", "Recupera um pouco de HP", 15, 20, 2));
    }
    
    private int calcularExperienciaParaProximoNivel() {
        return this.nivel * 100;
    }
    
    public void ganharExperiencia(int exp) {
        this.experienciaAtual += exp;
        System.out.println(this.nome + " ganhou " + exp + " pontos de experiência!");
        
        while (this.experienciaAtual >= this.experienciaParaProximoNivel) {
            this.experienciaAtual -= this.experienciaParaProximoNivel;
            subirNivel();
            this.experienciaParaProximoNivel = calcularExperienciaParaProximoNivel();
        }
    }
    
    @Override
    public void subirNivel() {
        super.subirNivel();
        vidaMaxima += 10;
        manaMaxima += 5;
        vidaAtual = vidaMaxima;
        manaAtual = manaMaxima;
        
        System.out.println(this.nome + " subiu para o nível " + this.nivel + "!");
        System.out.println("Atributos aumentados: Vida=" + vidaMaxima + ", Mana=" + manaMaxima);
    }
    
    @Override
    public void atacar(Entidade alvo) {
        int dano = nivel * 2 + danoExtra;
        alvo.receberDano(dano);
        System.out.println(this.nome + " atacou " + alvo.getNome() + " causando " + dano + " de dano!");
    }
    
    public void usarHabilidade(Habilidade habilidade, Entidade alvo) {
        if (manaAtual >= habilidade.getCustoMana()) {
            manaAtual -= habilidade.getCustoMana();
            habilidade.usar(this, alvo);
            System.out.println(this.nome + " usou " + habilidade.getNome() + " em " + alvo.getNome() + "!");
        } else {
            System.out.println("Mana insuficiente para usar " + habilidade.getNome() + "!");
        }
    }
    
    public void usarItem(int indexItem, Entidade alvo) {
        if (indexItem >= 0 && indexItem < inventario.getItens().tamanho()) {
            Item item = inventario.getItens().get(indexItem);
            item.usar(this, alvo);
            
            if (item.getQuantidade() <= 0) {
                inventario.removerItem(item.getId());
            }
        } else {
            System.out.println("Item inválido!");
        }
    }
    
    public void aprenderHabilidade(Habilidade novaHabilidade) {
        habilidades.adicionar(novaHabilidade);
        System.out.println(this.nome + " aprendeu a habilidade " + novaHabilidade.getNome() + "!");
    }
    
    public ListaEncadeada<Habilidade> getHabilidades() {
        return habilidades;
    }
    
    public Inventario getInventario() {
        return inventario;
    }
    
    public Jogador getJogador() {
        return jogador;
    }
    
    public int getExperienciaAtual() {
        return experienciaAtual;
    }
    
    public int getExperienciaParaProximoNivel() {
        return experienciaParaProximoNivel;
    }
    
    public int getDanoExtra() {
        return danoExtra;
    }
    
    public void setDanoExtra(int danoExtra) {
        this.danoExtra = danoExtra;
    }
    
    public void exibirStatus() {
        System.out.println("\n=== Status de " + nome + " ===");
        System.out.println("Nível: " + nivel);
        System.out.println("HP: " + vidaAtual + "/" + vidaMaxima);
        System.out.println("MP: " + manaAtual + "/" + manaMaxima);
        System.out.println("EXP: " + experienciaAtual + "/" + experienciaParaProximoNivel);
        System.out.println("Dano Extra: +" + danoExtra);
    }
}