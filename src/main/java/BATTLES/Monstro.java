package BATTLES;

public class Monstro extends Entidade {
    private int experienciaConcedida;
    
    public Monstro(String nome, int nivel, int vidaMaxima, int manaMaxima, int experienciaConcedida) {
        super(nome, nivel, vidaMaxima, manaMaxima);
        this.experienciaConcedida = experienciaConcedida;
    }
    
    @Override
    public void atacar(Entidade alvo) {
        int dano = nivel * 3;
        alvo.receberDano(dano);
        System.out.println(this.nome + " atacou " + alvo.getNome() + " causando " + dano + " de dano!");
    }
    
    // Monstros podem ter uma implementação diferente de subirNivel se necessário
    @Override
    public void subirNivel() {
        super.subirNivel();
        // Monstros podem ganhar menos atributos ao subir de nível
        vidaMaxima += 3;
        manaMaxima += 1;
        vidaAtual = vidaMaxima;
        manaAtual = manaMaxima;
    }
    
    public int getExperienciaConcedida() {
        return experienciaConcedida;
    }
}