package BATTLES;
public abstract class Entidade {
    protected String nome;
    protected int nivel;
    protected int vidaMaxima;
    protected int vidaAtual;
    protected int manaMaxima;
    protected int manaAtual;
    
    public Entidade(String nome, int nivel, int vidaMaxima, int manaMaxima) {
        this.nome = nome;
        this.nivel = nivel;
        this.vidaMaxima = vidaMaxima;
        this.vidaAtual = vidaMaxima;
        this.manaMaxima = manaMaxima;
        this.manaAtual = manaMaxima;
    }
    
    public abstract void atacar(Entidade alvo);
    
    public void receberDano(int dano) {
        vidaAtual -= dano;
        if (vidaAtual < 0) vidaAtual = 0;
    }
    
    public void curar(int quantidade) {
        vidaAtual += quantidade;
        if (vidaAtual > vidaMaxima) vidaAtual = vidaMaxima;
    }
    
    public boolean estaVivo() {
        return vidaAtual > 0;
    }
    
}