package BATTLES;

public class Personagem extends Entidade {
    private ListaEncadeada<Habilidade> habilidades;
    private ListaEncadeada<Item> inventario;
    private int experienciaAtual;
    private int experienciaParaProximoNivel;
    private Jogador jogador;  // Adicionando a referência ao Jogador
    
    public Personagem(String nome, int nivel, int vidaMaxima, int manaMaxima, Jogador jogador) {
        super(nome, nivel, vidaMaxima, manaMaxima);
        this.habilidades = new ListaEncadeada<>();
        this.inventario = new ListaEncadeada<>();
        this.experienciaAtual = 0;
        this.experienciaParaProximoNivel = calcularExperienciaParaProximoNivel();
        this.jogador = jogador;  // Inicializando a referência
    }
    
    private int calcularExperienciaParaProximoNivel() {
        return this.nivel * 100;
    }
    
    public void ganharExperiencia(int exp) {
        this.experienciaAtual += exp;
        System.out.println(this.nome + " ganhou " + exp + " pontos de experiência!");
        
        while (this.experienciaAtual >= this.experienciaParaProximoNivel) {
            this.experienciaAtual -= this.experienciaParaProximoNivel;
            subirNivel();  // Chama o método da classe pai e depois adiciona comportamentos extras
            this.experienciaParaProximoNivel = calcularExperienciaParaProximoNivel();
        }
    }
    
    @Override
    public void subirNivel() {
        super.subirNivel();  // Chama a implementação da classe Entidade
        
        // Melhorias adicionais específicas para Personagem
        vidaMaxima += 5;  // Personagens ganham mais vida que entidades comuns
        manaMaxima += 3;  // Personagens ganham mais mana que entidades comuns
        vidaAtual = vidaMaxima;
        manaAtual = manaMaxima;
        
        System.out.println(this.nome + " subiu para o nível " + this.nivel + "!");
        System.out.println("Atributos aumentados: Vida=" + vidaMaxima + ", Mana=" + manaMaxima);
    }
    
    // Restante dos métodos permanecem iguais...
    @Override
    public void atacar(Entidade alvo) {
        int dano = nivel * 2;
        alvo.receberDano(dano);
        System.out.println(this.nome + " atacou " + alvo.getNome() + " causando " + dano + " de dano!");
    }
    
    public void usarHabilidade(Habilidade habilidade, Entidade alvo) {
        if (manaAtual >= habilidade.getCustoMana()) {
            manaAtual -= habilidade.getCustoMana();
            habilidade.usar(this, alvo);
        } else {
            System.out.println("Mana insuficiente para usar " + habilidade.getNome() + "!");
        }
    }

    // Getters e Setters...
    public ListaEncadeada<Habilidade> getHabilidades() {
        return habilidades;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setHabilidades(ListaEncadeada<Habilidade> habilidades) {
        this.habilidades = habilidades;
    }

    public ListaEncadeada<Item> getInventario() {
        return inventario;
    }

    public void setInventario(ListaEncadeada<Item> inventario) {
        this.inventario = inventario;
    }
    
    public int getExperienciaAtual() {
        return experienciaAtual;
    }
    
    public int getExperienciaParaProximoNivel() {
        return experienciaParaProximoNivel;
    }
}