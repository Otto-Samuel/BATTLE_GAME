public class Monstro extends Entidade{
    private int experienciaConcedida;
    
    public Monstro(String nome, int nivel, int vidaMaxima, int manaMaxima, int experienciaConcedida) {
        super(nome, nivel, vidaMaxima, manaMaxima);
        this.experienciaConcedida = experienciaConcedida;
    }
    
    @Override
    public void atacar(Entidade alvo) {
        int dano = nivel * 3;
        alvo.receberDano(dano);
    }
    
    public int getExperienciaConcedida() {
        return experienciaConcedida;
    }
}
