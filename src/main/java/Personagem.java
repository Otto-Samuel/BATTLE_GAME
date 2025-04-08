import invent.Inventario;

public class Personagem extends Entidade{
    private ListaEncadeada<Habilidade> habilidades;
    private ListaEncadeada<Item> inventario;
    
    public Personagem(String nome, int nivel, int vidaMaxima, int manaMaxima) {
        super(nome, nivel, vidaMaxima, manaMaxima);
        this.habilidades = new ListaEncadeada<>();
        this.inventario = new ListaEncadeada<>();
    }
    
    @Override
    public void atacar(Entidade alvo) {
        // Lógica básica de ataque
        int dano = nivel * 2; // Exemplo simples
        alvo.receberDano(dano);
    }
    
    public void usarHabilidade(Habilidade habilidade, Entidade alvo) {
        if (manaAtual >= habilidade.getCustoMana()) {
            manaAtual -= habilidade.getCustoMana();
            habilidade.usar(this, alvo);
        }
    }
    
    public void subirNivel() {
        nivel++;
        vidaMaxima += 10;
        manaMaxima += 5;
        vidaAtual = vidaMaxima;
        manaAtual = manaMaxima;
    }
}
