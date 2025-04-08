package BATTLES;

public class Item {
    private int id;
    private String nome;
    private String descricao;
    private int tipo;
    private int efeito;
    private int quantidade;
    
    public Item(int id, String nome, String descricao, int tipo, int efeito, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.efeito = efeito;
        this.quantidade = quantidade;
    }
    
    public void usar(Entidade usuario, Entidade alvo) {
        if (quantidade > 0) {
            quantidade--;
            
            switch (tipo) {
                case 1:
                    alvo.curar(efeito);
                    System.out.println(usuario.getNome() + " usou " + nome + " em " + alvo.getNome() + " curando " + efeito + " de HP!");
                    break;
                case 2:
                    System.out.println(usuario.getNome() + " equipou " + nome + "!");
                    break;
                case 3:
                    alvo.receberDano(efeito);
                    System.out.println(usuario.getNome() + " usou " + nome + " em " + alvo.getNome() + " causando " + efeito + " de dano!");
                    break;
                default:
                    System.out.println("Tipo de item desconhecido!");
            }
        } else {
            System.out.println("Não há mais " + nome + " no inventário!");
        }
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public int getTipo() { return tipo; }
    public int getEfeito() { return efeito; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}