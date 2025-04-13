package BATTLES.PersonagemeMonstro;
public class Habilidade {
    private int id;
    private String nome;
    private String descricao;
    private int custoMana;
    private int dano;
    private int tipo;
    
    public Habilidade(int id, String nome, String descricao, int custoMana, int dano, int tipo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.custoMana = custoMana;
        this.dano = dano;
        this.tipo = tipo;
    }
    
    public void usar(Entidade usuario, Entidade alvo) {
        if (usuario.getManaAtual() >= custoMana) {
            usuario.setManaAtual(usuario.getManaAtual() - custoMana);
            
            switch (tipo) {
                case 1:
                    alvo.receberDano(dano);
                    System.out.println(usuario.getNome() + " usou " + nome + " em " + alvo.getNome() + " causando " + dano + " de dano!");
                    break;
                case 2:
                    alvo.curar(dano);
                    System.out.println(usuario.getNome() + " usou " + nome + " em " + alvo.getNome() + " curando " + dano + " de HP!");
                    break;
                case 3:
                    System.out.println(usuario.getNome() + " usou " + nome + " em " + alvo.getNome() + " para aumentar seus atributos!");
                    break;
                default:
                    System.out.println("Tipo de habilidade desconhecido!");
            }
        } else {
            System.out.println(usuario.getNome() + " não tem mana suficiente para usar " + nome + "!");
        }
    }
    
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public int getCustoMana() { return custoMana; }
    public int getDano() { return dano; }
    public int getTipo() { return tipo; }
}